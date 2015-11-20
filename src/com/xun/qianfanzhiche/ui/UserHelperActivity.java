package com.xun.qianfanzhiche.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.ListView;

import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.adapter.LevelIntroAdapter;
import com.xun.qianfanzhiche.base.BaseActivity;

public class UserHelperActivity extends BaseActivity {
	private ListView levelIntroList;
	private LevelIntroAdapter levelIntroAdapter;

	private List<String> listData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_helper);

		initView();
	}

	private void initView() {
		setActionBarTitle("帮助中心");
		levelIntroList = (ListView) findViewById(R.id.level_intro_list);
		listData = new ArrayList<String>();
		for (int i = 0; i < 15; i++) {
			listData.add(String.valueOf(i));
		}
		levelIntroAdapter = new LevelIntroAdapter(getApplicationContext(), listData);
		levelIntroList.setAdapter(levelIntroAdapter);
	}
}
