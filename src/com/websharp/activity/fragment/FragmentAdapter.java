package com.websharp.activity.fragment;

import java.util.ArrayList;
import java.util.List;

import com.websharputil.common.LogUtil;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

public class FragmentAdapter extends FragmentPagerAdapter {

	List<Fragment> fragmentList = new ArrayList<Fragment>();
	public FragmentAdapter(FragmentManager fm,List<Fragment> fragmentList) {
		super(fm);
		 
		this.fragmentList = fragmentList;
	}

	@Override
	public Fragment getItem(int position) {
		return fragmentList.get(position);
	}


	@Override
	public int getCount() {
		return fragmentList.size();
	}

}
