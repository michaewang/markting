package com.mars.markting.activity;

import com.mars.markting.R;
import com.zdp.aseo.content.AseoZdpAseo;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

public class RecomActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recom);
		AseoZdpAseo.initType(this, AseoZdpAseo.INSERT_TYPE);
		View btnIgnore = findViewById(R.id.btn_ignore);
		btnIgnore.setOnClickListener(this);
		btnIgnore.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				gotoMain();
			}
		}, 2300);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ignore:
			gotoMain();
			break;

		default:
			break;
		}
	}

	private void gotoMain() {
		startActivity(new Intent(this, MainActivity.class));
		finish();
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//�����?��
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

}
