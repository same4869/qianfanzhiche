package com.xun.qianfanzhiche.ui;

import android.app.ProgressDialog;
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
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.ResetPasswordByCodeListener;

import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.base.BaseActivity;
import com.xun.qianfanzhiche.bean.User;
import com.xun.qianfanzhiche.utils.StringUtil;
import com.xun.qianfanzhiche.utils.ToastUtil;

public class ResetPasswordActivity extends BaseActivity implements OnClickListener {
	private EditText phoneNumEt;
	private EditText verifyCodeEt;
	private EditText passwordEt;
	private Button sendBtn;
	private Button resetBtn;

	private MyCountTimer timer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reset_pwd);

		initView();
	}

	private void initView() {
		setActionBarTitle("重置密码");
		User user = BmobUser.getCurrentUser(this, User.class);
		phoneNumEt = (EditText) findViewById(R.id.et_phone);
		if (!StringUtil.isStringNullorBlank(user.getMobilePhoneNumber())) {
			phoneNumEt.setText(user.getMobilePhoneNumber());
		}
		verifyCodeEt = (EditText) findViewById(R.id.et_verify_code);
		passwordEt = (EditText) findViewById(R.id.et_pwd);
		sendBtn = (Button) findViewById(R.id.btn_send);
		resetBtn = (Button) findViewById(R.id.btn_reset);
		sendBtn.setOnClickListener(this);
		resetBtn.setOnClickListener(this);
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
			BmobSMS.requestSMSCode(this, number, "重置密码模板", new RequestSMSCodeListener() {

				@Override
				public void done(Integer smsId, BmobException ex) {
					if (ex == null) {// 验证码发送成功
						ToastUtil.show(getApplicationContext(), "验证码发送成功");
					} else {// 如果验证码发送错误，可停止计时
						timer.cancel();
					}
				}
			});
		} else {
			ToastUtil.show(getApplicationContext(), "请输入手机号码");
		}
	}

	private void resetPwd() {
		final String code = verifyCodeEt.getText().toString();
		final String pwd = passwordEt.getText().toString();
		if (TextUtils.isEmpty(code)) {
			ToastUtil.show(getApplicationContext(), "验证码不能为空");
			return;
		}
		if (TextUtils.isEmpty(pwd)) {
			ToastUtil.show(getApplicationContext(), "密码不能为空");
			return;
		}
		final ProgressDialog progress = new ProgressDialog(ResetPasswordActivity.this);
		progress.setMessage("正在重置密码...");
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		// V3.3.9提供的重置密码功能，只需要输入验证码和新密码即可
		BmobUser.resetPasswordBySMSCode(this, code, pwd, new ResetPasswordByCodeListener() {

			@Override
			public void done(BmobException ex) {
				progress.dismiss();
				if (ex == null) {
					ToastUtil.show(getApplicationContext(), "密码重置成功");
					finish();
				} else {
					ToastUtil.show(getApplicationContext(), "密码重置失败：code=" + ex.getErrorCode() + "，错误描述：" + ex.getLocalizedMessage());
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
		case R.id.btn_send:
			requestSMSCode();
			break;
		case R.id.btn_reset:
			resetPwd();
			break;
		default:
			break;
		}
	}

}
