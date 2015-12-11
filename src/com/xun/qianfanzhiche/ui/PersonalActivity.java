package com.xun.qianfanzhiche.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
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
import com.xun.qianfanzhiche.utils.StringUtil;
import com.xun.qianfanzhiche.utils.ToastUtil;

/**
 * 个人中心，我的帖子，消息中心
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
	private ImageView personalSex;
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
	private boolean isFromUserCenter;
	private List<String> imgUrls = new ArrayList<String>();
	private List<CommunityItem> data = new ArrayList<CommunityItem>();

	private int type = 0;// 0.我的帖子，个人中心 1.消息中心

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
			personalSex.setVisibility(View.VISIBLE);
			User user = BmobUser.getCurrentUser(getApplicationContext(), User.class);
			updatePersonalInfo(user);
		} else {
			if (mUser != null) {
				personalTitle.setText("TA发表过的");
			}
		}
		if (mUser != null && mUser.getSex() != null) {
			if (mUser.getSex().equals("0")) {
				personalSex.setImageResource(R.drawable.basic_female);
			} else if (mUser.getSex().equals("1")) {
				personalSex.setImageResource(R.drawable.basic_male);
			}
		}

		loadData(type);

		myScrollListener = new MyScrollListener();
		mImageLoader = new ImageLoaderWithCaches(getApplicationContext(), mListView, imgUrls);
		favListAdapter = new CommunityListAdapter(getApplicationContext(), data, mImageLoader);
		mListView.setAdapter(favListAdapter);

		updatePersonalInfo(mUser);
	}

	private void initView() {
		personalIcon = (ImageView) findViewById(R.id.personal_icon);
		personalName = (TextView) findViewById(R.id.personl_name);
		personalSign = (TextView) findViewById(R.id.personl_signature);
		personalSex = (ImageView) findViewById(R.id.personal_sex);
		personalTitle = (TextView) findViewById(R.id.personl_title);
		mListView = (ListView) findViewById(R.id.personal_list);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);

		Intent intent = getIntent();
		if (intent.getBooleanExtra("isFromUserCenter", false)) {
			isFromUserCenter = true;
		}
		if (intent.getBooleanExtra("isFromNotifyCenter", false)) {
			type = 1;
		}

		if (!isFromUserCenter) {
			mUser = ZhiCheApp.getInstance().getCurrentCommunityItem().getAuthor();
			setActionBarTitle("我的帖子");
		} else {
			mUser = BmobUtil.getCurrentUser(getApplicationContext());
			setActionBarTitle("个人中心");
		}
	}

	private void updatePersonalInfo(User user) {
		if (!StringUtil.isStringNullorBlank(user.getNickName())) {
			personalName.setText(user.getNickName() + "(" + user.getUsername() + ")");
		} else {
			personalName.setText(user.getUsername());
		}
		if (user.getCar() == null) {
			personalSign.setText(user.getSignature());
		} else {
			personalSign.setText(user.getSignature() + "(" + user.getCar() + " 车主)");
		}
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

	// type 0.我的帖子 1.消息中心
	private void loadData(int type) {
		progressBar.setVisibility(View.VISIBLE);
		BmobQuery<CommunityItem> query = new BmobQuery<CommunityItem>();
		query.setLimit(Constant.NUMBERS_PER_PAGE);
		query.setSkip(Constant.NUMBERS_PER_PAGE * (pageNum++));
		query.order("-createdAt");
		query.include("author");
		query.addWhereEqualTo("author", mUser);
		if (type == 1) {
			query.addWhereEqualTo("isHaveNewComment", true);
		}
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
				loadData(type);
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
