package com.xun.qianfanzhiche.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.adapter.CommentAdapter;
import com.xun.qianfanzhiche.base.BaseActivity;
import com.xun.qianfanzhiche.bean.Comment;
import com.xun.qianfanzhiche.bean.CommunityItem;
import com.xun.qianfanzhiche.bean.User;
import com.xun.qianfanzhiche.cache.ImageLoaderWithCaches;
import com.xun.qianfanzhiche.common.Constant;
import com.xun.qianfanzhiche.db.DatabaseManager;
import com.xun.qianfanzhiche.manager.ShareManager;
import com.xun.qianfanzhiche.utils.BmobUtil;
import com.xun.qianfanzhiche.utils.LogUtil;
import com.xun.qianfanzhiche.utils.StringUtil;
import com.xun.qianfanzhiche.utils.ToastUtil;

/**
 * 帖子详情评论页
 * 
 * @author xunwang
 * 
 *         2015-11-18
 */
public class CommunityDetailActivity extends BaseActivity implements OnClickListener {
	private ListView commentList;
	private TextView footer;
	private EditText commentContent;
	private Button commentCommit;
	private TextView userName;
	private TextView commentItemContent;
	private ImageView commentItemImage;
	private TextView timeTv;
	private TextView userLevelTv;
	private ImageView userLogo;
	private TextView loveTv;
	private TextView shareTv;
	private ImageView favImg;

	private CommunityItem communityItem;
	private ImageLoaderWithCaches imageLoaderWithCaches;
	private CommentAdapter commentAdapter;

	private User currentUser;

	private List<Comment> comments = new ArrayList<Comment>();
	private int pageNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_detail);

		initView();
		initData();
	}

	private void initData() {
		currentUser = BmobUser.getCurrentUser(this, User.class);
		imageLoaderWithCaches = new ImageLoaderWithCaches(getApplicationContext(), null, null);
		Intent intent = getIntent();
		communityItem = (CommunityItem) intent.getSerializableExtra("data");
		if (!StringUtil.isStringNullorBlank(communityItem.getAuthor().getNickName())) {
			userName.setText(communityItem.getAuthor().getNickName());
		} else {
			userName.setText(communityItem.getAuthor().getUsername());
		}
		timeTv.setText(communityItem.getCreatedAt());
		commentItemContent.setText(communityItem.getContent());
		if (communityItem.getImage() != null) {
			imageLoaderWithCaches.loadImagesWithUrl(commentItemImage, communityItem.getImage().getFileUrl(getApplicationContext()));
		} else {
			commentItemImage.setVisibility(View.GONE);
		}
		BmobUtil.queryCountForUserLevel(getApplicationContext(), userLevelTv, communityItem.getAuthor().getObjectId());
		// data.getImage().loadImage(getApplicationContext(), commentItemImage);
		String avatarUrl = null;
		if (communityItem.getAuthor() != null && communityItem.getAuthor().getAvatar() != null) {
			avatarUrl = communityItem.getAuthor().getAvatar().getFileUrl(getApplicationContext());
		}
		imageLoaderWithCaches.setImageFromUrl(avatarUrl, userLogo, R.drawable.user_icon_default);
		if (communityItem.isMyFav()) {
			favImg.setImageResource(R.drawable.ic_action_fav_choose);
		} else {
			favImg.setImageResource(R.drawable.ic_action_fav_normal);
		}
		loveTv.setText(communityItem.getLove() + "");
		if (BmobUtil.getCurrentUser(getApplicationContext()) != null
				&& (communityItem.isMyLove() || DatabaseManager.getInstance(getApplicationContext()).isLoved(communityItem))) {
			loveTv.setTextColor(Color.parseColor("#D95555"));
		} else {
			loveTv.setTextColor(Color.parseColor("#000000"));
		}

		commentAdapter = new CommentAdapter(getApplicationContext(), comments);
		commentList.setAdapter(commentAdapter);
		fetchComment();

		if (communityItem.getAuthor().getUsername().equals(currentUser.getUsername())) {
			communityItem.setHaveNewComment(false);
			communityItem.update(getApplicationContext(), new UpdateListener() {

				@Override
				public void onSuccess() {
					LogUtil.d(LogUtil.TAG, "用户已读");
				}

				@Override
				public void onFailure(int arg0, String arg1) {
				}
			});
		}

	}

	private void initView() {
		setActionBarTitle("帖子详情");
		commentList = (ListView) findViewById(R.id.comment_list);
		footer = (TextView) findViewById(R.id.loadmore);
		timeTv = (TextView) findViewById(R.id.item_time);
		userLevelTv = (TextView) findViewById(R.id.item_user_level);
		loveTv = (TextView) findViewById(R.id.item_action_love);
		shareTv = (TextView) findViewById(R.id.item_action_share);
		favImg = (ImageView) findViewById(R.id.item_action_fav);
		loveTv.setOnClickListener(this);
		shareTv.setOnClickListener(this);
		favImg.setOnClickListener(this);

		commentContent = (EditText) findViewById(R.id.comment_content);
		commentCommit = (Button) findViewById(R.id.comment_commit);
		commentCommit.setOnClickListener(this);

		userName = (TextView) findViewById(R.id.item_name);
		commentItemContent = (TextView) findViewById(R.id.item_content);
		commentItemImage = (ImageView) findViewById(R.id.item_img);
		commentItemImage.setScaleType(ScaleType.CENTER_INSIDE);

		userLogo = (ImageView) findViewById(R.id.item_avater);
		setListViewHeightBasedOnChildren(commentList);

	}

	private void fetchComment() {
		BmobQuery<Comment> query = new BmobQuery<Comment>();
		query.addWhereRelatedTo("relation", new BmobPointer(communityItem));
		query.include("user");
		query.order("createdAt");
		query.setLimit(Constant.NUMBERS_PER_PAGE);
		query.setSkip(Constant.NUMBERS_PER_PAGE * (pageNum++));
		query.findObjects(this, new FindListener<Comment>() {

			@Override
			public void onSuccess(List<Comment> data) {
				LogUtil.d(LogUtil.TAG, "get comment success!" + data.size());
				if (data.size() != 0 && data.get(data.size() - 1) != null) {

					if (data.size() < Constant.NUMBERS_PER_PAGE) {
						ToastUtil.show(getApplicationContext(), "已加载完所有评论~");
						footer.setText("暂无更多评论~");
					}

					commentAdapter.getDataList().addAll(data);
					commentAdapter.notifyDataSetChanged();
					setListViewHeightBasedOnChildren(commentList);
					LogUtil.d(LogUtil.TAG, "refresh");
				} else {
					ToastUtil.show(getApplicationContext(), "暂无更多评论~");
					footer.setText("暂无更多评论~");
					pageNum--;
				}
			}

			@Override
			public void onError(int arg0, String arg1) {
				ToastUtil.show(getApplicationContext(), "获取评论失败。请检查网络~");
				pageNum--;
			}
		});
	}

	private void publishComment(final User user, String content) {
		final Comment comment = new Comment();
		comment.setUser(user);
		comment.setCommentContent(content);
		comment.save(this, new SaveListener() {

			@Override
			public void onSuccess() {
				ToastUtil.show(getApplicationContext(), "评论成功。");
				if (commentAdapter.getDataList().size() < Constant.NUMBERS_PER_PAGE) {
					commentAdapter.getDataList().add(comment);
					commentAdapter.notifyDataSetChanged();
					setListViewHeightBasedOnChildren(commentList);
				}
				commentContent.setText("");
				hideSoftInput();

				BmobRelation relation = new BmobRelation();
				relation.add(comment);
				communityItem.setRelation(relation);
				if (!communityItem.getAuthor().getUsername().equals(user.getUsername())) {
					communityItem.setHaveNewComment(true);
				}
				communityItem.update(getApplicationContext(), new UpdateListener() {

					@Override
					public void onSuccess() {
						LogUtil.d(LogUtil.TAG, "更新评论成功。");
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						LogUtil.d(LogUtil.TAG, "更新评论失败。" + arg1);
					}
				});

			}

			@Override
			public void onFailure(int arg0, String arg1) {
				ToastUtil.show(getApplicationContext(), "评论失败。请检查网络~");
			}
		});
	}

	private void hideSoftInput() {
		InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(commentContent.getWindowToken(), 0);
	}

	// 动态设置listview的高度 item 总布局必须是linearLayout
	public void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)) + 15;
		listView.setLayoutParams(params);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.comment_commit:

			if (currentUser != null) {// 已登录
				String commentEdit = commentContent.getText().toString().trim();
				if (TextUtils.isEmpty(commentEdit)) {
					ToastUtil.show(getApplicationContext(), "评论内容不能为空。");
					return;
				}
				// comment now
				publishComment(currentUser, commentEdit);
			} else {// 未登录
				ToastUtil.show(getApplicationContext(), "发表评论前请先登录。");
			}
			break;
		case R.id.item_action_share:
			ShareManager.getInstance().showShare(getApplicationContext(), communityItem);
			break;
		case R.id.item_action_love:
			final boolean oldFav = communityItem.isMyFav();
			ToastUtil.show(getApplicationContext(), "点赞");
			if (BmobUtil.getCurrentUser(getApplicationContext()) == null) {
				ToastUtil.show(getApplicationContext(), "请先登录。");
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), LoginActivity.class);
				startActivity(intent);
				return;
			}
			if (communityItem.isMyLove()) {
				ToastUtil.show(getApplicationContext(), "您已赞过啦");
				return;
			}
			if (DatabaseManager.getInstance(getApplicationContext()).isLoved(communityItem)) {
				ToastUtil.show(getApplicationContext(), "您已赞过啦");
				communityItem.setMyLove(true);
				return;
			}
			communityItem.setLove(communityItem.getLove() + 1);
			loveTv.setTextColor(Color.parseColor("#D95555"));
			loveTv.setText(communityItem.getLove() + "");

			communityItem.increment("love", 1);
			if (communityItem.isMyFav()) {
				communityItem.setMyFav(false);
			}
			communityItem.update(getApplicationContext(), new UpdateListener() {

				@Override
				public void onSuccess() {
					communityItem.setMyLove(true);
					communityItem.setMyFav(oldFav);
					DatabaseManager.getInstance(getApplicationContext()).insertFav(communityItem);
				}

				@Override
				public void onFailure(int arg0, String arg1) {
					communityItem.setMyLove(true);
					communityItem.setMyFav(oldFav);
				}
			});
			break;
		case R.id.item_action_fav:
			onClickFav(v, communityItem);
			break;
		default:
			break;
		}

	}

	private void onClickFav(View v, final CommunityItem communityItem) {
		User user = BmobUser.getCurrentUser(getApplicationContext(), User.class);
		if (user != null && user.getSessionToken() != null) {
			BmobRelation favRelaton = new BmobRelation();

			communityItem.setMyFav(!communityItem.isMyFav());
			if (communityItem.isMyFav()) {
				((ImageView) v).setImageResource(R.drawable.ic_action_fav_choose);
				favRelaton.add(communityItem);
				user.setFavorite(favRelaton);
				user.update(getApplicationContext(), new UpdateListener() {

					@Override
					public void onSuccess() {
						ToastUtil.show(getApplicationContext(), "收藏成功。");
						DatabaseManager.getInstance(getApplicationContext()).insertFav(communityItem);
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						ToastUtil.show(getApplicationContext(), "收藏失败。请检查网络~" + arg0);
					}
				});

			} else {
				((ImageView) v).setImageResource(R.drawable.ic_action_fav_normal);
				favRelaton.remove(communityItem);
				user.setFavorite(favRelaton);
				ToastUtil.show(getApplicationContext(), "取消收藏。");
				user.update(getApplicationContext(), new UpdateListener() {

					@Override
					public void onSuccess() {
						DatabaseManager.getInstance(getApplicationContext()).deleteFav(communityItem);
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						ToastUtil.show(getApplicationContext(), "取消收藏失败。请检查网络~" + arg0);
					}
				});
			}

		} else {
			// 前往登录注册界面
			ToastUtil.show(getApplicationContext(), "收藏前请先登录。");
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), LoginActivity.class);
			startActivity(intent);
		}
	}

}
