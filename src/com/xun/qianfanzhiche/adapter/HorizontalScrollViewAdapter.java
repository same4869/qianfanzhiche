package com.xun.qianfanzhiche.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.cache.ImageLoaderWithCaches;
import com.xun.qianfanzhiche.common.Constant;

public class HorizontalScrollViewAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private List<Integer> mDatas;
	private ImageLoaderWithCaches imageLoaderWithCaches;

	public HorizontalScrollViewAdapter(Context context, List<Integer> mDatas,
			ImageLoaderWithCaches imageLoaderWithCaches) {
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		this.mDatas = mDatas;
		this.imageLoaderWithCaches = imageLoaderWithCaches;
	}

	public int getCount() {
		return 10;
	}

	public Object getItem(int position) {
		return mDatas.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.activity_index_gallery_item, parent, false);
			viewHolder.mImg = (ImageView) convertView.findViewById(R.id.id_index_gallery_item_image);
			viewHolder.mText = (TextView) convertView.findViewById(R.id.id_index_gallery_item_text);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		imageLoaderWithCaches.loadImagesWithUrl(viewHolder.mImg, Constant.QINIU_IMG_BASE_URL + "qfzc00"
				+ (position + 1) + ".jpg");
		//viewHolder.mImg.setImageResource(mDatas.get(position));
		viewHolder.mText.setText("some info ");

		return convertView;
	}

	private class ViewHolder {
		ImageView mImg;
		TextView mText;
	}

}
