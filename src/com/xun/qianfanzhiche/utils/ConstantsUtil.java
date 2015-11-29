package com.xun.qianfanzhiche.utils;

import com.xun.qianfanzhiche.common.Constant;

public class ConstantsUtil {
	public static String getConstantFromLocalOrRemote(String constantKey) {
		boolean isUseLocalConstants;
		if (ZhiCheSPUtil.getIsUseLocalConstants()) {
			isUseLocalConstants = true;
		} else {
			isUseLocalConstants = false;
		}
		if (constantKey.equals("QINIU_IMG_BASE_URL")) {
			if (isUseLocalConstants) {
				return Constant.QINIU_IMG_BASE_URL;
			} else {
				return ZhiCheSPUtil.getQiniuBaseUrl();
			}
		} else if (constantKey.equals("AUTO_HOME_BASE_URL")) {
			if (isUseLocalConstants) {
				return Constant.AUTO_HOME_BASE_URL;
			} else {
				return ZhiCheSPUtil.getAutoHomeBaseUrl();
			}
		} else if (constantKey.equals("AUTO_HOME_BASE_URL_SUFFIX")) {
			if (isUseLocalConstants) {
				return Constant.AUTO_HOME_BASE_URL_SUFFIX;
			} else {
				return ZhiCheSPUtil.getAutoHomeBaseUrlSuffix();
			}
		} else if (constantKey.equals("BAIDU_BAIKE_BASE_URL")) {
			if (isUseLocalConstants) {
				return Constant.BAIDU_BAIKE_BASE_URL;
			} else {
				return ZhiCheSPUtil.getBaiduBaiKeBaseUrl();
			}
		} else if (constantKey.equals("SINA_CAR_MAINTENANCE")) {
			if (isUseLocalConstants) {
				return Constant.SINA_CAR_MAINTENANCE;
			} else {
				return ZhiCheSPUtil.getSinaCarMaintenanceUrl();
			}
		} else if (constantKey.equals("CAR_ACTIVITY")) {
			if (isUseLocalConstants) {
				return Constant.CAR_ACTIVITY;
			} else {
				return ZhiCheSPUtil.getBaiduBaiKeBaseUrl();
			}
		}
		return constantKey;
	}
}
