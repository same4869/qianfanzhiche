package com.xun.qianfanzhiche.bean;

import cn.bmob.v3.BmobObject;

public class ConstantsBean extends BmobObject {

	private static final long serialVersionUID = -7926963084771236519L;

	private String qiniuBaseUrl;
	private String autoHomeBaseUrl;
	private String autoHomeBaseUrlSuffix;
	private String baiduBaiKeBaseUrl;
	private String sinaCarMaintenanceUrl;
	private String carActivityUrl;
	private String carVideoUrl;
	private String carNewsUrl;

	private boolean isUseLocalConstants;
	private boolean isShowPayMe;

	public String getCarVideoUrl() {
		return carVideoUrl;
	}

	public void setCarVideoUrl(String carVideoUrl) {
		this.carVideoUrl = carVideoUrl;
	}

	public String getCarNewsUrl() {
		return carNewsUrl;
	}

	public void setCarNewsUrl(String carNewsUrl) {
		this.carNewsUrl = carNewsUrl;
	}

	public String getSinaCarMaintenanceUrl() {
		return sinaCarMaintenanceUrl;
	}

	public void setSinaCarMaintenanceUrl(String sinaCarMaintenanceUrl) {
		this.sinaCarMaintenanceUrl = sinaCarMaintenanceUrl;
	}

	public String getCarActivityUrl() {
		return carActivityUrl;
	}

	public void setCarActivityUrl(String carActivityUrl) {
		this.carActivityUrl = carActivityUrl;
	}

	public boolean isUseLocalConstants() {
		return isUseLocalConstants;
	}

	public void setUseLocalConstants(boolean isUseLocalConstants) {
		this.isUseLocalConstants = isUseLocalConstants;
	}

	public String getQiniuBaseUrl() {
		return qiniuBaseUrl;
	}

	public void setQiniuBaseUrl(String qiniuBaseUrl) {
		this.qiniuBaseUrl = qiniuBaseUrl;
	}

	public String getAutoHomeBaseUrl() {
		return autoHomeBaseUrl;
	}

	public void setAutoHomeBaseUrl(String autoHomeBaseUrl) {
		this.autoHomeBaseUrl = autoHomeBaseUrl;
	}

	public String getAutoHomeBaseUrlSuffix() {
		return autoHomeBaseUrlSuffix;
	}

	public void setAutoHomeBaseUrlSuffix(String autoHomeBaseUrlSuffix) {
		this.autoHomeBaseUrlSuffix = autoHomeBaseUrlSuffix;
	}

	public String getBaiduBaiKeBaseUrl() {
		return baiduBaiKeBaseUrl;
	}

	public void setBaiduBaiKeBaseUrl(String baiduBaiKeBaseUrl) {
		this.baiduBaiKeBaseUrl = baiduBaiKeBaseUrl;
	}

	public boolean isShowPayMe() {
		return isShowPayMe;
	}

	public void setShowPayMe(boolean isShowPayMe) {
		this.isShowPayMe = isShowPayMe;
	}

}
