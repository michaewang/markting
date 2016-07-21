package com.mars.markting.activity;

import com.mars.markting.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class BoxActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_box);
		findViewById(R.id.img_back).setOnClickListener(this);
		findViewById(R.id.layout_box).setOnClickListener(this);
		findViewById(R.id.layout_null).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			finish();
			break;
		case R.id.layout_box:
			Intent intent = new Intent(this, WebActivity.class);
			intent.putExtra("direction", 5);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

}
