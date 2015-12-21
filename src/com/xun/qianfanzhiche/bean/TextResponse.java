package com.xun.qianfanzhiche.bean;

import java.io.Serializable;

public class TextResponse implements Serializable {
	private static final long serialVersionUID = -4665020267999994565L;
	private int code;// 100000
	private String text;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
