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
import com.xun.qianfanzhiche.utils.LogUtil;

public class CommunityListAdapter extends BaseAdapter {
	private Context mContext;
	private List<CommunityItem> data = new ArrayList<CommunityItem>();
	private ImageLoaderWithCaches mImageLoader;

	public CommunityListAdapter(Context context, ImageLoaderWithCaches mImageLoader) {
		// LogUtil.d(LogUtil.TAG, "CommunityListAdapter");
		this.mImageLoader = mImageLoader;
		this.mContext = context;
	}

	public void setData(List<CommunityItem> data) {
		// LogUtil.d(LogUtil.TAG, "setData");
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
			arg1 = LayoutInflater.from(mContext).inflate(R.layout.community_item, null);
			viewHolder.itemContent = (TextView) arg1.findViewById(R.id.item_content);
			viewHolder.itemImg = (ImageView) arg1.findViewById(R.id.item_img);
			viewHolder.itemAvater = (ImageView) arg1.findViewById(R.id.item_avater);
			viewHolder.itemName = (TextView) arg1.findViewById(R.id.item_name);
			arg1.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) arg1.getTag();
		}
		// LogUtil.d(LogUtil.TAG, "data.size() --> " + data.size());
		viewHolder.itemContent.setText(data.get(arg0).getContent());
		if (data.get(arg0).getImage() != null) {
			viewHolder.itemImg.setVisibility(View.VISIBLE);
			if (data.get(arg0).getAuthor() != null) {
				// viewHolder.itemName.setText(data.get(arg0).getAuthor().getTableName());
				LogUtil.d(LogUtil.TAG, "data.get(arg0).getAuthor().getUsername() --> " + data.get(arg0).getAuthor().getUsername());
			}
			viewHolder.itemImg.setTag(data.get(arg0).getImage().getFileUrl(mContext));
			mImageLoader.showImage(data.get(arg0).getImage().getFileUrl(mContext), viewHolder.itemImg);
			// LogUtil.d(LogUtil.TAG, "data.get(" + arg0 + ").getImage().getFileUrl(mContext) --> " + data.get(arg0).getImage().getFileUrl(mContext));
		} else {
			viewHolder.itemImg.setVisibility(View.GONE);
		}

		return arg1;
	}

	private class ViewHolder {
		ImageView itemAvater;
		TextView itemName;
		TextView itemContent;
		ImageView itemImg;
	}
}
