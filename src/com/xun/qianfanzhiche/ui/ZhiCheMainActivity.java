package com.xun.qianfanzhiche.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.xun.qianfanzhiche.R;
import com.xun.qianfanzhiche.adapter.HomePagerAdapter;
import com.xun.qianfanzhiche.anim.ZoomOutPageTransformer;
import com.xun.qianfanzhiche.fragment.MainFragment;

/**
 * 主Activity
 * 
 * @author xunwang
 * 
 *         2015-10-22
 */
public class ZhiCheMainActivity extends FragmentActivity {

	private ViewPager viewPager;
	public TabHost tabHost;
	private MainFragment mainFragment, mainFragment1, mainFragment2, mainFragment3;

	private List<Map<String, View>> tabViews = new ArrayList<Map<String, View>>();
	private List<Fragment> fragments = new ArrayList<Fragment>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();
		initTabHost();
		initFragment();
		initViewPager();
	}

	private void initView() {
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("千帆知车");
		viewPager = (ViewPager) findViewById(R.id.jazzyPager);
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
	}

	private void initTabHost() {
		tabHost.setup();
		tabHost.addTab(tabHost.newTabSpec("0").setIndicator(createTab("主页", 0)).setContent(android.R.id.tabcontent));
		tabHost.addTab(tabHost.newTabSpec("1").setIndicator(createTab("统计", 1)).setContent(android.R.id.tabcontent));
		tabHost.addTab(tabHost.newTabSpec("2").setIndicator(createTab("消息", 2)).setContent(android.R.id.tabcontent));
		tabHost.addTab(tabHost.newTabSpec("3").setIndicator(createTab("设置", 3)).setContent(android.R.id.tabcontent));
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				int index = Integer.parseInt(tabId);
				setTabSelectedState(index, 4);
				tabHost.getTabContentView().setVisibility(View.GONE);// 隐藏content
			}
		});
		tabHost.setCurrentTab(0);
	}

	/**
	 * 动态创建 TabWidget 的Tab项,并设置normalLayout的alpha为1，selectedLayout的alpha为0[显示normal，隐藏selected]
	 * 
	 * @param tabLabelText
	 * @param tabIndex
	 * @return
	 */
	private View createTab(String tabLabelText, int tabIndex) {
		View tabIndicator = LayoutInflater.from(this).inflate(R.layout.main_tabwidget_layout, null);
		TextView normalTV = (TextView) tabIndicator.findViewById(R.id.normalTV);
		TextView selectedTV = (TextView) tabIndicator.findViewById(R.id.selectedTV);
		normalTV.setText(tabLabelText);
		selectedTV.setText(tabLabelText);
		ImageView normalImg = (ImageView) tabIndicator.findViewById(R.id.normalImg);
		ImageView selectedImg = (ImageView) tabIndicator.findViewById(R.id.selectedImage);
		switch (tabIndex) {
		case 0:
			normalImg.setImageResource(R.drawable.scan_book);
			selectedImg.setImageResource(R.drawable.scan_book_hl);
			break;
		case 1:
			normalImg.setImageResource(R.drawable.scan_qr);
			selectedImg.setImageResource(R.drawable.scan_qr_hl);
			break;
		case 2:
			normalImg.setImageResource(R.drawable.scan_street);
			selectedImg.setImageResource(R.drawable.scan_street_hl);
			break;
		case 3:
			normalImg.setImageResource(R.drawable.scan_word);
			selectedImg.setImageResource(R.drawable.scan_word_hl);
			break;
		}
		View normalLayout = tabIndicator.findViewById(R.id.normalLayout);
		normalLayout.setAlpha(1f);// 透明度显示
		View selectedLayout = tabIndicator.findViewById(R.id.selectedLayout);
		selectedLayout.setAlpha(0f);// 透明的隐藏
		Map<String, View> map = new HashMap<String, View>();
		map.put("normal", normalLayout);
		map.put("selected", selectedLayout);
		tabViews.add(map);
		return tabIndicator;
	}

	/**
	 * 设置tab状态选中
	 * 
	 * @param index
	 */
	private void setTabSelectedState(int index, int tabCount) {
		for (int i = 0; i < tabCount; i++) {
			if (i == index) {
				tabViews.get(i).get("normal").setAlpha(0f);
				tabViews.get(i).get("selected").setAlpha(1f);
			} else {
				tabViews.get(i).get("normal").setAlpha(1f);
				tabViews.get(i).get("selected").setAlpha(0f);
			}
		}
		viewPager.setCurrentItem(index, false);// false表示 代码切换 pager 的时候不带scroll动画
	}

	@Override
	protected void onResume() {
		super.onResume();
		setTabSelectedState(tabHost.getCurrentTab(), 4);
	}

	private void initFragment() {
		mainFragment = new MainFragment();
		fragments.add(mainFragment);
		mainFragment1 = new MainFragment();
		fragments.add(mainFragment1);
		mainFragment2 = new MainFragment();
		fragments.add(mainFragment2);
		mainFragment3 = new MainFragment();
		fragments.add(mainFragment3);
	}

	private void initViewPager() {
		viewPager.setAdapter(new HomePagerAdapter(getSupportFragmentManager(), fragments));
		viewPager.setPageMargin(30);
		viewPager.addOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				tabHost.setCurrentTab(position);
			}

			@Override
			public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2) {
			}

			@Override
			public void onPageScrollStateChanged(int paramInt) {
			}
		});
		viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
	}

}
