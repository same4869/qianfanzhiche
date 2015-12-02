package com.xun.qianfanzhiche.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.base.BaseActivity;

/**
 * 
 * @author xunwang
 * 
 *         2015-11-24
 */
public class CommonWebActivity extends BaseActivity {
	public static final String COMMON_WEB_URL = "common_web_url";
	public static final String COMMON_WEB_TITLE = "common_web_title";

	private WebView commonWebView;
	private ProgressBar progressBar;

	private MyWebChromeClient myWebChromeClient;
	private FrameLayout videoFullLayout;
	private View myView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common_web);

		initView();
	}

	private void initView() {
		Intent intent = getIntent();
		String url = intent.getStringExtra(COMMON_WEB_URL);

		setActionBarTitle(intent.getStringExtra(COMMON_WEB_TITLE));

		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		videoFullLayout = (FrameLayout) findViewById(R.id.video_fullView);

		commonWebView = (WebView) findViewById(R.id.common_webview);
		initSetting();
		myWebChromeClient = new MyWebChromeClient();
		commonWebView.setWebChromeClient(myWebChromeClient);
		commonWebView.setWebViewClient(new MyWebViewClient());
		commonWebView.loadUrl(url);

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

	class MyWebChromeClient extends WebChromeClient {
		private CustomViewCallback myCallback = null;

		@Override
		public void onShowCustomView(View view, CustomViewCallback callback) {
			super.onShowCustomView(view, callback);
			setActionBarGone();
			commonWebView.setVisibility(View.INVISIBLE);
			// 如果一个视图已经存在，那么立刻终止并新建一个
			if (myView != null) {
				callback.onCustomViewHidden();
				myView = null;
				return;
			}
			videoFullLayout.addView(view);
			myView = view;
			myCallback = callback;
			videoFullLayout.setVisibility(View.VISIBLE);
		}

		@Override
		public void onHideCustomView() {
			super.onHideCustomView();
			if (myView == null)// 不是全屏播放状态
				return;

			myView.setVisibility(View.GONE);
			videoFullLayout.removeView(myView);
			myView = null;
			videoFullLayout.setVisibility(View.GONE);
			myCallback.onCustomViewHidden();
			commonWebView.setVisibility(View.VISIBLE);
			setActionBarVisible();
		}
	}

	public boolean inCustomView() {
		return (myView != null);
	}

	public void hideCustomView() {
		myWebChromeClient.onHideCustomView();
	}
}
