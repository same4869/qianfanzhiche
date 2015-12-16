package com.xun.qianfanzhiche.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.adapter.CommunityListAdapter;
import com.xun.qianfanzhiche.base.BaseActivity;
import com.xun.qianfanzhiche.bean.CommunityItem;
import com.xun.qianfanzhiche.bean.User;
import com.xun.qianfanzhiche.cache.ImageLoaderWithCaches;
import com.xun.qianfanzhiche.common.Constant;
import com.xun.qianfanzhiche.db.DatabaseManager;
import com.xun.qianfanzhiche.utils.BmobUtil;

/**
 * 我的收藏
 * 
 * @author xunwang
 * 
 *         2015-11-27
 */
public class FavActivity extends BaseActivity {
	private static final int LOADING = 1;
	private static final int LOADING_COMPLETED = 2;
	private static final int LOADING_FAILED = 3;
	private static final int NORMAL = 4;
	private static final int PRE_LOAD_OFFSET = 2;// 拉到距离底部多少条的时候加载下一页

	private ListView favListView;
	private CommunityListAdapter favListAdapter;
	private ProgressBar progressbar;
	private TextView networkTips;

	private ImageLoaderWithCaches mImageLoader;
	private MyScrollListener myScrollListener;
	private List<CommunityItem> data = new ArrayList<CommunityItem>();
	private List<String> imgUrls = new ArrayList<String>();
	private List<String> imgUrlsAvatar = new ArrayList<String>();
	private int pageNum = 0;
	private int start, end;
	private boolean mFirstFlag = true;
	private boolean isCleared, isAllLoaded;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fav);

		initView();
		initData();
	}

	private void initData() {
		loadData();
		myScrollListener = new MyScrollListener();
		mImageLoader = new ImageLoaderWithCaches(getApplicationContext(), favListView, imgUrls);
		favListAdapter = new CommunityListAdapter(getApplicationContext(), data, mImageLoader);
		favListView.setAdapter(favListAdapter);
	}

	private void putImgData(List<CommunityItem> object) {
		imgUrls.clear();
		imgUrlsAvatar.clear();
		for (int i = 0; i < object.size(); i++) {
			if (object.get(i).getImage() == null) {
				imgUrls.add("null");
			} else {
				imgUrls.add(object.get(i).getImage().getFileUrl(getApplicationContext()));
			}
			if (object.get(i).getAuthor() != null && object.get(i).getAuthor().getAvatar() != null) {
				imgUrlsAvatar.add(object.get(i).getAuthor().getAvatar().getFileUrl(getApplicationContext()));
			} else {
				imgUrlsAvatar.add("null");
			}
		}
	}

	private void loadData() {
		setState(LOADING);
		User user = BmobUser.getCurrentUser(getApplicationContext(), User.class);
		BmobQuery<CommunityItem> query = new BmobQuery<CommunityItem>();
		query.addWhereRelatedTo("favorite", new BmobPointer(user));
		query.order("-createdAt");
		query.setLimit(Constant.NUMBERS_PER_PAGE);
		BmobDate date = new BmobDate(new Date(System.currentTimeMillis()));
		query.addWhereLessThan("createdAt", date);
		query.setSkip(Constant.NUMBERS_PER_PAGE * (pageNum++));
		query.include("author");
		query.findObjects(getApplicationContext(), new FindListener<CommunityItem>() {

			@Override
			public void onSuccess(List<CommunityItem> list) {
				if (list.size() != 0 && list.get(list.size() - 1) != null) {
					if (isCleared == false) {
						data.clear();
						isCleared = true;
					}
					if (list.size() < Constant.NUMBERS_PER_PAGE) {
						isAllLoaded = true;
						// ToastUtil.show(getApplicationContext(), "已加载完所有数据~");
					}
					for (int i = 0; i < list.size(); i++) {
						list.get(i).setMyFav(true);
					}
					data.addAll(list);
					if (BmobUtil.getCurrentUser(getApplicationContext()) != null) {
						data = DatabaseManager.getInstance(getApplicationContext()).setFav(data);
					}
					putImgData(data);
					mImageLoader.setImgUrls(imgUrls);
					mImageLoader.setAvatarImgUrls(imgUrlsAvatar);
					favListAdapter.notifyDataSetChanged();
					favListView.setOnScrollListener(myScrollListener);
					setState(LOADING_COMPLETED);
				} else {
					isAllLoaded = true;
					// ToastUtil.show(getApplicationContext(), "暂无更多数据~");
					if (list.size() == 0 && data.size() == 0) {

						networkTips.setText("暂无收藏。快去收藏几个吧~");
						setState(LOADING_FAILED);
						pageNum--;

						return;
					}
					pageNum--;
					setState(LOADING_COMPLETED);
				}
			}

			@Override
			public void onError(int arg0, String arg1) {
				pageNum--;
				setState(LOADING_FAILED);
			}
		});
	}

	private void initView() {
		favListView = (ListView) findViewById(R.id.fav_list);
		progressbar = (ProgressBar) findViewById(R.id.fav_progressbar);
		networkTips = (TextView) findViewById(R.id.fav_network_tips);
		setActionBarTitle("我的收藏");
	}

	private class MyScrollListener implements OnScrollListener {
		@Override
		public void onScrollStateChanged(AbsListView arg0, int scrollState) {
			if (scrollState == SCROLL_STATE_IDLE) {
				mImageLoader.loadImages(start, end);
				mImageLoader.loadAvatar(start, end);
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
				mImageLoader.loadAvatar(start, end);
				mFirstFlag = false;
			}
			if (end > totalItemCount - PRE_LOAD_OFFSET && !isAllLoaded) {
				loadData();
			}
		}
	}

	public void setState(int state) {
		switch (state) {
		case LOADING:
			if (data.size() == 0) {
				favListView.setVisibility(View.GONE);
				progressbar.setVisibility(View.VISIBLE);
			}
			networkTips.setVisibility(View.GONE);
			break;
		case LOADING_COMPLETED:
			networkTips.setVisibility(View.GONE);
			progressbar.setVisibility(View.GONE);
			favListView.setVisibility(View.VISIBLE);
			break;
		case LOADING_FAILED:
			if (data.size() == 0) {
				favListView.setVisibility(View.VISIBLE);
				networkTips.setVisibility(View.VISIBLE);
			}
			progressbar.setVisibility(View.GONE);
			break;
		case NORMAL:

			break;
		default:
			break;
		}
	}

}
