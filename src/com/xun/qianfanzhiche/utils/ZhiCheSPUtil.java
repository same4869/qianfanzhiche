package com.xun.qianfanzhiche.utils;

public class ZhiCheSPUtil {
	private static final String TABLE_CONSTANTS = "table_constants";

	private static final String QINIU_BASE_URL = "qiniu_base_url";
	private static final String AUTO_HOME_BASE_URL = "auto_home_base_url";
	private static final String AUTO_HOME_BASE_URL_SUFFIX = "auto_home_base_url_suffix";
	private static final String BAIDU_BAIKE_BASE_URL = "baidu_baike_base_url";
	private static final String SINA_CAR_MAINTENANCE = "sina_car_maintenance";
	private static final String CAR_ACTIVITY = "car_activity";
	private static final String IS_USE_LOCAL_CONSTANTS = "is_use_local_constants";

	public static void setQiniuBaseUrl(String string) {
		SPUtil.putString(TABLE_CONSTANTS, QINIU_BASE_URL, string);
	}

	public static String getQiniuBaseUrl() {
		return SPUtil.getString(TABLE_CONSTANTS, QINIU_BASE_URL, null);
	}

	public static void setAutoHomeBaseUrl(String string) {
		SPUtil.putString(TABLE_CONSTANTS, AUTO_HOME_BASE_URL, string);
	}

	public static String getAutoHomeBaseUrl() {
		return SPUtil.getString(TABLE_CONSTANTS, AUTO_HOME_BASE_URL, null);
	}

	public static void setAutoHomeBaseUrlSuffix(String string) {
		SPUtil.putString(TABLE_CONSTANTS, AUTO_HOME_BASE_URL_SUFFIX, string);
	}

	public static String getAutoHomeBaseUrlSuffix() {
		return SPUtil.getString(TABLE_CONSTANTS, AUTO_HOME_BASE_URL_SUFFIX, null);
	}

	public static void setBaiduBaiKeBaseUrl(String string) {
		SPUtil.putString(TABLE_CONSTANTS, BAIDU_BAIKE_BASE_URL, string);
	}

	public static String getBaiduBaiKeBaseUrl() {
		return SPUtil.getString(TABLE_CONSTANTS, BAIDU_BAIKE_BASE_URL, null);
	}

	public static void setSinaCarMaintenanceUrl(String string) {
		SPUtil.putString(TABLE_CONSTANTS, SINA_CAR_MAINTENANCE, string);
	}

	public static String getSinaCarMaintenanceUrl() {
		return SPUtil.getString(TABLE_CONSTANTS, SINA_CAR_MAINTENANCE, null);
	}

	public static void setCarActivityUrl(String string) {
		SPUtil.putString(TABLE_CONSTANTS, CAR_ACTIVITY, string);
	}

	public static String getCarActivityUrl() {
		return SPUtil.getString(TABLE_CONSTANTS, CAR_ACTIVITY, null);
	}

	public static void setIsUseLocalConstants(boolean isUseConstants) {
		SPUtil.putBoolean(TABLE_CONSTANTS, IS_USE_LOCAL_CONSTANTS, isUseConstants);
	}

	public static boolean getIsUseLocalConstants() {
		return SPUtil.getBoolean(TABLE_CONSTANTS, IS_USE_LOCAL_CONSTANTS, true);
	}
}
