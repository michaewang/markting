package com.mars.markting.activity;

import com.mars.markting.R;
import com.mars.markting.utils.HackyViewPager;

import uk.co.senab.photoview.PhotoView;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

public class ViewPagerActivity extends Activity {

	private ViewPager mViewPager;
	private TextView mTvCount;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_pager);
		mTvCount = (TextView) findViewById(R.id.tv_count);
		mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
//		setContentView(mViewPager);	//���ָ�ĺ󣬲�����ViewPager,�����������
		findViewById(R.id.btn_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mViewPager.setAdapter(new SamplePagerAdapter());
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
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

	static class SamplePagerAdapter extends PagerAdapter {

		private static final int[] sDrawables = { R.drawable.ip1,
				R.drawable.ip2, R.drawable.ip3, R.drawable.ip4 };

		@Override
		public int getCount() {
			return sDrawables.length;
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			PhotoView photoView = new PhotoView(container.getContext());
			photoView.setImageResource(sDrawables[position]);
			// Now just add PhotoView to ViewPager and return it
			container.addView(photoView, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);

			return photoView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}

}
