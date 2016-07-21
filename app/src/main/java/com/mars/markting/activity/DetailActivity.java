package com.mars.markting.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.lib.uil.ToastUtils;
import com.lib.uil.UILUtils;
import com.mars.markting.R;
import com.mars.markting.app.SysApplication;
import com.mars.markting.bean.GoodsInfo;
import com.mars.markting.bean.InCart;
import com.mars.markting.fragment.DetailBannerItemFragment;
import com.mars.markting.utils.Constants;
import com.mars.markting.utils.DBUtils;
import com.mars.markting.utils.NumberUtils;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends FragmentActivity implements OnClickListener {

	private int[] mBanner = new int[] { R.drawable.ip1, R.drawable.ip2,
			R.drawable.ip3, R.drawable.ip4 };

	private ViewPager mPager;
	private ImageView mImgFavor;
	private TextView mTvCollect;
	private MenuDrawer mDrawer;

	private GoodsInfo mGoodsInfo;
	private InCart mInCart;
	private ArrayList<GoodsInfo> historyList = new ArrayList<GoodsInfo>();
	private HistoryAdapter mAdapter;

	private TextView mTvCount;

	private TextView mTvInCartNum;

	private ListView mLvDetailComment; // michael
	private ArrayAdapter<String> mDetalCommentAdapter;//michael
	private List<String> mDetalCommentData;//michael

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SysApplication.getInstance().addDetailActivity(this);
		SysApplication.getInstance().removeExtraActivity();
		initData();
		saveHistory();
		initMenu();
		initView();
		setOnListener();
		initPager();
		initListView();
		initInCartNum();
		initHistory();
	}

	private void setOnListener() {
		findViewById(R.id.btn_add_to_cart).setOnClickListener(this);
		findViewById(R.id.btn_goto_cart).setOnClickListener(this);
		findViewById(R.id.btn_collect).setOnClickListener(this);
		findViewById(R.id.img_history).setOnClickListener(this);
		findViewById(R.id.img_back).setOnClickListener(this);
	}

	private ScrollView mSV;//michael
	private void initView() {
		mSV = (ScrollView) findViewById(R.id.scrollView1);
		mSV.smoothScrollTo(0,0);
		mLvDetailComment = (ListView) findViewById(R.id.lv_detail_comment); //michael
		mLvDetailComment.setAdapter(mDetalCommentAdapter);//michael
		mLvDetailComment.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return false;
			}
		});
		mTvInCartNum = (TextView) findViewById(R.id.tv_incart);
		mTvCount = (TextView) findViewById(R.id.tv_count_page);
		mTvCollect = (TextView) findViewById(R.id.tv_collect);
		mImgFavor = (ImageView) findViewById(R.id.img_favor);
		TextView tvTitle = (TextView) findViewById(R.id.tv_title);
		TextView tvPrice = (TextView) findViewById(R.id.tv_price);
		TextView tvVip = (TextView) findViewById(R.id.tv_vip);
		ImageView imgVip = (ImageView) findViewById(R.id.img_vip);
		tvTitle.setText(mGoodsInfo.getGoodsName());
		tvPrice.setText(NumberUtils.formatPrice(mGoodsInfo.getGoodsPrice()));
		// �ж��Ƿ��ղ�
		if (DBUtils.hasFavor(mGoodsInfo)) {
			mTvCollect.setText("已关注");//michael
			mImgFavor.setImageResource(R.drawable.pd_collect_p);
		}
		int isPhone = mGoodsInfo.getIsPhone();
		if (isPhone == 1) {
			imgVip.setVisibility(View.VISIBLE);
			tvVip.setVisibility(View.VISIBLE);
		} else {
			imgVip.setVisibility(View.GONE);
			tvVip.setVisibility(View.GONE);
		}
	}

	private void initData() {
		Intent intent = getIntent();
		mGoodsInfo = (GoodsInfo) intent
				.getSerializableExtra(Constants.INTENT_KEY.INFO_TO_DETAIL);
		mInCart = new InCart(mGoodsInfo.getGoodsId(),
				mGoodsInfo.getGoodsName(), mGoodsInfo.getGoodsIcon(),
				mGoodsInfo.getGoodsType(), mGoodsInfo.getGoodsPrice(),
				mGoodsInfo.getGoodsPercent(), mGoodsInfo.getGoodsComment(),
				mGoodsInfo.getIsPhone(), mGoodsInfo.getIsFavor(), 1);
		//michael
		mDetalCommentData = new ArrayList<String>();
		for(int i = 0; i < 10; i++){
			mDetalCommentData.add("Detail comment : " + i);
		}

		mDetalCommentAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mDetalCommentData);
		//michael

	}

	/**
	 * ���浽�����ʷ
	 */
	private void saveHistory() {
		GoodsInfo info = new Select().from(GoodsInfo.class)
				.where("goodsId=? AND isFavor=0", mGoodsInfo.getGoodsId())
				.executeSingle();
		// ɾ��ͬ������
		if (info != null) {
			info.delete();
		}
		// ���뵽��ʷ��ݿ�
		mGoodsInfo.setIsFavor(0);
		DBUtils.save(mGoodsInfo);
	}

	/**
	 * ���ﳵ����Ʒ����
	 */
	private void initInCartNum() {
		int num = DBUtils.getInCartNum();
		if (num > 0) {
			mTvInCartNum.setVisibility(View.VISIBLE);
			mTvInCartNum.setText("" + num);
		} else {
			mTvInCartNum.setVisibility(View.INVISIBLE);
		}
	}

	private void initPager() {
		mPager = (ViewPager) findViewById(R.id.pager);
		FragmentManager fm = getSupportFragmentManager();
		MyPagerAdapter adapter = new MyPagerAdapter(fm);
		mPager.setAdapter(adapter);
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				mTvCount.setText(arg0 + 1 + "");
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	private void initListView() {
		ListView listView = (ListView) findViewById(R.id.listView1);
		mAdapter = new HistoryAdapter();
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				mDrawer.toggleMenu();
				Intent intent = new Intent(DetailActivity.this,
						DetailActivity.class);
				intent.putExtra(Constants.INTENT_KEY.INFO_TO_DETAIL,
						historyList.get(position));
				startActivity(intent);
			}
		});
	}

	private void initHistory() {
		new HistoryTask().execute();
	}

	private void initMenu() {
		mDrawer = MenuDrawer
				.attach(this, MenuDrawer.Type.OVERLAY, Position.END);
		mDrawer.setMenuView(R.layout.menudrawer_history);
		mDrawer.setContentView(R.layout.activity_detail);
		// �˵����
		mDrawer.setMenuSize(260);
		// �˵�����Ӱ
		mDrawer.setDropShadowEnabled(false);
	}

	class MyPagerAdapter extends FragmentStatePagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			DetailBannerItemFragment fragment = new DetailBannerItemFragment();
			fragment.setResId(mBanner[position]);
			fragment.setPosition(position);
			return fragment;
		}

		@Override
		public int getCount() {
			return mBanner.length;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			finish();
			break;
		case R.id.btn_goto_cart: // ���ﳵ
			gotoHomePage();
			break;
		case R.id.btn_collect: // �ղ�
			collect();
			break;
		case R.id.btn_add_to_cart: // ���빺�ﳵ
			add2Cart();
			break;
		case R.id.img_history: // �����¼
			mDrawer.toggleMenu();
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mDrawer.isMenuVisible()) {
				mDrawer.closeMenu();
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * ���빺�ﳵ
	 */
	private void add2Cart() {
		InCart inCart = new Select().from(InCart.class)
				.where("goodsId=?", mInCart.getGoodsId()).executeSingle();
		if (inCart != null) {
			// �����ﳵ���У�������+1
			inCart.setNum(inCart.getNum() + 1);
			inCart.save();
		} else {
			mInCart.save();
		}
		// ˢ�¹��ﳵ��Ʒ����
		initInCartNum();
		// Toast.makeText(this, "����ӵ����ﳵ", Toast.LENGTH_SHORT).show();
		ToastUtils.showToast(this, "DetalActivity add2Cart");
		// ֪ͨ��ҳˢ�¹��ﳵ��Ʒ��
		Intent intent = new Intent();
		intent.setAction(Constants.BROADCAST_FILTER.FILTER_CODE);
		intent.putExtra(Constants.BROADCAST_FILTER.EXTRA_CODE,
				Constants.INTENT_KEY.REFRESH_INCART);
		sendBroadcast(intent);
	}

	/**
	 * ��ת����ҳ���ﳵ
	 */
	private void gotoHomePage() {
		startActivity(new Intent(this, MainActivity.class));
		Intent intent = new Intent();
		intent.setAction(Constants.BROADCAST_FILTER.FILTER_CODE);
		intent.putExtra(Constants.BROADCAST_FILTER.EXTRA_CODE,
				Constants.INTENT_KEY.FROM_DETAIL);
		sendBroadcast(intent);
		finish();
		overridePendingTransition(0, 0);
	}

	/**
	 * �ղ�/ȡ���ղ�
	 */
	private void collect() {
		// �ж��Ƿ��ղ�
		mGoodsInfo = mGoodsInfo.clone();
		if (DBUtils.hasFavor(mGoodsInfo)) {
			// ����DBUtil�ķ�������Ҫ����favor = 1��������޷�ȡ���ע
			mGoodsInfo.setIsFavor(1);
			DBUtils.delete(mGoodsInfo);
			mTvCollect.setText("关注");//michael
			mImgFavor.setImageResource(R.drawable.pd_collect_n);
			ToastUtils.showToast(this, "取消关注");
		} else {
			mGoodsInfo.setIsFavor(1);
			DBUtils.save(mGoodsInfo);
			mTvCollect.setText("已关注");//michael
			mImgFavor.setImageResource(R.drawable.pd_collect_p);
			ToastUtils.showToast(this, "已关注");
		}
		// ֪ͨ�ղؽ���ˢ��
		Intent intent = new Intent();
		intent.setAction("updateFavor");
		sendBroadcast(intent);
	}

	class HistoryAdapter extends BaseAdapter {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View inflate = null;
			ViewHolder holder = null;
			if (convertView == null) {
				inflate = getLayoutInflater().inflate(
						R.layout.item_detail_menu_list, null);
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
			GoodsInfo info = historyList.get(position);
			holder.tvGoodsName.setText(info.getGoodsName());
			holder.tvGoodsPrice.setText(NumberUtils.formatPrice(info
					.getGoodsPrice()));
			UILUtils.displayImage(DetailActivity.this, info.getGoodsIcon(),
					holder.imgGoods);
			return inflate;
		}

		@Override
		public int getCount() {
			return historyList.size();
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
			historyList.clear();
			historyList.addAll(result);
			mAdapter.notifyDataSetChanged();
			DetailActivity.this.findViewById(R.id.progressBar1).setVisibility(
					View.GONE);
		}

	}

}
