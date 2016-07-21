package com.mars.bmop.signup;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import com.mars.markting.R;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.SaveListener;

public class SignUpActivity extends Activity implements OnClickListener {

	private EditText mEditUser;
	private EditText mEditPsw;
	private Button mBtnSignUp;

	private boolean isBtnChecked = true;
	private EditText mEditPswVal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);

		Bmob.initialize(this, "736e69baa6ed388b6bf4e86acb9390ff");
		mBtnSignUp = (Button) findViewById(R.id.btn_sign_up);
		mEditUser = (EditText) findViewById(R.id.edit_uid);
		mEditPsw = (EditText) findViewById(R.id.edit_psw);
		mEditPswVal = (EditText) findViewById(R.id.edit_psw_val);
		CheckBox btnCheck = (CheckBox) findViewById(R.id.btn_check);
		TextWatcher watcher = new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				setSignable();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		};
		mEditUser.addTextChangedListener(watcher);
		mEditPsw.addTextChangedListener(watcher);
		mEditPswVal.addTextChangedListener(watcher);
		btnCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				isBtnChecked = isChecked;
				setSignable();
			}

		});
		mBtnSignUp.setOnClickListener(this);
		findViewById(R.id.img_back).setOnClickListener(this);
	}
	
	/**
	 *
	 */
	private void setSignable() {
		if (isBtnChecked) {
			
			if (mEditUser.getText().toString().length() > 5
					&& mEditPsw.getText().toString().length() > 5
					&& mEditPswVal.getText().toString().length() > 5) {
				mBtnSignUp.setEnabled(true);
			} else {
				mBtnSignUp.setEnabled(false);
			}
		} else {
			mBtnSignUp.setEnabled(false);
		}
	}

	private void signup() {
		String userName = mEditUser.getText().toString();
		String pwd = mEditPsw.getText().toString();
		String pwdVal = mEditPswVal.getText().toString();

		if (!pwd.equals(pwdVal)) {
			mEditPswVal.setText("");
			mEditPswVal.setError("两次输入结果不一致");
			return;
		}
		JDUser user = new JDUser();
		user.setUsername(userName);
		user.setPassword(pwd);
		user.signUp(this, new SaveListener() {
			public void onSuccess() {
				Toast.makeText(SignUpActivity.this, "SignUpActcivty -> signup 2",
						Toast.LENGTH_LONG).show();
				finish();
			}

			public void onFailure(int arg0, String arg1) {
				mEditUser.setError("SignUpActcivty -> signup 3");
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			finish();
			break;
		case R.id.btn_sign_up:
			signup();
			break;

		default:
			break;
		}
	}

}
