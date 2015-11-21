package com.xun.qianfanzhiche.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.bean.CommunityItem;
import com.xun.qianfanzhiche.cache.ImageLoaderWithCaches;
import com.xun.qianfanzhiche.manager.ShareManger;
import com.xun.qianfanzhiche.utils.BmobUtil;

public class CommunityListAdapter extends BaseContentAdapter<CommunityItem> {
	private Context mContext;
	private List<CommunityItem> data;
	private ImageLoaderWithCaches mImageLoader;

	public CommunityListAdapter(Context context, List<CommunityItem> data, ImageLoaderWithCaches mImageLoader) {
		// LogUtil.d(LogUtil.TAG, "CommunityListAdapter");
		super(context, data);
		this.data = data;
		this.mImageLoader = mImageLoader;
		this.mContext = context;
	}

	public void setData(List<CommunityItem> data) {
		// LogUtil.d(LogUtil.TAG, "setData");
		this.data = data;
		setDataList(data);
		notifyDataSetChanged();
	}

	@Override
	public View getConvertView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.community_item, null);
			viewHolder.itemContent = (TextView) convertView.findViewById(R.id.item_content);
			viewHolder.itemImg = (ImageView) convertView.findViewById(R.id.item_img);
			viewHolder.itemAvater = (ImageView) convertView.findViewById(R.id.item_avater);
			viewHolder.itemName = (TextView) convertView.findViewById(R.id.item_name);
			viewHolder.itemTime = (TextView) convertView.findViewById(R.id.item_time);
			viewHolder.itemUserLeavel = (TextView) convertView.findViewById(R.id.item_user_level);
			viewHolder.itemFav = (ImageView) convertView.findViewById(R.id.item_action_fav);
			viewHolder.itemLove = (TextView) convertView.findViewById(R.id.item_action_love);
			viewHolder.itemShare = (TextView) convertView.findViewById(R.id.item_action_share);
			viewHolder.itemComment = (TextView) convertView.findViewById(R.id.item_action_comment);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		// LogUtil.d(LogUtil.TAG, "data.size() --> " + data.size());
		viewHolder.itemContent.setText(data.get(position).getContent());
		viewHolder.itemName.setText(data.get(position).getAuthor().getUsername());
		if (data.get(position).getImage() != null) {
			viewHolder.itemImg.setVisibility(View.VISIBLE);
			viewHolder.itemImg.setTag(data.get(position).getImage().getFileUrl(mContext));
			mImageLoader.showImage(data.get(position).getImage().getFileUrl(mContext), viewHolder.itemImg,
					R.drawable.bg_pic_loading);
		} else {
			viewHolder.itemImg.setVisibility(View.GONE);
		}
		String avatarUrl = null;
		if (data.get(position).getAuthor() != null && data.get(position).getAuthor().getAvatar() != null) {
			avatarUrl = data.get(position).getAuthor().getAvatar().getFileUrl(mContext);
		}
		mImageLoader.showImage(avatarUrl, viewHolder.itemAvater, R.drawable.defalut_avater);
		viewHolder.itemTime.setText(data.get(position).getCreatedAt());
		BmobUtil.queryCountForUserLevel(mContext, viewHolder.itemUserLeavel, data.get(position).getAuthor()
				.getObjectId());

		viewHolder.itemShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ShareManger.getInstance().showShare(mContext, data.get(position).getImage().getFileUrl(mContext));
			}
		});

		return convertView;
	}

	private class ViewHolder {
		ImageView itemAvater;
		TextView itemName;
		TextView itemContent;
		ImageView itemImg;
		TextView itemTime;
		TextView itemUserLeavel;
		ImageView itemFav;
		TextView itemLove;
		TextView itemShare;
		TextView itemComment;
	}
}
