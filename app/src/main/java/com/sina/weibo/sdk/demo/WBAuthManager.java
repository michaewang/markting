package com.sina.weibo.sdk.demo;

import android.app.Activity;
import android.os.Bundle;

import com.mars.markting.R;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

import java.text.SimpleDateFormat;

public class WBAuthManager {

	private static WBAuthManager mInstance = new WBAuthManager();
	private SsoHandler mSsoHandler;
	private AuthInfo mAuthInfo;

	private Oauth2AccessToken mAccessToken;
	private Activity activity;
	
	public void init(Activity activity) {
		this.activity = activity;
		mAuthInfo = new AuthInfo(activity, Constants.APP_KEY,
				Constants.REDIRECT_URL, Constants.SCOPE);
		mSsoHandler = new SsoHandler(activity, mAuthInfo);

		mAccessToken = AccessTokenKeeper.readAccessToken(activity);
		if (mAccessToken.isSessionValid()) {
			// updateTokenView(true);
		}
	}
	
	public String createMsg(boolean hasExisted) {
		String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
				.format(new java.util.Date(mAccessToken.getExpiresTime()));
		String format = activity.getString(R.string.weibosdk_demo_token_to_string_format_1);
		String formattedText = String.format(format, mAccessToken.getToken(), date);
		String message = formattedText;
		if (hasExisted) {
			message = activity.getString(R.string.weibosdk_demo_token_has_existed)
					+ "\n" + message;
		}
		return message;
	}

	public static WBAuthManager getInstance() {
		return mInstance;
	}

	public boolean isSessionValid(Bundle values) {
		mAccessToken = Oauth2AccessToken.parseAccessToken(values);
		return mAccessToken.isSessionValid();
	}

	public void authorize(WeiboAuthListener authListener) {
		mSsoHandler.authorize(authListener);
	}

	public Oauth2AccessToken getAccessToken() {
		return mAccessToken;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

}
