package com.xun.qianfanzhiche.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.xun.qianfanzhiche.R;

public class ItemBar extends FrameLayout {
	private TextView itemBarTitle, itemBarContent, itemBarArrow;
	private View topLine;

	public ItemBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public ItemBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public ItemBar(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.view_itembar, this);
		itemBarTitle = (TextView) view.findViewById(R.id.item_bar_title);
		itemBarContent = (TextView) view.findViewById(R.id.item_bar_content);
		itemBarArrow = (TextView) view.findViewById(R.id.item_bar_arrow);
		topLine = (View) view.findViewById(R.id.item_bar_top_line);
	}

	public void setTopLineVisible() {
		topLine.setVisibility(View.VISIBLE);
	}

	public void setItemBarTitle(String string) {
		itemBarTitle.setText(string);
	}

	public void setItemBarContent(String string) {
		itemBarContent.setText(string);
	}

}
