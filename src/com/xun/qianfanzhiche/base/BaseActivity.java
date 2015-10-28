package com.xun.qianfanzhiche.base;

import android.app.Activity;
import android.os.Bundle;

import com.xun.qianfanzhiche.app.ZhiCheApp;

public class BaseActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ZhiCheApp.getInstance().addActivity(this);
	}
}
