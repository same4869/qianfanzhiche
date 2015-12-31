package com.xun.qianfanzhiche.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

import com.umeng.analytics.MobclickAgent;
import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.app.ZhiCheApp;
import com.xun.qianfanzhiche.view.ZhiCheActionBar;
import com.xun.qianfanzhiche.view.ZhiCheActionBar.ActionBarListener;

public class BaseActivity extends Activity implements ActionBarListener {
	private FrameLayout contentView;
	private ZhiCheActionBar zhiCheActionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_base);
		ZhiCheApp.getInstance().addActivity(this);
		zhiCheActionBar = (ZhiCheActionBar) findViewById(R.id.actionbar);
		zhiCheActionBar.setOnActionBarListener(this);
		contentView = (FrameLayout) findViewById(R.id.base_content);
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
	public void setContentView(int layoutResId) {
		LayoutInflater.from(this).inflate(layoutResId, contentView);
	}

	@Override
	public void setContentView(View view) {
		contentView.addView(view);
	}

	@Override
	public void setContentView(View view, LayoutParams params) {
		contentView.addView(view, params);
	}

	public void setActionBarGone() {
		zhiCheActionBar.setVisibility(View.GONE);
	}

	public void setActionBarVisible() {
		zhiCheActionBar.setVisibility(View.VISIBLE);
	}

	public void setActionBarTitle(String string) {
		zhiCheActionBar.setTitle(string);
	}
	
	public ZhiCheActionBar getZhiCheActionBar() {
		return zhiCheActionBar;
	}

	@Override
	public void onBackImgClick() {
		finish();
	}

	@Override
	public void onTextTvClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onActionBarTopClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAddImgClick(int type) {
		// TODO Auto-generated method stub
		
	}

}
