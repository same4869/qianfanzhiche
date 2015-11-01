package com.xun.qianfanzhiche.fragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;

import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.adapter.CommunityListAdapter;
import com.xun.qianfanzhiche.base.BaseFragment;
import com.xun.qianfanzhiche.bean.CommunityItem;
import com.xun.qianfanzhiche.cache.ImageLoaderWithCaches;
import com.xun.qianfanzhiche.utils.LogUtil;

public class CommunityFragment extends BaseFragment implements
		OnRefreshListener {
	private ListView mListView;
	private SwipeRefreshLayout swipeView;
	private CommunityListAdapter communityListAdapter;
	private MyScrollListener myScrollListener;

	private ImageLoaderWithCaches mImageLoader;
	private int start, end;
	private boolean mFirstFlag = true;
	private List<String> imgUrls = new ArrayList<String>();

	private List<CommunityItem> data = new ArrayList<CommunityItem>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		loadData();
		View rootView = inflater.inflate(R.layout.fragment_community,
				container, false);
		swipeView = (SwipeRefreshLayout) rootView
				.findViewById(R.id.community_swipe);
		swipeView.setOnRefreshListener(this);
		swipeView.setColorSchemeResources(android.R.color.holo_blue_dark,
				android.R.color.holo_blue_light,
				android.R.color.holo_green_light,
				android.R.color.holo_green_light);
		mListView = (ListView) rootView.findViewById(R.id.community_list);
		mImageLoader = new ImageLoaderWithCaches(getContext(), mListView,
				imgUrls);
		communityListAdapter = new CommunityListAdapter(getContext(), data,
				mImageLoader);
		myScrollListener = new MyScrollListener();
		mListView.setAdapter(communityListAdapter);
		return rootView;
	}

	private class MyScrollListener implements OnScrollListener {
		@Override
		public void onScrollStateChanged(AbsListView arg0, int scrollState) {
			LogUtil.d(LogUtil.TAG, "onScrollStateChanged");
			if (scrollState == SCROLL_STATE_IDLE) {
				mImageLoader.loadImages(start, end);
			} else {
				mImageLoader.cancelAllTask();
			}
		}

		@Override
		public void onScroll(AbsListView arg0, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			LogUtil.d(LogUtil.TAG, "onScroll");
			if (firstVisibleItem == 0) {
				swipeView.setEnabled(true);
			} else {
				swipeView.setEnabled(false);
			}
			start = firstVisibleItem;
			end = firstVisibleItem + visibleItemCount;
			if (mFirstFlag && visibleItemCount > 0) {
				mImageLoader.loadImages(start, end);
				mFirstFlag = false;
			}
		}
	}

	private void loadData() {
		data.clear();
		imgUrls.clear();
		BmobQuery<CommunityItem> query = new BmobQuery<CommunityItem>();
		// query.addWhereEqualTo("CommunityItem", "");
		BmobDate date = new BmobDate(new Date(System.currentTimeMillis()));
		query.addWhereLessThan("createdAt", date);
		// 返回50条数据，如果不加上这条语句，默认返回10条数据
		query.setLimit(50);
		// 执行查询方法
		query.findObjects(getContext(), new FindListener<CommunityItem>() {
			@Override
			public void onSuccess(List<CommunityItem> object) {
				data = object;
				putImgData(data);
				communityListAdapter.setData(data);
				mListView.setOnScrollListener(myScrollListener);
			}

			@Override
			public void onError(int code, String msg) {
				LogUtil.d(LogUtil.TAG, "onError --> msg -->" + msg
						+ " code -->" + code);
			}
		});
	}

	private void putImgData(List<CommunityItem> object) {
		// LogUtil.d(LogUtil.TAG, "object.get(0).getImage().getUrl() --> "
		// + object.get(0).getImage().getUrl()
		// + " object.get(0).getImage().getFileUrl(getContext()) --> "
		// + object.get(0).getImage().getFileUrl(getContext()));
		for (int i = 0; i < object.size(); i++) {
			if (object.get(i).getImage() == null) {
				imgUrls.add("null");
			} else {
				imgUrls.add(object.get(i).getImage().getUrl());
			}
		}
	}

	@Override
	public void onRefresh() {
		swipeView.setRefreshing(true);
		loadData();
		swipeView.setRefreshing(false);
	}

}
