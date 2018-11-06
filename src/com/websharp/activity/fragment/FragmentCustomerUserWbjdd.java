package com.websharp.activity.fragment;

import com.websharp.activity.BaseActivity;
import com.websharp.activity.business.ActivityCustomerUserInfo;
import com.websharp.activity.list.ActivityDdDetail;
import com.websharp.activity.list.ActivityYbjdd;
import com.websharp.data.AppUtil;
import com.websharp.data.GlobalData;
import com.websharp.stb.R;
import com.websharputil.common.ConvertUtil;
import com.websharputil.common.Util;
import com.websharputil.date.DateUtil;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * 客户基本信息
 * 
 * @author dengzh
 * 
 */
public class FragmentCustomerUserWbjdd extends Fragment implements View.OnClickListener{


	LinearLayout layout_order_info_items;
	Spinner sp_year;
	public String curYear = GlobalData.GetYearList().get(0);
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_customer_user_order_order, container, false);
		init(view);

		return view;

	}

	private void init(View view) {
		sp_year = (Spinner)view.findViewById(R.id.sp_year);
		layout_order_info_items = (LinearLayout)view.findViewById(R.id.layout_order_info_items);
		ArrayAdapter adapterSource = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
				GlobalData.GetYearList());
		adapterSource.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_year.setAdapter(adapterSource);
		sp_year.setSelection(0, true);
		sp_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				curYear = GlobalData.GetYearList().get(position);
				((ActivityCustomerUserInfo)getActivity()).searchCustomerWbjdd();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
	}

	public void bindData() {
		// 绑定基本信息
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void bindCustomerOrder() {
		// // 订购列表
		layout_order_info_items.removeAllViews();
		LayoutInflater mInFlater = LayoutInflater.from(getActivity());

		for (int k = 0; k < GlobalData.listCustomerUserWbjdd.size(); k++) {
			View itemOrder = mInFlater.inflate(R.layout.item_bjdd, null);

			TextView tv_order_staff_name = (TextView) itemOrder.findViewById(R.id.tv_order_offer_name);
			TextView tv_order_org_name = (TextView) itemOrder.findViewById(R.id.tv_order_org_name);
			TextView tv_business = (TextView) itemOrder.findViewById(R.id.tv_business);
			TextView tv_create_date = (TextView) itemOrder.findViewById(R.id.tv_create_date);
			LinearLayout layout_order_outer = (LinearLayout) itemOrder.findViewById(R.id.layout_order_outer);
			LinearLayout layout_split = (LinearLayout)itemOrder.findViewById(R.id.layout_split);
			if(k == GlobalData.listCustomerUserWbjdd.size()-1){
				layout_split.setVisibility(View.GONE);
			}
			
			tv_order_staff_name.setText(GlobalData.listCustomerUserLsdd.get(k).STAFF_NAME);
			tv_order_org_name.setText(GlobalData.listCustomerUserLsdd.get(k).ORGANIZE_NAME);
			tv_business.setText(GlobalData.listCustomerUserWbjdd.get(k).BUSI_NAME);
			tv_create_date.setText(DateUtil.TimeParseStringToFormatString(GlobalData.listCustomerUserWbjdd.get(k).CREATE_DATE,"yyyy-MM-dd"));
			layout_order_outer.setTag(k+"");
			layout_order_outer.setOnClickListener(this);
			layout_order_info_items.addView(itemOrder);
		}

	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.layout_order_outer:
			Bundle b = new Bundle();
			b.putString("year", "_f_" + curYear);
			GlobalData.curCustomerOrder = GlobalData.listCustomerUserWbjdd.get(Integer.parseInt(v.getTag().toString()));
			Util.startActivity(getActivity(), ActivityDdDetail.class, b,false);
			break;
		}
	}
	


	public void clear() {


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

}
