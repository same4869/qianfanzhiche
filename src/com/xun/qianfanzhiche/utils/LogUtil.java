package com.xun.qianfanzhiche.utils;

import android.util.Log;

public class LogUtil {
	private static final boolean IS_DEBUG = false;
	public static final String TAG = "kkkkkkkk";

	public static void d(String tag, String msg) {
		if (IS_DEBUG) {
			Log.d(tag, msg);
		}
	}
}
