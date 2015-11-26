package com.xun.qianfanzhiche.manager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.xun.qianfanzhiche.R;

/**
 * 用户等级管理
 * 
 * @author xunwang
 * 
 *         2015-11-20
 */
public class UserLevelManager {
	private static UserLevelManager instance = null;
	private Typeface iconfont;

	private UserLevelManager() {

	}

	public static UserLevelManager getInstance() {
		if (instance == null) {
			syncInit();
		}
		return instance;
	}

	private static synchronized void syncInit() {
		if (instance == null) {
			instance = new UserLevelManager();
		}
	}

	// isRule在展示规则的时候为true，正常记录为false;
	public void formatUserLevel(Context context, TextView textView, int count, boolean isRule) {
		int level = 0;
		if (isRule) {
			level = count;
		} else {
			level = ruleForUserlevel(count);
		}

		if (level < 1 || level > 15) {
			return;
		}
		if (iconfont == null) {
			iconfont = Typeface.createFromAsset(context.getAssets(), "iconfont.ttf");
		}
		textView.setTypeface(iconfont);
		int j = (level + 2) / 3;
		String fontString = null;
		switch (j) {
		case 1:
			fontString = context.getResources().getString(R.string.level_icon_1);
			break;
		case 2:
			fontString = context.getResources().getString(R.string.level_icon_2);
			break;
		case 3:
			fontString = context.getResources().getString(R.string.level_icon_3);
			break;
		case 4:
			fontString = context.getResources().getString(R.string.level_icon_4);
			break;
		case 5:
			fontString = context.getResources().getString(R.string.level_icon_5);
			break;
		default:
			break;
		}
		continueFormatUserLeavel(textView, level);
		String[] userLevelStrings = context.getResources().getStringArray(R.array.user_level);
		String levelString = fontString + userLevelStrings[level - 1];
		SpannableString spannableFee = new SpannableString(levelString);
		spannableFee.setSpan(new AbsoluteSizeSpan(15, true), 1, String.valueOf(levelString).length(), SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
		spannableFee.setSpan(new ForegroundColorSpan(Color.GRAY), 1, String.valueOf(levelString).length(), SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
		textView.setText(spannableFee);
	}

	private void continueFormatUserLeavel(TextView textView, int level) {
		int i = level % 3;
		switch (i) {
		case 1:
			textView.setTextColor(Color.parseColor("#D2E9FF"));
			break;
		case 2:
			textView.setTextColor(Color.parseColor("#97CBFF"));
			break;
		case 0:
			textView.setTextColor(Color.parseColor("#2894FF"));
			break;

		default:
			break;
		}
	}

	public List<String> getRuleStringList() {
		List<String> list = new ArrayList<String>();
		list.add("0~3");
		list.add("4~10");
		list.add("11~20");
		list.add("21~30");
		list.add("31~40");
		list.add("41~50");
		list.add("51~70");
		list.add("71~90");
		list.add("90~120");
		list.add("121~150");
		list.add("151~180");
		list.add("181~220");
		list.add("221~260");
		list.add("261~300");
		list.add("300~");
		return list;
	}

	private int ruleForUserlevel(int count) {
		if (0 <= count && count <= 3) {
			return 1;
		} else if (4 <= count && count <= 10) {
			return 2;
		} else if (11 <= count && count <= 20) {
			return 3;
		} else if (21 <= count && count <= 30) {
			return 4;
		} else if (31 <= count && count <= 40) {
			return 5;
		} else if (41 <= count && count <= 50) {
			return 6;
		} else if (51 <= count && count <= 70) {
			return 7;
		} else if (71 <= count && count <= 90) {
			return 8;
		} else if (91 <= count && count <= 120) {
			return 9;
		} else if (121 <= count && count <= 150) {
			return 10;
		} else if (151 <= count && count <= 180) {
			return 11;
		} else if (181 <= count && count <= 220) {
			return 12;
		} else if (221 <= count && count <= 260) {
			return 13;
		} else if (261 <= count && count <= 300) {
			return 14;
		} else {
			return 15;
		}
	}
}
