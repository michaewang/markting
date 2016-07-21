package com.mars.markting.activity;

import com.mars.markting.R;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class OrdersActivity extends Activity implements OnClickListener {

	private TextView mTextView;
	private TextView mTextView2;
	private View indicator;
	private View indicator2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_orders);
		mTextView = (TextView) findViewById(R.id.TextView01);
		mTextView2 = (TextView) findViewById(R.id.TextView02);
		indicator = findViewById(R.id.indicator1);
		indicator2 = findViewById(R.id.indicator2);
		findViewById(R.id.img_back).setOnClickListener(this);
		findViewById(R.id.layout_order1).setOnClickListener(this);
		findViewById(R.id.layout_order2).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			finish();
			break;
		case R.id.layout_order1:
			indicator.setVisibility(View.VISIBLE);
			indicator2.setVisibility(View.INVISIBLE);
			mTextView.setTextColor(Color.RED);
			mTextView2.setTextColor(getResources().getColor(R.color.dark));
			break;
		case R.id.layout_order2:
			indicator2.setVisibility(View.VISIBLE);
			indicator.setVisibility(View.INVISIBLE);
			mTextView2.setTextColor(Color.RED);
			mTextView.setTextColor(getResources().getColor(R.color.dark));
			break;

		default:
			break;
		}
	}

}
