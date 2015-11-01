package com.xun.qianfanzhiche.app;

import android.app.Activity;
import android.app.Application;
import cn.bmob.v3.Bmob;

import com.xun.qianfanzhiche.common.Constant;
import com.xun.qianfanzhiche.utils.ActivityManagerUtils;

public class ZhiCheApp extends Application {
	private static ZhiCheApp mApplication = null;

	public static ZhiCheApp getInstance() {
		return mApplication;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mApplication = this;
		Bmob.initialize(getApplicationContext(), Constant.BMOB_APP_ID);
	}

	public void addActivity(Activity ac) {
		ActivityManagerUtils.getInstance().addActivity(ac);
	}

	public void exit() {
		ActivityManagerUtils.getInstance().removeAllActivity();
	}

	public Activity getTopActivity() {
		return ActivityManagerUtils.getInstance().getTopActivity();
	}
}
