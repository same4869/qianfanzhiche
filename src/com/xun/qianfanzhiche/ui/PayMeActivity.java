package com.xun.qianfanzhiche.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import cn.bmob.v3.listener.SaveListener;

import com.bmob.pay.tool.OrderQueryListener;
import com.bmob.pay.tool.PayListener;
import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.base.BaseActivity;
import com.xun.qianfanzhiche.bean.QFFoundBean;
import com.xun.qianfanzhiche.manager.PayManager;
import com.xun.qianfanzhiche.utils.ApkUtil;
import com.xun.qianfanzhiche.utils.BmobUtil;
import com.xun.qianfanzhiche.utils.LogUtil;

public class PayMeActivity extends BaseActivity implements OnClickListener {
	private Button payAliBtn, payWxBtn;
	private String username, orderId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay);

		initView();

	}

	private void initView() {
		setActionBarTitle("千帆基金");
		payAliBtn = (Button) findViewById(R.id.pay_ali_btn);
		payWxBtn = (Button) findViewById(R.id.pay_wx_btn);
		payAliBtn.setOnClickListener(this);
		payWxBtn.setOnClickListener(this);

		Intent intent = getIntent();
		boolean isLogined = intent.getBooleanExtra("isLogined", false);
		if (isLogined) {
			username = BmobUtil.getCurrentUser(getApplicationContext()).getUsername();
		} else {
			username = "IAmNotLogin";
		}
	}

	private void startAliPay(final double price, final String payInfo) {
		PayManager.getInstance().startAliPay(this, price, payInfo, new PayListener() {

			@Override
			public void unknow() {
				LogUtil.d(LogUtil.TAG, "unknow");
			}

			@Override
			public void succeed() {
				LogUtil.d(LogUtil.TAG, "succeed");
				updatePayInfoToBmob(price, payInfo, username, orderId, true, null);
			}

			@Override
			public void orderId(String arg0) {
				orderId = arg0;
				LogUtil.d(LogUtil.TAG, "orderId arg0 --> " + arg0);
			}

			@Override
			public void fail(int arg0, String arg1) {
				LogUtil.d(LogUtil.TAG, "fail arg0 --> " + arg0 + " arg1 --> " + arg1);
				updatePayInfoToBmob(price, payInfo, username, orderId, false, arg1);
			}
		});
	}

	private void startWxPay(final double price, final String payInfo) {
		PayManager.getInstance().startWeixinPay(this, price, payInfo, new PayListener() {

			@Override
			public void unknow() {
				LogUtil.d(LogUtil.TAG, "unknow");
			}

			@Override
			public void succeed() {
				LogUtil.d(LogUtil.TAG, "succeed");
			}

			@Override
			public void orderId(String arg0) {
				orderId = arg0;
				LogUtil.d(LogUtil.TAG, "orderId arg0 --> " + arg0);
			}

			@Override
			public void fail(int arg0, String arg1) {
				LogUtil.d(LogUtil.TAG, "fail arg0 --> " + arg0 + " arg1 --> " + arg1);
				// 当code为-2,意味着用户中断了操作
				// code为-3意味着没有安装BmobPlugin插件
				if (arg0 == -3) {
					new AlertDialog.Builder(getApplicationContext()).setMessage("监测到你尚未安装支付插件,无法进行微信支付,请选择安装插件(已打包在本地,无流量消耗)还是用支付宝支付")
							.setPositiveButton("安装", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									ApkUtil.installBmobPayPlugin(getApplicationContext(), "BmobPayPlugin.apk");
								}
							}).setNegativeButton("支付宝支付", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									startAliPay(price, payInfo);
								}
							}).create().show();
				}
			}
		});
	}

	private void orderQuery(String orderId) {
		PayManager.getInstance().orderQuery(this, orderId, new OrderQueryListener() {

			@Override
			public void succeed(String arg0) {
				LogUtil.d(LogUtil.TAG, "orderId arg0 --> " + arg0);
			}

			@Override
			public void fail(int arg0, String arg1) {
				LogUtil.d(LogUtil.TAG, "orderId arg0 --> " + arg0 + " arg1 --> " + arg1);
			}
		});
	}

	private void updatePayInfoToBmob(Double price, String payInfo, String username, String orderId, boolean payStatus, String failWhy) {
		final QFFoundBean qFFoundBean = new QFFoundBean();
		qFFoundBean.setUsername(username);
		qFFoundBean.setOrderId(orderId);
		qFFoundBean.setPayStatus(payStatus);
		qFFoundBean.setFailWhy(failWhy);
		qFFoundBean.setPrice(price);
		qFFoundBean.setPayInfo(payInfo);
		qFFoundBean.save(getApplicationContext(), new SaveListener() {

			@Override
			public void onSuccess() {
				LogUtil.d(LogUtil.TAG,
						"添加支付数据成功 用户名 --》" + qFFoundBean.getUsername() + " 订单号 --》" + qFFoundBean.getOrderId() + " 支付状态 --》" + qFFoundBean.isPayStatus());
			}

			@Override
			public void onFailure(int code, String arg0) {
				LogUtil.d(LogUtil.TAG, "添加支付数据失败 code --> " + code + " arg0 --> " + arg0);
			}
		});
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.pay_ali_btn:
			startAliPay(0.02, "我来自支付宝支付");
			break;
		case R.id.pay_wx_btn:
			// startWxPay(0.02, "我来自微信支付");
			orderQuery("21f944934d43ff2c2a8be5d526ac2063");
			break;
		default:
			break;
		}
	}
}