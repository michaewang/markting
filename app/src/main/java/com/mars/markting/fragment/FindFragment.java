package com.mars.markting.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.mars.markting.R;
import com.mars.markting.activity.BoxActivity;
import com.mars.markting.activity.MainActivity;
import com.mars.markting.activity.ShakeActivity;
import com.mars.markting.activity.WebActivity;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class FindFragment extends Fragment implements OnClickListener {

	private View layout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		if (layout != null) {
			// ��ֹ���new��Ƭ�ζ������ͼƬ��������
			return layout;
		}
		layout = inflater.inflate(R.layout.fragment_find, container, false);
		layout.findViewById(R.id.layout_QR).setOnClickListener(this);
		layout.findViewById(R.id.layout_box).setOnClickListener(this);
		layout.findViewById(R.id.layout_shake).setOnClickListener(this);
		layout.findViewById(R.id.layout_story).setOnClickListener(this);
		layout.findViewById(R.id.layout_activity).setOnClickListener(this);
		layout.findViewById(R.id.layout_xiaobing).setOnClickListener(this);
		return layout;
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		// ��layout�Ӹ�������Ƴ�
		ViewGroup parent = (ViewGroup) layout.getParent();
		parent.removeView(layout);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_QR:		//��ά��ɨ��
			((MainActivity) getActivity()).scanQRCode();
			break;
		case R.id.layout_box:	//�ٱ���
			startActivity(new Intent(getActivity(), BoxActivity.class));
			break;
		case R.id.layout_shake:	//ҡһҡ
			startActivity(new Intent(getActivity(), ShakeActivity.class));
			break;
		case R.id.layout_xiaobing:	//С��
			Intent intent = new Intent(getActivity(), WebActivity.class);
			intent.putExtra("direction", 6);
			startActivity(intent);
			break;
		case R.id.layout_story:	//����
			Intent intent2 = new Intent(getActivity(), WebActivity.class);
			intent2.putExtra("direction", 7);
			startActivity(intent2);
			break;
		case R.id.layout_activity:	//�
			Intent intent3 = new Intent(getActivity(), WebActivity.class);
			intent3.putExtra("direction", 8);
			startActivity(intent3);
			break;

		default:
			break;
		}
	}

}
