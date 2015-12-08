package com.xun.qianfanzhiche.bean;

import cn.bmob.v3.BmobObject;

public class QFFoundBean extends BmobObject {
	private static final long serialVersionUID = 1429838572145617526L;

	private String username;
	private String orderId;
	private boolean payStatus;
	private String failWhy;
	private Double price;
	private String payInfo;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public boolean isPayStatus() {
		return payStatus;
	}

	public void setPayStatus(boolean payStatus) {
		this.payStatus = payStatus;
	}

	public String getFailWhy() {
		return failWhy;
	}

	public void setFailWhy(String failWhy) {
		this.failWhy = failWhy;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getPayInfo() {
		return payInfo;
	}

	public void setPayInfo(String payInfo) {
		this.payInfo = payInfo;
	}

}