package com.xun.qianfanzhiche.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnSystemUiVisibilityChangeListener;
import android.view.WindowManager;
import android.widget.Toast;
import cn.bmob.v3.BmobUser;

import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.adapter.HomePagerAdapter;
import com.xun.qianfanzhiche.app.ZhiCheApp;
import com.xun.qianfanzhiche.base.BaseFragmentActivity;
import com.xun.qianfanzhiche.bean.User;
import com.xun.qianfanzhiche.fragment.CommunityFragment;
import com.xun.qianfanzhiche.fragment.ExtendFragment;
import com.xun.qianfanzhiche.fragment.MainFragment;
import com.xun.qianfanzhiche.fragment.UserFragment;
import com.xun.qianfanzhiche.utils.ToastUtil;
import com.xun.qianfanzhiche.view.CustomRadioGroup;
import com.xun.qianfanzhiche.view.CustomRadioGroup.OnItemChangedListener;
import com.xun.qianfanzhiche.view.ZhiCheActionBar;
import com.xun.qianfanzhiche.view.ZhiCheActionBar.ActionBarListener;

/**
 * 主Activity
 * 
 * @author xunwang
 * 
 *         2015-10-22
 */
public class ZhiCheMainActivity extends BaseFragmentActivity implements ActionBarListener, OnSystemUiVisibilityChangeListener {

	private ViewPager viewPager;
	private ZhiCheActionBar zhiCheActionBar;
	private ActionBarTopInterface actionBarTopInterface;
	private CustomRadioGroup footer;

	private List<Fragment> fragments = new ArrayList<Fragment>();
	private Boolean is2CallBack = false;// 是否双击退出
	private int[] itemImage = { R.drawable.basic_spades_d, R.drawable.basic_heart_d, R.drawable.basic_clubs_d, R.drawable.basic_diamonds_d };
	private int[] itemCheckedImage = { R.drawable.basic_spades_h, R.drawable.basic_heart_h, R.drawable.basic_clubs_h, R.drawable.basic_diamonds_h };
	private String[] itemText = { "品牌", "社区", "扩展", "个人" };

	private static final int CAMERA_WITH_DATA = 10000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStatusBarTransparent();
		setContentView(R.layout.activity_main);
		ZhiCheApp.getInstance().addActivity(this);

		initView();
		initFragment();
		initViewPager();
	}

	private void initView() {
		viewPager = (ViewPager) findViewById(R.id.main_body_viewpager);
		footer = (CustomRadioGroup) findViewById(R.id.main_footer);
		for (int i = 0; i < itemImage.length; i++) {
			footer.addItem(itemImage[i], itemCheckedImage[i], itemText[i]);
		}
		zhiCheActionBar = (ZhiCheActionBar) findViewById(R.id.actionbar);
		zhiCheActionBar.setOnActionBarListener(this);
		zhiCheActionBar.getBackImg().setVisibility(View.GONE);
		zhiCheActionBar.setTitle("汽车品牌");
		zhiCheActionBar.getaddImg().setVisibility(View.VISIBLE);
	}

	private void initFragment() {
		MainFragment mainFragment = new MainFragment();
		fragments.add(mainFragment);
		CommunityFragment communityFragment = new CommunityFragment();
		fragments.add(communityFragment);
		ExtendFragment extendFragment = new ExtendFragment();
		fragments.add(extendFragment);
		UserFragment userFragment = new UserFragment();
		fragments.add(userFragment);
	}

	private void initViewPager() {
		viewPager.setAdapter(new HomePagerAdapter(getSupportFragmentManager(), fragments));
		viewPager.setPageMargin(30);
		viewPager.addOnPageChangeListener(new MyOnPageChangeListener(footer));
		viewPager.setOffscreenPageLimit(3);
		// viewPager.setPageTransformer(true, new DepthPageTransformer());

		footer.setCheckedIndex(viewPager.getCurrentItem());
		footer.setOnItemChangedListener(new OnItemChangedListener() {
			public void onItemChanged() {
				viewPager.setCurrentItem(footer.getCheckedIndex(), false);
			}
		});
		/**
		 * BUG :显示不出数字。数字尺寸太大
		 */
		// footer.setItemNewsCount(0, 10);// 设置消息数量
		// footer.setItemNewsCount(1, 99);// 设置消息数量
		// footer.setItemNewsCount(2, 10);// 设置消息数量
		// footer.setItemNewsCount(3, 1);// 设置消息数量
	}

	private void setStatusBarTransparent() {

		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			// 托盘重叠显示在Activity上
			View decorView = getWindow().getDecorView();
			int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
			decorView.setSystemUiVisibility(uiOptions);
			decorView.setOnSystemUiVisibilityChangeListener(this);
			// 设置托盘透明
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		} else {
		}

	}

	public class MyOnPageChangeListener implements OnPageChangeListener {
		private CustomRadioGroup customRadioGroup;

		public MyOnPageChangeListener(CustomRadioGroup c) {
			this.customRadioGroup = c;
		}

		@Override
		public void onPageSelected(int position) {
			switch (position) {
			case 0:
				zhiCheActionBar.setTitle("汽车品牌");
				zhiCheActionBar.setAddImgResource(R.drawable.recog_photo);
				zhiCheActionBar.setAddImgType(0);
				break;
			case 1:
				zhiCheActionBar.setTitle("知车社区");
				zhiCheActionBar.setAddImgResource(R.drawable.basic_todolist_pen);
				zhiCheActionBar.setAddImgType(1);
				break;
			case 2:
				zhiCheActionBar.setTitle("扩展功能");
				zhiCheActionBar.setAddImgResource(R.drawable.recog_photo);
				zhiCheActionBar.setAddImgType(0);
				break;
			case 3:
				zhiCheActionBar.setTitle("个人中心");
				zhiCheActionBar.setAddImgResource(R.drawable.recog_photo);
				zhiCheActionBar.setAddImgType(0);
				break;
			default:
				break;
			}
			customRadioGroup.setCheckedIndex(position);
		}

		@Override
		public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2) {
			if (paramFloat != 0.0f) {
				int right, left;
				if (paramInt1 == customRadioGroup.getCheckedIndex()) {
					// 方向向右
					left = customRadioGroup.getCheckedIndex();
					right = customRadioGroup.getCheckedIndex() + 1;
				} else {
					// 方向向左
					left = customRadioGroup.getCheckedIndex() - 1;
					right = customRadioGroup.getCheckedIndex();

				}
				customRadioGroup.itemChangeChecked(left, right, paramFloat);
			}
		}

		@Override
		public void onPageScrollStateChanged(int paramInt) {
		}

	}

	// 选项卡底部新消息提醒
	public void setItemsNewCount(int indexPage, int count) {
		footer.setItemNewsCount(indexPage, count);
	}

	@Override
	public void onBackImgClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAddImgClick(int type) {
		// Intent intent = new Intent(ZhiCheMainActivity.this, RadarActivity.class);
		// startActivity(intent);
		// return;
		if (type == 0) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent, CAMERA_WITH_DATA);
		} else if (type == 1) {
			User user = BmobUser.getCurrentUser(this, User.class);
			if (user != null) {
				Intent intent = new Intent(this, CommunityNewPublishActivity.class);
				startActivity(intent);
			} else {
				Intent intent = new Intent(this, LoginActivity.class);
				startActivity(intent);
				ToastUtil.show(getApplicationContext(), "您还没有登录噢，请先登录~");
			}
		}
	}

	@Override
	public void onTextTvClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!is2CallBack) {
				is2CallBack = true;
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						is2CallBack = false;
					}
				}, 2500);

			} else {
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		}
		return true;
	}

	@Override
	public void onActionBarTopClick() {
		actionBarTopInterface.onActionBarTopClick();
	}

	public void setActionBarTopInterface(ActionBarTopInterface actionBarTopInterface) {
		this.actionBarTopInterface = actionBarTopInterface;
	}

	public interface ActionBarTopInterface {
		public void onActionBarTopClick();
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg2 == null) {
			return;
		}
		switch (arg0) {
		case CAMERA_WITH_DATA:
			Intent intent = new Intent(ZhiCheMainActivity.this, RadarActivity.class);
			intent.putExtra("cameraData", arg2.getParcelableExtra("data"));
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	@Override
	public void onSystemUiVisibilityChange(int visibility) {
		// TODO Auto-generated method stub

	}

}
