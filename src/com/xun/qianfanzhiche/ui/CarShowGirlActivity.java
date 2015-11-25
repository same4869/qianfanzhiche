package com.xun.qianfanzhiche.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.adapter.GalleryAdapter;
import com.xun.qianfanzhiche.adapter.GalleryAdapter.OnItemClickLitener;
import com.xun.qianfanzhiche.base.BaseActivity;
import com.xun.qianfanzhiche.cache.ImageLoaderWithCaches;
import com.xun.qianfanzhiche.common.Constant;
import com.xun.qianfanzhiche.view.MyRecyclerView;
import com.xun.qianfanzhiche.view.MyRecyclerView.OnItemScrollChangeListener;

public class CarShowGirlActivity extends BaseActivity {
	private MyRecyclerView mRecyclerView;
	private GalleryAdapter mAdapter;
	private ImageView mImg;
	private List<String> mDatas = new ArrayList<String>();
	private ImageLoaderWithCaches imageLoaderWithCaches;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_car_show_girl);

		mImg = (ImageView) findViewById(R.id.id_content);
		mDatas = getQiNiuImgData(36);

		mRecyclerView = (MyRecyclerView) findViewById(R.id.id_recyclerview_horizontal);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
		linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

		imageLoaderWithCaches = new ImageLoaderWithCaches(getApplicationContext(), null, null);
		mRecyclerView.setLayoutManager(linearLayoutManager);
		mAdapter = new GalleryAdapter(this, mDatas, imageLoaderWithCaches);
		mRecyclerView.setAdapter(mAdapter);

		mRecyclerView.setOnItemScrollChangeListener(new OnItemScrollChangeListener() {
			@Override
			public void onChange(View view, int position) {
				imageLoaderWithCaches.loadImagesWithUrl(mImg, mDatas.get(position));
			};
		});

		mAdapter.setOnItemClickLitener(new OnItemClickLitener() {
			@Override
			public void onItemClick(View view, int position) {
				imageLoaderWithCaches.loadImagesWithUrl(mImg, mDatas.get(position));
			}
		});
	}

	// 首先先支持三位数
	private List<String> getQiNiuImgData(int count) {
		List<String> mData = new ArrayList<String>();
		for (int i = 1; i <= count; i++) {
			if (i < 10) {
				mData.add(Constant.QINIU_IMG_BASE_URL + "qfzc000" + i + ".jpg");
			} else if (i >= 10 && i <= 99) {
				mData.add(Constant.QINIU_IMG_BASE_URL + "qfzc00" + i + ".jpg");
			} else if (i >= 100 && i <= 999) {
				mData.add(Constant.QINIU_IMG_BASE_URL + "qfzc0" + i + ".jpg");
			}
		}
		return mData;
	}
}
