package com.xun.qianfanzhiche.app;

import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.xun.qianfanzhiche.bean.ConstantsBean;
import com.xun.qianfanzhiche.common.Constant;
import com.xun.qianfanzhiche.utils.ActivityManagerUtils;
import com.xun.qianfanzhiche.utils.LogUtil;
import com.xun.qianfanzhiche.utils.ToastUtil;
import com.xun.qianfanzhiche.utils.ZhiCheSPUtil;

public class ZhiCheApp extends Application {
	private static ZhiCheApp mApplication = null;
	public static IWXAPI api;

	public static ZhiCheApp getInstance() {
		return mApplication;
	}

	public SharedPreferences getZhiCheSharedPreferences(String tbl) {
		return getSharedPreferences(tbl, Context.MODE_PRIVATE);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mApplication = this;
		Bmob.initialize(getApplicationContext(), Constant.BMOB_APP_ID);
		register2WX();
		// setConstants();
		getConstants();
	}
	
	private void register2WX() {
		api = WXAPIFactory.createWXAPI(this, Constant.WEIXIN_APP_ID, true);
		api.registerApp(Constant.WEIXIN_APP_ID);
	}

	private void getConstants() {
		BmobQuery<ConstantsBean> query = new BmobQuery<ConstantsBean>();
		// 返回50条数据，如果不加上这条语句，默认返回10条数据
		query.setLimit(1);
		// 执行查询方法
		query.findObjects(this, new FindListener<ConstantsBean>() {
			@Override
			public void onSuccess(List<ConstantsBean> object) {
				for (ConstantsBean constantsBean : object) {
					LogUtil.d(LogUtil.TAG, "constantsBean.getQiniuBaseUrl() --> " + constantsBean.getQiniuBaseUrl()
							+ " constantsBean.getAutoHomeBaseUrl() --> " + constantsBean.getAutoHomeBaseUrl()
							+ " constantsBean.getBaiduBaiKeBaseUrl() --> " + constantsBean.getBaiduBaiKeBaseUrl());
					ZhiCheSPUtil.setQiniuBaseUrl(constantsBean.getQiniuBaseUrl());
					ZhiCheSPUtil.setAutoHomeBaseUrl(constantsBean.getAutoHomeBaseUrl());
					ZhiCheSPUtil.setAutoHomeBaseUrlSuffix(constantsBean.getAutoHomeBaseUrlSuffix());
					ZhiCheSPUtil.setBaiduBaiKeBaseUrl(constantsBean.getBaiduBaiKeBaseUrl());
					ZhiCheSPUtil.setIsUseLocalConstants(constantsBean.isUseLocalConstants());
				}
			}

			@Override
			public void onError(int code, String msg) {
				// ToastUtil.show(getApplicationContext(), "初始化参数错误 msg --> " +
				// msg);
				LogUtil.d(LogUtil.TAG, "初始化参数错误 msg --> " + msg);
				ZhiCheSPUtil.setIsUseLocalConstants(true);
			}
		});
	}

	private void setConstants() {
		ConstantsBean constantsBean = new ConstantsBean();
		// 注意：不能调用gameScore.setObjectId("")方法
		constantsBean.setQiniuBaseUrl(Constant.QINIU_IMG_BASE_URL);
		constantsBean.setAutoHomeBaseUrl(Constant.AUTO_HOME_BASE_URL);
		constantsBean.setAutoHomeBaseUrlSuffix(Constant.AUTO_HOME_BASE_URL_SUFFIX);
		constantsBean.setBaiduBaiKeBaseUrl(Constant.BAIDU_BAIKE_BASE_URL);
		constantsBean.setSinaCarMaintenanceUrl(Constant.SINA_CAR_MAINTENANCE);
		constantsBean.setCarActivityUrl(Constant.CAR_ACTIVITY);
		constantsBean.setUseLocalConstants(false);
		constantsBean.save(getApplicationContext(), new SaveListener() {

			@Override
			public void onSuccess() {
				ToastUtil.show(getApplicationContext(), "初始化常量成功");
			}

			@Override
			public void onFailure(int code, String arg0) {
				ToastUtil.show(getApplicationContext(), "初始化常量失败");
			}
		});
	}

	public void addActivity(Activity ac) {
		ActivityManagerUtils.getInstance().addActivity(ac);
	}

	public void exit() {
		ActivityManagerUtils.getInstance().removeAllActivity();
	}

	public Activity getTopActivity() {
		return ActivityManagerUtils.getInstance().getTopActivity();
	}
}
