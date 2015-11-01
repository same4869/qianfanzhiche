package com.xun.qianfanzhiche.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.bean.CommunityItem;
import com.xun.qianfanzhiche.cache.ImageLoaderWithCaches;
import com.xun.qianfanzhiche.utils.ScreenUtil;

public class CommunityListAdapter extends BaseAdapter {
	private Context mContext;
	private List<CommunityItem> data = new ArrayList<CommunityItem>();
	private ImageLoaderWithCaches mImageLoader;

	public CommunityListAdapter(Context context, List<CommunityItem> data,
			ImageLoaderWithCaches mImageLoader) {
		this.mImageLoader = mImageLoader;
		this.mContext = context;
		this.data = data;
	}

	public void setData(List<CommunityItem> data) {
		this.data = data;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		if (arg1 == null) {
			viewHolder = new ViewHolder();
			arg1 = LayoutInflater.from(mContext).inflate(
					R.layout.community_item, null);
			viewHolder.itemContent = (TextView) arg1
					.findViewById(R.id.item_content);
			viewHolder.itemImg = (ImageView) arg1.findViewById(R.id.item_img);
			arg1.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) arg1.getTag();
		}
		viewHolder.itemContent.setText(data.get(arg0).getContent());
		if (data.get(arg0).getImage() != null) {
			viewHolder.itemImg.setVisibility(View.VISIBLE);
			// data.get(arg0)
			// .getImage()
			// .loadImageThumbnail(mContext, viewHolder.itemImg,
			// ScreenUtil.dpToPx(mContext.getResources(), 250),
			// ScreenUtil.dpToPx(mContext.getResources(), 250));
			mImageLoader.showImage(data.get(arg0).getImage().getUrl(),
					viewHolder.itemImg);
		} else {
			viewHolder.itemImg.setVisibility(View.GONE);
		}

		return arg1;
	}

	private class ViewHolder {
		TextView itemContent;
		ImageView itemImg;
	}
}
