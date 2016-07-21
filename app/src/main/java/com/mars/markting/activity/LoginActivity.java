package com.mars.markting.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.mars.bmop.signup.JDUser;
import com.mars.bmop.signup.SignUpActivity;
import com.mars.markting.R;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.demo.AccessTokenKeeper;
import com.sina.weibo.sdk.demo.Constants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.User;
import com.sina.weibo.sdk.utils.LogUtil;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends Activity implements OnClickListener, OnCheckedChangeListener{
	
	private ToggleButton mTgBtnShowPsw;
	private EditText mEditPsw;
	private EditText mEditUid;
	private ImageView mBtnClearUid;
	private ImageView mBtnClearPsw;
	
	private static final String TAG = "weibosdk";
	/**ͨ��Bmob��¼*/
	private final int LOG_BY_BMOB = 1;
	/**ͨ��΢����¼*/
	private final int LOG_BY_WEIBO = 2;

    /** ��ʾ��֤�����Ϣ���� AccessToken */
    private AuthInfo mAuthInfo;
    
    /** ��װ�� "access_token"��"expires_in"��"refresh_token"�����ṩ�����ǵĹ��?��  */
    private Oauth2AccessToken mAccessToken;

    /** ע�⣺SsoHandler ���� SDK ֧�� SSO ʱ��Ч */
    private SsoHandler mSsoHandler;
    
    /** �û���Ϣ�ӿ� */
    private UsersAPI mUsersAPI;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		Bmob.initialize(this, "736e69baa6ed388b6bf4e86acb9390ff");
		initUI();
		setOnListener();
		initUid();
	}
	
	/**
	 * ��ʼ����ס���û���
	 */
	private void initUid() {
		SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
		String uid = sp.getString("uid", "");
		mEditUid.setText(uid);
	}

	private void setOnListener() {
		mEditUid.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (mEditUid.getText().toString().length() > 0) {
					mBtnClearUid.setVisibility(View.VISIBLE);
					if (mEditPsw.getText().toString().length() >0) {
						mBtnLogin.setEnabled(true);
					} else {
						mBtnLogin.setEnabled(false);
					}
				} else {
					mBtnLogin.setEnabled(false);
					mBtnClearUid.setVisibility(View.INVISIBLE);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		mEditPsw.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (mEditPsw.getText().toString().length() > 0) {
					mBtnClearPsw.setVisibility(View.VISIBLE);
					if (mEditUid.getText().toString().length() > 0) {
						mBtnLogin.setEnabled(true);
					} else {
						mBtnLogin.setEnabled(false);
					}
				} else {
					mBtnLogin.setEnabled(false);
					mBtnClearPsw.setVisibility(View.INVISIBLE);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		mBtnLogin.setOnClickListener(this);
		mBtnClearUid.setOnClickListener(this);
		mBtnClearPsw.setOnClickListener(this);
		mTgBtnShowPsw.setOnCheckedChangeListener(this);
		findViewById(R.id.img_back).setOnClickListener(this);
		findViewById(R.id.btn_login_wb).setOnClickListener(this);
		findViewById(R.id.tv_quick_sign_up).setOnClickListener(this);
	}

	private void initUI() {
		mBtnLogin = (Button) findViewById(R.id.btn_login);
		mEditUid = (EditText) findViewById(R.id.edit_uid);
		mEditPsw = (EditText) findViewById(R.id.edit_psw);
		mBtnClearUid = (ImageView) findViewById(R.id.img_login_clear_uid);
		mBtnClearPsw = (ImageView) findViewById(R.id.img_login_clear_psw);
		mTgBtnShowPsw = (ToggleButton) findViewById(R.id.tgbtn_show_psw);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:	//��¼
			login();
			break;
		case R.id.img_back:	//����
			finish();
			break;
		case R.id.btn_login_wb:	//΢����¼
			loginWB();
			break;
		case R.id.img_login_clear_uid:	//����û���
			clearText(mEditUid);
			break;
		case R.id.img_login_clear_psw:	//�������
			clearText(mEditPsw);
			break;
		case R.id.tv_quick_sign_up:	//����ע��
			startActivity(new Intent(this, SignUpActivity.class));
			break;

		default:
			break;
		}
	}
	
	/**
	 * ΢����¼
	 */
	private void loginWB() {
		mAuthInfo = new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
        mSsoHandler = new SsoHandler(this, mAuthInfo);
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        if (mAccessToken.isSessionValid()) {
        	getUserData();
        } else {
        	// ��������΢����¼Activity
        	mSsoHandler.authorize(new AuthListener());
        }
	}
	
	/**
	 * ��ȡ�û���Ϣ
	 */
	protected void getUserData() {
		if (mAccessToken != null) {
			// ��ȡ�û���Ϣ�ӿ�
			mUsersAPI = new UsersAPI(this, Constants.APP_KEY, mAccessToken);
			 long uid = Long.parseLong(mAccessToken.getUid());
             mUsersAPI.show(uid, mListener);
		}
	}
	
	/**
     * ΢�� OpenAPI �ص��ӿڡ�
     */
    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                LogUtil.i(TAG, response);
                // ���� User#parse ��JSON��������User����
                User user = User.parse(response);
                if (user != null) {
                    Toast.makeText(LoginActivity.this,
                            "��¼�ɹ�", 
                            Toast.LENGTH_SHORT).show();
                    //ͨ��΢����¼
                    setSP(LOG_BY_WEIBO);
                    Intent data = new Intent();
                    data.putExtra("screen_name", user.screen_name);
                    data.putExtra("profile_image_url", user.profile_image_url);
					setResult(com.mars.markting.utils.Constants.INTENT_KEY.LOGIN_RESULT_SUCCESS_CODE, data );
//                	UILUtils.displayImage(LoginActivity.this, user.profile_image_url, mImgUserIcon);
					finish();
                } else {
                    Toast.makeText(LoginActivity.this, "�޷���ȡ�û���Ϣ", Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            LogUtil.e(TAG, e.getMessage());
            ErrorInfo info = ErrorInfo.parse(e.getMessage());
            Toast.makeText(LoginActivity.this, info.toString(), Toast.LENGTH_LONG).show();
        }
    };
	private Button mBtnLogin;
    
    /**
     * �� SSO ��Ȩ Activity �˳�ʱ���ú�����á�
     * 
     * @see {@link Activity#onActivityResult}
     */
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        // SSO ��Ȩ�ص�
        // ��Ҫ������ SSO ��½�� Activity ������д onActivityResult
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    /**
     * ΢����֤��Ȩ�ص��ࡣ
     * 1. SSO ��Ȩʱ����Ҫ�� {@link #onActivityResult} �е��� {@link SsoHandler#authorizeCallBack} ��
     *    �ûص��Żᱻִ�С�
     * 2. �� SSO ��Ȩʱ������Ȩ����󣬸ûص��ͻᱻִ�С�
     * ����Ȩ�ɹ����뱣��� access_token��expires_in��uid ����Ϣ�� SharedPreferences �С�
     */
    class AuthListener implements WeiboAuthListener {
        
        @Override
        public void onComplete(Bundle values) {
            // �� Bundle �н��� Token
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            if (mAccessToken.isSessionValid()) {
                // ���� Token �� SharedPreferences
                AccessTokenKeeper.writeAccessToken(LoginActivity.this, mAccessToken);
                Toast.makeText(LoginActivity.this,
                        R.string.weibosdk_demo_toast_auth_success, Toast.LENGTH_SHORT).show();
                loginWB();
            } else {
                // ���¼������������յ� Code��
                // 1. ����δ��ƽ̨��ע���Ӧ�ó���İ�����ǩ��ʱ��
                // 2. ����ע���Ӧ�ó��������ǩ����ȷʱ��
                // 3. ������ƽ̨��ע��İ����ǩ������ǰ���Ե�Ӧ�õİ����ǩ��ƥ��ʱ��
                String code = values.getString("code");
                String message = getString(R.string.weibosdk_demo_toast_auth_failed);
                if (!TextUtils.isEmpty(code)) {
                    message = message + "\nObtained the code: " + code;
                }
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancel() {
            Toast.makeText(LoginActivity.this,
                    R.string.weibosdk_demo_toast_auth_canceled, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(LoginActivity.this,
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
	/**
	 * ��տؼ��ı�
	 * @param
	 */
	private void clearText(EditText edit) {
		edit.setText("");
	}
	
	/**
	 * ����¼;�����浽SharedPreferences��1ΪBmob��2Ϊ΢��
	 */
	private void setSP(int type) {
		//���浱ǰλ�õ�SharedPreferences
		SharedPreferences sp = this
				.getSharedPreferences("login_type", Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putInt("login_type", type);
		edit.commit();
	}

	/**
	 * ��¼��ť
	 */
	private void login() {
		String userName = mEditUid.getText().toString();
		String pwd = mEditPsw.getText().toString();

		final JDUser user = new JDUser();
		user.setUsername(userName);
		user.setPassword(pwd);
		user.login(this, new SaveListener() {
			public void onSuccess() {
				//���û���浽Preference
				SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
				Editor edit = sp.edit();
				edit.putString("uid", mEditUid.getText().toString());
				edit.commit();
				
				String id2 = user.getObjectId();
				Toast.makeText(LoginActivity.this, "��¼�ɹ�",
						Toast.LENGTH_SHORT).show();
				//ͨ��Bmob��¼
				setSP(LOG_BY_BMOB);
				Intent intent = new Intent();
				intent.putExtra("uid", id2);
				setResult(com.mars.markting.utils.Constants.INTENT_KEY.LOGIN_RESULT_SUCCESS_CODE, intent );
				LoginActivity.this.finish();
				
			}

			public void onFailure(int arg0, String arg1) {
				Toast.makeText(LoginActivity.this, arg0 + " ..." +arg1 , Toast.LENGTH_LONG)
						.show();
			}
		});
	}

	/**
	 * �Ƿ���ʾ����
	 */
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			//��ʾ����
			mEditPsw.setTransformationMethod(
					HideReturnsTransformationMethod.getInstance());
		} else {
			//��������
			mEditPsw.setTransformationMethod(
					PasswordTransformationMethod.getInstance());
		}
	}

}
