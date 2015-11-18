package com.xun.qianfanzhiche.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;

import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.adapter.CommunityListAdapter;
import com.xun.qianfanzhiche.base.BaseFragment;
import com.xun.qianfanzhiche.bean.CommunityItem;
import com.xun.qianfanzhiche.cache.ImageLoaderWithCaches;
import com.xun.qianfanzhiche.ui.CommunityDetailActivity;
import com.xun.qianfanzhiche.utils.LogUtil;

/**
 * 社区页面
 * 
 * @author xunwang
 * 
 *         2015-11-18
 */
public class CommunityFragment extends BaseFragment implements OnRefreshListener, OnClickListener {
	private ListView mListView;
	private SwipeRefreshLayout swipeView;
	private CommunityListAdapter communityListAdapter;
	private MyScrollListener myScrollListener;
	private ObjectAnimator mAnimator;
	private View touchTopView;

	private ImageLoaderWithCaches mImageLoader;
	private int start, end;
	private boolean mFirstFlag = true;

	private float mFirstY, mCurrentY;
	private int direction;
	private boolean mShow;
	// private boolean isSetScrollListener = false;
	private List<String> imgUrls = new ArrayList<String>();
	private List<CommunityItem> data = new ArrayList<CommunityItem>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_community, container, false);
		loadData();
		swipeView = (SwipeRefreshLayout) rootView.findViewById(R.id.community_swipe);
		swipeView.setOnRefreshListener(this);
		swipeView.setColorSchemeResources(android.R.color.holo_blue_dark, android.R.color.holo_red_light, android.R.color.holo_green_light,
				android.R.color.holo_orange_light);
		mListView = (ListView) rootView.findViewById(R.id.community_list);
		mImageLoader = new ImageLoaderWithCaches(getContext(), mListView, imgUrls);
		communityListAdapter = new CommunityListAdapter(getContext(), mImageLoader);
		myScrollListener = new MyScrollListener();
		mListView.setAdapter(communityListAdapter);
		touchTopView = (View) rootView.findViewById(R.id.community_touch_top);
		touchTopView.setOnClickListener(this);
		toolBarAnim(1);
		mShow = !mShow;
		mListView.setOnTouchListener(new MyTouchListener());
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(getActivity(), CommunityDetailActivity.class);
				intent.putExtra("data", data.get(position));
				startActivity(intent);
			}
		});
		return rootView;
	}

	private class MyTouchListener implements OnTouchListener {
		final int mTouchSlop = ViewConfiguration.get(getActivity()).getScaledTouchSlop();

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mFirstY = event.getY();
				break;
			case MotionEvent.ACTION_MOVE:
				// LogUtil.d(LogUtil.TAG, "MotionEvent.ACTION_MOVE");
				mCurrentY = event.getY();
				if (mCurrentY - mFirstY > mTouchSlop) {
					direction = 0;
				} else if (mFirstY - mCurrentY > mTouchSlop) {
					direction = 1;
				}
				if (direction == 1) {
					if (mShow) {
						toolBarAnim(1);
						mShow = !mShow;
					}
				} else if (direction == 0) {
					if (!mShow) {
						toolBarAnim(0);
						mShow = !mShow;
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				break;
			}
			return false;
		}

	}

	private class MyScrollListener implements OnScrollListener {
		@Override
		public void onScrollStateChanged(AbsListView arg0, int scrollState) {
			// LogUtil.d(LogUtil.TAG, "onScrollStateChanged");
			if (scrollState == SCROLL_STATE_IDLE) {
				mImageLoader.loadImages(start, end);
			} else {
				mImageLoader.cancelAllTask();
			}
		}

		@Override
		public void onScroll(AbsListView arg0, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			// LogUtil.d(LogUtil.TAG, "onScroll mFirstFlag --> " + mFirstFlag + " visibleItemCount --> " + visibleItemCount);
			if (firstVisibleItem == 0) {
				swipeView.setEnabled(true);
				if (mShow) {
					toolBarAnim(1);
					mShow = !mShow;
				}
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

	@Override
	public void onStop() {
		super.onStop();
		mFirstFlag = true;
	}

	private void loadData() {
		data.clear();
		BmobQuery<CommunityItem> query = new BmobQuery<CommunityItem>();
		// query.addWhereEqualTo("CommunityItem", "");
		BmobDate date = new BmobDate(new Date(System.currentTimeMillis()));
		query.addWhereLessThan("createdAt", date);
		query.include("author");
		// 返回50条数据，如果不加上这条语句，默认返回10条数据
		query.setLimit(50);
		// 执行查询方法
		query.findObjects(getContext(), new FindListener<CommunityItem>() {
			@Override
			public void onSuccess(List<CommunityItem> object) {
				// LogUtil.d(LogUtil.TAG, "onSuccess object size--> " + object.size());
				data = object;
				Collections.reverse(data);
				putImgData(data);
				mImageLoader.setImgUrls(imgUrls);
				communityListAdapter.setData(data);
				// if (!isSetScrollListener) {
				mListView.setOnScrollListener(myScrollListener);
				// isSetScrollListener = true;
				// }
				swipeView.setRefreshing(false);
			}

			@Override
			public void onError(int code, String msg) {
				LogUtil.d(LogUtil.TAG, "onError --> msg -->" + msg + " code -->" + code);
				swipeView.setRefreshing(false);
			}
		});
	}

	private void putImgData(List<CommunityItem> object) {
		imgUrls.clear();
		for (int i = 0; i < object.size(); i++) {
			if (object.get(i).getImage() == null) {
				imgUrls.add("null");
			} else {
				imgUrls.add(object.get(i).getImage().getFileUrl(getContext()));
			}
		}
	}

	private void toolBarAnim(int flag) {
		if (mAnimator != null && mAnimator.isRunning()) {
			mAnimator.cancel();
		}
		if (flag == 0) {
			mAnimator = ObjectAnimator.ofFloat(touchTopView, "translationY", touchTopView.getTranslationY(), 0);
		} else {
			mAnimator = ObjectAnimator.ofFloat(touchTopView, "translationY", touchTopView.getTranslationY(), -touchTopView.getHeight());
		}
		mAnimator.start();
	}

	@Override
	public void onRefresh() {
		swipeView.setRefreshing(true);
		loadData();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.community_touch_top:
			mListView.smoothScrollToPosition(0);
			break;

		default:
			break;
		}
	}

}
