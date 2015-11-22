package com.xun.qianfanzhiche.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.base.BaseFragment;
import com.xun.qianfanzhiche.ui.CommonWebActivity;
import com.xun.qianfanzhiche.ui.LoginActivity;

public class MainFragment extends BaseFragment {
	private Button sign_up_button;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container, false);
		sign_up_button = (Button) rootView.findViewById(R.id.sign_up_button);
		sign_up_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getContext(), CommonWebActivity.class);
				startActivity(intent);
			}
		});
		return rootView;
	}

}
