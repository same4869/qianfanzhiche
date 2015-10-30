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
import com.xun.qianfanzhiche.utils.LogUtil;

public class CommunityFragment extends BaseFragment implements OnRefreshListener {
	private ListView mListView;
	private SwipeRefreshLayout swipeView;
	private CommunityListAdapter communityListAdapter;

	private List<CommunityItem> data = new ArrayList<CommunityItem>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_community, container, false);
		swipeView = (SwipeRefreshLayout) rootView.findViewById(R.id.community_swipe);
		swipeView.setOnRefreshListener(this);
		swipeView.setColorSchemeResources(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_green_light,
				android.R.color.holo_green_light);
		mListView = (ListView) rootView.findViewById(R.id.community_list);
		loadData();
		communityListAdapter = new CommunityListAdapter(getContext(), data);
		mListView.setAdapter(communityListAdapter);
		mListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {

			}

			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
				if (arg1 == 0) {
					swipeView.setEnabled(true);
				} else {
					swipeView.setEnabled(false);
				}
			}
		});
		return rootView;
	}

	private void loadData() {
		data.clear();
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
				communityListAdapter.setData(data);
			}

			@Override
			public void onError(int code, String msg) {
				LogUtil.d(LogUtil.TAG, "onError --> msg -->" + msg);
			}
		});
	}

	@Override
	public void onRefresh() {
		swipeView.setRefreshing(true);
		loadData();
		swipeView.setRefreshing(false);
	}

}
