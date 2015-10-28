package com.xun.qianfanzhiche.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.xun.qianfanzhiche.app.ZhiCheApp;

public class ToastUtil {
	private static Context context = null;
	private static Toast toast = null;

	@SuppressLint("ShowToast")
	public static void show(Context context, String text) {
		if (ToastUtil.context == context) {
			toast.setText(text);
			toast.setDuration(Toast.LENGTH_SHORT);

		} else {
			ToastUtil.context = context;
			toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		}

		ZhiCheApp.getInstance().getTopActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				toast.setGravity(Gravity.BOTTOM, 0, 0);
				toast.show();
			}
		});
	}

	public static void cancelToast() {
		if (toast != null) {
			toast.cancel();
		}
	}
}
