package com.xun.qianfanzhiche.app;

import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.xun.qianfanzhiche.bean.CommunityItem;
import com.xun.qianfanzhiche.bean.ConstantsBean;
import com.xun.qianfanzhiche.common.Constant;
import com.xun.qianfanzhiche.utils.ActivityManagerUtils;
import com.xun.qianfanzhiche.utils.LogUtil;
import com.xun.qianfanzhiche.utils.ZhiCheSPUtil;

public class ZhiCheApp extends Application {
	private static ZhiCheApp mApplication = null;
	public static IWXAPI api;
	private CommunityItem currentCommunityItem = null;

	public static ZhiCheApp getInstance() {
		return mApplication;
	}

	public SharedPreferences getZhiCheSharedPreferences(String tbl) {
		return getSharedPreferences(tbl, Context.MODE_PRIVATE);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		if (!checkSignature()) {
			System.exit(-1);
			return;
		}

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
							+ " constantsBean.getBaiduBaiKeBaseUrl() --> " + constantsBean.getBaiduBaiKeBaseUrl()
							+ " constantsBean.getCarActivityUrl() --> " + constantsBean.getCarActivityUrl()
							+ " constantsBean.getCarVideoUrl() --> " + constantsBean.getCarVideoUrl()
							+ " constantsBean.getCarNewsUrl() --> " + constantsBean.getCarNewsUrl()
							+ " constantsBean.isUseLocalConstants() --> " + constantsBean.isUseLocalConstants()
							+ " constantsBean.isShowPayMe() --> " + constantsBean.isShowPayMe());
					ZhiCheSPUtil.setQiniuBaseUrl(constantsBean.getQiniuBaseUrl());
					ZhiCheSPUtil.setAutoHomeBaseUrl(constantsBean.getAutoHomeBaseUrl());
					ZhiCheSPUtil.setAutoHomeBaseUrlSuffix(constantsBean.getAutoHomeBaseUrlSuffix());
					ZhiCheSPUtil.setBaiduBaiKeBaseUrl(constantsBean.getBaiduBaiKeBaseUrl());
					ZhiCheSPUtil.setCarActivityUrl(constantsBean.getCarActivityUrl());
					ZhiCheSPUtil.setCarVideoUrl(constantsBean.getCarVideoUrl());
					ZhiCheSPUtil.setCarNewsUrl(constantsBean.getCarNewsUrl());
					ZhiCheSPUtil.setIsUseLocalConstants(constantsBean.isUseLocalConstants());
					ZhiCheSPUtil.setIsShowPayMe(constantsBean.isShowPayMe());
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
		Log.d("kkkkkkk", "setConstants");
		ConstantsBean constantsBean = new ConstantsBean();
		// 注意：不能调用gameScore.setObjectId("")方法
		constantsBean.setQiniuBaseUrl(Constant.QINIU_IMG_BASE_URL);
		constantsBean.setAutoHomeBaseUrl(Constant.AUTO_HOME_BASE_URL);
		constantsBean.setAutoHomeBaseUrlSuffix(Constant.AUTO_HOME_BASE_URL_SUFFIX);
		constantsBean.setBaiduBaiKeBaseUrl(Constant.BAIDU_BAIKE_BASE_URL);
		constantsBean.setSinaCarMaintenanceUrl(Constant.SINA_CAR_MAINTENANCE);
		constantsBean.setCarActivityUrl(Constant.CAR_ACTIVITY);
		constantsBean.setCarVideoUrl(Constant.CAR_VIDEO);
		constantsBean.setCarNewsUrl(Constant.CAR_NEWS);
		constantsBean.setUseLocalConstants(false);
		constantsBean.setShowPayMe(false);
		constantsBean.save(getApplicationContext(), new SaveListener() {

			@Override
			public void onSuccess() {
				LogUtil.d(LogUtil.TAG, "初始化常量成功");
				// ToastUtil.show(getApplicationContext(), "初始化常量成功");
			}

			@Override
			public void onFailure(int code, String arg0) {
				LogUtil.d(LogUtil.TAG, "初始化常量失败");
				// ToastUtil.show(getApplicationContext(), "初始化常量失败");
			}
		});
	}

	private boolean checkSignature() {
		byte[] sig = a.getSign(this);

		String hash = a.digest(sig, "MD5").toUpperCase();

		if (hash.equals("AAA224F4C8A3567941A6F4ACAE0B2C93") || hash.equals("7456F65E98B49AFB201FB0F14EF6842F")) {
			return true;
		}

		return false;
	}

	public CommunityItem getCurrentCommunityItem() {
		return currentCommunityItem;
	}

	public void setCurrentCommunityItem(CommunityItem currentCommunityItem) {
		this.currentCommunityItem = currentCommunityItem;
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
