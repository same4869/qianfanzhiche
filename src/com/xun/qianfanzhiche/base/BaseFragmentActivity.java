package com.xun.qianfanzhiche.base;

import net.youmi.android.AdManager;
import net.youmi.android.offers.OffersManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.update.UmengUpdateAgent;
import com.xun.qianfanzhiche.common.Constant;

public class BaseFragmentActivity extends FragmentActivity {
	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate(arg0);
		
		UmengUpdateAgent.update(this);
		
		AdManager.getInstance(getApplicationContext()).init(Constant.YOU_MI_APP_ID, Constant.YOU_MI_APP_SECRET, true);
		OffersManager.getInstance(getApplicationContext()).onAppLaunch();
		
		PushAgent mPushAgent = PushAgent.getInstance(getApplicationContext());
		mPushAgent.enable();
		PushAgent.getInstance(getApplicationContext()).onAppStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		OffersManager.getInstance(getApplicationContext()).onAppExit();
	}

}
