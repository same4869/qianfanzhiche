package com.xun.qianfanzhiche.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.base.BaseActivity;
import com.xun.qianfanzhiche.bean.CommunityItem;
import com.xun.qianfanzhiche.bean.User;
import com.xun.qianfanzhiche.utils.ToastUtil;
import com.xun.qianfanzhiche.view.ZhiCheActionBar;
import com.xun.qianfanzhiche.view.ZhiCheActionBar.ActionBarListener;

public class CommunityNewPublishActivity extends BaseActivity implements ActionBarListener {
	private EditText communityEt;
	private ZhiCheActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_publish);
		initView();
	}

	private void initView() {
		communityEt = (EditText) findViewById(R.id.edit_content);
		actionBar = (ZhiCheActionBar) findViewById(R.id.actionbar);
		actionBar.setOnActionBarListener(this);
	}

	private void publish(String content) {
		User user = BmobUser.getCurrentUser(getApplicationContext(), User.class);

		final CommunityItem communityItem = new CommunityItem();
		communityItem.setAuthor(user);
		communityItem.setContent(content);
		communityItem.setTitle("我是测试标题");
		communityItem.save(getApplicationContext(), new SaveListener() {

			@Override
			public void onSuccess() {
				ToastUtil.show(CommunityNewPublishActivity.this, "发表成功！");
				setResult(RESULT_OK);
				finish();
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				ToastUtil.show(CommunityNewPublishActivity.this, "发表失败！yg" + arg1);
			}
		});
	}

	@Override
	public void onBackImgClick() {
		finish();
	}

	@Override
	public void onAddImgClick() {
		String commitContent = communityEt.getText().toString().trim();
		if (TextUtils.isEmpty(commitContent)) {
			ToastUtil.show(getApplicationContext(), "内容不能为空");
			return;
		}
		publish(commitContent);
	}
}
