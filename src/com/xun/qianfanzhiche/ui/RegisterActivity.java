package com.xun.qianfanzhiche.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import cn.bmob.v3.listener.SaveListener;

import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.base.BaseActivity;
import com.xun.qianfanzhiche.bean.User;
import com.xun.qianfanzhiche.utils.StringUtil;
import com.xun.qianfanzhiche.utils.ToastUtil;

public class RegisterActivity extends BaseActivity implements OnClickListener {
	private EditText usernameEt;
	private EditText passwordEt;
	private EditText password2Et;
	private Button registerBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		initView();
	}

	private void initView() {
		usernameEt = (EditText) findViewById(R.id.et_account);
		passwordEt = (EditText) findViewById(R.id.et_password);
		password2Et = (EditText) findViewById(R.id.et_pwd_again);
		registerBtn = (Button) findViewById(R.id.btn_register);
		registerBtn.setOnClickListener(this);
		setActionBarTitle("欢迎注册");
	}

	// 注册
	private void signUp(String username, String password) {
		User bu = new User();
		bu.setUsername(username);
		bu.setPassword(password);
		// 注意：不能用save方法进行注册
		bu.signUp(this, new SaveListener() {
			@Override
			public void onSuccess() {
				ToastUtil.show(getApplicationContext(), "注册成功");
			}

			@Override
			public void onFailure(int code, String msg) {
				ToastUtil.show(getApplicationContext(), "注册失败 --> " + msg);
			}
		});
	}

	private boolean canSignUp() {
		String userNameString = usernameEt.getText().toString();
		String passwordString = passwordEt.getText().toString();
		String password2String = password2Et.getText().toString();
		if (!StringUtil.isStringNullorBlank(userNameString) && !StringUtil.isStringNullorBlank(passwordString)
				&& !StringUtil.isStringNullorBlank(password2String)) {
			if (passwordString.equals(password2String)) {
				return true;
			}
			return false;
		} else {
			return false;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_register:
			if (canSignUp()) {
				signUp(usernameEt.getText().toString(), passwordEt.getText().toString());
			} else {
				ToastUtil.show(getApplicationContext(), "请确认都不为空且两次密码输入一致");
			}

			break;

		default:
			break;
		}

	}

}
