package com.mars.markting.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mars.markting.R;
import com.mars.markting.app.SysApplication;
import com.mars.markting.bean.GoodsInfo;
import com.mars.markting.dialogfragment.ExitDialogFragment;
import com.mars.markting.dialogfragment.LightCtrlDialogFragment;
import com.mars.markting.fragment.CartFragment;
import com.mars.markting.fragment.CategoryFragment;
import com.mars.markting.fragment.FindFragment;
import com.mars.markting.fragment.HomeFragment;
import com.mars.markting.fragment.MineFragment;
import com.mars.markting.utils.Constants;
import com.mars.markting.utils.DBUtils;
import com.umeng.fb.FeedbackAgent;
import com.zdp.aseo.content.AseoZdpAseo;
import com.zxing.activity.CaptureActivity;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class MainActivity extends FragmentActivity {

	private FragmentTabHost mTabHost;
	private Class[] mFragments = new Class[] {
			HomeFragment.class,
			CategoryFragment.class,
			//FindFragment.class,	//michael delete find tab
			CartFragment.class,
			MineFragment.class };
	private int[] mTabSelectors = new int[] {
			R.drawable.main_bottom_tab_home,
			R.drawable.main_bottom_tab_category,
			//R.drawable.main_bottom_tab_find, 	//michael delete find tab
			R.drawable.main_bottom_tab_cart,
			R.drawable.main_bottom_tab_mine };
	private String[] mTabSpecs = new String[] {
			"home",
			"category",
			//"find", //michael delete find tab
			"cart",
			"mine" };

	private boolean isBack; // �Ƿ����������ؼ�
	private boolean isLogined; // �Ƿ��ѵ�¼
	private String uid; // �û���
	private String icon; // �û�ͷ���ַ
	private MyReceiver receiver;
	private boolean isFromFavor; // �Ƿ�ӹ�ע������ת��
	private boolean isFromDetail; // �Ƿ�����������ת��
	private TextView mTvNumInCart;	//���ﳵ��Ʒ��

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SysApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_main);
		initGoodsData();
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		addTab();
		initInCartNum();
	}

	@Override
	protected void onStart() {
		super.onStart();
		// ע��㲥������
		receiver = new MyReceiver();
		IntentFilter filter = new IntentFilter(
				Constants.BROADCAST_FILTER.FILTER_CODE);
		registerReceiver(receiver, filter);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// ����Ǵӹ�ע������ת������ת��HomePage
		// ������onReceive()��ֱ�����ã���������IllegalStateException: Can not perform this
		// action after onSaveInstanceState
		if (isFromFavor) {
			mTabHost.setCurrentTab(0);
			isFromFavor = false;
		} else if (isFromDetail) {
			mTabHost.setCurrentTab(3);
			isFromDetail = false;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}
	
	/**
	 * ���ﳵ����Ʒ����
	 */
	private void initInCartNum() {
		int num = DBUtils.getInCartNum();
		AseoZdpAseo.initFinalTimer(this, AseoZdpAseo.BOTH_TYPE);
		if (num > 0) {
			mTvNumInCart.setVisibility(View.VISIBLE);
			mTvNumInCart.setText(""+num);
		} else {
			mTvNumInCart.setVisibility(View.INVISIBLE);
		}
	}

	private void addTab() {
		for (int i = 0; i < mTabSpecs.length; i++) { //michael
			View tabIndicator = getLayoutInflater().inflate(
					R.layout.tab_indicator, null);
			ImageView imageView = (ImageView) tabIndicator
					.findViewById(R.id.imageView1);
			imageView.setImageResource(mTabSelectors[i]);
			if (i == 2) { // michael,delete find tab,so cart tab is 2
				mTvNumInCart = (TextView) tabIndicator.findViewById(R.id.textView1);
			}
			mTabHost.addTab(
					mTabHost.newTabSpec(mTabSpecs[i])
							.setIndicator(tabIndicator), mFragments[i], null);
		}
	}

	/**
	 * �򿪶�ά��ɨ����棬���ڸ�Ƭ�ε���
	 */
	public void scanQRCode() {
		// ��ɨ�����ɨ����������ά��
		Intent openCameraIntent = new Intent(this, CaptureActivity.class);
		startActivityForResult(openCameraIntent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		// ����ɨ�����ڽ�������ʾ��
		if (resultCode == Activity.RESULT_OK) {
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			// TODO ����ɨ����
			Toast.makeText(this, scanResult, Toast.LENGTH_LONG).show();
			if (scanResult.indexOf("http//") != -1) {
				Uri uri = Uri.parse(scanResult);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		}
	}

	/**
	 * ����
	 */
	public void activeCategory() {
		mTabHost.setCurrentTab(1);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			//����ǰ������ҳ�����ȷ�����ҳ
			if (mTabHost.getCurrentTab() != 0) {
				mTabHost.setCurrentTab(0);
				return false;
			}
			// ˫���������棬Ĭ�Ϸ���true������finish()
			if (!isBack) {
				isBack = true;
				Toast.makeText(this, "�ٰ�һ�η��ؼ�ص�����", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.addCategory(Intent.CATEGORY_HOME);
				AseoZdpAseo.init(this, AseoZdpAseo.BOTH_TYPE);
				startActivity(intent);
				return false;
			}

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * ��Ƭ�ε��ã����õ�¼��Ϣ
	 * 
	 * @param isLogined
	 * @param uid
	 * @param icon
	 */
	public void setIsLogined(boolean isLogined, String uid, String icon) {
		this.isLogined = isLogined;
		this.uid = uid;
		this.icon = icon;
	}

	/**
	 * ��Ƭ�ε��ã���ȡ�Ƿ��ѵ�¼
	 * 
	 * @return
	 */
	public boolean getLogined() {
		return isLogined;
	}

	/**
	 * ��Ƭ�ε��ã���ȡ�û���
	 * 
	 * @return
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * ��Ƭ�ε��ã���ȡ�û�ͷ���ַ
	 * 
	 * @return
	 */
	public String getIcon() {
		return icon;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		setIconEnable(menu, true);
		MenuItem item = menu.add(0, 1, 1, "����");
		item.setIcon(R.drawable.main_menu_setup);
		MenuItem item2 = menu.add(0, 2, 2, "�����¼");
		item2.setIcon(R.drawable.main_menu_history);
		MenuItem item3 = menu.add(0, 3, 3, "�����");
		item3.setIcon(R.drawable.main_menu_feedback);
		MenuItem item4 = menu.add(0, 4, 4, "���ȵ���");
		item4.setIcon(R.drawable.main_menu_lightmode_day);
		MenuItem item5 = menu.add(0, 5, 5, "����");
		item5.setIcon(R.drawable.main_menu_about);
		MenuItem item6 = menu.add(0, 6, 6, "�˳�");
		item6.setIcon(R.drawable.main_menu_exit);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1: // ����
			startActivity(new Intent(this, MoreActivity.class));
			break;
		case 2: // �����¼
			startActivity(new Intent(this, HistoryActivity.class));
			break;
		case 3: // �����
			new FeedbackAgent(this).startFeedbackActivity();
			break;
		case 4: // ���ȵ���
			LightCtrlDialogFragment lightCtrlDialogFragment = new LightCtrlDialogFragment();
			lightCtrlDialogFragment.show(getSupportFragmentManager(), null);
			break;
		case 5: // ����
			startActivity(new Intent(this, AboutActivity.class));
			break;
		case 6: // �˳�
			ExitDialogFragment fragment = new ExitDialogFragment();
			fragment.show(getSupportFragmentManager(), null);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	// enableΪtrueʱ���˵����ͼ����Ч��enableΪfalseʱ��Ч��4.0ϵͳĬ����Ч
	private void setIconEnable(Menu menu, boolean enable) {
		try {
			Class<?> clazz = Class
					.forName("com.android.internal.view.menu.MenuBuilder");
			Method m = clazz.getDeclaredMethod("setOptionalIconsVisible",
					boolean.class);
			m.setAccessible(true);

			// MenuBuilderʵ��Menu�ӿڣ������˵�ʱ����������menu��ʵ����MenuBuilder����(java�Ķ�̬����)
			m.invoke(menu, enable);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String extra = intent
					.getStringExtra(Constants.BROADCAST_FILTER.EXTRA_CODE);
			if (extra.equals(Constants.INTENT_KEY.FROM_FAVOR)) {
				isFromFavor = true;
			} else if (extra.equals(Constants.INTENT_KEY.REFRESH_INCART)) {
				initInCartNum();
			} else if (extra.equals(Constants.INTENT_KEY.FROM_DETAIL)) {
				isFromDetail = true;
			} else if (extra.equals(Constants.INTENT_KEY.LOGOUT)) {
				isLogined = false;
			}
		}

	}


	//michael
	private static ArrayList<GoodsInfo> mGoableGoods = new ArrayList<GoodsInfo>();
	private void initGoodsData(){
		//goodsId goodsName  goodsIcon  goodsType  goodsPrice  goodsPercent  goodsComment  isPhone  isFavor
		mGoableGoods.add(new GoodsInfo("100001", "Levi's T-shirt", "http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods01.jpg", "goodsType 1", 153.00, "好评度96%", 1224, 1, 0));
		mGoableGoods.add(new GoodsInfo("100002", "Levi's Trousers", "http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods02.jpg", "goodsType 2", 479.00, "好评度95%", 645, 0, 0));
		mGoableGoods.add(new GoodsInfo("100003", "GXG T-shirt", "http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods03.jpg", "goodsType 3", 149.00, "好评度99%", 1856, 0, 0));
		mGoableGoods.add(new GoodsInfo("100004", "Apple iPad mini ME276CH/A ", "http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods04.jpg", "goodsType 4", 2138.00, "好评度97%", 865, 0, 0));
		mGoableGoods.add(new GoodsInfo("100005", "ThinkPad i3-4005U 4GB 500G+8GSSD 1G WIN8.1", "http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods05.jpg", "goodsType 5", 3299.00, "好评度95%", 236, 0, 0));
		mGoableGoods.add(new GoodsInfo("100006", "Logitech G502", "http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods06.jpg", "goodsType 6", 499.00, "好评度95%", 115, 0, 0));
		mGoableGoods.add(new GoodsInfo("100007", "Swissgear SA7777WH 12", "http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods07.jpg", "goodsType 7", 199.00, "好评度95%", 745, 0, 0));
		mGoableGoods.add(new GoodsInfo("100008", "Transcend  340ϵ 256G SATA3 (TS256GSSD340)", "http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods08.jpg", "goodsType 8", 569.00, "好评度95%", 854, 1, 0));
		mGoableGoods.add(new GoodsInfo("100009", "Canon EOS 700D EF-S 18-135mm f/3.5-5.6 IS STM", "http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods09.jpg", "goodsType 9", 5099.00, "好评度94%", 991, 0, 0));
		mGoableGoods.add(new GoodsInfo("100010", "F-WHEEL", "http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods10.jpg", "goodsType 10", 2999.00, "好评度93%", 1145, 0, 0));
		mGoableGoods.add(new GoodsInfo("100011", "Bicycle", "http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods11.jpg", "goodsType 11", 1088.00, "好评度92%", 909, 0, 0));
		mGoableGoods.add(new GoodsInfo("100012", "Book 1", "http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods12.jpg", "goodsType 12", 25.40, "好评度95%", 1443, 0, 0));
		mGoableGoods.add(new GoodsInfo("100013", "Book 2", "http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods13.jpg", "goodsType 13", 19.70, "好评度98%", 3702, 0, 0));
		mGoableGoods.add(new GoodsInfo("100014", "Book 3", "http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods14.jpg", "goodsType 14", 38.40, "好评度97%", 442, 1, 0));
		mGoableGoods.add(new GoodsInfo("100015", "Book 4", "http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods15.jpg", "goodsType 15", 57.80, "好评度93%", 765, 0, 0));
		mGoableGoods.add(new GoodsInfo("100016", "Book 5", "http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods13.jpg", "goodsType 16", 25.70, "好评度98%", 100, 0, 0));
	}

	public static ArrayList<GoodsInfo> getAllGoodInfo(){
		return mGoableGoods;
	}
}
