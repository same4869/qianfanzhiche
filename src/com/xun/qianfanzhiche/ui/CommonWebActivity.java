package com.xun.qianfanzhiche.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.base.BaseActivity;
import com.xun.qianfanzhiche.fragment.MainFragment;

/**
 * 
 * @author xunwang
 * 
 *         2015-11-24
 */
public class CommonWebActivity extends BaseActivity {
	public static final String COMMON_WEB_URL = "common_web_url";
	
	private WebView commonWebView;
	private ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common_web);

		initView();
	}

	private void initView() {
		Intent intent = getIntent();
		String url = intent.getStringExtra(COMMON_WEB_URL);

		progressBar = (ProgressBar) findViewById(R.id.progressBar);

		commonWebView = (WebView) findViewById(R.id.common_webview);
		initSetting();
		commonWebView.setWebViewClient(new MyWebViewClient());
		commonWebView.loadUrl(url);
		//"http://auto.sina.cn/guangzhouchezhan/?vt=4&pos=25"

	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initSetting() {
		WebSettings settings = commonWebView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
	}

	private class MyWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return super.shouldOverrideUrlLoading(view, url);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			progressBar.setVisibility(View.VISIBLE);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			progressBar.setVisibility(View.GONE);
		}

	}

	@Override
	public void onBackImgClick() {
		if (commonWebView.canGoBack()) {
			commonWebView.goBack();
			return;
		}
		super.onBackPressed();
	}

	@Override
	public void onBackPressed() {
		if (commonWebView.canGoBack()) {
			commonWebView.goBack();
			return;
		}
		super.onBackPressed();
	}
}
