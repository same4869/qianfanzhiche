package com.xun.qianfanzhiche.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.xun.qianfanzhiche.R;

public class ZhiCheActionBar extends FrameLayout {
	private ImageView backImg, addImg;
	private TextView titleTv, textTv;

	private ActionBarListener actionBarListener;

	public ZhiCheActionBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context, attrs);
	}

	public ZhiCheActionBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context, attrs);
	}

	public ZhiCheActionBar(Context context) {
		super(context);
		initView(context, null);
	}

	private void initView(Context context, AttributeSet attrs) {
		View view = LayoutInflater.from(context).inflate(R.layout.view_actionbar, this);
		backImg = (ImageView) view.findViewById(R.id.actionbar_back);
		addImg = (ImageView) view.findViewById(R.id.actionbar_add);
		titleTv = (TextView) view.findViewById(R.id.actionbar_title);
		textTv = (TextView) view.findViewById(R.id.actionbar_text);
		backImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (actionBarListener != null) {
					actionBarListener.onBackImgClick();
				}
			}
		});
		addImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (actionBarListener != null) {
					actionBarListener.onAddImgClick();
				}
			}
		});
		textTv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (actionBarListener != null) {
					actionBarListener.onTextTvClick();
				}
			}
		});
	}

	public void setOnActionBarListener(ActionBarListener actionBarListener) {
		this.actionBarListener = actionBarListener;
	}

	public void setTitle(String string) {
		titleTv.setText(string);
	}

	public void setTextString(String string) {
		addImg.setVisibility(View.GONE);
		textTv.setVisibility(View.VISIBLE);
		textTv.setText(string);
	}

	public interface ActionBarListener {
		void onBackImgClick();

		void onAddImgClick();

		void onTextTvClick();
	}
}
