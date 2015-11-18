package com.xun.qianfanzhiche.bean;

import cn.bmob.v3.BmobObject;

public class Comment extends BmobObject {
	private static final long serialVersionUID = 3538700486767011529L;
	private User user;
	private String commentContent;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}
}
