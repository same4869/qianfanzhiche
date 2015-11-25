package com.xun.qianfanzhiche.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.xun.qianfanzhiche.utils.ScreenUtil;
import com.xun.qianfanzhiche.view.PathComposerLayout;
import com.xun.qianfanzhiche.view.PathComposerLayout.PathStatusInterface;

/**
 * 车标展示主界面
 * 
 * @author xunwang
 * 
 *         2015-11-24
 */
public class MainFragment extends BaseFragment implements PathStatusInterface, OnClickListener {
	public static final String CAR_GRID_URL_KEY = "car_grid_url_key";
	private GridView carGridView;
	private CarGridAdapter carGridAdapter;
	private View carGridCover;

	private ImageLoaderWithCaches mImageLoader;

	private List<CarGridBean> list;
	private List<String> imgsUrlList = new ArrayList<String>();
	private List<CarGridBean> newList = new ArrayList<CarGridBean>();
	private List<String> newImgsUrlList = new ArrayList<String>();
	private List<String> newCarKey = new ArrayList<String>();

	private int start, end;
	private boolean mFirstFlag = true;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container, false);
		carGridView = (GridView) rootView.findViewById(R.id.car_gridview);
		carGridCover = (View) rootView.findViewById(R.id.car_gridview_cover);
		carGridCover.setOnClickListener(this);
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
		PathComposerLayout clayout = (PathComposerLayout) rootView.findViewById(R.id.car_gridview_path);
		clayout.setOnPathStatusListener(this);
		clayout.init(new int[] { R.drawable.composer_camera, R.drawable.composer_camera, R.drawable.composer_camera, R.drawable.composer_camera,
				R.drawable.composer_music, R.drawable.composer_place, R.drawable.composer_sleep, R.drawable.composer_thought, R.drawable.composer_with },
				R.drawable.composer_button, R.drawable.composer_icn_plus, PathComposerLayout.RIGHTBOTTOM, (int) (ScreenUtil.getDensity(getActivity()) * 200),
				300);
		clayout.setButtonsOnClickListener(clickit);
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

	OnClickListener clickit = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// if (v.getId() == 100 + 0) {
			// Log.d("kkkkkkkk", "1");
			// } else if (v.getId() == 100 + 1) {
			// Log.d("kkkkkkkk", "2");
			// } else if (v.getId() == 100 + 2) {
			// Log.d("kkkkkkkk", "3");
			// } else if (v.getId() == 100 + 3) {
			// Log.d("kkkkkkkk", "4");
			// } else if (v.getId() == 100 + 4) {
			// Log.d("kkkkkkkk", "5");
			// } else if (v.getId() == 100 + 5) {
			// Log.d("kkkkkkkk", "6");
			// } else if (v.getId() == 100 + 6) {
			// Log.d("kkkkkkkk", "7");
			// } else if (v.getId() == 100 + 7) {
			// Log.d("kkkkkkkk", "8");
			// } else if (v.getId() == 100 + 8) {
			// Log.d("kkkkkkkk", "9");
			// } else if (v.getId() == 100 + 9) {
			// Log.d("kkkkkkkk", "10");
			// }
			changeCarCategory(String.valueOf(v.getId() - 100));
		}
	};

	private void changeCarCategory(String carCategory) {
		String[] carGridKeyList = getResources().getStringArray(R.array.car_grid_key);
		newList.clear();
		list = QiNiuUtil.getCardGridInfo(getContext());
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getCarCategory().equals(carCategory)) {
				newList.add(list.get(i));
				newImgsUrlList.add(list.get(i).getCarUrl());
				newCarKey.add(carGridKeyList[i]);
			}
		}
		mImageLoader = new ImageLoaderWithCaches(getContext(), carGridView, newImgsUrlList);
		carGridAdapter.setDataChange(newList, mImageLoader, newCarKey);
	}

	@Override
	public void onPathIsExpand(boolean isExpand) {
		if (isExpand) {
			carGridCover.setVisibility(View.VISIBLE);
		} else {
			carGridCover.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
