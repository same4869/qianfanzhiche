package com.xun.qianfanzhiche.utils;

import java.util.List;

import android.content.Context;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

import com.xun.qianfanzhiche.bean.CommunityItem;
import com.xun.qianfanzhiche.bean.User;
import com.xun.qianfanzhiche.manager.UserLevelManager;

public class BmobUtil {
	public static void queryCountForUserLevel(final Context mContext, final TextView textView, final String userId) {
		BmobQuery<CommunityItem> query = new BmobQuery<CommunityItem>();
		// 查询playerName叫“比目”的数据
		query.addWhereEqualTo("author", userId);
		// 返回100条数据，如果不加上这条语句，默认返回10条数据
		query.include("author");
		query.setLimit(100);
		// 执行查询方法
		query.findObjects(mContext, new FindListener<CommunityItem>() {
			@Override
			public void onSuccess(List<CommunityItem> object) {
				// Log.d("kkkkkkkk", "userId --> " + userId + " object.size() --> " + object.size());
				UserLevelManager.getInstance().formatUserLevel(mContext, textView, object.size(), false);
			}

			@Override
			public void onError(int code, String msg) {
			}
		});
	}

	public static User getCurrentUser(Context context) {
		User user = BmobUser.getCurrentUser(context, User.class);
		if (user != null) {
			return user;
		}
		return null;
	}
}
