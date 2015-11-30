package com.xun.qianfanzhiche.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.bmob.pay.tool.PayListener;
import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.base.BaseFragment;
import com.xun.qianfanzhiche.manager.PayManager;
import com.xun.qianfanzhiche.ui.CarShowGirlActivity;
import com.xun.qianfanzhiche.ui.CommonWebActivity;
import com.xun.qianfanzhiche.ui.FavActivity;
import com.xun.qianfanzhiche.ui.LoginActivity;
import com.xun.qianfanzhiche.utils.ApkUtil;
import com.xun.qianfanzhiche.utils.BmobUtil;
import com.xun.qianfanzhiche.utils.ConstantsUtil;
import com.xun.qianfanzhiche.utils.LogUtil;
import com.xun.qianfanzhiche.view.ItemBar;

/**
 * 扩展页面
 * 
 * @author xunwang
 * 
 *         2015-11-27
 */
public class ExtendFragment extends BaseFragment implements OnClickListener {
	private ItemBar carShowGirlItemBar, userFavItemBar, carMaintenance, carActivity, payMeItemBar;

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_extend, null);
		carShowGirlItemBar = (ItemBar) root.findViewById(R.id.car_show_girl);
		carShowGirlItemBar.setItemBarTitle("汽车车模");
		carShowGirlItemBar.setOnClickListener(this);
		userFavItemBar = (ItemBar) root.findViewById(R.id.user_fav);
		userFavItemBar.setItemBarTitle("我的收藏");
		userFavItemBar.setOnClickListener(this);
		carMaintenance = (ItemBar) root.findViewById(R.id.car_maintenance);
		carMaintenance.setItemBarTitle("汽车保养");
		carMaintenance.setOnClickListener(this);
		carActivity = (ItemBar) root.findViewById(R.id.car_activity);
		carActivity.setItemBarTitle("汽车活动");
		carActivity.setOnClickListener(this);
		payMeItemBar = (ItemBar) root.findViewById(R.id.pay_me);
		payMeItemBar.setItemBarTitle("给我钱");
		payMeItemBar.setOnClickListener(this);
		return root;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.car_show_girl:
			Intent showGirlIntent = new Intent(getActivity(), CarShowGirlActivity.class);
			startActivity(showGirlIntent);
			break;
		case R.id.user_fav:
			if (BmobUtil.getCurrentUser(getContext()) != null) {
				Intent intent = new Intent(getActivity(), FavActivity.class);
				startActivity(intent);
			} else {
				Intent intent = new Intent(getActivity(), LoginActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.car_maintenance:
			Intent carMaintenanceIntent = new Intent(getActivity(), CommonWebActivity.class);
			carMaintenanceIntent.putExtra(CommonWebActivity.COMMON_WEB_URL, ConstantsUtil.getConstantFromLocalOrRemote("SINA_CAR_MAINTENANCE"));
			startActivity(carMaintenanceIntent);
			break;
		case R.id.car_activity:
			Intent carActivityIntent = new Intent(getActivity(), CommonWebActivity.class);
			carActivityIntent.putExtra(CommonWebActivity.COMMON_WEB_URL, ConstantsUtil.getConstantFromLocalOrRemote("CAR_ACTIVITY"));
			startActivity(carActivityIntent);
			break;
		case R.id.pay_me:
			// PayManager.getInstance().startAliPay(getActivity(), 0.02, "测试费用", new PayListener() {
			//
			// @Override
			// public void unknow() {
			// LogUtil.d(LogUtil.TAG, "unknow");
			// }
			//
			// @Override
			// public void succeed() {
			// LogUtil.d(LogUtil.TAG, "succeed");
			// }
			//
			// @Override
			// public void orderId(String arg0) {
			// LogUtil.d(LogUtil.TAG, "orderId arg0 --> " + arg0);
			// }
			//
			// @Override
			// public void fail(int arg0, String arg1) {
			// LogUtil.d(LogUtil.TAG, "fail arg0 --> " + arg0 + " arg1 --> " + arg1);
			// }
			// });
			PayManager.getInstance().startWeixinPay(getActivity(), 0.02, "微信测试", new PayListener() {

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
					LogUtil.d(LogUtil.TAG, "orderId arg0 --> " + arg0);
				}

				@Override
				public void fail(int arg0, String arg1) {
					LogUtil.d(LogUtil.TAG, "fail arg0 --> " + arg0 + " arg1 --> " + arg1);
					// 当code为-2,意味着用户中断了操作
					// code为-3意味着没有安装BmobPlugin插件
					if (arg0 == -3) {
						new AlertDialog.Builder(getContext()).setMessage("监测到你尚未安装支付插件,无法进行微信支付,请选择安装插件(已打包在本地,无流量消耗)还是用支付宝支付")
								.setPositiveButton("安装", new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int which) {
										ApkUtil.installBmobPayPlugin(getContext(), "BmobPayPlugin.apk");
									}
								}).setNegativeButton("支付宝支付", new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int which) {
										// payByAli();
									}
								}).create().show();
					}
				}
			});
			break;
		default:
			break;
		}

	}

}
