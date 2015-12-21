package com.xun.qianfanzhiche.bean;

import cn.bmob.v3.BmobObject;

public class QianFanRobotRecordBean extends BmobObject {

	private static final long serialVersionUID = 7121675124510695052L;
	private String username;
	private String text;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
