package com.xun.qianfanzhiche.base;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;

import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.bean.CommunityItem;

public class BaseCommunityFragment extends BaseFragmentActivity implements OnRefreshListener {
	private ListView mListView;
	private SwipeRefreshLayout swipeView;
	private CommunityListAdapter communityListAdapter;

	private List<String> data = new ArrayList<String>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_community, container, false);
		swipeView = (SwipeRefreshLayout) rootView.findViewById(R.id.community_swipe);
		swipeView.setOnRefreshListener(this);
		swipeView.setColorSchemeResources(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_green_light,
				android.R.color.holo_green_light);
		mListView = (ListView) rootView.findViewById(R.id.community_list);
		loadData();
		communityListAdapter = new CommunityListAdapter();
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
		BmobQuery<CommunityItem> query = new BmobQuery<CommunityItem>();
		// 查询playerName叫“比目”的数据
		// query.addWhereEqualTo("CommunityItem", "");
		BmobDate date = new BmobDate(new Date(System.currentTimeMillis()));
		query.addWhereLessThan("createdAt", date);
		// 返回50条数据，如果不加上这条语句，默认返回10条数据
		query.setLimit(50);
		// 执行查询方法
		query.findObjects(getContext(), new FindListener<CommunityItem>() {
			@Override
			public void onSuccess(List<CommunityItem> object) {
				Log.d("kkkkkkkk", "onSuccess");
				for (CommunityItem communityItem : object) {
					data.add(communityItem.getContent());
				}
				communityListAdapter.notifyDataSetChanged();
			}

			@Override
			public void onError(int code, String msg) {
				Log.d("kkkkkkkk", "onError --> msg -->" + msg);
			}
		});
	}

	public class CommunityListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int arg0) {
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		public void addData(String string) {
			data.add(string);
			notifyDataSetChanged();
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			TextView textView = new TextView(getActivity());
			textView.setText("Position --> " + data.get(arg0));
			return textView;
		}

	}

	@Override
	public void onRefresh() {
		swipeView.setRefreshing(true);
		(new Handler()).postDelayed(new Runnable() {
			@Override
			public void run() {
				swipeView.setRefreshing(false);
				int i = (int) Math.random();
				communityListAdapter.addData(String.valueOf(i));
			}
		}, 2000);
	}

}
