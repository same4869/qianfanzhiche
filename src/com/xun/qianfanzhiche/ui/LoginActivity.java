package com.xun.qianfanzhiche.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.xun.qianfanzhiche.utils.BitmapUtil;
import com.xun.qianfanzhiche.utils.LogUtil;
import com.xun.qianfanzhiche.utils.ScreenUtil;
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
	// Constant
	public static final int PASSWORD_MIN_LENGTH = 5;
	public static final int LOGIN_SUCCESS = 0; // 登录成功
	public static final int LOGIN_FAILED = 1; // 登录失败
	public static final int LOGIN_SLIDER_TIP = 2; // 登录页面滑块向左自动滑动
	public static final int LOGIN_PHOTO_ROTATE_TIP = 3; // 登录页面加载图片转动

	// Widgets
	private ImageView moImgPhoto;
	private ImageView moImgProgress;
	private LinearLayout moLayoutWelcome;
	private View moViewSlideLine;
	private EditText moEditUsername;
	private EditText moEditPassword;
	private ImageView moImgSlider;
	private Button moBtnClearUsername;
	private Button moBtnClearPassword;
	private Button moBtnRegister;
	private RelativeLayout loginLayout;
	private ImageView loginAuthQQBtn;
	private ImageView loginAuthWeixinBtn;

	// Members
	private Handler moHandler;
	private boolean mbIsSlidingBack;
	private int miSliderMinX, miSliderMaxX, miLastX;

	public int status = 0;// 0为登录，1为注册
	private Bitmap bg;

	public static Tencent mTencent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		setHandler();
		initView();
		initListener();
	}

	private void setHandler() {
		moHandler = new Handler() {
			@Override
			public void handleMessage(Message poMsg) {
				switch (poMsg.what) {
				case LOGIN_SUCCESS:
					// 登录成功
					moHandler.postDelayed(new Runnable() {

						@Override
						public void run() {
							setResult(RESULT_OK);
							finish();
						}
					}, 3000);
					break;
				case LOGIN_FAILED:
					// 登录失败
					moHandler.postDelayed(new Runnable() {

						@Override
						public void run() {
							stopLogin();
							setStatus(0);
						}
					}, 3000);
					break;
				case LOGIN_SLIDER_TIP:
					moImgSlider.layout(miLastX, moImgSlider.getTop(), miLastX + moImgSlider.getWidth(), moImgSlider.getTop() + moImgSlider.getHeight());
					break;
				case LOGIN_PHOTO_ROTATE_TIP:
					moImgPhoto.setImageBitmap((Bitmap) poMsg.obj);
					break;
				}
			}
		};
	}

	private void initView() {
		moImgPhoto = (ImageView) findViewById(R.id.login_img_photo);
		moImgProgress = (ImageView) findViewById(R.id.login_img_progress);
		moLayoutWelcome = (LinearLayout) findViewById(R.id.login_layout_welcome);
		moViewSlideLine = findViewById(R.id.login_view_line);
		moEditUsername = (EditText) findViewById(R.id.login_edit_username);
		moEditPassword = (EditText) findViewById(R.id.login_edit_password);
		moImgSlider = (ImageView) findViewById(R.id.login_img_slide);
		moBtnClearUsername = (Button) findViewById(R.id.login_btn_clear_username);
		moBtnClearPassword = (Button) findViewById(R.id.login_btn_clear_password);
		moBtnRegister = (Button) findViewById(R.id.login_btn_register);
		loginLayout = (RelativeLayout) findViewById(R.id.login_layout);
		loginAuthQQBtn = (ImageView) findViewById(R.id.login_auth_qq_button);
		loginAuthWeixinBtn = (ImageView) findViewById(R.id.login_auth_weixin_button);

		bg = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
		mbIsSlidingBack = false;
		miLastX = 0;
		miSliderMinX = 0;
		miSliderMaxX = 0;
		setStatus(0);
	}

	private void initListener() {
		moEditUsername.addTextChangedListener(new OnEditUsername());
		moEditPassword.addTextChangedListener(new OnEditPassword());
		moBtnClearUsername.setOnClickListener(new OnClearEditText());
		moBtnClearPassword.setOnClickListener(new OnClearEditText());
		moImgSlider.setOnClickListener(new OnSliderClicked());
		moImgSlider.setOnTouchListener(new OnSliderDragged());
		moBtnRegister.setOnClickListener(new OnRegister());
		loginAuthQQBtn.setOnClickListener(this);
	}

	// 处理用户名编辑事件
	private class OnEditUsername implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// 1. 处理右侧清除按钮隐藏/显示
			if (s.length() >= 1)
				moBtnClearUsername.setVisibility(View.VISIBLE);
			else
				moBtnClearUsername.setVisibility(View.GONE);

			// 2. 处理滑块是否可滑动
			initWidgetForCanLogin();
		}

		@Override
		public void afterTextChanged(Editable s) {
		}
	}

	// 处理密码编辑事件
	private class OnEditPassword implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// 1. 处理右侧清空按钮显示/隐藏
			if (s.length() >= 1)
				moBtnClearPassword.setVisibility(View.VISIBLE);
			else if (s.length() == 0 && moBtnClearPassword.getVisibility() == View.VISIBLE)
				moBtnClearPassword.setVisibility(View.GONE);

			// 2. 处理滑块是否可滑动
			initWidgetForCanLogin();
		}

		@Override
		public void afterTextChanged(Editable s) {
		}

	}

	// 清除输入控件中的文字的事件处理
	private class OnClearEditText implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.login_btn_clear_username:
				// 如果清除帐号则密码一并清除
				moEditUsername.setText("");
				moEditPassword.setText("");
				break;
			case R.id.login_btn_clear_password:
				// 清除已输密码
				moEditPassword.setText("");
				break;
			default:
				break;
			}
		}
	}

	// 滑动图标点击事件
	private class OnSliderClicked implements OnClickListener {
		@Override
		public void onClick(View v) {
			// 如果不符合登录条件 则跳转到忘记密码界面
			if (!canLogin()) {
				// Utils.gotoActivity(LoginActivity.this, ForgetPwdActivity.class, false, null);
			}
		}
	}

	// 滑动图标滑动事件
	private class OnSliderDragged implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			ScreenUtil.closeKeybord(moEditPassword, LoginActivity.this);
			ScreenUtil.closeKeybord(moEditUsername, LoginActivity.this);
			if (canLogin() && !mbIsSlidingBack) {
				if (miSliderMaxX == 0) {
					miSliderMinX = moViewSlideLine.getLeft() - moImgSlider.getWidth() / 2;
					miSliderMaxX = moViewSlideLine.getRight() - moImgSlider.getWidth() / 2;
				}
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					miLastX = (int) event.getRawX();
				case MotionEvent.ACTION_MOVE:
					int liX = (int) event.getRawX();
					if (liX > miSliderMaxX)
						liX = miSliderMaxX;
					else if (liX < miSliderMinX)
						liX = miSliderMinX;
					if (liX != miLastX) {
						moImgSlider.layout(liX, moImgSlider.getTop(), liX + moImgSlider.getWidth(), moImgSlider.getTop() + moImgSlider.getHeight());
						miLastX = liX;
						if (miLastX == miSliderMaxX) {
							// startRotateImg();
							if (status == 0) {
								String lsUsername = moEditUsername.getText().toString();
								String lsPassword = moEditPassword.getText().toString();
								startLogin();
								login(lsUsername, lsPassword);
							}
						}
					}
					break;
				case MotionEvent.ACTION_UP:
					if ((int) event.getRawX() < miSliderMaxX)
						slideBack();
					break;
				}

			}
			return false;
		}
	}

	// 注册事件
	private class OnRegister implements OnClickListener {
		@Override
		public void onClick(View v) {
			String lsUsername = moEditUsername.getText().toString();
			String lsPassword = moEditPassword.getText().toString();
			startLogin();
			signUp(lsUsername, lsPassword);
		}
	}

	// 根据是否可以登录，初始化相关控件
	private void initWidgetForCanLogin() {
		if (canLogin()) {
			moImgSlider.setImageResource(R.drawable.ic_arrow_circle_right);
		} else {
			moImgSlider.setImageResource(R.drawable.ic_ask_circle);
		}
	}

	// 判断当前用户输入是否合法，是否可以登录
	private boolean canLogin() {
		Editable loUsername = moEditUsername.getText();
		Editable loPassword = moEditPassword.getText();
		return !StringUtil.isStringNullorBlank(loUsername.toString()) && loPassword.length() >= PASSWORD_MIN_LENGTH;
	}

	// 滑块向会自动滑动
	private void slideBack() {
		new Thread() {
			@Override
			public void run() {
				mbIsSlidingBack = true;
				while (miLastX > miSliderMinX) {
					miLastX -= 5;
					if (miLastX < miSliderMinX)
						miLastX = miSliderMinX;
					Message loMsg = new Message();
					loMsg.what = LOGIN_SLIDER_TIP;
					moHandler.sendMessage(loMsg);
					try {
						Thread.sleep(3);
					} catch (InterruptedException e) {
					}
				}
				mbIsSlidingBack = false;
			}
		}.start();
	}

	// 动画开启
	private void startLogin() {
		Animation loAnimRotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
		Animation loAnimScale = AnimationUtils.loadAnimation(this, R.anim.login_photo_scale_small);
		// 匀速动画
		LinearInterpolator linearInterpolator = new LinearInterpolator();
		// 加速动画
		// AccelerateInterpolator accelerateInterpolator = new
		// AccelerateInterpolator();
		// 弹跳动画
		// BounceInterpolator bounceInterpolator = new BounceInterpolator();

		loAnimRotate.setInterpolator(linearInterpolator);
		loAnimScale.setInterpolator(linearInterpolator);
		moImgProgress.setVisibility(View.VISIBLE);
		moImgProgress.startAnimation(loAnimRotate);
		moImgPhoto.startAnimation(loAnimScale);

		moImgSlider.setVisibility(View.GONE);
		moViewSlideLine.setVisibility(View.GONE);
		moEditUsername.setVisibility(View.GONE);
		moEditPassword.setVisibility(View.GONE);
		moBtnClearUsername.setVisibility(View.GONE);
		moBtnClearPassword.setVisibility(View.GONE);
		moBtnRegister.setVisibility(View.GONE);

		moLayoutWelcome.setVisibility(View.VISIBLE);
	}

	// 动画结束
	private void stopLogin() {
		Animation loAnimScale = AnimationUtils.loadAnimation(this, R.anim.login_photo_scale_big);
		LinearInterpolator loLin = new LinearInterpolator();
		loAnimScale.setInterpolator(loLin);
		moImgProgress.clearAnimation();
		moImgProgress.setVisibility(View.GONE);
		moImgPhoto.clearAnimation();
		moImgPhoto.startAnimation(loAnimScale);

		moImgSlider.setVisibility(View.VISIBLE);
		moViewSlideLine.setVisibility(View.VISIBLE);
		moEditUsername.setVisibility(View.VISIBLE);
		moEditPassword.setVisibility(View.VISIBLE);
		moBtnClearUsername.setVisibility(View.VISIBLE);
		moBtnClearPassword.setVisibility(View.VISIBLE);
		moBtnRegister.setVisibility(View.VISIBLE);
		moLayoutWelcome.setVisibility(View.GONE);
	}

	private void setStatus(int status) {
		if (status == 0) {
			moBtnRegister.setVisibility(View.GONE);
			loginLayout.setBackground(new BitmapDrawable(getResources(), BitmapUtil.fastblur(getApplicationContext(), bg, 15)));
			setActionBarTitle("登录");
		} else {
			moBtnRegister.setVisibility(View.VISIBLE);
			loginLayout.setBackground(new BitmapDrawable(getResources(), BitmapUtil.fastblur(getApplicationContext(), bg, 25)));
			setActionBarTitle("注册");
		}
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
				setStatus(0);
			}

			@Override
			public void onFailure(int code, String msg) {
				ToastUtil.show(getApplicationContext(), "注册失败 --> " + msg);
			}
		});
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
				moHandler.sendEmptyMessage(LOGIN_SUCCESS);
			}

			@Override
			public void onFailure(int code, String msg) {
				ToastUtil.show(getApplicationContext(), "登录失败 --> " + msg);
				moHandler.sendEmptyMessage(LOGIN_FAILED);
			}
		});
	}

	@Override
	public void onTextTvClick() {
		if (status == 0) {
			ToastUtil.show(getApplicationContext(), "登录");
			status = 1;
			setStatus(status);
		} else {
			ToastUtil.show(getApplicationContext(), "注册");
			status = 0;
			setStatus(status);
		}
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
		default:
			break;
		}
	}
}
