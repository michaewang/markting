package com.an.cityselect;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.mars.markting.R;
import com.lib.uil.ScreenUtils;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.ArrayList;

/**
 *
 * 
 * @author Anthony
 * 
 */
public class CitySelectActivity extends Activity implements OnClickListener {

	private Button btn_submit;
	private ImageView img_back;
	private ListView lv_city;
	private ArrayList<MyRegion> regions;

	private CityAdapter adapter;
	private static int PROVINCE = 0x00;
	private static int CITY = 0x01;
	private static int DISTRICT = 0x02;
	private CityUtils util;

	private TextView[] tvs = new TextView[3];
	private int[] ids = { R.id.rb_province, R.id.rb_city, R.id.rb_district };

	private City city;
	int last, current;
	private int lastIndex;	//

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_city_select);

		viewInit();

	}

	private void viewInit() {

		city = new City();
		Intent in = getIntent();
		city = in.getParcelableExtra("city");
		
		
		
		for (int i = 0; i < tvs.length; i++) {
			tvs[i] = (TextView) findViewById(ids[i]);
			tvs[i].setOnClickListener(this);
		}

		if(city==null){
			city = new City();
			city.setProvince("");
			city.setCity("");
			city.setDistrict("");
		}else{
			if(city.getProvince()!=null&&!city.getProvince().equals("")){
				tvs[0].setText(city.getProvince());
			}
			if(city.getCity()!=null&&!city.getCity().equals("")){
				tvs[1].setText(city.getCity());
			}
			if(city.getDistrict()!=null&&!city.getDistrict().equals("")){
				tvs[2].setText(city.getDistrict());
			}
		}
		
		mIndicator = findViewById(R.id.indicator);
		btn_submit = (Button) findViewById(R.id.btn_submit);
		img_back = (ImageView) findViewById(R.id.img_back);
		
		util = new CityUtils(this, hand);
		util.initProvince();
		tvs[current].setTextColor(Color.RED);
		lv_city = (ListView) findViewById(R.id.lv_city);

		regions = new ArrayList<MyRegion>();
		adapter = new CityAdapter(this);
		lv_city.setAdapter(adapter);
		
	}

	protected void onStart() {
		super.onStart();
		lv_city.setOnItemClickListener(onItemClickListener);
		img_back.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
	};
	
	/**
	 *
	 */
	@Override
	protected void onResume() {
		super.onResume();
		screenWidth = ScreenUtils.getScreenWidth(this);
		int width = screenWidth/4;
		int leftMargin = screenWidth/6 - width/2;
		LayoutParams params = (LayoutParams) mIndicator.getLayoutParams();
		params.width = width;
		params.leftMargin = leftMargin;
		mIndicator.setLayoutParams(params);
	}
	
	/**
	 *
	 * 
	 * @param index
	 */
	private void moveIndicator(int index) {
		int moveWidth = screenWidth/3;
		ObjectAnimator
				.ofFloat(mIndicator, "translationX",
						lastIndex * moveWidth, index * moveWidth)
				.setDuration(duration).start();
	}


	@SuppressLint("HandlerLeak")
	Handler hand = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {

			case 1:
				System.out.println("what======" + msg.what);

				regions = (ArrayList<MyRegion>) msg.obj;
				adapter.clear();
				adapter.addAll(regions);
				adapter.update();
				break;

			case 2:
				System.out.println("what======" + msg.what);
				regions = (ArrayList<MyRegion>) msg.obj;
				adapter.clear();
				adapter.addAll(regions);
				adapter.update();
				break;

			case 3:
				System.out.println("what======" + msg.what);
				regions = (ArrayList<MyRegion>) msg.obj;
				adapter.clear();
				adapter.addAll(regions);
				adapter.update();
				break;
			}
		};
	};

	OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
								long arg3) {

			if (current == PROVINCE) {
				String newProvince = regions.get(arg2).getName();
				if (!newProvince.equals(city.getProvince())) {
					city.setProvince(newProvince);
					city.setCity("");
					city.setDistrict("");
					tvs[0].setText(regions.get(arg2).getName());
					city.setRegionId(regions.get(arg2).getId());
					city.setProvinceCode(regions.get(arg2).getId());
					city.setCityCode("");
					city.setDistrictCode("");
					tvs[1].setText("michael");
					tvs[2].setText("michael ");
				}
				moveIndicator(1);
				lastIndex = 1;
				current = 1;
				//
				util.initCities(city.getProvinceCode());
			} else if (current == CITY) {
				String newCity = regions.get(arg2).getName();
				if (!newCity.equals(city.getCity())) {
					city.setCity(newCity);
					city.setDistrict("");
					tvs[1].setText(regions.get(arg2).getName());
					city.setRegionId(regions.get(arg2).getId());
					city.setCityCode(regions.get(arg2).getId());
					city.setDistrictCode("");
					tvs[2].setText("michael ");
				}
				moveIndicator(2);
				lastIndex = 2;
				//
				util.initDistricts(city.getCityCode());
				current = 2;

			} else if (current == DISTRICT) {
				current = 2;
				city.setDistrictCode(regions.get(arg2).getId());
				city.setRegionId(regions.get(arg2).getId());
				city.setDistrict(regions.get(arg2).getName());
				tvs[2].setText(regions.get(arg2).getName());
				moveIndicator(2);
				lastIndex = 2;
			}
			tvs[last].setTextColor(Color.BLACK);
			tvs[current].setTextColor(Color.RED);
			last = current;
		}
	};
	private View mIndicator;
	private int screenWidth;	//
	private final long duration = 300; //

	class CityAdapter extends ArrayAdapter<MyRegion> {

		LayoutInflater inflater;

		public CityAdapter(Context con) {
			super(con, 0);
			inflater = LayoutInflater.from(CitySelectActivity.this);
		}

		@Override
		public View getView(int arg0, View v, ViewGroup arg2) {
			v = inflater.inflate(R.layout.item_city_select, null);
			TextView tv_city = (TextView) v.findViewById(R.id.tv_city);
			tv_city.setText(getItem(arg0).getName());
			return v;
		}

		public void update() {
			this.notifyDataSetChanged();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back://
			finish();
			overridePendingTransition(0, R.anim.slide_out_right);
			break;
		case R.id.btn_submit://

			Intent in = new Intent();
			in.putExtra(CityConstant.CITY_CODE, city);
			Log.e("City", ""+city.getCity());
			setResult(CityConstant.RESULT_CITY,in);
			finish();
			overridePendingTransition(0, R.anim.slide_out_right);
			break;
		}
		if (ids[0] == v.getId()) {
			//
			moveIndicator(0);
			lastIndex = 0;
			current = 0;
			util.initProvince();
			tvs[last].setTextColor(Color.BLACK);
			tvs[current].setTextColor(Color.RED);
			last = current;
		} else if (ids[1] == v.getId()) {
			if (city.getProvinceCode() == null|| city.getProvinceCode().equals("")) {
				current = 0;
				Toast.makeText(CitySelectActivity.this, "michael CitySelectActivity 11",
						Toast.LENGTH_SHORT).show();
				return;
			}
			//
			moveIndicator(1);
			lastIndex = 1;
			util.initCities(city.getProvinceCode());
			current = 1;
			tvs[last].setTextColor(Color.BLACK);
			tvs[current].setTextColor(Color.RED);
			last = current;
		} else if (ids[2] == v.getId()) {
			if (city.getProvinceCode() == null
					|| city.getProvinceCode().equals("")) {
				Toast.makeText(CitySelectActivity.this, "michael CitySelectActivity 2",
						Toast.LENGTH_SHORT).show();
				current = 0;
				util.initProvince();
				return;
			} else if (city.getCityCode() == null
					|| city.getCityCode().equals("")) {
				Toast.makeText(CitySelectActivity.this, "michael CitySelectActivity 3",
						Toast.LENGTH_SHORT).show();
				current = 1;
				util.initCities(city.getProvince());
				return;
			}
			//ָʾ������
			moveIndicator(2);
			lastIndex = 2;
			current = 2;
			util.initDistricts(city.getCityCode());
			tvs[last].setTextColor(Color.BLACK);
			tvs[current].setTextColor(Color.RED);
			last = current;
		}

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			finish();
			overridePendingTransition(0, R.anim.slide_out_right);
		}
		return super.onKeyDown(keyCode, event);
	}

}
