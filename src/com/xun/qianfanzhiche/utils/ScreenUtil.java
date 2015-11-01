package com.xun.qianfanzhiche.utils;

import android.content.res.Resources;
import android.util.TypedValue;

/**
 * 屏幕相关工具
 * 
 * @author xunwang
 * 
 *         2015-10-22
 */
public class ScreenUtil {
	public static int dpToPx(Resources res, int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
	}
}
