package com.xun.qianfanzhiche.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.base.BaseFragment;
import com.xun.qianfanzhiche.common.Constant;
import com.xun.qianfanzhiche.ui.CarShowGirlActivity;
import com.xun.qianfanzhiche.ui.CommonWebActivity;
import com.xun.qianfanzhiche.ui.FavActivity;
import com.xun.qianfanzhiche.ui.LoginActivity;
import com.xun.qianfanzhiche.utils.BmobUtil;
import com.xun.qianfanzhiche.utils.ConstantsUtil;
import com.xun.qianfanzhiche.view.ItemBar;

/**
 * 扩展页面
 * 
 * @author xunwang
 * 
 *         2015-11-27
 */
public class ExtendFragment extends BaseFragment implements OnClickListener {
	private ItemBar carShowGirlItemBar, userFavItemBar, carMaintenance, carActivity;

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
			startActivity(carMaintenanceIntent);
			break;
		case R.id.car_activity:
			Intent carActivityIntent = new Intent(getActivity(), CommonWebActivity.class);
			carActivityIntent.putExtra(CommonWebActivity.COMMON_WEB_URL,
					ConstantsUtil.getConstantFromLocalOrRemote("CAR_ACTIVITY"));
			startActivity(carActivityIntent);
			break;
		default:
			break;
		}

	}

}
