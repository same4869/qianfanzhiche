package com.xun.qianfanzhiche.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.bean.Comment;
import com.xun.qianfanzhiche.utils.StringUtil;

public class CommentAdapter extends BaseContentAdapter<Comment> {

	public CommentAdapter(Context context, List<Comment> list) {
		super(context, list);
	}

	@Override
	public View getConvertView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.comment_item, null);
			viewHolder.userName = (TextView) convertView.findViewById(R.id.userName_comment);
			viewHolder.commentContent = (TextView) convertView.findViewById(R.id.content_comment);
			viewHolder.index = (TextView) convertView.findViewById(R.id.index_comment);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final Comment comment = dataList.get(position);
		if (comment.getUser() != null) {
			if (!StringUtil.isStringNullorBlank(comment.getUser().getNickName())) {
				viewHolder.userName.setText(comment.getUser().getNickName());
			} else {
				viewHolder.userName.setText(comment.getUser().getUsername());
			}
		}
		switch (position) {
		case 0:
			viewHolder.index.setText("沙发");
			break;
		case 1:
			viewHolder.index.setText("板凳");
			break;
		case 2:
			viewHolder.index.setText("地板");
			break;
		default:
			viewHolder.index.setText((position + 1) + "楼");
			break;
		}

		viewHolder.commentContent.setText(comment.getCommentContent());

		return convertView;
	}

	public static class ViewHolder {
		public TextView userName;
		public TextView commentContent;
		public TextView index;
	}
}
