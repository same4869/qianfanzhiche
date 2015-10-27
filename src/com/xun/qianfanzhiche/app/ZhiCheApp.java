package com.xun.qianfanzhiche.app;

import android.app.Application;
import cn.bmob.v3.Bmob;

import com.xun.qianfanzhiche.common.Constant;

public class ZhiCheApp extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		Bmob.initialize(getApplicationContext(), Constant.BMOB_APP_ID);
	}
}
