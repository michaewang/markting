package com.mars.markting.activity;

import java.util.ArrayList;
import java.util.Random;

import com.mars.markting.R;
import com.mars.markting.bean.GoodsInfo;
import com.mars.markting.utils.Constants;
import com.mars.markting.utils.NumberUtils;
import com.lib.uil.UILUtils;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ShakeActivity extends Activity implements OnClickListener,
		SensorEventListener {

	private SensorManager mSensorManager;
	private Vibrator mVibrator;
	private final int ROCKPOWER = 15;// ���Ǵ�����ϵ��
	private View mViewResult;
	private int num = 3;	//ʣ�����
	private TextView mTvNum;
	
	private ArrayList<GoodsInfo> list = new ArrayList<GoodsInfo>();
	private ImageView mImgGoods;
	private TextView mTvName;
	private TextView mTvPrice;
	private GoodsInfo info;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shake);
		initData();
		initView();
		initSensor();
		findViewById(R.id.btn_back).setOnClickListener(this);
		findViewById(R.id.btn_detail).setOnClickListener(this);
		findViewById(R.id.btn_close).setOnClickListener(this);
	}
	
	private void initData() {
		list.add(new GoodsInfo("100001", "Levi's��ά˹��ʿ����ʱ�г�������T��82176-0005 ��/�� L", "http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods01.jpg", "����Ь��", 153.00, "����96%", 1224, 1, 0));
		list.add(new GoodsInfo("100002", "Levi's��ά˹505ϵ����ʿ����ֱ��ţ�п�00505-1185 ţ��ɫ 36 34", "http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods02.jpg", "����Ь��", 479.00, "����95%", 645, 0, 0));
		list.add(new GoodsInfo("100003", "GXG��װ ����ר�� 2015��װ�¿� ��ʿʱ�а�ɫ����Բ�����T��#42244315 ��ɫ M", "http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods03.jpg", "����Ь��", 149.00, "��������", 1856, 0, 0));
		list.add(new GoodsInfo("100004", "Apple iPad mini ME276CH/A �䱸 Retina ��ʾ�� 7.9Ӣ��ƽ����� ��16G WiFi�棩��ջ�ɫ", "http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods04.jpg", "��������", 2138.00, "����97%", 865, 0, 0));
		list.add(new GoodsInfo("100005", "���루ThinkPad���ᱡϵ��E450C(008CD) 14Ӣ��ʼǱ����� ��i3-4005U 4GB 500G+8GSSD 1G WIN8.1��", "http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods05.jpg", "��������", 3299.00, "����95%", 236, 0, 0));
		list.add(new GoodsInfo("100006", "�޼���Logitech��G502 ����Ӧ��Ϸ���", "http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods06.jpg", "����Ь��", 499.00, "����95%", 115, 0, 0));
		list.add(new GoodsInfo("100007", "��ʿ��Swissgear��SA7777WH 12Ӣ��ʱ��������˫����Ա��� �װ�ɫ", "http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods07.jpg", "����Ь��", 199.00, "����95%", 745, 0, 0));
		list.add(new GoodsInfo("100008", "����Transcend�� 340ϵ�� 256G SATA3 ��̬Ӳ��(TS256GSSD340)", "http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods08.jpg", "��������", 569.00, "����95%", 854, 1, 0));
		list.add(new GoodsInfo("100009", "���ܣ�Canon�� EOS 700D �����׻� ��EF-S 18-135mm f/3.5-5.6 IS STM��ͷ��", "http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods09.jpg", "��������", 5099.00, "����94%", 991, 0, 0));
		list.add(new GoodsInfo("100010", "��������F-WHEEL) ���ܵ綯���ֳ� ��ƽ����ֳ� ����ϵ������ ֧�� ���� ���� ��ɫD1��20KM��֧��", "http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods10.jpg", "�˶�����", 2999.00, "����93%", 1145, 0, 0));
		list.add(new GoodsInfo("100011", "����21��26�����Ͻ����г� ����ŵ���� ����ɵ���������ɽ�س� QJ243 ��Ӫ", "http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods11.jpg", "�˶�����", 1088.00, "����92%", 909, 0, 0));
		list.add(new GoodsInfo("100012", "���Ƕ�һ������������ ��Ӫ", "http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods12.jpg", "ͼ������", 25.40, "����95%", 1443, 0, 0));
		list.add(new GoodsInfo("100013", "����Զ��", "http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods13.jpg", "ͼ������", 19.70, "����98%", 3702, 0, 0));
		list.add(new GoodsInfo("100014", "���ڵ�����", "http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods14.jpg", "ͼ������", 38.40, "����97%", 442, 1, 0));
		list.add(new GoodsInfo("100015", "Photoshopרҵ��ͼ���� �����1��", "http://7xi38r.com1.z0.glb.clouddn.com/@/server_anime/goodsicons/goods15.jpg", "ͼ������", 57.80, "����93%", 765, 0, 0));
	}

	@Override
	protected void onResume() {
		super.onResume();
		// ���ٶȴ�����
		mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
		// ����SENSOR_DELAY_UI��SENSOR_DELAY_FASTEST��SENSOR_DELAY_GAME�ȣ�
		// ��ݲ�ͬӦ�ã���Ҫ�ķ�Ӧ���ʲ�ͬ��������ʵ������趨
				SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	/**
	 * ��ʾ��Ʒ
	 */
	private void showGoods() {
		MediaPlayer player = MediaPlayer.create(this, R.raw.shakeing);
		player.start();
		mVibrator.vibrate(500);// �����𶯡�
		if (mViewResult.getVisibility() == View.VISIBLE) {
			return;
		}
		if (num > 0) {
			num--;
			mTvNum.setText("" + num);
		} else {
			Toast.makeText(this, "����Ļ�������ˣ�����������~", Toast.LENGTH_SHORT).show();
			return;
		}
		Random random = new Random();
		int nextInt = random.nextInt(list.size());
		info = list.get(nextInt);
		mTvName.setText(info.getGoodsName());
		mTvPrice.setText(NumberUtils.formatPrice(info.getGoodsPrice()));
		UILUtils.displayImage(this, info.getGoodsIcon(), mImgGoods);
		mTvName.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				MediaPlayer player2 = MediaPlayer.create(ShakeActivity.this, R.raw.shake_something);
				player2.start();
				mViewResult.setVisibility(View.VISIBLE);
			}
		}, 800);
	}
	
	@Override
	protected void onStop() {
		mSensorManager.unregisterListener(this);// �˳�����󣬰Ѵ������ͷš�-----��ʡ��Դ
		super.onStop();
	}

	private void initSensor() {
		// ��ȡ�������������
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		// �𶯷���
		mVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE); // ����Ҫ��androidmainfest����ע��Ŷ��
	}

	private void initView() {
		mViewResult = findViewById(R.id.layout_result);
		mImgGoods = (ImageView) findViewById(R.id.img_goods);
		mTvName = (TextView) findViewById(R.id.tv_name);
		mTvPrice = (TextView) findViewById(R.id.tv_price);
		mTvNum = (TextView) findViewById(R.id.tv_num);
		final ImageView imgHand = (ImageView) findViewById(R.id.img_shake_hand);
		ImageView imgAnim = (ImageView) findViewById(R.id.img_anim);
		imgHand.postDelayed(new Runnable() {

			@Override
			public void run() {
				ViewHelper.setPivotX(imgHand, imgHand.getWidth() / 2f);
				ViewHelper.setPivotY(imgHand, imgHand.getHeight());
				ObjectAnimator.ofFloat(imgHand, "rotation", -30, 30, 0)
						.setDuration(1500).start();
				imgHand.postDelayed(this, 3000);
			}
		}, 1500);
		AnimationDrawable drawable = (AnimationDrawable) imgAnim.getDrawable();
		drawable.start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_close:
			mViewResult.setVisibility(View.GONE);
			break;
		case R.id.btn_detail:
			gotoDetail();
			break;

		default:
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (mViewResult.getVisibility() == View.VISIBLE) {
				mViewResult.setVisibility(View.GONE);
				return false;
			}
			break;

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * ��Ʒ����
	 */
	private void gotoDetail() {
		Intent intent = new Intent(this, DetailActivity.class);
		intent.putExtra(Constants.INTENT_KEY.INFO_TO_DETAIL, info);
		startActivity(intent );
	}
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		int sensorType = event.sensor.getType();
		// values[0]:X�ᣬvalues[1]��Y�ᣬvalues[2]��Z��
		float[] values = event.values;
		if (sensorType == Sensor.TYPE_ACCELEROMETER) {
			// �� ���if����д����дҪҡһҡ��ô�ӣ�֪��ô����ͷ~~~
			if ((Math.abs(values[0]) > ROCKPOWER || Math.abs(values[1]) > ROCKPOWER || Math.abs(values[2]) > ROCKPOWER)) {
				showGoods();
			} 
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

}
