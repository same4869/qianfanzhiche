package com.xun.qianfanzhiche.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.adapter.CommunityListAdapter;
import com.xun.qianfanzhiche.app.ZhiCheApp;
import com.xun.qianfanzhiche.base.BaseActivity;
import com.xun.qianfanzhiche.bean.CommunityItem;
import com.xun.qianfanzhiche.bean.User;
import com.xun.qianfanzhiche.cache.ImageLoaderWithCaches;
import com.xun.qianfanzhiche.common.Constant;
import com.xun.qianfanzhiche.db.DatabaseManager;
import com.xun.qianfanzhiche.utils.BmobUtil;
import com.xun.qianfanzhiche.utils.ToastUtil;

/**
 * 个人中心
 * 
 * @author xunwang
 * 
 *         2015-12-2
 */
public class PersonalActivity extends BaseActivity implements OnClickListener {
	private static final int PRE_LOAD_OFFSET = 2;// 拉到距离底部多少条的时候加载下一页

	private ImageView personalIcon;
	private TextView personalName;
	private TextView personalSign;
	private ImageView goSettings;
	private TextView personalTitle;
	private ListView mListView;
	private ProgressBar progressBar;

	private User mUser;
	private CommunityListAdapter favListAdapter;
	private ImageLoaderWithCaches mImageLoader;
	private MyScrollListener myScrollListener;

	private boolean isCleared, isAllLoaded;
	private int pageNum;
	private int start, end;
	private boolean mFirstFlag = true;
	private List<String> imgUrls = new ArrayList<String>();
	private List<CommunityItem> data = new ArrayList<CommunityItem>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal);

		initView();
		initData();
	}

	private void initData() {
		mImageLoader = new ImageLoaderWithCaches(getApplicationContext(), null, null);
		if (isCurrentUser(mUser)) {
			personalTitle.setText("我发表过的");
			goSettings.setVisibility(View.VISIBLE);
			User user = BmobUser.getCurrentUser(getApplicationContext(), User.class);
			updatePersonalInfo(user);
		} else {
			goSettings.setVisibility(View.GONE);
			if (mUser != null) {
				personalTitle.setText("TA发表过的");
			}
		}

		loadData();

		myScrollListener = new MyScrollListener();
		mImageLoader = new ImageLoaderWithCaches(getApplicationContext(), mListView, imgUrls);
		favListAdapter = new CommunityListAdapter(getApplicationContext(), data, mImageLoader);
		mListView.setAdapter(favListAdapter);
	}

	private void initView() {
		personalIcon = (ImageView) findViewById(R.id.personal_icon);
		personalName = (TextView) findViewById(R.id.personl_name);
		personalSign = (TextView) findViewById(R.id.personl_signature);
		goSettings = (ImageView) findViewById(R.id.go_settings);
		personalTitle = (TextView) findViewById(R.id.personl_title);
		mListView = (ListView) findViewById(R.id.personal_list);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);

		mUser = ZhiCheApp.getInstance().getCurrentCommunityItem().getAuthor();
		updatePersonalInfo(mUser);
	}

	private void updatePersonalInfo(User user) {
		personalName.setText(user.getUsername());
		personalSign.setText(user.getSignature());
		if (user.getAvatar() != null) {
			mImageLoader.loadImagesWithUrl(personalIcon, user.getAvatar().getFileUrl(getApplicationContext()));
		}
	}

	/**
	 * 判断点击条目的用户是否是当前登录用户
	 * 
	 * @return
	 */
	private boolean isCurrentUser(User user) {
		if (null != user) {
			User cUser = BmobUser.getCurrentUser(getApplicationContext(), User.class);
			if (cUser != null && cUser.getObjectId().equals(user.getObjectId())) {
				return true;
			}
		}
		return false;
	}

	private void loadData() {
		progressBar.setVisibility(View.VISIBLE);
		BmobQuery<CommunityItem> query = new BmobQuery<CommunityItem>();
		query.setLimit(Constant.NUMBERS_PER_PAGE);
		query.setSkip(Constant.NUMBERS_PER_PAGE * (pageNum++));
		query.order("-createdAt");
		query.include("author");
		query.addWhereEqualTo("author", mUser);
		query.findObjects(getApplicationContext(), new FindListener<CommunityItem>() {

			@Override
			public void onSuccess(List<CommunityItem> list) {
				progressBar.setVisibility(View.GONE);
				if (list.size() != 0 && list.get(list.size() - 1) != null) {
					if (isCleared == false) {
						data.clear();
						isCleared = true;
					}

					if (list.size() < Constant.NUMBERS_PER_PAGE) {
						isAllLoaded = true;
						ToastUtil.show(getApplicationContext(), "已加载完所有数据~");
					}

					data.addAll(list);
					if (BmobUtil.getCurrentUser(getApplicationContext()) != null) {
						data = DatabaseManager.getInstance(getApplicationContext()).setFav(data);
					}
					putImgData(data);
					mImageLoader.setImgUrls(imgUrls);
					favListAdapter.notifyDataSetChanged();
					mListView.setOnScrollListener(myScrollListener);
				} else {
					isAllLoaded = true;
					ToastUtil.show(getApplicationContext(), "暂无更多数据~");
					pageNum--;
				}
			}

			@Override
			public void onError(int arg0, String msg) {
				pageNum--;
			}
		});
	}

	private void putImgData(List<CommunityItem> object) {
		imgUrls.clear();
		for (int i = 0; i < object.size(); i++) {
			if (object.get(i).getImage() == null) {
				imgUrls.add("null");
			} else {
				imgUrls.add(object.get(i).getImage().getFileUrl(getApplicationContext()));
			}
		}
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
			if (end > totalItemCount - PRE_LOAD_OFFSET && !isAllLoaded) {
				loadData();
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
