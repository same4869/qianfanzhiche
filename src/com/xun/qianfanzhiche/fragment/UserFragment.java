package com.xun.qianfanzhiche.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.v3.BmobUser;

import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.base.BaseFragment;
import com.xun.qianfanzhiche.bean.User;
import com.xun.qianfanzhiche.ui.LoginActivity;
import com.xun.qianfanzhiche.ui.UserHelperActivity;
import com.xun.qianfanzhiche.utils.ToastUtil;
import com.xun.qianfanzhiche.view.ItemBar;

/**
 * 用户信息页面
 * 
 * @author xunwang
 * 
 *         2015-11-18
 */
public class UserFragment extends BaseFragment implements OnClickListener {
	private ImageView avaterImg;
	private TextView avaterText;
	private ItemBar sexItemBar, changePasswordItemBar, changeUserItemBar, logoutItemBar, userHelperItemBar;

	private boolean isLogin = false;

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_user, null);
		avaterImg = (ImageView) root.findViewById(R.id.avater_img);
		avaterImg.setOnClickListener(this);
		avaterText = (TextView) root.findViewById(R.id.avater_text);
		sexItemBar = (ItemBar) root.findViewById(R.id.sex);
		sexItemBar.setItemBarTitle("性别");
		sexItemBar.setItemBarContent("男");
		changePasswordItemBar = (ItemBar) root.findViewById(R.id.update_password);
		changePasswordItemBar.setItemBarTitle("修改密码");
		changeUserItemBar = (ItemBar) root.findViewById(R.id.change_user);
		changeUserItemBar.setItemBarTitle("更改用户");
		logoutItemBar = (ItemBar) root.findViewById(R.id.logout);
		logoutItemBar.setItemBarTitle("登出");
		logoutItemBar.setOnClickListener(this);
		userHelperItemBar = (ItemBar) root.findViewById(R.id.user_helper);
		userHelperItemBar.setOnClickListener(this);
		userHelperItemBar.setItemBarTitle("帮助中心");
		loadData();
		return root;
	}

	private void loadData() {
		User user = BmobUser.getCurrentUser(getActivity(), User.class);
		if (user != null) {
			avaterText.setText(user.getUsername());
			isLogin = true;
		} else {
			ToastUtil.show(getContext(), "未登录");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.avater_img:
			if (!isLogin) {
				Intent intent = new Intent(getContext(), LoginActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.sex:

			break;
		case R.id.update_password:

			break;
		case R.id.change_user:

			break;
		case R.id.user_helper:
			Intent userHelperIntent = new Intent(getActivity(), UserHelperActivity.class);
			startActivity(userHelperIntent);
			break;
		case R.id.logout:
			if (isLogin) {
				BmobUser.logOut(getContext()); // 清除缓存用户对象
				BmobUser currentUser = BmobUser.getCurrentUser(getContext()); // 现在的currentUser是null了
				if (currentUser == null) {
					ToastUtil.show(getContext(), "登出成功");
					isLogin = false;
				}
			}
			break;

		default:
			break;
		}
	}

}
