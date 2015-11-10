package com.xun.qianfanzhiche.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.v3.BmobUser;

import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.base.BaseFragment;
import com.xun.qianfanzhiche.bean.User;

public class UserFragment extends BaseFragment implements OnClickListener {
	private ImageView avaterImg;
	private TextView avaterText;

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_user, null);
		avaterImg = (ImageView) root.findViewById(R.id.avater_img);
		avaterText = (TextView) root.findViewById(R.id.avater_text);
		loadData();
		return root;
	}

	private void loadData() {
		User user = BmobUser.getCurrentUser(getActivity(), User.class);
		avaterText.setText(user.getUsername());
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
