package com.xun.qianfanzhiche.manager;

import java.util.Random;

import android.content.Context;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.xun.qianfanzhiche.bean.CommunityItem;

/**
 * 分享管理
 * 
 * @author wangxun
 * 
 */
public class ShareManager {
	private static ShareManager instance = null;

	private ShareManager() {

	}

	public static ShareManager getInstance() {
		if (instance == null) {
			syncInit();
		}
		return instance;
	}

	private static synchronized void syncInit() {
		if (instance == null) {
			instance = new ShareManager();
		}
	}

	public void showShare(Context context, CommunityItem communityItem) {
		if (communityItem == null) {
			return;
		}
		ShareSDK.initSDK(context);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		// oks.setTitle(communityItem.getTitle());
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl("http://sharesdk.cn");
		// text是分享文本，所有平台都需要这个字段
		oks.setText(communityItem.getContent());
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		// oks.setImagePath("/sdcard/test.jpg");// 确保SDcard下面存在此张图片
		String imgUrl;
		if (communityItem.getImage() == null) {
			Random random = new java.util.Random();// 定义随机类
			String[] indexs = { "1", "10", "11", "110", "12", "13", "14", "15", "20", "25", "26", "3", "33" };
			int index = random.nextInt(14);
			imgUrl = "http://7xnmgu.com1.z0.glb.clouddn.com/chebiao-" + indexs[index] + ".jpg";
		} else {
			imgUrl = communityItem.getImage().getFileUrl(context);
		}
		oks.setImageUrl(imgUrl);
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("http://sharesdk.cn");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("千帆知车");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite("千帆知车");
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("http://sharesdk.cn");

		// 启动分享GUI
		oks.show(context);
	}
}
