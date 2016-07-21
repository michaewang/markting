package com.mars.markting.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lib.json.JSONUtils;
import com.lib.uil.UILUtils;
import com.lib.volley.HTTPUtils;
import com.lib.volley.VolleyListener;
import com.mars.markting.R;
import com.mars.markting.activity.BoxActivity;
import com.mars.markting.activity.DetailActivity;
import com.mars.markting.activity.FavorActivity;
import com.mars.markting.activity.MainActivity;
import com.mars.markting.activity.OrdersActivity;
import com.mars.markting.activity.PurseActivity;
import com.mars.markting.activity.SearchActivity;
import com.mars.markting.activity.WebActivity;
import com.mars.markting.bean.GoodsInfo;
import com.mars.markting.bean.URLGoodInfo;
import com.mars.markting.utils.Constants;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.mars.markting.utils.MyImageLoader;
import com.nineoldandroids.animation.ObjectAnimator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogRecord;

import com.android.volley.VolleyError;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class HomeFragment extends Fragment implements OnClickListener {

	private PullToRefreshScrollView mPtrScrollView;

	private ViewPager mPager;
	private int[] mBanner = new int[] { R.drawable.img_home_banner1,
			R.drawable.img_home_banner2, R.drawable.img_home_banner3,
			R.drawable.img_home_banner4 };
	private GoodsInfo[] mInfos;
	private ImageView mImageView;
	private ImageView mImgCover;
	private int mLastPos;// ��¼��һ��ViewPager��λ��
	private boolean isDragging;// �Ƿ���ק
	private boolean isFoucusRight; // ScrollView�Ƿ�������Ҳ�
	private View layout;
	private int secondCount = 10800; // ����ʱ3Сʱ

	private HorizontalScrollView mScrollView;
	private HorizontalScrollView mScrollView2;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		initGoodsInfos();
		if (layout != null) {
			// ��ֹ���new��Ƭ�ζ������ͼƬ��������
			return layout;
		}
		layout = inflater.inflate(R.layout.fragment_home, container, false);
		initGoodDataFromUrl();
		initView();
		initPager();
		autoScroll();
		initTimer();


		String temString ;// key + " = " + results.get(key) + "\n" ;
		String filePath = "sdcard/StringTest2.txt";

		File saveFile = new File(filePath);

		if (!saveFile.exists()) {
			//File dir = new File(saveFile.getParent());
			//dir.mkdirs();
			try {
				saveFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Log.d("michael","new new new");

		FileOutputStream fos = null;
		for(int i = 0; i < 10; i++){
			temString = i + "\n";
			try {
				fos = new FileOutputStream(saveFile, true);
				fos.write(temString.getBytes());
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}





			return layout;
	}

	//michael start
	private List<URLGoodInfo> mGoodFromURL = new ArrayList<URLGoodInfo>();
	private TextView mTVSpec1;
	private TextView mTVSpec2;
	private TextView mTVSpec3;
	private TextView mTVSpec4;
	private ImageView mIVSpec1;
	private ImageView mIVSpec2;
	private ImageView mIVSpec3;
	private ImageView mIVSpec4;
	private List<TextView> mListTV = new ArrayList<TextView>();
	private List<ImageView> mListIV = new ArrayList<ImageView>();
	private MyImageLoader mImageLoader;

	private int SYNC_MSG = 0;

	private Handler myHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
				case 0:

					if(mGoodFromURL.size() > 0){

						for(int i = 0; i < mGoodFromURL.size(); i++){
							Log.d("michael", "goodname = " + mGoodFromURL.get(i).getGoodname() +
									" imgurl = " + mGoodFromURL.get(i).getImageurl());

							mListTV.get(i).setText(mGoodFromURL.get(i).getGoodname());
							mListIV.get(i).setTag(mGoodFromURL.get(i).getImageurl());//设置Tag
							mImageLoader.showImageAsycTask(mListIV.get(i), mGoodFromURL.get(i).getImageurl());
							//UILUtils.displayImage(getActivity(), mGoodFromURL.get(i).getImageurl(), mListIV.get(i));
						}
					}
				break;
			}
			super.handleMessage(msg);
		}
	};

	private void initGoodDataFromUrl(){

		mImageLoader = new MyImageLoader();

		mTVSpec1 = (TextView) layout.findViewById(R.id.tv_special_title);
		mTVSpec2 = (TextView) layout.findViewById(R.id.tv_special_title2);
		mTVSpec3 = (TextView) layout.findViewById(R.id.tv_special_title3);
		mTVSpec4 = (TextView) layout.findViewById(R.id.tv_special_title4);

		mIVSpec1 = (ImageView) layout.findViewById(R.id.SpecImageView03);
		mIVSpec2 = (ImageView) layout.findViewById(R.id.SpecImageView02);
		mIVSpec3 = (ImageView) layout.findViewById(R.id.SpecImageView04);
		mIVSpec4 = (ImageView) layout.findViewById(R.id.SpecImageView01);

		mListTV.add(mTVSpec1);
		mListTV.add(mTVSpec2);
		mListTV.add(mTVSpec3);
		mListTV.add(mTVSpec4);

		mListIV.add(mIVSpec1);
		mListIV.add(mIVSpec2);
		mListIV.add(mIVSpec3);
		mListIV.add(mIVSpec4);


		HTTPUtils.getVolley(getActivity(), Constants.URL.GOODJSON,
				new VolleyListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {

					}

					@Override
					public void onResponse(String arg0) {

						Type type = new TypeToken<ArrayList<URLGoodInfo>>() {
						}.getType();
						ArrayList<URLGoodInfo> list = JSONUtils.parseJSONArray(arg0, type);
						mGoodFromURL.addAll(list);

						Message message = new Message();
						message.what = SYNC_MSG;
						myHandler.sendMessage(message);
					}
				});
	}
	//michael end
	private void initGoodsInfos() {
		mInfos = new GoodsInfo[] {
				new GoodsInfo(
						"100001",
						"XiaoShiDai",
						"http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods01.jpg",
						"goodsType 1", 153.00, "好评度96%", 1224, 1, 0),
				new GoodsInfo(
						"100002",
						"AiShengHuo",
						"http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods02.jpg",
						"goodsType 1", 479.00, "好评度95%", 645, 0, 0),
				new GoodsInfo(
						"100003",
						"FengShangJi",
						"http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods03.jpg",
						"goodsType 1", 149.00, "好评度99", 1856, 0, 0),
				new GoodsInfo(
						"100004",
						"YuYueXueYaJi",
						"http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods04.jpg",
						"goodsType 1", 2138.00, "好评度97%", 865, 0, 0),
				new GoodsInfo(
						"100005",
						"FANCI JieZhi",
						"http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods05.jpg",
						"goodsType 1", 3299.00, "好评度95%", 236, 0, 0),
				new GoodsInfo(
						"100006",
						"YouYanJI",
						"http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods06.jpg",
						"goodsType 1", 499.00, "好评度95%", 115, 0, 0),
				new GoodsInfo(
						"100007",
						"NaiFenFangXinGou",
						"http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods07.jpg",
						"goodsType 1", 199.00, "好评度95%", 745, 0, 0),
				new GoodsInfo(
						"100008",
						"DanBingLvDaiTanKe",
						"http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods08.jpg",
						"goodsType 1", 569.00, "好评度95%", 854, 1, 0),
				new GoodsInfo(
						"100009",
						"XiaoChuiZiQiangXianBao",
						"http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods09.jpg",
						"goodsType 1", 5099.00, "好评度94%", 991, 0, 0),
				new GoodsInfo(
						"100010",
						"KaiChunPaiXinKuan",
						"http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods10.jpg",
						"goodsType 1", 2999.00, "好评度93%", 1145, 0, 0),
				new GoodsInfo(
						"100011",
						"BaoKuanChaoDiJia",
						"http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods11.jpg",
						"goodsType 1", 1088.00, "好评度92%", 909, 0, 0),
				new GoodsInfo(
						"100012",
						"499BaHeShouJi",
						"http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods12.jpg",
						"goodsType 1", 25.40, "好评度95%", 1443, 0, 0),
				new GoodsInfo(
						"100013",
						"HaoHuoTuiJianTuiJianLan",
						"http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods13.jpg",
						"goodsType 1", 19.70, "好评度98%", 3702, 0, 0),
				new GoodsInfo(
						"100014",
						"TeSeShiChangGunDong 1",
						"http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods14.jpg",
						"goodsType 1", 38.40, "好评度97%", 442, 1, 0),
				new GoodsInfo(
						"100015",
						"TeSeShiChangGunDong 2",
						"http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods15.jpg",
						"goodsType 1", 57.80, "好评度93%", 765, 0, 0) };
	}

	private void initView() {
		layout.findViewById(R.id.img_home_category).setOnClickListener(this);
		layout.findViewById(R.id.img_home_search_code).setOnClickListener(this);
		/*
		layout.findViewById(R.id.layout_my_focus).setOnClickListener(this); //michael
		layout.findViewById(R.id.layout_logistics).setOnClickListener(this);
		layout.findViewById(R.id.layout_top_up).setOnClickListener(this);
		layout.findViewById(R.id.layout_film).setOnClickListener(this);
		layout.findViewById(R.id.layout_game_top_up).setOnClickListener(this);
		layout.findViewById(R.id.layout_purse).setOnClickListener(this);
		layout.findViewById(R.id.layout_jingdou).setOnClickListener(this);
		layout.findViewById(R.id.layout_more).setOnClickListener(this);
		layout.findViewById(R.id.img_discount_banner).setOnClickListener(this);
		*/
		layout.findViewById(R.id.layout_home_search).setOnClickListener(this);
		layout.findViewById(R.id.layout_discount).setOnClickListener(this);
		layout.findViewById(R.id.layout_discount_phone).setOnClickListener(this);

		layout.findViewById(R.id.layout_recom).setOnClickListener(this);
		layout.findViewById(R.id.layout_recom2).setOnClickListener(this);
		layout.findViewById(R.id.layout_recom3).setOnClickListener(this);
		layout.findViewById(R.id.layout_recom4).setOnClickListener(this);
		layout.findViewById(R.id.layout_recom5).setOnClickListener(this);
		layout.findViewById(R.id.img_banner6).setOnClickListener(this);
		layout.findViewById(R.id.img_banner7).setOnClickListener(this);
		layout.findViewById(R.id.layout_special).setOnClickListener(this);
		layout.findViewById(R.id.layout_special2).setOnClickListener(this);
		layout.findViewById(R.id.layout_special3).setOnClickListener(this);
		layout.findViewById(R.id.layout_special4).setOnClickListener(this);
		layout.findViewById(R.id.img_banner8).setOnClickListener(this);
		layout.findViewById(R.id.img_banner9).setOnClickListener(this);
		mPtrScrollView = (PullToRefreshScrollView) layout
				.findViewById(R.id.ptrScrollView_home);
		mPtrScrollView
				.setOnRefreshListener(new OnRefreshListener<ScrollView>() {
					public void onRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						new GetDataTask().execute();
					}
				});

		mImgCover = (ImageView) layout.findViewById(R.id.img_cover);
		mImageView = (ImageView) layout.findViewById(R.id.img_indicator01);
		mScrollView = (HorizontalScrollView) layout
				.findViewById(R.id.layout_recom_banner);
		mScrollView2 = (HorizontalScrollView) layout
				.findViewById(R.id.layout_special_banner);
	}

	private void activeCategory() {
		MainActivity activity = (MainActivity) getActivity();
		activity.activeCategory();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		// ��layout�Ӹ�������Ƴ�
		ViewGroup parent = (ViewGroup) layout.getParent();
		parent.removeView(layout);
	}

	private void initPager() {
		mPager = (ViewPager) layout.findViewById(R.id.pager_banner);
		FragmentManager fm = getChildFragmentManager();
		MyPagerAdapter adapter = new MyPagerAdapter(fm);
		mPager.setAdapter(adapter);
		mPager.setCurrentItem(1000);
		mPager.setOnPageChangeListener(new MyPagerListener());
	}

	/**
	 * �Զ�����
	 */
	private void autoScroll() {
		mPager.postDelayed(new Runnable() {

			@Override
			public void run() {
				if (!isDragging) {
					// ���û�û����ק�����Զ�����
					mPager.setCurrentItem(mPager.getCurrentItem() + 1);
				}
				if (isFoucusRight) {
					mScrollView.fullScroll(ScrollView.FOCUS_LEFT);
				} else {
					mScrollView.fullScroll(ScrollView.FOCUS_RIGHT);
				}
				isFoucusRight = !isFoucusRight;
				mPager.postDelayed(this, 3000);
			}
		}, 3000);
		mScrollView2.postDelayed(new Runnable() {

			@Override
			public void run() {
				if (isFoucusRight) {
					mScrollView2.fullScroll(ScrollView.FOCUS_RIGHT);
				} else {
					mScrollView2.fullScroll(ScrollView.FOCUS_LEFT);
				}
				mScrollView2.postDelayed(this, 3000);
			}
		}, 4000);
	}

	/**
	 * ����ʱ
	 */
	private void initTimer() {
		final TextView tvHour = (TextView) layout.findViewById(R.id.tv_hour);
		final TextView tvMinute = (TextView) layout
				.findViewById(R.id.tv_minute);
		final TextView tvSecond = (TextView) layout
				.findViewById(R.id.tv_second);
		tvHour.post(new Runnable() {

			@Override
			public void run() {
				if (secondCount > 0) {

					secondCount--;
					int h = secondCount / 3600;
					int m = secondCount / 60 % 60;
					int s = secondCount % 60;
					StringBuffer hour = new StringBuffer();
					StringBuffer minute = new StringBuffer();
					StringBuffer second = new StringBuffer();
					if (h < 10) {
						hour.append(0);
					}
					if (m < 10) {
						minute.append(0);
					}
					if (s < 10) {
						second.append(0);
					}
					hour.append(h);
					minute.append(m);
					second.append(s);
					tvHour.setText(hour);
					tvMinute.setText(minute);
					tvSecond.setText(second);
					tvHour.postDelayed(this, 1000);
				}
			}
		});
	}

	class MyPagerAdapter extends FragmentStatePagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			BannerItemFragment fragment = new BannerItemFragment();
			fragment.setResId(mBanner[position % mBanner.length]);
			fragment.setGoodsInfo(mInfos[position % mBanner.length]);
			return fragment;
		}

		@Override
		public int getCount() {
			return 10000;
		}

	}

	class MyPagerListener implements OnPageChangeListener {

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
		}

		@Override
		public void onPageSelected(int position) {
			int width = mImgCover.getWidth();
			LayoutParams layoutParams = (LayoutParams) mImageView
					.getLayoutParams();
			int rightMargin = layoutParams.rightMargin;
			int endPos = (width + rightMargin) * (position % 4);
			int startPos = 0;
			if (mLastPos < position) {
				// ͼƬ�����ƶ�
				startPos = (width + rightMargin) * (position % 4 - 1);
			} else {
				// ͼƬ�����ƶ�
				startPos = (width + rightMargin) * (position % 4 + 1);
			}
			ObjectAnimator.ofFloat(mImgCover, "translationX", startPos, endPos)
					.setDuration(300).start();
			mLastPos = position;
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			switch (state) {
			case ViewPager.SCROLL_STATE_DRAGGING:
				// �û���ק
				isDragging = true;
				break;
			case ViewPager.SCROLL_STATE_IDLE:
				// ����״̬
				isDragging = false;
				break;
			case ViewPager.SCROLL_STATE_SETTLING:
				// ���ͷ�ʱ
				isDragging = false;
				break;

			default:
				break;
			}
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_home_category: // �л�������
			activeCategory();
			break;
		case R.id.img_home_search_code: // ��ά��ɨ��
			//((MainActivity) getActivity()).scanQRCode();
			StringTest();
			break;
		case R.id.layout_home_search:
			gotoSearch();
			break;
		/*
		case R.id.layout_my_focus: // �ҵĹ�ע
			startActivity(new Intent(getActivity(), FavorActivity.class));
			break;
		case R.id.layout_logistics: // ������ѯ
			startActivity(new Intent(getActivity(), OrdersActivity.class));
			break;
		case R.id.layout_top_up: // ��ֵ
			Intent intent = new Intent(getActivity(), WebActivity.class);
			intent.putExtra("direction", 1);
			startActivity(intent);
			break;
		case R.id.layout_film: // ��ӰƱ
			Intent intent2 = new Intent(getActivity(), WebActivity.class);
			intent2.putExtra("direction", 3);
			startActivity(intent2);
			break;
		case R.id.layout_game_top_up: // ��Ϸ��ֵ
			Intent intent3 = new Intent(getActivity(), WebActivity.class);
			intent3.putExtra("direction", 2);
			startActivity(intent3);
			break;
		case R.id.layout_purse: // С���
			startActivity(new Intent(getActivity(), PurseActivity.class));
			break;
		case R.id.layout_jingdou: // ��ȡ����
			Intent intent4 = new Intent(getActivity(), WebActivity.class);
			intent4.putExtra("direction", 4);
			startActivity(intent4);
			break;
		case R.id.layout_more: // ���
			startActivity(new Intent(getActivity(), BoxActivity.class));
			break;
			case R.id.img_discount_banner: // �̷۷��Ĺ�
			gotoDetail(6);
			break;
		*/
		case R.id.layout_discount: // �ײ���ɱ
			gotoDetail(4);
			break;
		case R.id.layout_discount_phone: // �ֻ�ר��
			gotoDetail(5);
			break;

		case R.id.layout_recom: // ֵ����
			gotoDetail(7);
			break;
		case R.id.layout_recom2: // ��ѡ�Ƽ�
			gotoDetail(8);
			break;
		case R.id.layout_recom3: // ����
			gotoDetail(9);
			break;
		case R.id.layout_recom4: // �Ź�
			gotoDetail(10);
			break;
		case R.id.layout_recom5: // �����ڳ�
			gotoDetail(11);
			break;
		case R.id.img_banner6:
			gotoDetail(12);
			break;
		case R.id.img_banner7:
			gotoDetail(13);
			break;
		case R.id.layout_special:	//Сʳ��
			gotoDetail(0);
			break;
		case R.id.layout_special2:	//�����
			gotoDetail(1);
			break;
		case R.id.layout_special3:	//���м�
			gotoDetail(2);
			break;
		case R.id.layout_special4:	//Ѫѹ��
			gotoDetail(3);
			break;
		case R.id.img_banner8:
			gotoDetail(13);
			break;
		case R.id.img_banner9:
			gotoDetail(14);
			break;

		default:
			break;
		}
	}
	
	/**
	 * ת����Ʒ����
	 */
	private void gotoDetail(int index) {
		Intent intent = new Intent(getActivity(), DetailActivity.class);
		intent.putExtra(Constants.INTENT_KEY.INFO_TO_DETAIL, mInfos[index]);
		startActivity(intent);
	}

	private void gotoSearch() {
		Intent intent = new Intent(getActivity(), SearchActivity.class);
		startActivity(intent);
		// activity�����޶���
		getActivity().overridePendingTransition(0, 0);
	}

	private class GetDataTask extends AsyncTask<Void, Void, String[]> {
		protected String[] doInBackground(Void... params) {
			// ����ˢ��
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
			}
			return null;
		}

		protected void onPostExecute(String[] result) {
			mPtrScrollView.onRefreshComplete();// �ر�ˢ�¶���
		}

	}

	static int string1 = 0;
	private void StringTest() {
		String temString = "abc" + ":"+"\n";
		Log.d("michael","temString = " + temString +
				"Environment.getExternalStorageDirectory().toString() = " + Environment.getExternalStorageDirectory().toString());
		//String filePath = Environment.getExternalStorageDirectory().toString() + File.separator + "StringTest.txt";
		String filePath = "sdcard/StringTest2.txt";

			File saveFile = new File(filePath);

			if (!saveFile.exists()) {
				//File dir = new File(saveFile.getParent());
				//dir.mkdirs();
				try {
					saveFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}


			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(saveFile, true);
				fos.write(temString.getBytes());
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}

	}


}
