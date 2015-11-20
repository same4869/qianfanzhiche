package com.xun.qianfanzhiche.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
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
import com.xun.qianfanzhiche.utils.BmobUtil;
import com.xun.qianfanzhiche.utils.LogUtil;
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

	private CommunityItem communityItem;
	private ImageLoaderWithCaches imageLoaderWithCaches;
	private CommentAdapter commentAdapter;

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
		imageLoaderWithCaches = new ImageLoaderWithCaches(getApplicationContext(), null, null);
		Intent intent = getIntent();
		communityItem = (CommunityItem) intent.getSerializableExtra("data");
		userName.setText(communityItem.getAuthor().getUsername());
		timeTv.setText(communityItem.getCreatedAt());
		commentItemContent.setText(communityItem.getContent());
		if (communityItem.getImage() != null) {
			imageLoaderWithCaches.loadImagesWithUrl(commentItemImage, communityItem.getImage().getFileUrl(getApplicationContext()));
		}
		BmobUtil.queryCountForUserLevel(getApplicationContext(), userLevelTv, communityItem.getAuthor().getObjectId());
		// data.getImage().loadImage(getApplicationContext(), commentItemImage);
		commentAdapter = new CommentAdapter(getApplicationContext(), comments);
		commentList.setAdapter(commentAdapter);
		fetchComment();

	}

	private void initView() {
		setActionBarTitle("帖子详情");
		commentList = (ListView) findViewById(R.id.comment_list);
		footer = (TextView) findViewById(R.id.loadmore);
		timeTv = (TextView) findViewById(R.id.item_time);
		userLevelTv = (TextView) findViewById(R.id.item_user_level);

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

	private void publishComment(User user, String content) {
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
			User currentUser = BmobUser.getCurrentUser(this, User.class);
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

		default:
			break;
		}

	}

}
