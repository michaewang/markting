package com.example.baidumap;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

public class LocationActivity extends FragmentActivity implements OnGetGeoCoderResultListener {
	public MyLocationListenner myListener = new MyLocationListenner();
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private LocationClient mLocClient;
	boolean isFirstLoc = true;
	private BitmapDescriptor mCurrentMarker;
	private GeoCoder mSearch;
	private String location = "";

	/**
	 *
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					//
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}
			//
			LatLng ptCenter = new LatLng(location.getLatitude(), location.getLongitude());
			//
			mSearch.reverseGeoCode(new ReverseGeoCodeOption()
					.location(ptCenter));
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		findViewById(R.id.img_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		findViewById(R.id.tv_set).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (location.equals("")) {
					Toast.makeText(LocationActivity.this, "Michael LocationActivity", Toast.LENGTH_SHORT).show();
					return;
				}
				MyDialogFragment fragment = new MyDialogFragment();
				fragment.setLocation(location);
				fragment.show(getSupportFragmentManager(), null);
			}
		});
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		//
		mBaiduMap.setMyLocationEnabled(true);
		//
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);//
		option.setCoorType("bd09ll"); //
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		
		//
		mCurrentMarker = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_geo);
		mBaiduMap
				.setMyLocationConfigeration(new MyLocationConfiguration(
						LocationMode.NORMAL , true, mCurrentMarker));
		
		//
				mSearch = GeoCoder.newInstance();
				mSearch.setOnGetGeoCodeResultListener(this);
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		//
		mLocClient.stop();
		//
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}

	
	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			//
			return;
		}
		location  = result.getAddress();
	}

}
