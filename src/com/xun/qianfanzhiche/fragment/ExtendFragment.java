package com.xun.qianfanzhiche.fragment;

import net.youmi.android.offers.OffersManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.base.BaseFragment;
import com.xun.qianfanzhiche.ui.CarShowGirlActivity;
import com.xun.qianfanzhiche.ui.CommonWebActivity;
import com.xun.qianfanzhiche.ui.FavActivity;
import com.xun.qianfanzhiche.ui.LoginActivity;
import com.xun.qianfanzhiche.ui.PayMeActivity;
import com.xun.qianfanzhiche.ui.PersonalActivity;
import com.xun.qianfanzhiche.utils.BmobUtil;
import com.xun.qianfanzhiche.utils.ConstantsUtil;
import com.xun.qianfanzhiche.utils.LogUtil;
import com.xun.qianfanzhiche.utils.ZhiCheSPUtil;
import com.xun.qianfanzhiche.view.ItemBar;

/**
 * 扩展页面
 * 
 * @author xunwang
 * 
 *         2015-11-27
 */
public class ExtendFragment extends BaseFragment implements OnClickListener,
		net.youmi.android.listener.Interface_ActivityListener {
	private ItemBar carShowGirlItemBar, userFavItemBar, carMaintenance, carActivity, payMeItemBar, carVideoItemBar,
			appWallItemBar, carNewsItemBar, notifyCenterItemBar;

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_extend, null);
		carShowGirlItemBar = (ItemBar) root.findViewById(R.id.car_show_girl);
		carShowGirlItemBar.setItemBarTitle("汽车车模");
		carShowGirlItemBar.setOnClickListener(this);
		carShowGirlItemBar.setTopLineVisible();
		userFavItemBar = (ItemBar) root.findViewById(R.id.user_fav);
		userFavItemBar.setItemBarTitle("我的收藏");
		userFavItemBar.setOnClickListener(this);
		userFavItemBar.setTopLineVisible();
		carMaintenance = (ItemBar) root.findViewById(R.id.car_maintenance);
		carMaintenance.setItemBarTitle("汽车保养");
		carMaintenance.setOnClickListener(this);
		carActivity = (ItemBar) root.findViewById(R.id.car_activity);
		carActivity.setItemBarTitle("汽车活动");
		carActivity.setOnClickListener(this);
		carActivity.setTopLineVisible();
		payMeItemBar = (ItemBar) root.findViewById(R.id.pay_me);
		payMeItemBar.setItemBarTitle("千帆微打赏");
		payMeItemBar.setOnClickListener(this);
		payMeItemBar.setTopLineVisible();
		carVideoItemBar = (ItemBar) root.findViewById(R.id.car_video);
		carVideoItemBar.setItemBarTitle("汽车视频");
		carVideoItemBar.setOnClickListener(this);
		appWallItemBar = (ItemBar) root.findViewById(R.id.app_wall);
		appWallItemBar.setItemBarTitle("软件推荐");
		appWallItemBar.setOnClickListener(this);
		carNewsItemBar = (ItemBar) root.findViewById(R.id.car_news);
		carNewsItemBar.setItemBarTitle("汽车新闻");
		carNewsItemBar.setOnClickListener(this);
		notifyCenterItemBar = (ItemBar) root.findViewById(R.id.user_notify_center);
		notifyCenterItemBar.setItemBarTitle("通知中心");
		notifyCenterItemBar.setOnClickListener(this);
		if (!ZhiCheSPUtil.getIsShowPayMe()) {
			payMeItemBar.setVisibility(View.GONE);
		}
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
			carMaintenanceIntent.putExtra(CommonWebActivity.COMMON_WEB_URL,
					ConstantsUtil.getConstantFromLocalOrRemote("SINA_CAR_MAINTENANCE"));
			carMaintenanceIntent.putExtra(CommonWebActivity.COMMON_WEB_TITLE, "汽车保养");
			startActivity(carMaintenanceIntent);
			break;
		case R.id.car_activity:
			Intent carActivityIntent = new Intent(getActivity(), CommonWebActivity.class);
			carActivityIntent.putExtra(CommonWebActivity.COMMON_WEB_URL,
					ConstantsUtil.getConstantFromLocalOrRemote("CAR_ACTIVITY"));
			carActivityIntent.putExtra(CommonWebActivity.COMMON_WEB_TITLE, "汽车活动");
			startActivity(carActivityIntent);
			break;
		case R.id.pay_me:
			boolean isLogined = false;
			if (BmobUtil.getCurrentUser(getContext()) != null) {
				isLogined = true;
			}
			Intent payIntent = new Intent(getActivity(), PayMeActivity.class);
			payIntent.putExtra("isLogined", isLogined);
			startActivity(payIntent);
			break;
		case R.id.car_video:
			Intent carVideoIntent = new Intent(getActivity(), CommonWebActivity.class);
			carVideoIntent.putExtra(CommonWebActivity.COMMON_WEB_URL,
					ConstantsUtil.getConstantFromLocalOrRemote("CAR_VIDEO"));
			carVideoIntent.putExtra(CommonWebActivity.COMMON_WEB_TITLE, "汽车视频");
			startActivity(carVideoIntent);
			break;
		case R.id.app_wall:
			OffersManager.getInstance(getActivity()).showOffersWall(this);
			break;
		case R.id.car_news:
			Intent carNewsIntent = new Intent(getActivity(), CommonWebActivity.class);
			carNewsIntent.putExtra(CommonWebActivity.COMMON_WEB_URL,
					ConstantsUtil.getConstantFromLocalOrRemote("CAR_NEWS"));
			carNewsIntent.putExtra(CommonWebActivity.COMMON_WEB_TITLE, "汽车新闻");
			startActivity(carNewsIntent);
			break;
		case R.id.user_notify_center:
			Intent notifyCenterIntent = new Intent(getActivity(), PersonalActivity.class);
			notifyCenterIntent.putExtra("isFromNotifyCenter", true);
			startActivity(notifyCenterIntent);
			break;
		default:
			break;
		}

	}

	@Override
	public void onActivityDestroy(Context arg0) {
		LogUtil.d(LogUtil.TAG, "onActivityDestroy");
	}
}
