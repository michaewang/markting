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
import com.mars.markting.utils.Constants;
import com.mars.markting.utils.DBUtils;
import com.mars.markting.utils.NumberUtils;
import com.lib.uil.ScreenUtils;
import com.lib.uil.UILUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class FavorActivity extends Activity implements OnClickListener {

	private final String strGoods = "��û�й�ע���κ���Ʒ���β�ȥ���~";
	private final String strStore = "��û�й�ע���κε��̣��β�ȥ���~";
	private ArrayList<GoodsInfo> mData = new ArrayList<GoodsInfo>();
	private FavorAdapter mAdapter;
	private SwipeMenuListView mListView;
	private ProgressBar mBar;
	private View layoutNull;
	private TextView mTvGoods;
	private TextView mTvStore;
	private View indicator;
	private View indicator2;
	private TextView mBtnMore;
	private TextView mTvDisc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favor);
		initData();
		initListView();
		mBar = (ProgressBar) findViewById(R.id.progressBar1);
		layoutNull = findViewById(R.id.layout_null);
		mTvGoods = (TextView) findViewById(R.id.tv_goods);
		mTvStore = (TextView) findViewById(R.id.tv_store);
		mTvDisc = (TextView) findViewById(R.id.tv_disc);
		indicator = findViewById(R.id.indicator1);
		indicator2 = findViewById(R.id.indicator2);
		findViewById(R.id.img_back).setOnClickListener(this);
		mBtnMore = (TextView) findViewById(R.id.btn_more);
		mBtnMore.setOnClickListener(this);
		findViewById(R.id.layout_goods).setOnClickListener(this);
		findViewById(R.id.layout_store).setOnClickListener(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		initData();
	}

	private void initListView() {
		mListView = (SwipeMenuListView) findViewById(R.id.listView1);
		mAdapter = new FavorAdapter();
		mListView.setAdapter(mAdapter);
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				SwipeMenuItem deleteItem = new SwipeMenuItem(FavorActivity.this);
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
						0x3F, 0x25)));
				deleteItem.setWidth(ScreenUtils
						.getScreenWidth(FavorActivity.this) / 3);
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
				Intent intent = new Intent(FavorActivity.this,
						DetailActivity.class);
				intent.putExtra(Constants.INTENT_KEY.INFO_TO_DETAIL,
						mData.get(position));
				startActivityForResult(intent, 0);
			}
		});
	}

	private void initData() {
		new FavorTask().execute();
	}

	/**
	 * ɾ����
	 */
	private void deleteItem(int position) {
		DBUtils.delete(mData.get(position));
		mData.remove(position);
		if (mData.size() == 0) {
			layoutNull.setVisibility(View.VISIBLE);
		}
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			finish();
			break;
		case R.id.btn_more:
			gotoHomePage();
			break;
		case R.id.layout_goods: // ��Ʒ
			goodsClick();
			break;
		case R.id.layout_store: // ����
			stroeClick();
			break;

		default:
			break;
		}
	}

	private void goodsClick() {
		if (mData.size() > 0) {
			mListView.setVisibility(View.VISIBLE);
			layoutNull.setVisibility(View.GONE);
		} else {
			layoutNull.setVisibility(View.VISIBLE);
		}
		mBtnMore.setText("ȥ��ҳ���");
		mTvDisc.setText(strGoods);
		mTvGoods.setTextColor(Color.RED);
		mTvStore.setTextColor(getResources().getColor(R.color.dark));
		indicator.setVisibility(View.VISIBLE);
		indicator2.setVisibility(View.INVISIBLE);
	}

	private void stroeClick() {
		layoutNull.setVisibility(View.VISIBLE);
		mListView.setVisibility(View.GONE);
		mBtnMore.setText("ȥ�õ��̹��");
		mTvDisc.setText(strStore);
		mTvStore.setTextColor(Color.RED);
		mTvGoods.setTextColor(getResources().getColor(R.color.dark));
		indicator.setVisibility(View.INVISIBLE);
		indicator2.setVisibility(View.VISIBLE);
	}

	/**
	 * ��Ʒ�б�
	 */
	private void gotoHomePage() {
		startActivity(new Intent(this, MainActivity.class));
		Intent intent = new Intent();
		intent.setAction(Constants.BROADCAST_FILTER.FILTER_CODE);
		intent.putExtra(Constants.BROADCAST_FILTER.EXTRA_CODE,
				Constants.INTENT_KEY.FROM_FAVOR);
		sendBroadcast(intent);
		finish();
		overridePendingTransition(0, 0);
	}

	class FavorAdapter extends BaseAdapter {

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
			UILUtils.displayImage(FavorActivity.this, info.getGoodsIcon(),
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

	class FavorTask extends AsyncTask<Void, Void, List<GoodsInfo>> {

		@Override
		protected List<GoodsInfo> doInBackground(Void... params) {
			return DBUtils.getFavor();
		}

		@Override
		protected void onPostExecute(List<GoodsInfo> result) {
			super.onPostExecute(result);
			mData.clear();
			mData.addAll(result);
			mAdapter.notifyDataSetChanged();
			if (mData.size() == 0) {
				layoutNull.setVisibility(View.VISIBLE);
				mListView.setVisibility(View.GONE);
			} else {
				layoutNull.setVisibility(View.GONE);
				mListView.setVisibility(View.VISIBLE);
			}
			mBar.setVisibility(View.GONE);
		}

	}

}
