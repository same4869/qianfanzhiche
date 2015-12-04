package com.xun.qianfanzhiche.fragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;

import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.adapter.CommunityListAdapter;
import com.xun.qianfanzhiche.base.BaseFragment;
import com.xun.qianfanzhiche.bean.CommunityItem;
import com.xun.qianfanzhiche.cache.ImageLoaderWithCaches;
import com.xun.qianfanzhiche.common.Constant;
import com.xun.qianfanzhiche.db.DatabaseManager;
import com.xun.qianfanzhiche.ui.ZhiCheMainActivity;
import com.xun.qianfanzhiche.ui.ZhiCheMainActivity.ActionBarTopInterface;
import com.xun.qianfanzhiche.utils.BmobUtil;
import com.xun.qianfanzhiche.utils.LogUtil;
import com.xun.qianfanzhiche.utils.ToastUtil;

/**
 * 社区页面
 * 
 * @author xunwang
 * 
 *         2015-11-18
 */
public class CommunityFragment extends BaseFragment implements OnRefreshListener, OnClickListener, ActionBarTopInterface {
	private static final int PRE_LOAD_OFFSET = 2;// 拉到距离底部多少条的时候加载下一页

	private ListView mListView;
	private SwipeRefreshLayout swipeView;
	private CommunityListAdapter communityListAdapter;
	private MyScrollListener myScrollListener;
	private ProgressBar progressBar;

	private ImageLoaderWithCaches mImageLoader;
	private int start, end;
	private boolean mFirstFlag = true;

	private List<String> imgUrls = new ArrayList<String>();
	private List<String> imgUrlsAvatar = new ArrayList<String>();
	private List<CommunityItem> data = new ArrayList<CommunityItem>();
	private boolean isCleared, isAllLoaded;
	private int pageNum = 0;
	private int mCurrentScrollState;
	private boolean isLoading;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_community, container, false);
		// loadData();

		swipeView = (SwipeRefreshLayout) rootView.findViewById(R.id.community_swipe);
		swipeView.setOnRefreshListener(this);
		swipeView.setColorSchemeResources(android.R.color.holo_blue_dark, android.R.color.holo_red_light, android.R.color.holo_green_light,
				android.R.color.holo_orange_light);
		mListView = (ListView) rootView.findViewById(R.id.community_list);
		progressBar = (ProgressBar) rootView.findViewById(R.id.community_progressbar);
		mImageLoader = new ImageLoaderWithCaches(getContext(), mListView, imgUrls);
		communityListAdapter = new CommunityListAdapter(getContext(), data, mImageLoader);
		myScrollListener = new MyScrollListener();
		mListView.setAdapter(communityListAdapter);
		((ZhiCheMainActivity) getActivity()).setActionBarTopInterface(this);
		return rootView;
	}

	private class MyScrollListener implements OnScrollListener {
		@Override
		public void onScrollStateChanged(AbsListView arg0, int scrollState) {
			// LogUtil.d(LogUtil.TAG, "onScrollStateChanged");
			mCurrentScrollState = scrollState;
			if (scrollState == SCROLL_STATE_IDLE) {
				mImageLoader.loadImages(start, end);
				mImageLoader.loadAvatar(start, end);
			} else {
				mImageLoader.cancelAllTask();
			}
		}

		@Override
		public void onScroll(AbsListView arg0, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			// LogUtil.d(LogUtil.TAG, "onScroll mFirstFlag --> " + mFirstFlag +
			// " visibleItemCount --> " + visibleItemCount);
			if (firstVisibleItem == 0) {
				swipeView.setEnabled(true);
			} else {
				swipeView.setEnabled(false);
			}
			start = firstVisibleItem;
			end = firstVisibleItem + visibleItemCount;
			if (mFirstFlag && visibleItemCount > 0) {
				mImageLoader.loadImages(start, end);
				mImageLoader.loadAvatar(start, end);
				mFirstFlag = false;
			}
			if (end > totalItemCount - PRE_LOAD_OFFSET && !isAllLoaded && mCurrentScrollState != SCROLL_STATE_IDLE && isLoading == false) {
				loadData();
			}
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		mFirstFlag = true;
		pageNum = 0;
		isAllLoaded = false;
		data.clear();
	}

	@Override
	public void onResume() {
		super.onResume();
		loadData();
	}

	private void loadData() {
		// data.clear();
		isLoading = true;
		BmobQuery<CommunityItem> query = new BmobQuery<CommunityItem>();
		// query.addWhereEqualTo("CommunityItem", "");
		BmobDate date = new BmobDate(new Date(System.currentTimeMillis()));
		query.addWhereLessThan("createdAt", date);
		query.order("-createdAt");
		query.include("author");
		// 返回50条数据，如果不加上这条语句，默认返回10条数据
		query.setLimit(Constant.NUMBERS_PER_PAGE);
		query.setSkip(Constant.NUMBERS_PER_PAGE * (pageNum++));
		// 执行查询方法
		query.findObjects(getContext(), new FindListener<CommunityItem>() {
			@Override
			public void onSuccess(List<CommunityItem> list) {
				LogUtil.d(LogUtil.TAG, "onSuccess list size--> " + list.size());
				if (list.size() != 0 && list.get(list.size() - 1) != null) {
					if (isCleared == false) {
						data.clear();
						isCleared = true;
					}
					if (list.size() < Constant.NUMBERS_PER_PAGE) {
						isAllLoaded = true;
						ToastUtil.show(getContext(), "已加载完所有数据~");
					}
					data.addAll(list);
					putImgData(data);
					mImageLoader.setImgUrls(imgUrls);
					mImageLoader.setAvatarImgUrls(imgUrlsAvatar);
					if (BmobUtil.getCurrentUser(getContext()) != null) {
						data = DatabaseManager.getInstance(getContext()).setFav(data);
					}
					communityListAdapter.setData(data);
					mListView.setOnScrollListener(myScrollListener);
				} else {
					isAllLoaded = true;
					ToastUtil.show(getContext(), "暂无更多数据~");
					if (list.size() == 0 && data.size() == 0) {
						pageNum--;
						return;
					}
					pageNum--;
				}
				swipeView.setRefreshing(false);
				progressBar.setVisibility(View.GONE);
				isLoading = false;
			}

			@Override
			public void onError(int code, String msg) {
				LogUtil.d(LogUtil.TAG, "onError --> msg -->" + msg + " code -->" + code);
				pageNum--;
				swipeView.setRefreshing(false);
				isLoading = false;
			}
		});
	}

	private void putImgData(List<CommunityItem> object) {
		imgUrls.clear();
		imgUrlsAvatar.clear();
		for (int i = 0; i < object.size(); i++) {
			if (object.get(i).getImage() == null) {
				imgUrls.add("null");
			} else {
				imgUrls.add(object.get(i).getImage().getFileUrl(getContext()));
			}
			if (object.get(i).getAuthor() != null && object.get(i).getAuthor().getAvatar() != null) {
				imgUrlsAvatar.add(object.get(i).getAuthor().getAvatar().getFileUrl(getContext()));
			} else {
				imgUrlsAvatar.add("null");
			}
		}
	}

	@Override
	public void onRefresh() {
		swipeView.setRefreshing(true);
		isCleared = false;
		pageNum = 0;
		isAllLoaded = false;
		loadData();
	}

	@Override
	public void onClick(View arg0) {
	}

	@Override
	public void onActionBarTopClick() {
		mListView.smoothScrollToPosition(0);
	}

}
