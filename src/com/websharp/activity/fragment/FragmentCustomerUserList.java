package com.websharp.activity.fragment;

import java.util.ArrayList;
import java.util.List;

import com.websharp.activity.business.ActivityCustomerUserInfo;
import com.websharp.activity.list.ActivityDdDetail;
import com.websharp.activity.list.ActivityYbjdd;
import com.websharp.dao.EntityCustomerOrder;
import com.websharp.dao.EntityCustomerUser;
import com.websharp.dao.EntityUser;
import com.websharp.data.GlobalData;
import com.websharp.stb.R;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 客户下的用户列表
 * 
 * @author dengzh
 * 
 */
public class FragmentCustomerUserList extends Fragment implements View.OnClickListener {

	LinearLayout layout_customer_list_contaimer;
	TextView tv_title_user_list;
	public String title = "";
	public static ArrayList<EntityCustomerUser> listUser = new ArrayList<EntityCustomerUser>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.widget_customer_list, container, false);
		init(view);
		return view;

	}

	private void init(View view) {
		layout_customer_list_contaimer = (LinearLayout) view.findViewById(R.id.layout_customer_list_contaimer);
		tv_title_user_list = (TextView) view.findViewById(R.id.tv_title_user_list);
	}

	public void bindData() {
		tv_title_user_list.setText(title);
		layout_customer_list_contaimer.removeAllViews();
		// 绑定基本信息
		LayoutInflater mInFlater = LayoutInflater.from(getActivity());

		for (int i = 0; i < listUser.size(); i++) {
			View itemUserList = mInFlater.inflate(R.layout.item_customer_user_list, null);
			LinearLayout layout_bg = (LinearLayout) itemUserList.findViewById(R.id.layout_bg);
			LinearLayout layout_outer = (LinearLayout) itemUserList.findViewById(R.id.layout_outer);
			// TextView tv_name = (TextView) itemUserList
			// .findViewById(R.id.tv_name);
			TextView tv_num = (TextView) itemUserList.findViewById(R.id.tv_num);
			TextView tv_plan = (TextView) itemUserList.findViewById(R.id.tv_plan);
			TextView tv_type = (TextView) itemUserList.findViewById(R.id.tv_type);
			LinearLayout layout_split = (LinearLayout) itemUserList.findViewById(R.id.layout_split);
			// tv_name.setText(GlobalData.listCustomerUser.get(i).USER_NAME);
			tv_num.setText(listUser.get(i).BILL_ID); 
			tv_plan.setText(listUser.get(i).PLAN_NAME);
			tv_type.setText(listUser.get(i).SUBSCRIBER_TYPE);
			if(tv_type.getText().toString().trim().indexOf("宽带")>=0){
				tv_num.setText(listUser.get(i).LOGIN_NAME+"\n密码:"+listUser.get(i).PASSWORD);
			}

			if (i == listUser.size() - 1) {
				layout_split.setVisibility(View.GONE);
			}
			
			if (listUser.get(i).OS_STATUS == null  ||  listUser.get(i).OS_STATUS.trim().equals("null")) {
				
			} else {
				layout_bg.setBackgroundResource(R.color.color_yellow);
			}
			//Util.createToast(getActivity(),i+":"+ listUser.get(i).OS_STATUS, 5000).show();
			layout_outer.setTag(listUser.get(i));
			layout_outer.setOnClickListener(this);
			layout_customer_list_contaimer.addView(itemUserList);
		}
	}

	public void clear() {
		layout_customer_list_contaimer.removeAllViews();
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
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_outer:
			if (title.equals("预安装用户列表")) {
				Bundle b = new Bundle();
				b.putString("curCustomerOrderID", ((EntityCustomerUser) v.getTag()).CUST_ORDER_ID);
				Util.startActivity(getActivity(), ActivityDdDetail.class, b, false);
			} else {
				GlobalData.curCustomerUser = (EntityCustomerUser) v.getTag();
				Util.startActivity(getActivity(), ActivityCustomerUserInfo.class, false);
			}
			break;
		}

	}

}
