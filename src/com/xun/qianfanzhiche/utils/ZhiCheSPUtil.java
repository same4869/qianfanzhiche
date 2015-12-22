package com.xun.qianfanzhiche.utils;

public class ZhiCheSPUtil {
	private static final String TABLE_CONSTANTS = "table_constants";

	private static final String QINIU_BASE_URL = "qiniu_base_url";
	private static final String AUTO_HOME_BASE_URL = "auto_home_base_url";
	private static final String AUTO_HOME_BASE_URL_SUFFIX = "auto_home_base_url_suffix";
	private static final String BAIDU_BAIKE_BASE_URL = "baidu_baike_base_url";
	private static final String SINA_CAR_MAINTENANCE = "sina_car_maintenance";
	private static final String CAR_ACTIVITY = "car_activity";
	private static final String CAR_VIDEO = "car_video";
	private static final String CAR_NEWS = "car_news";
	private static final String SPLASH_IMG_URL = "splash_img_url";
	private static final String PAY_ME_WEB_URL = "pay_me_web_url";

	private static final String IS_USE_LOCAL_CONSTANTS = "is_use_local_constants";
	private static final String IS_SHOW_PAY_ME_CONSTANTS = "is_show_pay_me_constants";

	private static final String IS_FIRST_OPEN_APP = "is_first_open_app";

	private static final String IS_FIRST_SHOW_ROBOT = "is_first_show_robot";

	public static void setCarVideoUrl(String string) {
		SPUtil.putString(TABLE_CONSTANTS, CAR_VIDEO, string);
	}

	public static String getCarVideoUrl() {
		return SPUtil.getString(TABLE_CONSTANTS, CAR_VIDEO, null);
	}

	public static void setCarNewsUrl(String string) {
		SPUtil.putString(TABLE_CONSTANTS, CAR_NEWS, string);
	}

	public static String getCarNewsUrl() {
		return SPUtil.getString(TABLE_CONSTANTS, CAR_NEWS, null);
	}

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

	public static void setSplashImgUrl(String string) {
		SPUtil.putString(TABLE_CONSTANTS, SPLASH_IMG_URL, string);
	}

	public static String getSplashImgUrl() {
		return SPUtil.getString(TABLE_CONSTANTS, SPLASH_IMG_URL, null);
	}

	public static void setPaymeWebUrl(String string) {
		SPUtil.putString(TABLE_CONSTANTS, PAY_ME_WEB_URL, string);
	}

	public static String getPaymeWebUrl() {
		return SPUtil.getString(TABLE_CONSTANTS, PAY_ME_WEB_URL, null);
	}

	public static void setIsUseLocalConstants(boolean isUseConstants) {
		SPUtil.putBoolean(TABLE_CONSTANTS, IS_USE_LOCAL_CONSTANTS, isUseConstants);
	}

	public static boolean getIsUseLocalConstants() {
		return SPUtil.getBoolean(TABLE_CONSTANTS, IS_USE_LOCAL_CONSTANTS, true);
	}

	public static void setIsShowPayMe(boolean isPayMe) {
		SPUtil.putBoolean(TABLE_CONSTANTS, IS_SHOW_PAY_ME_CONSTANTS, isPayMe);
	}

	public static boolean getIsShowPayMe() {
		return SPUtil.getBoolean(TABLE_CONSTANTS, IS_SHOW_PAY_ME_CONSTANTS, false);
	}

	public static void setIsFirstOpenApp(boolean isFisrt) {
		SPUtil.putBoolean(TABLE_CONSTANTS, IS_FIRST_OPEN_APP, isFisrt);
	}

	public static boolean getIsFirstOpenApp() {
		return SPUtil.getBoolean(TABLE_CONSTANTS, IS_FIRST_OPEN_APP, true);
	}

	public static void setIsFirstShowRobot(boolean isFisrt) {
		SPUtil.putBoolean(TABLE_CONSTANTS, IS_FIRST_SHOW_ROBOT, isFisrt);
	}

	public static boolean getIsFirstShowRobot() {
		return SPUtil.getBoolean(TABLE_CONSTANTS, IS_FIRST_SHOW_ROBOT, true);
	}
}
