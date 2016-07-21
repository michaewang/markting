package com.mars.markting.activity;

import java.util.ArrayList;
import java.util.List;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.mars.markting.R;
import com.mars.markting.bean.GoodsInfo;
import com.mars.markting.dialogfragment.DeleteHistoryDialogFragment;
import com.mars.markting.utils.Constants;
import com.mars.markting.utils.DBUtils;
import com.mars.markting.utils.NumberUtils;
import com.lib.uil.ScreenUtils;
import com.lib.uil.UILUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class HistoryActivity extends FragmentActivity implements OnClickListener {

	private ArrayList<GoodsInfo> mData = new ArrayList<GoodsInfo>();
	private HistoryAdapter mAdapter;
	private SwipeMenuListView mListView;
	private ProgressBar mBar;
	private TextView mTvNull;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		initData();
		initListView();
		mBar = (ProgressBar) findViewById(R.id.progressBar1);
		mTvNull = (TextView) findViewById(R.id.tv_null);
		findViewById(R.id.img_back).setOnClickListener(this);
		findViewById(R.id.tv_clear).setOnClickListener(this);
	}
	
	private void initListView() {
		mListView = (SwipeMenuListView) findViewById(R.id.listView1);
		mAdapter = new HistoryAdapter();
		mListView.setAdapter(mAdapter);
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				SwipeMenuItem deleteItem = new SwipeMenuItem(HistoryActivity.this);
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
						0x3F, 0x25)));
				deleteItem.setWidth(ScreenUtils
						.getScreenWidth(HistoryActivity.this) / 3);
				deleteItem.setTitle("ɾ��");
				deleteItem.setTitleSize(18);
				deleteItem.setTitleColor(Color.WHITE);
				menu.addMenuItem(deleteItem);

			}
		};
		mListView.setMenuCreator(creator);
		mListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public void onMenuItemClick(int position, SwipeMenu menu, int index) {
				deleteItem(position);
			}
		});
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				Intent intent = new Intent(HistoryActivity.this,
						DetailActivity.class);
				intent.putExtra(Constants.INTENT_KEY.INFO_TO_DETAIL,
						mData.get(position));
				startActivity(intent);
			}
		});
	}

	private void initData() {
		new HistoryTask().execute();
	}
	
	/**
	 * ɾ����
	 */
	private void deleteItem(int position) {
		DBUtils.delete(mData.get(position));
		mData.remove(position);
		if (mData.size() == 0) {
			mTvNull.setVisibility(View.VISIBLE);
		}
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			finish();
			break;
		case R.id.tv_clear:
			DeleteHistoryDialogFragment fragment = new DeleteHistoryDialogFragment();
			fragment.show(getSupportFragmentManager(), null);
			break;

		default:
			break;
		}
	}

	/**
	 * ��ռ�¼
	 */
	public void clearHistory() {
		mBar.setVisibility(View.VISIBLE);
		new ClearTask().execute();
	}

	class HistoryAdapter extends BaseAdapter {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View inflate = null;
			ViewHolder holder = null;
			if (convertView == null) {
				inflate = getLayoutInflater().inflate(R.layout.item_favor_list,
						null);
				holder = new ViewHolder();
				holder.imgGoods = (ImageView) inflate
						.findViewById(R.id.img_icon);
				holder.tvGoodsName = (TextView) inflate
						.findViewById(R.id.tv_title);
				holder.tvGoodsPrice = (TextView) inflate
						.findViewById(R.id.tv_price);
				inflate.setTag(holder);
			} else {
				inflate = convertView;
				holder = (ViewHolder) inflate.getTag();
			}
			GoodsInfo info = mData.get(position);
			holder.tvGoodsName.setText(info.getGoodsName());
			holder.tvGoodsPrice.setText(NumberUtils.formatPrice(info
					.getGoodsPrice()));
			UILUtils.displayImage(HistoryActivity.this, info.getGoodsIcon(),
					holder.imgGoods);
			return inflate;
		}

		@Override
		public int getCount() {
			return mData.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

	}

	class ViewHolder {
		ImageView imgGoods;
		TextView tvGoodsName;
		TextView tvGoodsPrice;
	}

	class HistoryTask extends AsyncTask<Void, Void, List<GoodsInfo>> {

		@Override
		protected List<GoodsInfo> doInBackground(Void... params) {
			return DBUtils.getHistory();
		}

		@Override
		protected void onPostExecute(List<GoodsInfo> result) {
			super.onPostExecute(result);
			mData.clear();
			mData.addAll(result);
			mAdapter.notifyDataSetChanged();
			if (mData.size() == 0) {
				mListView.setVisibility(View.GONE);
				mTvNull.setVisibility(View.VISIBLE);
			} else {
				mTvNull.setVisibility(View.GONE);
				mListView.setVisibility(View.VISIBLE);
			}
			mBar.setVisibility(View.GONE);
		}

	}
	
	class ClearTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			mData.clear();
			DBUtils.deleteHistory();
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			mAdapter.notifyDataSetChanged();
			mBar.setVisibility(View.GONE);
			mTvNull.setVisibility(View.VISIBLE);
		}
		
	}

}
