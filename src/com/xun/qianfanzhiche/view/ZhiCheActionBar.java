package com.xun.qianfanzhiche.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xun.qianfanzhiche.R;

public class ZhiCheActionBar extends FrameLayout implements OnClickListener {
	private RelativeLayout actionbarLayout;
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
		actionbarLayout = (RelativeLayout) view.findViewById(R.id.actionbar_layout);
		backImg = (ImageView) view.findViewById(R.id.actionbar_back);
		addImg = (ImageView) view.findViewById(R.id.actionbar_add);
		titleTv = (TextView) view.findViewById(R.id.actionbar_title);
		textTv = (TextView) view.findViewById(R.id.actionbar_text);
		actionbarLayout.setOnClickListener(this);
		backImg.setOnClickListener(this);
		addImg.setOnClickListener(this);
		textTv.setOnClickListener(this);
	}

	public ImageView getBackImg() {
		return backImg;
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
	
	public void setAddImgResource(int resourceId){
		addImg.setImageResource(resourceId);
	}

	public interface ActionBarListener {
		void onBackImgClick();

		void onAddImgClick();

		void onTextTvClick();

		void onActionBarTopClick();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.actionbar_back:
			if (actionBarListener != null) {
				actionBarListener.onBackImgClick();
			}
			break;
		case R.id.actionbar_add:
			if (actionBarListener != null) {
				actionBarListener.onAddImgClick();
			}
			break;
		case R.id.actionbar_text:
			if (actionBarListener != null) {
				actionBarListener.onTextTvClick();
			}
			break;
		case R.id.actionbar_layout:
			if (actionBarListener != null) {
				actionBarListener.onActionBarTopClick();
			}
			break;
		default:
			break;
		}
	}
}
