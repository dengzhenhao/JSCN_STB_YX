package com.websharp.activity.fragment;

import com.websharp.activity.BaseActivity;
import com.websharp.data.AppUtil;
import com.websharp.data.GlobalData;
import com.websharp.stb.R;
import com.websharputil.common.ConvertUtil;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 客户基本信息
 * 
 * @author dengzh
 * 
 */
public class FragmentCustomerUserResource extends Fragment implements View.OnClickListener{


	LinearLayout layout_resource_info_items;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_customer_user_resource, container, false);
		init(view);
		return view;

	}

	private void init(View view) {
		layout_resource_info_items = (LinearLayout) view.findViewById(R.id.layout_resource_info_items);
	}

	public void bindData() {
		// 绑定基本信息
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void bindCustomerResource() {
		layout_resource_info_items.removeAllViews();
		LayoutInflater mInFlater = LayoutInflater.from(getActivity());
		// 资源列表
		for (int k = 0; k < GlobalData.listCustomerResources.size(); k++) {
			View itemResource = mInFlater.inflate(R.layout.item_customer_resource, null);
			TextView tv_resource_code = (TextView) itemResource.findViewById(R.id.tv_resource_code);
			TextView tv_resource_name = (TextView) itemResource.findViewById(R.id.tv_resource_name);
			LinearLayout layout_split = (LinearLayout)itemResource.findViewById(R.id.layout_split);
			if(k == GlobalData.listCustomerResources.size()-1){
				layout_split.setVisibility(View.GONE);
			}
			tv_resource_code.setText(GlobalData.listCustomerResources.get(k).RES_EQU_NO);
			tv_resource_name.setText(GlobalData.listCustomerResources.get(k).RES_NAME);

			LinearLayout layout_resource_outer = (LinearLayout) itemResource.findViewById(R.id.layout_resource_outer);
			LinearLayout layout_resource_detail_contaimer = (LinearLayout) itemResource
					.findViewById(R.id.layout_resource_detail_contaimer);

			// tv_resource_num.setText(GlobalData.listCustomerResources.get(k).RES_EQU_NO);
			// tv_valid_date.setText(GlobalData.listCustomerResources.get(k).VALID_DATE);
			// tv_user_mode.setText(GlobalData.listCustomerResources.get(k).RES_USE_MODE);
			// if (k % 2 == 1) {
			// itemResource.setBackgroundColor(getResources().getColor(
			// android.R.color.white));
			// }

			// 资源型号 RES_CODE
			// 资源名称 RES_NAME
			// 资源编号 RES_EQU_NO
			// 生效日期 VALID_DATE
			// 资源用途 RES_USE_MODE

//			layout_resource_detail_contaimer.addView(
//					AppUtil.getKeyValueView("资源型号", GlobalData.listCustomerResources.get(k).RES_CODE, mInFlater));
//			layout_resource_detail_contaimer.addView(
//					AppUtil.getKeyValueView("资源名称", GlobalData.listCustomerResources.get(k).RES_NAME, mInFlater));
//			layout_resource_detail_contaimer.addView(
//					AppUtil.getKeyValueView("资源编号", GlobalData.listCustomerResources.get(k).RES_EQU_NO, mInFlater));
			layout_resource_detail_contaimer.addView(
					AppUtil.getKeyValueView("生效日期", GlobalData.listCustomerResources.get(k).VALID_DATE, mInFlater));
			layout_resource_detail_contaimer.addView(AppUtil.getKeyValueViewLast("资源用途",
					GlobalData.GetStaticDataName("RES_USE_MODE", GlobalData.listCustomerResources.get(k).RES_USE_MODE),
					mInFlater));

			layout_resource_outer.setOnClickListener(this);
			layout_resource_info_items.addView(itemResource);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.layout_resource_outer:
			setItemVisibility(v);
			break;
		}
	}
	
	private void setItemVisibility(View v) {
		LinearLayout layout_container = (LinearLayout) ((View) v.getParent()).findViewById(R.id.layout_container);
		if (layout_container.getVisibility() == View.GONE) {
			layout_container.setVisibility(View.VISIBLE);
		} else {
			layout_container.setVisibility(View.GONE);
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
