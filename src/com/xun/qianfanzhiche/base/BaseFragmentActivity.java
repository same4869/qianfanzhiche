package com.xun.qianfanzhiche.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.update.UmengUpdateAgent;

public class BaseFragmentActivity extends FragmentActivity {
	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate(arg0);

		UmengUpdateAgent.update(this);

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
	}

}
