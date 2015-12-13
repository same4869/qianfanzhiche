package com.xun.qianfanzhiche.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.base.BaseActivity;
import com.xun.qianfanzhiche.manager.DownLoadManager;

public class SplashActivity extends BaseActivity {
	private final int SPLASH_DISPLAY_LENGHT = 2500;
	private ImageView splashImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		setActionBarGone();

		splashImageView = (ImageView) findViewById(R.id.splash);
		try {
			String myJpgPath = DownLoadManager.ALBUM_PATH + DownLoadManager.mFileName;
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 1;
			Bitmap bm = BitmapFactory.decodeFile(myJpgPath, options);
			if (bm == null) {
				splashImageView.setImageResource(R.drawable.splash001);
			} else {
				splashImageView.setImageBitmap(bm);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent mainIntent = new Intent(SplashActivity.this, ZhiCheMainActivity.class);
				startActivity(mainIntent);
				finish();
			}
		}, SPLASH_DISPLAY_LENGHT);
	}
}
