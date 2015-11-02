package com.xun.qianfanzhiche.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.base.BaseActivity;
import com.xun.qianfanzhiche.utils.StringUtil;
import com.xun.qianfanzhiche.utils.ToastUtil;

public class SignUpActivity extends BaseActivity implements OnClickListener {
	private EditText username, password;
	private Button submit, login;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		initView();
	}

	private void initView() {
		username = (EditText) findViewById(R.id.sign_up_username);
		password = (EditText) findViewById(R.id.sign_up_password);
		submit = (Button) findViewById(R.id.sign_up_submit);
		submit.setOnClickListener(this);
		login = (Button) findViewById(R.id.sign_up_login);
		login.setOnClickListener(this);
	}

	private void signUp(String username, String password) {
		BmobUser bu = new BmobUser();
		bu.setUsername(username);
		bu.setPassword(password);
		// 注意：不能用save方法进行注册
		bu.signUp(this, new SaveListener() {
			@Override
			public void onSuccess() {
				ToastUtil.show(getApplicationContext(), "注册成功");
				finish();
			}

			@Override
			public void onFailure(int code, String msg) {
				ToastUtil.show(getApplicationContext(), "注册失败 --> " + msg);
			}
		});
	}

	private void login(String username, String password) {
		BmobUser bu2 = new BmobUser();
		bu2.setUsername(username);
		bu2.setPassword(password);
		bu2.login(this, new SaveListener() {
			@Override
			public void onSuccess() {
				ToastUtil.show(getApplicationContext(), "登录成功");
				finish();
			}

			@Override
			public void onFailure(int code, String msg) {
				ToastUtil.show(getApplicationContext(), "登录失败");
			}
		});
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.sign_up_submit:
			if (!StringUtil.isStringNullorBlank(username.getText().toString()) && !StringUtil.isStringNullorBlank(password.getText().toString())) {
				signUp(username.getText().toString(), password.getText().toString());
			}
			break;
		case R.id.sign_up_login:
			if (!StringUtil.isStringNullorBlank(username.getText().toString()) && !StringUtil.isStringNullorBlank(password.getText().toString())) {
				login(username.getText().toString(), password.getText().toString());
			}
			break;
		default:
			break;
		}
	}
}
