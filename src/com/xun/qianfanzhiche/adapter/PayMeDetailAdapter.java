package com.xun.qianfanzhiche.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.bean.QFFoundBean;

public class PayMeDetailAdapter extends BaseContentAdapter<QFFoundBean> {

	public PayMeDetailAdapter(Context context, List<QFFoundBean> list) {
		super(context, list);
	}

	@Override
	public View getConvertView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.pay_me_list_item, null);
			viewHolder.userName = (TextView) convertView.findViewById(R.id.payMeUsername);
			viewHolder.payPrice = (TextView) convertView.findViewById(R.id.payMePrice);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String username = dataList.get(position).getUsername();
		if (dataList.get(position).getUsername().equals("IAmNotLogin")) {
			username = "低调用户";
		}
		viewHolder.userName.setText(username);
		viewHolder.payPrice.setText(dataList.get(position).getPrice() + "元");

		return convertView;
	}

	public static class ViewHolder {
		public TextView userName;
		public TextView payPrice;
	}

}
