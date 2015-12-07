package com.xun.qianfanzhiche.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.VerifySMSCodeListener;

import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.base.BaseActivity;
import com.xun.qianfanzhiche.bean.User;
import com.xun.qianfanzhiche.utils.ToastUtil;

public class UserBindPhoneActivity extends BaseActivity implements OnClickListener {
	private EditText phoneNumEt;
	private EditText verifyCodeEt;
	private TextView sendBtn;
	private TextView bindBtn;

	private MyCountTimer timer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bind_phone);

		initView();
	}

	private void initView() {
		setActionBarTitle("绑定手机");
		phoneNumEt = (EditText) findViewById(R.id.et_number);
		verifyCodeEt = (EditText) findViewById(R.id.et_input);
		sendBtn = (TextView) findViewById(R.id.tv_send);
		bindBtn = (TextView) findViewById(R.id.tv_bind);
		sendBtn.setOnClickListener(this);
		bindBtn.setOnClickListener(this);
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
			BmobSMS.requestSMSCode(this, number, "手机号码登陆模板", new RequestSMSCodeListener() {

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

	private void verifyOrBind() {
		final String phone = phoneNumEt.getText().toString();
		String code = verifyCodeEt.getText().toString();
		if (TextUtils.isEmpty(phone)) {
			ToastUtil.show(getApplicationContext(), "手机号码不能为空");
			return;
		}

		if (TextUtils.isEmpty(code)) {
			ToastUtil.show(getApplicationContext(), "验证码不能为空");
			return;
		}
		final ProgressDialog progress = new ProgressDialog(this);
		progress.setMessage("正在验证短信验证码...");
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		// V3.3.9提供的一键注册或登录方式，可传手机号码和验证码
		BmobSMS.verifySmsCode(this, phone, code, new VerifySMSCodeListener() {

			@Override
			public void done(BmobException ex) {
				progress.dismiss();
				if (ex == null) {
					ToastUtil.show(getApplicationContext(), "手机号码已验证");
					bindMobilePhone(phone);
				} else {
					ToastUtil.show(getApplicationContext(), "验证失败：code=" + ex.getErrorCode() + "，错误描述：" + ex.getLocalizedMessage());
				}
			}
		});
	}

	private void bindMobilePhone(String phone) {
		// 开发者在给用户绑定手机号码的时候需要提交两个字段的值：mobilePhoneNumber、mobilePhoneNumberVerified
		User user = new User();
		user.setMobilePhoneNumber(phone);
		user.setMobilePhoneNumberVerified(true);
		User cur = BmobUser.getCurrentUser(UserBindPhoneActivity.this, User.class);
		user.update(UserBindPhoneActivity.this, cur.getObjectId(), new UpdateListener() {

			@Override
			public void onSuccess() {
				ToastUtil.show(getApplicationContext(), "手机号码绑定成功");
				finish();
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				ToastUtil.show(getApplicationContext(), "手机号码绑定失败：" + arg0 + "-" + arg1);
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_send:
			requestSMSCode();
			break;
		case R.id.tv_bind:
			verifyOrBind();
			break;
		default:
			break;
		}
	}

}
