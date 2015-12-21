package com.xun.qianfanzhiche.manager;

import android.util.SparseArray;

import com.alibaba.fastjson.JSON;
import com.xun.qianfanzhiche.bean.TextResponse;

public class TulingManager {
	private static TulingManager instance = null;

	private TulingManager() {

	}

	public static TulingManager getInstance() {
		if (instance == null) {
			syncInit();
		}
		return instance;
	}

	private static synchronized void syncInit() {
		if (instance == null) {
			instance = new TulingManager();
		}
	}

	// 先只暂时支持10000的图灵信息，其他不支持
	public String processString(String turingResult) {
		String SEARCH_TYPE = null; // 截取出来的返回类型码
		if (turingResult.charAt(8) != '4') {
			SEARCH_TYPE = turingResult.substring(8, 14);
		} else {
			SEARCH_TYPE = turingResult.substring(8, 13);
		}
		int type = Integer.parseInt(SEARCH_TYPE);
		AnswerType type2 = AnswerType.fromInt(type);
		if (!type2.equals(AnswerType.TEXT)) {
			return "千帆冷冷暂时听不懂哟，快去鞭笞作者增强我%>_<%";
		}
		TextResponse textResponse = JSON.parseObject(turingResult, TextResponse.class);
		if (textResponse != null) {
			return textResponse.getText();
		}
		return null;
	}

	// 100000 文本类数据
	// 200000 网址类数据
	// 301000 小说
	// 302000 新闻
	// 304000 应用、软件、下载
	// 305000 列车
	// 306000 航班
	// 307000 团购
	// 308000 优惠,movie
	// 309000 酒店
	// 310000 彩票
	// 311000 价格
	// 312000 餐厅
	// 40001 key的长度错误（32位）
	// 40002 请求内容为空
	// 40003 key错误或帐号未激活
	// 40004 当天请求次数已用完
	// 40005 暂不支持该功能
	// 40006 服务器升级中
	// 40007 服务器数据格式异常
	public enum AnswerType {
		TEXT(100000), URL(200000), NOVEL(301000), NEWS(302000), APP(304000), TRAIN(305000), FLIGHT(306000), GROUP(307000), MOVIE(308000), HOTEL(309000), LOTTERY(
				310000), PRICE(311000), FOOD(312000), KEY_LENGTH_ERROR(40001), REQUEST_NULL(40002), KEY_ERROR(40003), NO_REQUEST_COUNT(40004), NO_SUPPORT(40005), UPDATING(
				40006), FORMAT_ERROR(40007), NUKNOWN(0);
		private int nCode;

		AnswerType(int nCode) {
			this.nCode = nCode;
		}

		private static final SparseArray<AnswerType> intToTypeMap = new SparseArray<AnswerType>();
		static {
			for (AnswerType type : AnswerType.values()) {
				intToTypeMap.put(type.nCode, type);
			}
		}

		public static AnswerType fromInt(int i) {
			AnswerType type = intToTypeMap.get(Integer.valueOf(i));
			if (type == null)
				return AnswerType.NUKNOWN;
			return type;
		}
	}
}
