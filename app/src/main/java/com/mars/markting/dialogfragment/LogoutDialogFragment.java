package com.mars.markting.dialogfragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.mars.markting.R;
import com.mars.markting.app.SysApplication;
import com.sina.weibo.sdk.demo.AccessTokenKeeper;
import com.sina.weibo.sdk.demo.Constants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.LogoutAPI;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.BmobUser;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class LogoutDialogFragment extends DialogFragment implements
		OnClickListener {

	private FragmentActivity mActivity;
	private View btnLogout;
	private View divider;
	private int logType;
	/** �ǳ�������Ӧ��listener */
	private LogOutRequestListener mLogoutListener = new LogOutRequestListener();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		mActivity = getActivity();
		View inflate = inflater.inflate(R.layout.fragment_dialog_logout, null);
		inflate.findViewById(R.id.tv_exit).setOnClickListener(this);
		divider = inflate.findViewById(R.id.divider);
		btnLogout = inflate.findViewById(R.id.tv_logout);
		btnLogout.setOnClickListener(this);
		initLogin();
		getDialog().getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		return inflate;
	}

	private void initLogin() {
		// ��ȡ��¼����
		SharedPreferences sp = mActivity.getSharedPreferences("login_type",
				Context.MODE_PRIVATE);
		int type = sp.getInt("login_type", 0);
		switch (type) {
		case 0: // δ��¼��ֻ��ʾ�˳��ͻ��˰�ť
			btnLogout.setVisibility(View.GONE);
			divider.setVisibility(View.GONE);
			break;
		case 1: // ͨ��Bmob��¼
			logType = 1;
			break;
		case 2: // ͨ��΢����¼
			logType = 2;
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		dismiss();
		switch (v.getId()) {
		case R.id.tv_exit: // �˳�Ӧ��
			SysApplication.getInstance().exit();
			break;
		case R.id.tv_logout: // ע��
			logout();
			break;

		default:
			break;
		}
	}

	private void logout() {
		dismiss();
		switch (logType) {
		case 1: // ͨ��Bmobע��
			logoutBmob();
			break;
		case 2: // ͨ��΢��ע��
			logoutWB();
			break;

		default:
			break;
		}
	}

	private void logoutWB() {
		new LogoutAPI(mActivity, Constants.APP_KEY,
				AccessTokenKeeper.readAccessToken(mActivity))
				.logout(mLogoutListener);
	}

	private void logoutBmob() {
		BmobUser.logOut(mActivity);
		anounceLogout();
		Toast.makeText(mActivity, "��ע��", Toast.LENGTH_SHORT).show();
	}

	/**
	 * ���͹㲥ע��
	 */
	private void anounceLogout() {
		// ����sp
		SharedPreferences sp = mActivity.getSharedPreferences("login_type",
				Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putInt("login_type", 0);
		edit.commit();
		Intent intent = new Intent();
		intent.setAction(com.mars.markting.utils.Constants.BROADCAST_FILTER.FILTER_CODE);
		intent.putExtra(
				com.mars.markting.utils.Constants.BROADCAST_FILTER.EXTRA_CODE,
				com.mars.markting.utils.Constants.INTENT_KEY.LOGOUT);
		mActivity.sendBroadcast(intent);
	}

	/**
	 * �ǳ���ť�ļ����������յǳ�������API ������ļ�������
	 */
	private class LogOutRequestListener implements RequestListener {
		@Override
		public void onComplete(String response) {
			if (!TextUtils.isEmpty(response)) {
				try {
					JSONObject obj = new JSONObject(response);
					String value = obj.getString("result");

					if ("true".equalsIgnoreCase(value)) {
						AccessTokenKeeper.clear(mActivity);
						Toast.makeText(mActivity, "��ע��", Toast.LENGTH_SHORT)
								.show();
						anounceLogout();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(mActivity, "ע�����", Toast.LENGTH_SHORT).show();
		}
	}

}
