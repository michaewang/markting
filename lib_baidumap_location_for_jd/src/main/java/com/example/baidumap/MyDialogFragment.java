package com.example.baidumap;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class MyDialogFragment extends DialogFragment {

	private FragmentActivity mActivity;
	private String location; // ��ǰ��ַ

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mActivity = getActivity();
		View inflate = inflater.inflate(R.layout.fragment_my_dialog, null);
		TextView tvAdress = (TextView) inflate.findViewById(R.id.tv_dl_adress);
		tvAdress.setText(location);
		inflate.findViewById(R.id.btn_ok).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						//���浱ǰλ�õ�SharedPreferences
						SharedPreferences sp = mActivity
								.getSharedPreferences("baidumap_location", Context.MODE_PRIVATE);
						Editor edit = sp.edit();
						edit.putString("location", location);
						edit.commit();
						Toast.makeText(mActivity, "Delete ok", Toast.LENGTH_LONG)
								.show();
						//�رնԻ���
						MyDialogFragment.this.dismiss();
					}
				});
		inflate.findViewById(R.id.btn_cancel).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						MyDialogFragment.this.dismiss();
					}
				});
		getDialog().getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		return inflate;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
