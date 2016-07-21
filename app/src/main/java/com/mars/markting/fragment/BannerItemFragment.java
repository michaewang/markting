package com.mars.markting.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mars.markting.R;
import com.mars.markting.activity.DetailActivity;
import com.mars.markting.bean.GoodsInfo;
import com.mars.markting.utils.Constants;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class BannerItemFragment extends Fragment implements OnClickListener {

	private int position;
	private ImageView mImageView;
	private int imageRes;
	private GoodsInfo goodsInfo;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View inflate = inflater.inflate(R.layout.fragment_banner_item,
				container, false);
		mImageView = (ImageView) inflate.findViewById(R.id.imageView1);
		mImageView.setImageResource(imageRes);
		inflate.setOnClickListener(this);
		return inflate;
	}

	public void setResId(int imageRes) {
		this.imageRes = imageRes;
	}

	@Override
	public void onClick(View v) {
		if (goodsInfo != null) {
			Intent intent = new Intent(getActivity(), DetailActivity.class);
			intent.putExtra(Constants.INTENT_KEY.INFO_TO_DETAIL, goodsInfo);
			startActivity(intent);
		}
	}

	public void setGoodsInfo(GoodsInfo goodsInfo) {
		this.goodsInfo = goodsInfo;
	}

}
