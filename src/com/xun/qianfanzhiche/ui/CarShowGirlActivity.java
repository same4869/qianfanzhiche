package com.xun.qianfanzhiche.ui;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.adapter.HorizontalScrollViewAdapter;
import com.xun.qianfanzhiche.base.BaseActivity;
import com.xun.qianfanzhiche.cache.ImageLoaderWithCaches;
import com.xun.qianfanzhiche.common.Constant;
import com.xun.qianfanzhiche.view.MyHorizontalScrollView;
import com.xun.qianfanzhiche.view.MyHorizontalScrollView.CurrentImageChangeListener;
import com.xun.qianfanzhiche.view.MyHorizontalScrollView.OnItemClickListener;

public class CarShowGirlActivity extends BaseActivity {
	private MyHorizontalScrollView mHorizontalScrollView;
	private HorizontalScrollViewAdapter mAdapter;
	private ImageView mImg;
	private List<Integer> mDatas = new ArrayList<Integer>();
	private ImageLoaderWithCaches imageLoaderWithCaches;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_car_show_girl);

		mImg = (ImageView) findViewById(R.id.id_content);

		mHorizontalScrollView = (MyHorizontalScrollView) findViewById(R.id.id_horizontalScrollView);
		imageLoaderWithCaches = new ImageLoaderWithCaches(getApplicationContext(), null, null);
		mAdapter = new HorizontalScrollViewAdapter(this, mDatas, imageLoaderWithCaches);
		// 添加滚动回调
		mHorizontalScrollView.setCurrentImageChangeListener(new CurrentImageChangeListener() {
			@Override
			public void onCurrentImgChanged(int position, View viewIndicator) {
				// mImg.setImageResource(mDatas.get(position));
				imageLoaderWithCaches.loadImagesWithUrl(mImg, Constant.QINIU_IMG_BASE_URL + "qfzc00" + (position + 1)
						+ ".jpg");
				viewIndicator.setBackgroundColor(Color.parseColor("#AA024DA4"));
			}
		});
		// 添加点击回调
		mHorizontalScrollView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onClick(View view, int position) {
				// mImg.setImageResource(mDatas.get(position));
				imageLoaderWithCaches.loadImagesWithUrl(mImg, Constant.QINIU_IMG_BASE_URL + "qfzc00" + (position + 1)
						+ ".jpg");
				view.setBackgroundColor(Color.parseColor("#AA024DA4"));
			}
		});
		// 设置适配器
		mHorizontalScrollView.initDatas(mAdapter);
	}
}
