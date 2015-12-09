package com.xun.qianfanzhiche.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.base.BaseActivity;

public class AboutActivity extends BaseActivity implements OnClickListener {
	private static final String WEIBO_URL = "http://weibo.com/qianfanzhiche";
	
	private TextView weiboTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		setActionBarTitle("关于我们");

		initView();
	}

	private void initView() {
		weiboTv = (TextView) findViewById(R.id.about_weibo);
		weiboTv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.about_weibo:
			Uri uri = Uri.parse(WEIBO_URL);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
			break;

		default:
			break;
		}

	}
}
