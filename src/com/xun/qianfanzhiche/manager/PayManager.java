package com.xun.qianfanzhiche.manager;


/**
 * 支付
 * 
 * @author xunwang
 * 
 *         2015-11-30
 */
public class PayManager {
	private static PayManager instance = null;

	private PayManager() {

	}

	public static PayManager getInstance() {
		if (instance == null) {
			syncInit();
		}
		return instance;
	}

	private static synchronized void syncInit() {
		if (instance == null) {
			instance = new PayManager();
		}
	}

	// public void startAliPay(Activity context, Double price, String name,
	// PayListener listener) {
	// new BmobPay(context).pay(price, name, listener);
	// }
	//
	// public void startWeixinPay(Activity context, Double price, String name,
	// PayListener listener) {
	// new BmobPay(context).payByWX(price, name, listener);
	// }
	//
	// public void orderQuery(Activity context, String orderId,
	// OrderQueryListener orderQueryListener) {
	// new BmobPay(context).query(orderId, orderQueryListener);
	// }
}
