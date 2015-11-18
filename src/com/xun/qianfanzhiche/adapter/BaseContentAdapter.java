package com.xun.qianfanzhiche.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class BaseContentAdapter<T> extends BaseAdapter {
	protected Context mContext;
	protected List<T> dataList;
	protected LayoutInflater mInflater;

	public BaseContentAdapter(Context context, List<T> list) {
		mContext = context;
		dataList = list;
		mInflater = LayoutInflater.from(mContext);
	}

	public List<T> getDataList() {
		return dataList;
	}

	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getConvertView(position, convertView, parent);
	}

	public abstract View getConvertView(int position, View convertView, ViewGroup parent);

}
