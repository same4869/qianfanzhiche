package com.xun.qianfanzhiche.ui;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.base.BaseActivity;

public class CommonWebActivity extends BaseActivity {
	private WebView commonWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common_web);

		initView();
	}

	private void initView() {
		commonWebView = (WebView) findViewById(R.id.common_webview);
		commonWebView.setWebViewClient(new MyWebViewClient());
		commonWebView.loadUrl("http://car.m.autohome.com.cn/");

	}

	private class MyWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return super.shouldOverrideUrlLoading(view, url);
		}

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		commonWebView.goBack();
	}
}
