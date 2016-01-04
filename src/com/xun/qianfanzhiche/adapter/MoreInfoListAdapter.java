package com.xun.qianfanzhiche.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.bean.ShiTuBean.DataBean.SameInfoBean;
import com.xun.qianfanzhiche.cache.ImageLoaderWithCaches;

public class MoreInfoListAdapter extends BaseContentAdapter<SameInfoBean> {
	private ImageLoaderWithCaches mImageLoader;

	public MoreInfoListAdapter(Context context, List<SameInfoBean> list, ImageLoaderWithCaches mImageLoader) {
		super(context, list);
		this.mImageLoader = mImageLoader;
	}

	@Override
	public View getConvertView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.view_more_info_item, null);
			viewHolder.img = (ImageView) convertView.findViewById(R.id.more_info_img);
			viewHolder.title = (TextView) convertView.findViewById(R.id.more_info_title);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		mImageLoader.loadImagesWithUrl(viewHolder.img, dataList.get(position).getObjURL());
		viewHolder.title.setText(dataList.get(position).getFromPageTitle());

		return convertView;
	}

	public static class ViewHolder {
		public ImageView img;
		public TextView title;
	}

}
