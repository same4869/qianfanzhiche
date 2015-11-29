package com.xun.qianfanzhiche.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.base.BaseActivity;

public class SplashActivity extends BaseActivity {
	private final int SPLASH_DISPLAY_LENGHT = 1000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		setActionBarGone();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent mainIntent = new Intent(SplashActivity.this, ZhiCheMainActivity.class);
				startActivity(mainIntent);
				finish();
			}
		}, SPLASH_DISPLAY_LENGHT);
	}
}
