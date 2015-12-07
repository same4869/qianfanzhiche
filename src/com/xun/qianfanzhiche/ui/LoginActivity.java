package com.xun.qianfanzhiche.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.BmobUser.BmobThirdUserAuth;
import cn.bmob.v3.listener.OtherLoginListener;
import cn.bmob.v3.listener.SaveListener;

import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.app.ZhiCheApp;
import com.xun.qianfanzhiche.base.BaseActivity;
import com.xun.qianfanzhiche.bean.User;
import com.xun.qianfanzhiche.common.Constant;
import com.xun.qianfanzhiche.utils.LogUtil;
import com.xun.qianfanzhiche.utils.StringUtil;
import com.xun.qianfanzhiche.utils.ToastUtil;

/**
 * 注册登录页面
 * 
 * @author xunwang
 * 
 *         2015-11-18
 */
public class LoginActivity extends BaseActivity implements OnClickListener {
	public static final int PASSWORD_MIN_LENGTH = 5;

	private EditText accountEditText, passwordEditText;
	private Button loginBtn, oneKeyBtn, registerBtn;
	private ImageView loginQQBtn;

	public static Tencent mTencent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		initView();
	}

	private void initView() {
		accountEditText = (EditText) findViewById(R.id.et_account);
		passwordEditText = (EditText) findViewById(R.id.et_password);
		loginBtn = (Button) findViewById(R.id.btn_login);
		oneKeyBtn = (Button) findViewById(R.id.btn_onekey);
		registerBtn = (Button) findViewById(R.id.btn_register);
		loginQQBtn = (ImageView) findViewById(R.id.login_auth_qq_button);
		loginBtn.setOnClickListener(this);
		oneKeyBtn.setOnClickListener(this);
		registerBtn.setOnClickListener(this);
		loginQQBtn.setOnClickListener(this);
		setActionBarTitle("欢迎登录");
	}

	// 判断当前用户输入是否合法，是否可以登录
	private boolean canLogin() {
		Editable loUsername = accountEditText.getText();
		Editable loPassword = passwordEditText.getText();
		if (loUsername.length() < PASSWORD_MIN_LENGTH && loPassword.length() < PASSWORD_MIN_LENGTH) {
			return false;
		}
		return !StringUtil.isStringNullorBlank(loUsername.toString()) && loPassword.length() >= PASSWORD_MIN_LENGTH;
	}

	// 登录
	private void login(String username, String password) {
		User bu2 = new User();
		bu2.setUsername(username);
		bu2.setPassword(password);
		bu2.login(this, new SaveListener() {
			@Override
			public void onSuccess() {
				ToastUtil.show(getApplicationContext(), "登录成功");
				setResult(RESULT_OK);
				finish();
			}

			@Override
			public void onFailure(int code, String msg) {
				ToastUtil.show(getApplicationContext(), "登录失败 --> " + msg);
			}
		});
	}

	private void qqAuthorize() {
		if (mTencent == null) {
			mTencent = Tencent.createInstance(Constant.QQ_APP_ID, getApplicationContext());
		}
		mTencent.logout(this);
		mTencent.login(this, "all", new IUiListener() {

			@Override
			public void onComplete(Object arg0) {
				if (arg0 != null) {
					JSONObject jsonObject = (JSONObject) arg0;
					try {
						String token = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_ACCESS_TOKEN);
						String expires = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_EXPIRES_IN);
						String openId = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_OPEN_ID);
						BmobThirdUserAuth authInfo = new BmobThirdUserAuth(BmobThirdUserAuth.SNS_TYPE_QQ, token, expires, openId);
						loginWithAuth(authInfo);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onError(UiError arg0) {
				ToastUtil.show(getApplicationContext(), "QQ授权出错：" + arg0.errorCode + "--" + arg0.errorDetail);
			}

			@Override
			public void onCancel() {
				ToastUtil.show(getApplicationContext(), "取消qq授权");
			}
		});
	}

	public void loginWithAuth(final BmobThirdUserAuth authInfo) {
		BmobUser.loginWithAuthData(LoginActivity.this, authInfo, new OtherLoginListener() {

			@Override
			public void onSuccess(JSONObject userAuth) {
				LogUtil.d(LogUtil.TAG, authInfo.getSnsType() + "登陆成功返回:" + userAuth);
				Intent intent = new Intent(LoginActivity.this, ZhiCheMainActivity.class);
				intent.putExtra("json", userAuth.toString());
				intent.putExtra("from", authInfo.getSnsType());
				startActivity(intent);
			}

			@Override
			public void onFailure(int code, String msg) {
				ToastUtil.show(getApplicationContext(), "第三方登陆失败：" + msg);
			}

		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mTencent.onActivityResult(requestCode, resultCode, data);
		LogUtil.d(LogUtil.TAG, "data --> " + data);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_auth_qq_button:
			qqAuthorize();
			break;
		case R.id.login_auth_weixin_button:
			// send oauth request
			final SendAuth.Req req = new SendAuth.Req();
			req.scope = "snsapi_userinfo";
			req.state = "wechat_sdk_qianfanzhiche";
			ZhiCheApp.api.sendReq(req);
			break;
		case R.id.btn_login:
			if (canLogin()) {
				login(accountEditText.getText().toString(), passwordEditText.getText().toString());
			} else {
				ToastUtil.show(getApplicationContext(), "用户名或密码不能低于5位噢");
			}
			break;
		case R.id.btn_register:
			Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
			startActivity(registerIntent);
			break;
		case R.id.btn_onekey:
			Intent loginOneKeyIntent = new Intent(LoginActivity.this, LoginOneKeyActivity.class);
			startActivity(loginOneKeyIntent);
			break;
		default:
			break;
		}
	}
}
