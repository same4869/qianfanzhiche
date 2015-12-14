package com.xun.qianfanzhiche.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.RequestSMSCodeListener;

import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.base.BaseActivity;
import com.xun.qianfanzhiche.bean.User;
import com.xun.qianfanzhiche.utils.ToastUtil;

public class LoginOneKeyActivity extends BaseActivity implements OnClickListener {
	private EditText phoneNumEt;
	private EditText verifyCodeEt;
	private Button sendBtn;
	private Button loginBtn;

	private MyCountTimer timer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_onekey);

		initView();
	}

	private void initView() {
		phoneNumEt = (EditText) findViewById(R.id.et_phone);
		verifyCodeEt = (EditText) findViewById(R.id.et_verify_code);
		sendBtn = (Button) findViewById(R.id.btn_send);
		loginBtn = (Button) findViewById(R.id.btn_login);
		sendBtn.setOnClickListener(this);
		loginBtn.setOnClickListener(this);
		setActionBarTitle("手机号码一键登录");
	}

	class MyCountTimer extends CountDownTimer {

		public MyCountTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			sendBtn.setText((millisUntilFinished / 1000) + "秒后重发");
			sendBtn.setEnabled(false);
		}

		@Override
		public void onFinish() {
			sendBtn.setText("重新发送验证码");
			sendBtn.setEnabled(true);
		}
	}

	private void requestSMSCode() {
		String number = phoneNumEt.getText().toString();
		if (!TextUtils.isEmpty(number)) {
			timer = new MyCountTimer(60000, 1000);
			timer.start();
			BmobSMS.requestSMSCode(getApplicationContext(), number, "一键注册或登录模板", new RequestSMSCodeListener() {

				@Override
				public void done(Integer smsId, BmobException ex) {
					if (ex == null) {// 验证码发送成功
						ToastUtil.show(getApplicationContext(), "验证码发送成功");
					} else {
						timer.cancel();
					}
				}
			});
		} else {
			ToastUtil.show(getApplicationContext(), "请输入手机号码");
		}
	}

	private void oneKeyLogin() {
		final String phone = phoneNumEt.getText().toString();
		final String code = verifyCodeEt.getText().toString();

		if (TextUtils.isEmpty(phone)) {
			ToastUtil.show(getApplicationContext(), "手机号码不能为空噢");
			return;
		}

		if (TextUtils.isEmpty(code)) {
			ToastUtil.show(getApplicationContext(), "验证码不能为空噢");
			return;
		}
		final ProgressDialog progress = new ProgressDialog(LoginOneKeyActivity.this);
		progress.setMessage("正在验证短信验证码...");
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		// V3.3.9提供的一键注册或登录方式，可传手机号码和验证码
		BmobUser.signOrLoginByMobilePhone(LoginOneKeyActivity.this, phone, code, new LogInListener<User>() {

			@Override
			public void done(User user, BmobException ex) {
				progress.dismiss();
				if (ex == null) {
					ToastUtil.show(getApplicationContext(), "登录成功,请尽快重置密码");
					Intent intent = new Intent(LoginOneKeyActivity.this, ZhiCheMainActivity.class);
					startActivity(intent);
					finish();
				} else {
					ToastUtil.show(getApplicationContext(), "登录失败：code=" + ex.getErrorCode() + "，错误描述：" + ex.getLocalizedMessage());
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (timer != null) {
			timer.cancel();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			oneKeyLogin();
			break;
		case R.id.btn_send:
			requestSMSCode();
			break;
		default:
			break;
		}
	}

}
