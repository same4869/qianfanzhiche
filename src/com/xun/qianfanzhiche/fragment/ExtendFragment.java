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
import com.xun.qianfanzhiche.ui.FavActivity;
import com.xun.qianfanzhiche.ui.LoginActivity;
import com.xun.qianfanzhiche.utils.BmobUtil;
import com.xun.qianfanzhiche.view.ItemBar;

/**
 * 扩展页面
 * 
 * @author xunwang
 * 
 *         2015-11-27
 */
public class ExtendFragment extends BaseFragment implements OnClickListener {
	private ItemBar carShowGirlItemBar, userFavItemBar;

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
		return root;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.car_show_girl:

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
		default:
			break;
		}

	}

}
