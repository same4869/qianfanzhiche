package com.xun.qianfanzhiche.ui;

import java.util.Date;
import java.util.LinkedList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.adapter.TulingRobotAdapter;
import com.xun.qianfanzhiche.base.BaseActivity;
import com.xun.qianfanzhiche.bean.QianFanRobotRecordBean;
import com.xun.qianfanzhiche.bean.TulingRobotBean;
import com.xun.qianfanzhiche.bean.User;
import com.xun.qianfanzhiche.manager.TulingManager;
import com.xun.qianfanzhiche.utils.HttpUtil;
import com.xun.qianfanzhiche.utils.LogUtil;
import com.xun.qianfanzhiche.utils.ToastUtil;

/**
 * 千帆机器人聊天界面
 * 
 * @author xunwang
 * 
 *         2015-12-21
 */
public class QianfanRobotActivity extends BaseActivity implements OnClickListener {
	private ListView mRobotListView;
	private EditText mRobotEdt;
	private Button mRobotBtn;

	private TulingRobotAdapter adapter;
	private TulingAsyncTask tulingAsyncTask;
	private User user;

	private LinkedList<TulingRobotBean> sList = null;
	private LinkedList<TulingRobotBean> beans = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qianfan_robot);
		sList = new LinkedList<TulingRobotBean>();
		beans = new LinkedList<TulingRobotBean>();
		String[] msg = new String[] { "您好，我是刚被您召唤出来的千帆冷冷，你可以问我一些汽车百科，太难的我可不会噢，偷偷告诉你，问其他的其实我也会噢~" };

		// 0 是教师； 1 是学生
		for (int i = 0; i < 1; i++) {
			sList.add(new TulingRobotBean(msg[i], R.drawable.qianfanlengleng, "", 1));
		}

		// 归放到 同一个 类集合Bean中
		for (int j = 0; j < sList.size(); j++) {
			beans.add(sList.get(j));
		}
		initView();
	}

	private void initView() {
		setActionBarTitle("千帆问答");

		mRobotListView = (ListView) findViewById(R.id.robot_lvMessages);
		mRobotEdt = (EditText) findViewById(R.id.robot_edt);
		mRobotBtn = (Button) findViewById(R.id.robot_enter);
		mRobotBtn.setOnClickListener(this);

		adapter = new TulingRobotAdapter(this, beans);
		mRobotListView.setAdapter(adapter);

		user = BmobUser.getCurrentUser(getApplicationContext(), User.class);
	}

	private class TulingAsyncTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			String turingResult = HttpUtil.httpGetUtil(params[0]);
			try {
				// 强行延迟，因为太快了
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return turingResult;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Log.d("kkkkkkkk", "result --> " + result);
			String resString = TulingManager.getInstance().processString(result);
			adapter.addItemNotifiChange(new TulingRobotBean(resString, R.drawable.qianfanlengleng, new Date() + "", 1));
			mRobotListView.setSelection(beans.size() - 1);
			updateRobotMsgToBmob("图灵返回", resString);
		}

	}

	private void updateRobotMsgToBmob(String username, String text) {
		final QianFanRobotRecordBean qianFanRobotRecordBean = new QianFanRobotRecordBean();
		qianFanRobotRecordBean.setUsername(username);
		qianFanRobotRecordBean.setText(text);
		qianFanRobotRecordBean.save(getApplicationContext(), new SaveListener() {

			@Override
			public void onSuccess() {
				LogUtil.d(LogUtil.TAG, "添加图灵数据成功 用户名 --》" + qianFanRobotRecordBean.getUsername() + " 消息 --》" + qianFanRobotRecordBean.getText());
			}

			@Override
			public void onFailure(int code, String arg0) {
				LogUtil.d(LogUtil.TAG, "添加图灵数据失败 code --> " + code + " arg0 --> " + arg0);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.robot_enter:
			String txt = mRobotEdt.getText().toString();
			if (txt == null || txt.equals("")) {
				ToastUtil.show(getApplicationContext(), "内容不要为空噢");
				return;
			}
			if (user != null) {
				updateRobotMsgToBmob(user.getUsername(), txt);
			} else {
				updateRobotMsgToBmob("未登录用户", txt);
			}
			adapter.addItemNotifiChange(new TulingRobotBean(txt, R.drawable.default_photo, new Date() + "", 0));
			mRobotEdt.setText("");
			mRobotListView.setSelection(beans.size() - 1);
			if (tulingAsyncTask != null) {
				tulingAsyncTask.cancel(true);
				tulingAsyncTask = null;
			}
			tulingAsyncTask = new TulingAsyncTask();
			tulingAsyncTask.execute(txt);
			break;

		default:
			break;
		}
	}
}
