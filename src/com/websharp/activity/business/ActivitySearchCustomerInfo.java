package com.websharp.activity.business;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.websharp.activity.BaseActivity;
import com.websharp.activity.fragment.FragmentCustomerUserList;
import com.websharp.activity.list.ActivityAccountInfo;
import com.websharp.activity.list.ActivityCzjl;
import com.websharp.activity.list.ActivityDbjl;
import com.websharp.activity.list.ActivityWbjdd;
import com.websharp.activity.list.ActivityXfzd;
import com.websharp.activity.list.ActivityYbjdd;
import com.websharp.data.GlobalData;
import com.websharp.stb.R;
import com.websharputil.common.Util;

/**
 * 客户基本信息
 * 
 * @author dengzh
 * 
 */
public class ActivitySearchCustomerInfo extends BaseActivity {
	LinearLayout layout_back;
	TextView tv_title;
	LinearLayout layout_ybjdd;
	LinearLayout layout_wbjdd;
	LinearLayout layout_zhxx;
	LinearLayout layout_czjl;
	LinearLayout layout_dbjl;
	LinearLayout layout_xfzd;

	FragmentCustomerUserList frag_customer_user_list;

	@Override
	public void onClick(View v) {
		Bundle b = new Bundle();
		if (v.getTag() != null) {
			b.putString("title", v.getTag().toString());
		}
		switch (v.getId()) {
		case R.id.layout_ybjdd:
			if(GlobalData.curCustomer == null){
				Util.createToast(this, "用户不存在，无法进行此操作", 3000).show();
				return;
			}
			Util.startActivity(this, ActivityYbjdd.class, b, false);
			break;
		case R.id.layout_wbjdd:
			if(GlobalData.curCustomer == null){
				Util.createToast(this, "用户不存在，无法进行此操作", 3000).show();
				return;
			}
			Util.startActivity(this, ActivityWbjdd.class, b, false);
			break;
		case R.id.layout_czjl:
			if(GlobalData.curCustomer == null){
				Util.createToast(this, "用户不存在，无法进行此操作", 3000).show();
				return;
			}
			Util.startActivity(this, ActivityCzjl.class, b, false);
			break;
		case R.id.layout_zhxx:
			if(GlobalData.curCustomer == null){
				Util.createToast(this, "用户不存在，无法进行此操作", 3000).show();
				return;
			}
			Util.startActivity(this, ActivityAccountInfo.class, b, false);
			break;
		case R.id.layout_dbjl:
			if(GlobalData.curCustomer == null){
				Util.createToast(this, "用户不存在，无法进行此操作", 3000).show();
				return;
			}
			Util.startActivity(this, ActivityDbjl.class, b, false);
			break;
		case R.id.layout_xfzd:
			if(GlobalData.curCustomer == null){
				Util.createToast(this, "用户不存在，无法进行此操作", 3000).show();
				return;
			}
			Util.startActivity(this, ActivityXfzd.class, b, false);
			break;
		case R.id.layout_back:
			finish();
			break;
		}
	}

	@Override
	public void initLayout() {
		setContentView(R.layout.activity_search_customer_info);
	}

	@Override
	public void init() {
		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		layout_ybjdd = (LinearLayout) findViewById(R.id.layout_ybjdd);
		layout_wbjdd = (LinearLayout) findViewById(R.id.layout_wbjdd);
		layout_zhxx = (LinearLayout)findViewById(R.id.layout_zhxx);
		layout_czjl = (LinearLayout) findViewById(R.id.layout_czjl);
		layout_dbjl = (LinearLayout) findViewById(R.id.layout_dbjl);
		layout_xfzd = (LinearLayout) findViewById(R.id.layout_xfzd);
		frag_customer_user_list = (FragmentCustomerUserList) getFragmentManager()
				.findFragmentById(R.id.frag_customer_user_list);

	}

	@Override
	public void bindData() {
		baseSetOnClickListener(this, layout_back, layout_ybjdd, layout_wbjdd,layout_zhxx, layout_czjl, layout_dbjl, layout_xfzd);
		frag_customer_user_list.bindData();
		tv_title.setText("客户信息查询");
	}

}
