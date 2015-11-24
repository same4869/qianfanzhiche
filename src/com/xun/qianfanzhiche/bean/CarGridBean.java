package com.xun.qianfanzhiche.bean;

import java.io.Serializable;

public class CarGridBean implements Serializable {

	private static final long serialVersionUID = 629367502057215648L;

	private String carName;
	private String carUrl;
	private String carDetailUrl;

	public String getCarName() {
		return carName;
	}

	public void setCarName(String carName) {
		this.carName = carName;
	}

	public String getCarUrl() {
		return carUrl;
	}

	public void setCarUrl(String carUrl) {
		this.carUrl = carUrl;
	}

	public String getCarDetailUrl() {
		return carDetailUrl;
	}

	public void setCarDetailUrl(String carDetailUrl) {
		this.carDetailUrl = carDetailUrl;
	}

}
