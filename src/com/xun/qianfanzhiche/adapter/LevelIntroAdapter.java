package com.xun.qianfanzhiche.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.manager.UserLevelManager;

public class LevelIntroAdapter extends BaseContentAdapter<String> {

	public LevelIntroAdapter(Context context, List<String> list) {
		super(context, list);
	}

	@Override
	public View getConvertView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.level_intro_item, null);
			viewHolder.levelKey = (TextView) convertView.findViewById(R.id.level_intro_key);
			viewHolder.levelValue = (TextView) convertView.findViewById(R.id.level_intro_value);
			viewHolder.levelValue2 = (TextView) convertView.findViewById(R.id.level_intro_value2);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.levelKey.setText("Lv" + (position + 1));
		UserLevelManager.getInstance().formatUserLevel(mContext, viewHolder.levelValue, position + 1, true);
		viewHolder.levelValue2.setText(UserLevelManager.getInstance().getRuleStringList().get(position));
		return convertView;
	}

	public static class ViewHolder {
		public TextView levelKey;
		public TextView levelValue;
		public TextView levelValue2;
	}

}
