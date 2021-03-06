package com.xun.qianfanzhiche.ui;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.adapter.PayMeDetailAdapter;
import com.xun.qianfanzhiche.base.BaseActivity;
import com.xun.qianfanzhiche.bean.QFFoundBean;
import com.xun.qianfanzhiche.manager.PayManager;
import com.xun.qianfanzhiche.utils.ApkUtil;
import com.xun.qianfanzhiche.utils.BmobUtil;
import com.xun.qianfanzhiche.utils.LogUtil;
import com.xun.qianfanzhiche.utils.StringUtil;
import com.xun.qianfanzhiche.utils.ZhiCheSPUtil;
import com.xun.qianfanzhiche.view.TypeTextView;
import com.xun.qianfanzhiche.view.TypeTextView.OnTypeViewListener;

public class PayMeActivity extends BaseActivity implements OnClickListener {
	private static final int NUMBERS_PER_PAGE = 30;// 榜单1次拉30条
	private static final int PRE_LOAD_OFFSET = 3;// 拉到距离底部多少条的时候加载下一页

	private Button payAliBtn, payWxBtn;
	private PayMeDetailAdapter payMeDetailAdapter, payMeDetailAdapter2;
	private ListView payListView, payListView2;
	private MyScrollListener myScrollListener;
	private MyScrollListener2 myScrollListener2;
	private TypeTextView paymeTipTv1, paymeTipTv2;
	private WebView paymeWebView;
	private ProgressBar progressBar;

	private String username, orderId;
	// 1都是最新榜，2都是感谢榜
	private List<QFFoundBean> datas = new ArrayList<QFFoundBean>();
	private List<QFFoundBean> datas2 = new ArrayList<QFFoundBean>();

	private ViewPager mPager;// 页卡内容
	private List<View> listViews; // Tab页面列表
	private ImageView cursor;// 动画图片
	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int bmpW;// 动画图片宽度

	private int pageNum = 0;
	private int pageNum2 = 0;
	private int start, end;
	private boolean isCleared, isAllLoaded, isAllLoaded2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay);

		initImageView();
		initView();
		initViewPager();
		initData();
		initData2();
	}

	private void initImageView() {
		cursor = (ImageView) findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.cursor).getWidth();// 获取图片宽度
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		offset = (screenW / 3 - bmpW) / 2;// 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);// 设置动画初始位置
	}

	private void initViewPager() {
		Intent intent = getIntent();
		boolean isLogined = intent.getBooleanExtra("isLogined", false);
		if (isLogined) {
			username = BmobUtil.getCurrentUser(getApplicationContext()).getUsername();
		} else {
			username = "IAmNotLogin";
		}

		mPager = (ViewPager) findViewById(R.id.vPager);
		listViews = new ArrayList<View>();
		LayoutInflater mInflater = getLayoutInflater();
		View mainView = mInflater.inflate(R.layout.view_pay_me_main, null);
		TextView usernameTv = (TextView) mainView.findViewById(R.id.usernameTv);
		if (isLogined) {
			usernameTv.setText("尊敬的" + BmobUtil.getCurrentUser(getApplicationContext()).getUsername() + "，您好！");
		} else {
			usernameTv.setTextColor(Color.RED);
			usernameTv.setText("亲爱的用户，您还没有登录噢，这样打赏会自动命名为“低调用户”噢。");
		}

		paymeWebView = (WebView) mainView.findViewById(R.id.pay_me_web);
		initWebSetting();
		progressBar = (ProgressBar) mainView.findViewById(R.id.progressBar);
		paymeWebView.setWebViewClient(new MyWebViewClient());
		String url = ZhiCheSPUtil.getPaymeWebUrl();
		if (url != null && url.startsWith("htt")) {
			paymeWebView.setVisibility(View.VISIBLE);
			paymeWebView.loadUrl(url);
		}

		paymeTipTv1 = (TypeTextView) mainView.findViewById(R.id.pay_me_tip_1);
		paymeTipTv2 = (TypeTextView) mainView.findViewById(R.id.pay_me_tip_2);
		paymeTipTv1.setOnTypeViewListener(new OnTypeViewListener() {

			@Override
			public void onTypeStart() {

			}

			@Override
			public void onTypeOver() {
				paymeTipTv2.start(getResources().getString(R.string.payme_tip_2));
			}
		});
		paymeTipTv1.start(getResources().getString(R.string.payme_tip_1));
		listViews.add(mainView);

		View payMeListView = mInflater.inflate(R.layout.view_pay_me_list, null);
		payListView = (ListView) payMeListView.findViewById(R.id.pay_info_list);
		payMeDetailAdapter = new PayMeDetailAdapter(getApplicationContext(), datas);
		payListView.setAdapter(payMeDetailAdapter);
		listViews.add(payMeListView);

		View payMeListView2 = mInflater.inflate(R.layout.view_pay_me_list2, null);
		payListView2 = (ListView) payMeListView2.findViewById(R.id.pay_info_list2);
		payMeDetailAdapter2 = new PayMeDetailAdapter(getApplicationContext(), datas);
		payListView2.setAdapter(payMeDetailAdapter2);
		listViews.add(payMeListView2);

		mPager.setAdapter(new MyPagerAdapter(listViews));
		mPager.setCurrentItem(0);
		mPager.addOnPageChangeListener(new MyOnPageChangeListener());

		myScrollListener = new MyScrollListener();
		myScrollListener2 = new MyScrollListener2();
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initWebSetting() {
		WebSettings settings = paymeWebView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
	}

	private class MyWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return super.shouldOverrideUrlLoading(view, url);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			progressBar.setVisibility(View.VISIBLE);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			progressBar.setVisibility(View.GONE);
		}

	}

	// 把所有支持成功的订单号查询出来显示
	private void initData() {
		BmobQuery<QFFoundBean> query = new BmobQuery<QFFoundBean>();
		// 查询playerName叫“比目”的数据
		query.addWhereEqualTo("payStatus", true);
		query.order("-createdAt");
		query.setLimit(NUMBERS_PER_PAGE);
		query.setSkip(NUMBERS_PER_PAGE * (pageNum++));
		// 执行查询方法
		query.findObjects(this, new FindListener<QFFoundBean>() {
			@Override
			public void onSuccess(List<QFFoundBean> list) {
				LogUtil.d(LogUtil.TAG, "打赏成功的条目为 list.size() -->" + list.size());
				if (list.size() != 0 && list.get(list.size() - 1) != null) {
					if (isCleared == false) {
						datas.clear();
						isCleared = true;
					}
					if (list.size() < NUMBERS_PER_PAGE) {
						isAllLoaded = true;
					}
					datas.addAll(list);
					payMeDetailAdapter.setDataList(datas);
					payMeDetailAdapter.notifyDataSetChanged();
					payListView.setOnScrollListener(myScrollListener);
				} else {
					isAllLoaded = true;
					pageNum--;
				}
			}

			@Override
			public void onError(int code, String msg) {
				LogUtil.d(LogUtil.TAG, "打赏 查询失败：" + msg);
				pageNum--;
			}
		});
	}

	// 把所有支持成功的订单号查询出来显示
	private void initData2() {
		BmobQuery<QFFoundBean> query = new BmobQuery<QFFoundBean>();
		// 查询playerName叫“比目”的数据
		query.addWhereEqualTo("payStatus", true);
		query.order("-price");
		query.setLimit(NUMBERS_PER_PAGE);
		query.setSkip(NUMBERS_PER_PAGE * (pageNum2++));
		// 执行查询方法
		query.findObjects(this, new FindListener<QFFoundBean>() {
			@Override
			public void onSuccess(List<QFFoundBean> list) {
				LogUtil.d(LogUtil.TAG, "打赏成功2的条目为 list.size() -->" + list.size());
				if (list.size() != 0 && list.get(list.size() - 1) != null) {
					if (isCleared == false) {
						datas2.clear();
						isCleared = true;
					}
					if (list.size() < NUMBERS_PER_PAGE) {
						isAllLoaded2 = true;
					}
					datas2.addAll(list);
					payMeDetailAdapter2.setDataList(datas2);
					payMeDetailAdapter2.notifyDataSetChanged();
					payListView2.setOnScrollListener(myScrollListener2);
				} else {
					isAllLoaded2 = true;
					pageNum2--;
				}
			}

			@Override
			public void onError(int code, String msg) {
				LogUtil.d(LogUtil.TAG, "打赏2 查询失败：" + msg);
				pageNum2--;
			}
		});
	}

	private void initView() {
		setActionBarTitle("千帆微打赏");
		payAliBtn = (Button) findViewById(R.id.pay_ali_btn);
		payWxBtn = (Button) findViewById(R.id.pay_wx_btn);
		payAliBtn.setOnClickListener(this);
		payWxBtn.setOnClickListener(this);
	}

	private class MyScrollListener implements OnScrollListener {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {

		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			start = firstVisibleItem;
			end = firstVisibleItem + visibleItemCount;
			if (end > totalItemCount - PRE_LOAD_OFFSET && !isAllLoaded) {
				initData();
			}
		}
	}

	private class MyScrollListener2 implements OnScrollListener {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {

		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			start = firstVisibleItem;
			end = firstVisibleItem + visibleItemCount;
			if (end > totalItemCount - PRE_LOAD_OFFSET && !isAllLoaded2) {
				initData2();
			}
		}

	}

	// private void startAliPay(final double price, final String payInfo) {
	// PayManager.getInstance().startAliPay(this, price, payInfo, new
	// PayListener() {
	//
	// @Override
	// public void unknow() {
	// LogUtil.d(LogUtil.TAG, "unknow");
	// }
	//
	// @Override
	// public void succeed() {
	// LogUtil.d(LogUtil.TAG, "succeed");
	// updatePayInfoToBmob(price, payInfo, username, orderId, true, null);
	// }
	//
	// @Override
	// public void orderId(String arg0) {
	// orderId = arg0;
	// LogUtil.d(LogUtil.TAG, "orderId arg0 --> " + arg0);
	// }
	//
	// @Override
	// public void fail(int arg0, String arg1) {
	// LogUtil.d(LogUtil.TAG, "fail arg0 --> " + arg0 + " arg1 --> " + arg1);
	// updatePayInfoToBmob(price, payInfo, username, orderId, false, arg1);
	// }
	// });
	// }
	//
	// private void startWxPay(final double price, final String payInfo) {
	// PayManager.getInstance().startWeixinPay(this, price, payInfo, new
	// PayListener() {
	//
	// @Override
	// public void unknow() {
	// LogUtil.d(LogUtil.TAG, "unknow");
	// }
	//
	// @Override
	// public void succeed() {
	// LogUtil.d(LogUtil.TAG, "succeed");
	// }
	//
	// @Override
	// public void orderId(String arg0) {
	// orderId = arg0;
	// LogUtil.d(LogUtil.TAG, "orderId arg0 --> " + arg0);
	// }
	//
	// @Override
	// public void fail(int arg0, String arg1) {
	// LogUtil.d(LogUtil.TAG, "fail arg0 --> " + arg0 + " arg1 --> " + arg1);
	// // 当code为-2,意味着用户中断了操作
	// // code为-3意味着没有安装BmobPlugin插件
	// if (arg0 == -3) {
	// new
	// AlertDialog.Builder(PayMeActivity.this).setMessage("监测到你尚未安装支付插件,无法进行微信打赏,请选择安装插件(已打包在本地,无流量消耗)还是用支付宝打赏")
	// .setPositiveButton("安装", new DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// ApkUtil.installBmobPayPlugin(getApplicationContext(),
	// "BmobPayPlugin.apk");
	// }
	// }).setNegativeButton("支付宝打赏", new DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// startAliPay(price, payInfo);
	// }
	// }).create().show();
	// }
	// }
	// });
	// }
	//
	// private void orderQuery(String orderId) {
	// PayManager.getInstance().orderQuery(this, orderId, new
	// OrderQueryListener() {
	//
	// @Override
	// public void succeed(String arg0) {
	// LogUtil.d(LogUtil.TAG, "orderId arg0 --> " + arg0);
	// }
	//
	// @Override
	// public void fail(int arg0, String arg1) {
	// LogUtil.d(LogUtil.TAG, "orderId arg0 --> " + arg0 + " arg1 --> " + arg1);
	// }
	// });
	// }

	private void updatePayInfoToBmob(Double price, String payInfo, String username, String orderId, boolean payStatus, String failWhy) {
		final QFFoundBean qFFoundBean = new QFFoundBean();
		qFFoundBean.setUsername(username);
		qFFoundBean.setOrderId(orderId);
		qFFoundBean.setPayStatus(payStatus);
		qFFoundBean.setFailWhy(failWhy);
		qFFoundBean.setPrice(price);
		qFFoundBean.setPayInfo(payInfo);
		qFFoundBean.save(getApplicationContext(), new SaveListener() {

			@Override
			public void onSuccess() {
				LogUtil.d(LogUtil.TAG,
						"添加打赏数据成功 用户名 --》" + qFFoundBean.getUsername() + " 订单号 --》" + qFFoundBean.getOrderId() + " 打赏状态 --》" + qFFoundBean.isPayStatus());
			}

			@Override
			public void onFailure(int code, String arg0) {
				LogUtil.d(LogUtil.TAG, "添加打赏数据失败 code --> " + code + " arg0 --> " + arg0);
			}
		});
	}

	/**
	 * ViewPager适配器
	 */
	public class MyPagerAdapter extends PagerAdapter {
		public List<View> mListViews;

		public MyPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			return mListViews.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
	}

	/**
	 * 头标点击监听
	 */
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mPager.setCurrentItem(index);
		}
	};

	/**
	 * 页卡切换监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		int two = one * 2;// 页卡1 -> 页卡3 偏移量

		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
				}
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, one, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
				}
				break;
			case 2:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, two, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
				}
				break;
			}
			currIndex = arg0;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			cursor.startAnimation(animation);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	public void showPayDialog(final int type) {
		AlertDialog editDialog = new AlertDialog.Builder(PayMeActivity.this).create();
		editDialog.setCanceledOnTouchOutside(false);
		View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_useredit, null);
		editDialog.show();
		editDialog.setContentView(v);
		editDialog.getWindow().setGravity(Gravity.CENTER);
		// 只用下面这一行弹出对话框时需要点击输入框才能弹出软键盘
		editDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		// 加上下面这一行弹出对话框时软键盘随之弹出
		editDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

		TextView titleTv = (TextView) v.findViewById(R.id.user_edit_title);
		final EditText editText = (EditText) v.findViewById(R.id.user_edit_edittext);
		editText.setInputType(InputType.TYPE_CLASS_NUMBER);
		Button button = (Button) v.findViewById(R.id.user_edit_btn_ok);

		titleTv.setText("非常感谢，请输入打赏金额（元）");
		// button.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// String editString = editText.getText().toString();
		// if (StringUtil.isStringNullorBlank(editString) ||
		// editString.equals("0")) {
		// return;
		// }
		// if (type == 0) {
		// startAliPay(Double.parseDouble(editString), "我来自支付宝打赏");
		// } else if (type == 1) {
		// startWxPay(Double.parseDouble(editString), "我来自支付宝打赏");
		// }
		// }
		// });

	}

	@Override
	public void onBackPressed() {
		if (paymeWebView.canGoBack()) {
			paymeWebView.goBack();
			return;
		}
		super.onBackPressed();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.pay_ali_btn:
			showPayDialog(0);
			break;
		case R.id.pay_wx_btn:
			showPayDialog(1);
			// orderQuery("21f944934d43ff2c2a8be5d526ac2063");
			break;
		default:
			break;
		}
	}
}
