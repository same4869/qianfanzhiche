package com.xun.qianfanzhiche.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.UpdateListener;

import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.bean.CommunityItem;
import com.xun.qianfanzhiche.bean.User;
import com.xun.qianfanzhiche.cache.ImageLoaderWithCaches;
import com.xun.qianfanzhiche.db.DatabaseManager;
import com.xun.qianfanzhiche.manager.ShareManger;
import com.xun.qianfanzhiche.ui.LoginActivity;
import com.xun.qianfanzhiche.utils.BmobUtil;
import com.xun.qianfanzhiche.utils.ToastUtil;

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
		final ViewHolder viewHolder;
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

		if (data.get(position).isMyFav()) {
			viewHolder.itemFav.setImageResource(R.drawable.ic_action_fav_choose);
		} else {
			viewHolder.itemFav.setImageResource(R.drawable.ic_action_fav_normal);
		}
		viewHolder.itemFav.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ToastUtil.show(mContext, "收藏");
				onClickFav(v, data.get(position));
			}
		});

		viewHolder.itemLove.setText(data.get(position).getLove() + "");
		if (BmobUtil.getCurrentUser(mContext) != null
				&& (data.get(position).isMyLove() || DatabaseManager.getInstance(mContext).isLoved(data.get(position)))) {
			viewHolder.itemLove.setTextColor(Color.parseColor("#D95555"));
		} else {
			viewHolder.itemLove.setTextColor(Color.parseColor("#000000"));
		}
		viewHolder.itemLove.setOnClickListener(new OnClickListener() {
			boolean oldFav = data.get(position).isMyFav();

			@Override
			public void onClick(View v) {
				ToastUtil.show(mContext, "点赞");
				if (BmobUtil.getCurrentUser(mContext) == null) {
					ToastUtil.show(mContext, "请先登录。");
					Intent intent = new Intent();
					intent.setClass(mContext, LoginActivity.class);
					mContext.startActivity(intent);
					return;
				}
				if (data.get(position).isMyLove()) {
					ToastUtil.show(mContext, "您已赞过啦");
					return;
				}
				if (DatabaseManager.getInstance(mContext).isLoved(data.get(position))) {
					ToastUtil.show(mContext, "您已赞过啦");
					data.get(position).setMyLove(true);
					// data.get(position).setLove(data.get(position).getLove() +
					// 1);
					// viewHolder.itemLove.setTextColor(Color.parseColor("#D95555"));
					// viewHolder.itemLove.setText(data.get(position).getLove()
					// + "");
					return;
				}
				data.get(position).setLove(data.get(position).getLove() + 1);
				viewHolder.itemLove.setTextColor(Color.parseColor("#D95555"));
				viewHolder.itemLove.setText(data.get(position).getLove() + "");

				data.get(position).increment("love", 1);
				if (data.get(position).isMyFav()) {
					data.get(position).setMyFav(false);
				}
				data.get(position).update(mContext, new UpdateListener() {

					@Override
					public void onSuccess() {
						data.get(position).setMyLove(true);
						data.get(position).setMyFav(oldFav);
						DatabaseManager.getInstance(mContext).insertFav(data.get(position));
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						data.get(position).setMyLove(true);
						data.get(position).setMyFav(oldFav);
					}
				});
			}
		});

		return convertView;
	}

	private void onClickFav(View v, final CommunityItem communityItem) {
		User user = BmobUser.getCurrentUser(mContext, User.class);
		if (user != null && user.getSessionToken() != null) {
			BmobRelation favRelaton = new BmobRelation();

			communityItem.setMyFav(!communityItem.isMyFav());
			if (communityItem.isMyFav()) {
				((ImageView) v).setImageResource(R.drawable.ic_action_fav_choose);
				favRelaton.add(communityItem);
				user.setFavorite(favRelaton);
				user.update(mContext, new UpdateListener() {

					@Override
					public void onSuccess() {
						ToastUtil.show(mContext, "收藏成功。");
						DatabaseManager.getInstance(mContext).insertFav(communityItem);
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						ToastUtil.show(mContext, "收藏失败。请检查网络~" + arg0);
					}
				});

			} else {
				((ImageView) v).setImageResource(R.drawable.ic_action_fav_normal);
				favRelaton.remove(communityItem);
				user.setFavorite(favRelaton);
				ToastUtil.show(mContext, "取消收藏。");
				user.update(mContext, new UpdateListener() {

					@Override
					public void onSuccess() {
						DatabaseManager.getInstance(mContext).deleteFav(communityItem);
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						ToastUtil.show(mContext, "取消收藏失败。请检查网络~" + arg0);
					}
				});
			}

		} else {
			// 前往登录注册界面
			ToastUtil.show(mContext, "收藏前请先登录。");
			Intent intent = new Intent();
			intent.setClass(mContext, LoginActivity.class);
			mContext.startActivity(intent);
		}
	}

	public static class ViewHolder {
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
