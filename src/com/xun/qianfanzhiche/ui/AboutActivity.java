package com.xun.qianfanzhiche.ui;

import android.os.Bundle;

import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.base.BaseActivity;

public class AboutActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		setActionBarTitle("关于我们");
	}
}
