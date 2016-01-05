package com.xun.qianfanzhiche.ui;

import java.io.ByteArrayOutputStream;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.adapter.MoreInfoListAdapter;
import com.xun.qianfanzhiche.base.BaseActivity;
import com.xun.qianfanzhiche.bean.ShiTuBean;
import com.xun.qianfanzhiche.cache.ImageLoaderWithCaches;
import com.xun.qianfanzhiche.common.Constant;
import com.xun.qianfanzhiche.utils.HttpUtil;
import com.xun.qianfanzhiche.utils.LogUtil;
import com.xun.qianfanzhiche.utils.ScreenUtil;
import com.xun.qianfanzhiche.view.RadarView;

public class RadarActivity extends BaseActivity {
	private RadarView radarView;
	private TextView guessWord1, guessWord2, guessWord3;
	private ShiTuBean shiTuBean;
	private ListView moreInfoListView;
	private MoreInfoListAdapter moreInfoListAdapter;
	private ImageLoaderWithCaches imageLoaderWithCaches;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_radar);

		initView();
		initData();
	}

	private void initData() {
		Intent intent = getIntent();
		Bitmap photo = intent.getParcelableExtra("cameraData");
		if (photo != null) {
			RecogAsynctask myAsynctask = new RecogAsynctask();
			myAsynctask.execute(convertBitmapToByte(photo));
		}
	}

	private void initView() {
		setActionBarTitle("千帆智能识别");
		int viewSize = ScreenUtil.getScreenWidth(this);
		radarView = (RadarView) findViewById(R.id.radar_view);
		radarView.resetViewSize(viewSize);
		radarView.start();
		guessWord1 = (TextView) findViewById(R.id.guess_tip1);
		guessWord2 = (TextView) findViewById(R.id.guess_tip2);
		guessWord3 = (TextView) findViewById(R.id.guess_tip3);
		moreInfoListView = (ListView) findViewById(R.id.more_info_list);
		moreInfoListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (shiTuBean.getData() != null && shiTuBean.getData().getSameInfo() != null) {
					Intent intent = new Intent(RadarActivity.this, CommonWebActivity.class);
					intent.putExtra(CommonWebActivity.COMMON_WEB_URL, shiTuBean.getData().getSameInfo().get(position).getFromURL());
					startActivity(intent);
				}
			}
		});

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

			}
		}, 1000);
	}

	private void randomTextTip(final TextView textView, String tips, final int id) {
		int top = radarView.getTop();
		int bottom = radarView.getBottom();
		int left = radarView.getLeft();
		int right = radarView.getRight();
		if (left < 0) {
			left = 0;
		}
		int y = top + (int) (Math.random() * (bottom - top) * 0.8);
		int x = left + (int) (Math.random() * (right - left) * 0.8);
		final RelativeLayout.LayoutParams flp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		flp.setMargins(x, y, 0, 0);
		textView.setText(tips);
		textView.setLayoutParams(flp);
		textView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RadarActivity.this, CommonWebActivity.class);
				intent.putExtra(CommonWebActivity.COMMON_WEB_URL, Constant.BAIDU_SEARCH_BASE_URL + textView.getText().toString());
				startActivity(intent);
			}
		});
		Animation mAnimation = AnimationUtils.loadAnimation(this, R.anim.fade);
		textView.setAnimation(mAnimation);
		mAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				if (shiTuBean.getData() != null && shiTuBean.getData().getGuessWord() != null && shiTuBean.getData().getGuessWord().size() > 1 && id == 1) {
					randomTextTip(guessWord2, shiTuBean.getData().getGuessWord().get(1), 2);
				} else if (shiTuBean.getData() != null && shiTuBean.getData().getGuessWord() != null && shiTuBean.getData().getGuessWord().size() > 2
						&& id == 2) {
					randomTextTip(guessWord3, shiTuBean.getData().getGuessWord().get(2), 3);
				}
			}
		});
	}

	private class RecogAsynctask extends AsyncTask<byte[], Void, String> {

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			shiTuBean = JSON.parseObject(result, ShiTuBean.class);
			if (shiTuBean != null) {
				if (shiTuBean.getData() != null && shiTuBean.getData().getGuessWord() != null && shiTuBean.getData().getGuessWord().size() > 0) {
					randomTextTip(guessWord1, shiTuBean.getData().getGuessWord().get(0), 1);
				}
				if (shiTuBean.getData() != null && shiTuBean.getData().getSameInfo() != null) {
					imageLoaderWithCaches = new ImageLoaderWithCaches(getApplicationContext(), null, null);
					moreInfoListAdapter = new MoreInfoListAdapter(getApplicationContext(), shiTuBean.getData().getSameInfo(), imageLoaderWithCaches);
					moreInfoListView.setAdapter(moreInfoListAdapter);
				}
			}
			LogUtil.d(LogUtil.TAG, "result --> " + result);
		}

		@Override
		protected String doInBackground(byte[]... params) {
			String jsonResult = HttpUtil.request(Constant.SHITU_URL, params[0]);
			LogUtil.d(LogUtil.TAG, "jsonResult --> " + jsonResult);
			return decode(jsonResult);
		}

	}

	public static String decode(String unicodeStr) {
		if (unicodeStr == null) {
			return null;
		}
		StringBuffer retBuf = new StringBuffer();
		int maxLoop = unicodeStr.length();
		for (int i = 0; i < maxLoop; i++) {
			if (unicodeStr.charAt(i) == '\\') {
				if ((i < maxLoop - 5) && ((unicodeStr.charAt(i + 1) == 'u') || (unicodeStr.charAt(i + 1) == 'U')))
					try {
						retBuf.append((char) Integer.parseInt(unicodeStr.substring(i + 2, i + 6), 16));
						i += 5;
					} catch (NumberFormatException localNumberFormatException) {
						retBuf.append(unicodeStr.charAt(i));
					}
				else
					retBuf.append(unicodeStr.charAt(i));
			} else {
				retBuf.append(unicodeStr.charAt(i));
			}
		}
		return retBuf.toString();
	}

	public static byte[] convertBitmapToByte(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 100, baos);
		byte[] appicon = baos.toByteArray();// 转为byte数组
		return appicon;
	}
}
