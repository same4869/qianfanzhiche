package com.xun.qianfanzhiche.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mob.tools.network.StringPart;
import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.bean.CarGridBean;
import com.xun.qianfanzhiche.cache.ImageLoaderWithCaches;
import com.xun.qianfanzhiche.common.Constant;
import com.xun.qianfanzhiche.fragment.MainFragment;
import com.xun.qianfanzhiche.ui.CommonWebActivity;

public class CarGridAdapter extends BaseContentAdapter<CarGridBean> {
	private Context context;
	private ImageLoaderWithCaches mImageLoader;
	private List<String> carKey = new ArrayList<String>();

	private String[] carGridKeyList;

	public CarGridAdapter(Context context, List<CarGridBean> list, ImageLoaderWithCaches mImageLoader) {
		super(context, list);
		this.mImageLoader = mImageLoader;
		this.context = context;
		Resources res = context.getResources();
		carGridKeyList = res.getStringArray(R.array.car_grid_key);
		for (int i = 0; i < carGridKeyList.length; i++) {
			carKey.add(carGridKeyList[i]);
		}
	}

	public void setDataChange(List<CarGridBean> list, ImageLoaderWithCaches mImageLoader, List<String> carKey) {
		setDataList(list);
		this.mImageLoader = mImageLoader;
		this.carKey = carKey;
		super.notifyDataSetChanged();
	}

	@Override
	public View getConvertView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.car_grid_item, null);
			viewHolder.carName = (TextView) convertView.findViewById(R.id.car_name);
			viewHolder.carImg = (ImageView) convertView.findViewById(R.id.car_img);
			viewHolder.carPrice = (ImageView) convertView.findViewById(R.id.car_detail);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String[] names = dataList.get(position).getCarName().split("-");
		viewHolder.carName.setText(names[0]);
		viewHolder.carImg.setTag(dataList.get(position).getCarUrl());
		mImageLoader.showImage(dataList.get(position).getCarUrl(), viewHolder.carImg, R.drawable.bg_pic_loading);

		viewHolder.carPrice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, CommonWebActivity.class);
				String url = Constant.AUTO_HOME_BASE_URL + carKey.get(position) + Constant.AUTO_HOME_BASE_URL_SUFFIX;
				intent.putExtra(MainFragment.CAR_GRID_URL_KEY, url);
				context.startActivity(intent);
			}
		});
		return convertView;
	}

	public static class ViewHolder {
		public TextView carName;
		public ImageView carImg;
		public ImageView carPrice;
	}

}
