package com.websharp.activity.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.reflect.TypeToken;
import com.websharp.activity.business.ActivityCustomerUserInfo;
import com.websharp.activity.business.ActivityWorkLog;
import com.websharp.stb.R;
import com.websharputil.common.ConvertUtil;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;
import com.websharputil.json.JSONUtils;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class FragmentWorkLogPage extends Fragment implements View.OnClickListener {

	private ViewPager mPageVp;

	private List<Fragment> mFragmentList = new ArrayList<Fragment>();
	private FragmentAdapter mFragmentAdapter;

	private TextView tv_tab_dg, tv_tab_gh, tv_tab_ye;
	private int tagCount = 3;
	/**
	 * Tab的那个引导线
	 */
	private ImageView mTabLineIv;
	/**
	 * Fragment
	 */
	public FragmentWorkLogDg fragmentDg;
	public FragmentWorkLogGh fragmentGh;
	public FragmentWorkLogYe fragmentYe;

	/**
	 * ViewPager的当前选中页
	 */
	private int currentIndex;

	/**
	 * 屏幕的宽度
	 */
	private int screenWidth;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_work_log_page, container, false);
		init(view);
		initViewPage();
		initTabLineWidth();
		bindData();
		return view;
	}

	private void initViewPage() {
		tv_tab_dg.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mPageVp.setCurrentItem(0, true);
			}
		});
		tv_tab_gh.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mPageVp.setCurrentItem(1, true);
			}
		});
		tv_tab_ye.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mPageVp.setCurrentItem(2, true);
			}
		});

		fragmentDg = new FragmentWorkLogDg();
		fragmentGh = new FragmentWorkLogGh();
		fragmentYe = new FragmentWorkLogYe();

		mFragmentList.add(fragmentDg);
		mFragmentList.add(fragmentGh);
		mFragmentList.add(fragmentYe);

		mFragmentAdapter = new FragmentAdapter(
				((ActivityWorkLog) this.getActivity()).getSupportFragmentManager(), mFragmentList);
		mPageVp.setAdapter(mFragmentAdapter);

		mPageVp.setOnPageChangeListener(new OnPageChangeListener() {

			/**
			 * state滑动中的状态 有三种状态（0，1，2） 1：正在滑动 2：滑动完毕 0：什么都没做。
			 */
			@Override
			public void onPageScrollStateChanged(int state) {
				if (state == 2) {
					LogUtil.d(mPageVp.getCurrentItem() + "");
				}
			}

			/**
			 * position :当前页面，及你点击滑动的页面 offset:当前页面偏移的百分比
			 * offsetPixels:当前页面偏移的像素位置
			 */
			@Override
			public void onPageScrolled(int position, float offset, int offsetPixels) {
				LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv.getLayoutParams();

				// Log.e("offset:", currentIndex + " " + position);
				/**
				 * 利用currentIndex(当前所在页面)和position(下一个页面)以及offset来
				 * 设置mTabLineIv的左边距 滑动场景： 记3个页面, 从左到右分别为0,1,2 0->1; 1->2; 2->1;
				 * 1->0
				 */

				if (currentIndex == 0 && position == 0)// 0->1
				{
					lp.leftMargin = (int) (offset * (screenWidth * 1.0 / tagCount)
							+ currentIndex * (screenWidth / tagCount));

				} else if (currentIndex == 1 && position == 0) // 1->0
				{
					lp.leftMargin = (int) (-(1 - offset) * (screenWidth * 1.0 / tagCount)
							+ currentIndex * (screenWidth / tagCount));

				} else if (currentIndex == 1 && position == 1) // 1->2
				{
					lp.leftMargin = (int) (offset * (screenWidth * 1.0 / tagCount)
							+ currentIndex * (screenWidth / tagCount));

				} else if (currentIndex == 2 && position == 1) // 2->1
				{
					lp.leftMargin = (int) (-(1 - offset) * (screenWidth * 1.0 / tagCount)
							+ currentIndex * (screenWidth / tagCount));
				} else if (currentIndex == 2 && position == 2) // 2->2
				{
					lp.leftMargin = (int) (offset * (screenWidth * 1.0 / tagCount)
							+ currentIndex * (screenWidth / tagCount));
				} 
//				else if (currentIndex == 3 && position == 2) // 3->2
//				{
//					lp.leftMargin = (int) (-(1 - offset) * (screenWidth * 1.0 / tagCount)
//							+ currentIndex * (screenWidth / tagCount));
//				} 
//				else if (currentIndex == 3 && position == 3) // 3->3
//				{
//					lp.leftMargin = (int) (offset * (screenWidth * 1.0 / tagCount)
//							+ currentIndex * (screenWidth / tagCount));
//				}

				mTabLineIv.setLayoutParams(lp);
			}

			@Override
			public void onPageSelected(int position) {
				resetTextView();
				switch (position) {
				case 0:
					tv_tab_dg.setTextColor(getResources().getColor(android.R.color.black));
					break;
				case 1:
					tv_tab_gh.setTextColor(getResources().getColor(android.R.color.black));
					break;
				case 2:
					tv_tab_ye.setTextColor(getResources().getColor(android.R.color.black));
					break;
				}
				currentIndex = position;
			}
		});

	}

	/**
	 * 设置滑动条的宽度为屏幕的1/3(根据Tab的个数而定)
	 */
	private void initTabLineWidth() {
		DisplayMetrics dpMetrics = new DisplayMetrics();
		this.getActivity().getWindow().getWindowManager().getDefaultDisplay().getMetrics(dpMetrics);
		screenWidth = dpMetrics.widthPixels;
		screenWidth -= ConvertUtil.dip2px(getActivity(), 20);
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv.getLayoutParams();
		lp.width = screenWidth / tagCount;
		mTabLineIv.setLayoutParams(lp);
	}

	/**
	 * 重置颜色
	 */
	private void resetTextView() {
		tv_tab_dg.setTextColor(getResources().getColor(android.R.color.darker_gray));
		tv_tab_gh.setTextColor(getResources().getColor(android.R.color.darker_gray));
		tv_tab_ye.setTextColor(getResources().getColor(android.R.color.darker_gray));
	}

	private void init(View view) {
		tv_tab_dg = (TextView) view.findViewById(R.id.tv_tab_dg);
		tv_tab_gh = (TextView) view.findViewById(R.id.tv_tab_gh);
		tv_tab_ye = (TextView) view.findViewById(R.id.tv_tab_ye);
		mTabLineIv = (ImageView) view.findViewById(R.id.id_tab_line_iv);
		mPageVp = (ViewPager) view.findViewById(R.id.id_page_vp);
		mPageVp.setOffscreenPageLimit(tagCount);
	}

	private void bindData() {

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// bindData();
	}

	@Override
	public void onStart() {
		super.onStart();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		}
	}

	public void refreshWorkorder() {
		LogUtil.d("%s", "刷新工单111");
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}
}
