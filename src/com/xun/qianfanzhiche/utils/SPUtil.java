package com.xun.qianfanzhiche.utils;

import android.content.SharedPreferences;

import com.xun.qianfanzhiche.app.ZhiCheApp;

public class SPUtil {
	public static int getInt(String tbl, String key, int def) {
		if (ZhiCheApp.getInstance() == null) {
			return def;
		}
		SharedPreferences prefs = ZhiCheApp.getInstance().getZhiCheSharedPreferences(tbl);
		return prefs.getInt(key, def);
	}

	public static long getLong(String tbl, String key, long def) {
		if (ZhiCheApp.getInstance() == null) {
			return def;
		}
		SharedPreferences prefs = ZhiCheApp.getInstance().getZhiCheSharedPreferences(tbl);
		return prefs.getLong(key, def);
	}

	public static String getString(String tbl, String key, String def) {
		if (ZhiCheApp.getInstance() == null) {
			return def;
		}
		SharedPreferences prefs = ZhiCheApp.getInstance().getZhiCheSharedPreferences(tbl);
		return prefs.getString(key, def);
	}

	public static boolean getBoolean(String tbl, String key, boolean def) {
		if (ZhiCheApp.getInstance() == null) {
			return def;
		}
		SharedPreferences prefs = ZhiCheApp.getInstance().getZhiCheSharedPreferences(tbl);
		return prefs.getBoolean(key, def);
	}

	public static float getFloat(String tbl, String key, float def) {
		if (ZhiCheApp.getInstance() == null) {
			return def;
		}

		SharedPreferences prefs = ZhiCheApp.getInstance().getZhiCheSharedPreferences(tbl);
		return prefs.getFloat(key, def);
	}

	public static void putInt(String tbl, String key, int value) {
		SharedPreferences prefs = ZhiCheApp.getInstance().getZhiCheSharedPreferences(tbl);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static void putLong(String tbl, String key, long value) {
		SharedPreferences prefs = ZhiCheApp.getInstance().getZhiCheSharedPreferences(tbl);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	public static void putString(String tbl, String key, String value) {
		SharedPreferences prefs = ZhiCheApp.getInstance().getZhiCheSharedPreferences(tbl);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static void putBoolean(String tbl, String key, boolean value) {
		SharedPreferences prefs = ZhiCheApp.getInstance().getZhiCheSharedPreferences(tbl);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static void putFloat(String tbl, String key, float value) {
		SharedPreferences prefs = ZhiCheApp.getInstance().getZhiCheSharedPreferences(tbl);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putFloat(key, value);
		editor.commit();
	}
}
