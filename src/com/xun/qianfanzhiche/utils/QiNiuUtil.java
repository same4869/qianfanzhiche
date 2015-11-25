package com.xun.qianfanzhiche.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;

import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.bean.CarGridBean;
import com.xun.qianfanzhiche.common.Constant;

public class QiNiuUtil {
	public static List<CarGridBean> getCardGridInfo(Context context) {
		List<CarGridBean> carGridList = new ArrayList<CarGridBean>();
		Resources res = context.getResources();
		String[] carGridKeyList = res.getStringArray(R.array.car_grid_key);
		String[] carGridNameList = res.getStringArray(R.array.car_grid_name);
		String[] carGridBaikeKeyList = res.getStringArray(R.array.car_grid_baike_key);
		for (int i = 0; i < carGridKeyList.length; i++) {
			CarGridBean carGridBean = new CarGridBean();
			carGridBean.setCarName(carGridNameList[i]);
			String[] strings = carGridNameList[i].split("-");
			carGridBean.setCarUrl(Constant.QINIU_IMG_BASE_URL + "chebiao-" + strings[0] + ".jpg");
			if (strings.length >= 2) {
				carGridBean.setCarCategory(strings[1]);
			}
			carGridBean.setCarDetailUrl(Constant.BAIDU_BAIKE_BASE_URL + carGridBaikeKeyList[i] + ".html");
			carGridList.add(carGridBean);
		}
		return carGridList;
	}
}
