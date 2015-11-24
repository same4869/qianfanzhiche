package com.xun.qianfanzhiche.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.adapter.CarGridAdapter;
import com.xun.qianfanzhiche.base.BaseFragment;
import com.xun.qianfanzhiche.bean.CarGridBean;
import com.xun.qianfanzhiche.cache.ImageLoaderWithCaches;
import com.xun.qianfanzhiche.ui.CommonWebActivity;
import com.xun.qianfanzhiche.utils.QiNiuUtil;

/**
 * 车标展示主界面
 * 
 * @author xunwang
 * 
 *         2015-11-24
 */
public class MainFragment extends BaseFragment {
	public static final String CAR_GRID_URL_KEY = "car_grid_url_key";
	private GridView carGridView;
	private CarGridAdapter carGridAdapter;

	private ImageLoaderWithCaches mImageLoader;

	private List<CarGridBean> list;
	private List<String> imgsUrlList = new ArrayList<String>();

	private int start, end;
	private boolean mFirstFlag = true;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container, false);
		carGridView = (GridView) rootView.findViewById(R.id.car_gridview);
		initData();
		mImageLoader = new ImageLoaderWithCaches(getContext(), carGridView, imgsUrlList);
		carGridAdapter = new CarGridAdapter(getContext(), list, mImageLoader);
		carGridView.setAdapter(carGridAdapter);
		carGridView.setOnScrollListener(new MyScrollListener());
		carGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(getActivity(), CommonWebActivity.class);
				intent.putExtra(CAR_GRID_URL_KEY, list.get(position).getCarDetailUrl());
				startActivity(intent);
			}
		});
		return rootView;
	}

	private void initData() {
		list = QiNiuUtil.getCardGridInfo(getContext());
		for (int i = 0; i < list.size(); i++) {
			imgsUrlList.add(list.get(i).getCarUrl());
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		mFirstFlag = true;
	}

	private class MyScrollListener implements OnScrollListener {
		@Override
		public void onScrollStateChanged(AbsListView arg0, int scrollState) {
			if (scrollState == SCROLL_STATE_IDLE) {
				mImageLoader.loadImages(start, end);
			} else {
				mImageLoader.cancelAllTask();
			}
		}

		@Override
		public void onScroll(AbsListView arg0, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			start = firstVisibleItem;
			end = firstVisibleItem + visibleItemCount;
			if (mFirstFlag && visibleItemCount > 0) {
				mImageLoader.loadImages(start, end);
				mFirstFlag = false;
			}
		}
	}
}
