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
public class FragmentCustomerUserPackage extends Fragment implements View.OnClickListener {

	LinearLayout layout_order_info_items;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_customer_user_order_package, container, false);
		init(view);

		return view;

	}

	private void init(View view) {

		layout_order_info_items = (LinearLayout) view.findViewById(R.id.layout_order_info_items);
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
		for (int k = 0; k < GlobalData.listCustomerPackage.size(); k++) {
			View itemOrder = mInFlater.inflate(R.layout.item_customer_order, null);

			TextView tv_order_offer_name = (TextView) itemOrder.findViewById(R.id.tv_order_offer_name);

			TextView tv_order_offer_srvpkg_name = (TextView) itemOrder.findViewById(R.id.tv_order_offer_srvpkg_name);
			LinearLayout layout_order_outer = (LinearLayout) itemOrder.findViewById(R.id.layout_order_outer);
			LinearLayout layout_split = (LinearLayout) itemOrder.findViewById(R.id.layout_split);
			if (k == GlobalData.listCustomerPackage.size() - 1) {
				layout_split.setVisibility(View.GONE);
			}
			LinearLayout layout_order_detail_contaimer = (LinearLayout) itemOrder
					.findViewById(R.id.layout_order_detail_contaimer);
			tv_order_offer_name.setText(GlobalData.listCustomerPackage.get(k).OFFER_NAME);
			tv_order_offer_srvpkg_name.setText(GlobalData.listCustomerPackage.get(k).SRVPKG_NAME);
			// if (k % 2 == 1) {
			// itemOrder.setBackgroundColor(getResources().getColor(
			// android.R.color.white));
			// }
			// 套餐ID OFFER_ID
			// 套餐名称 OFFER_NAME
			// 产品ID SRVPKG_ID
			// 产品名称 SRVPKG_NAME
			// 类型 不需要
			// 状态 OS_STATUS
			// 生效日期 VALID_DATE
			// 失效日期 EXPIRE_DATE
			// 最后受理日期 DONE_DATE
			// 受理营业厅 ORGANIZE_NAME
			// 操作员 STAFF_NAME
			//
			// layout_order_detail_contaimer
			// .addView(AppUtil.getKeyValueView("套餐ID",
			// GlobalData.listCustomerPackage.get(k).OFFER_ID, mInFlater));
			// layout_order_detail_contaimer.addView(
			// AppUtil.getKeyValueView("套餐名称",
			// GlobalData.listCustomerPackage.get(k).OFFER_NAME, mInFlater));
			// layout_order_detail_contaimer
			// .addView(AppUtil.getKeyValueView("产品ID",
			// GlobalData.listCustomerPackage.get(k).SRVPKG_ID, mInFlater));
			// layout_order_detail_contaimer.addView(
			// AppUtil.getKeyValueView("产品名称",
			// GlobalData.listCustomerPackage.get(k).SRVPKG_NAME, mInFlater));
			layout_order_detail_contaimer.addView(
					AppUtil.getKeyValueView("操作人", GlobalData.listCustomerPackage.get(k).STAFF_NAME, mInFlater));
			layout_order_detail_contaimer.addView(
					AppUtil.getKeyValueView("营业厅", GlobalData.listCustomerPackage.get(k).ORGANIZE_NAME, mInFlater));
			String valueOsStatus = "";
			if (GlobalData.listCustomerPackage.get(k).OS_STATUS != null) {
				char[] statusArr = GlobalData.listCustomerPackage.get(k).OS_STATUS.toCharArray();

				for (int i = 0; i < statusArr.length; i++) {
					if (i > 0)
						valueOsStatus += ",";
					valueOsStatus += GlobalData.GetStaticDataName("OS_STATUS", String.valueOf(statusArr[i]));
				}
			}
			layout_order_detail_contaimer.addView(
					AppUtil.getKeyValueView("状态", valueOsStatus.equals("-") ? "正常" : valueOsStatus, mInFlater));
			layout_order_detail_contaimer.addView(
					AppUtil.getKeyValueView("生效日期", GlobalData.listCustomerPackage.get(k).VALID_DATE, mInFlater));
			if (GlobalData.listCustomerPackage.get(k).END_DATE != null
					&& !GlobalData.listCustomerPackage.get(k).END_DATE.equals("null")
					&& !GlobalData.listCustomerPackage.get(k).END_DATE.isEmpty()) {
				layout_order_detail_contaimer.addView(AppUtil.getKeyValueViewLast("失效日期",
						GlobalData.listCustomerPackage.get(k).END_DATE, mInFlater));
			} else {
				layout_order_detail_contaimer.addView(AppUtil.getKeyValueViewLast("失效日期",
						GlobalData.listCustomerPackage.get(k).EXPIRE_DATE, mInFlater));
			}
			// layout_order_detail_contaimer
			// .addView(AppUtil.getKeyValueView("最后受理日期",
			// GlobalData.listCustomerPackage.get(k).DONE_DATE, mInFlater));
			// layout_order_detail_contaimer
			// .addView(AppUtil.getKeyValueView("受理营业厅",
			// GlobalData.listCustomerPackage.get(k).ORGANIZE_NAME, mInFlater));
			// layout_order_detail_contaimer
			// .addView(AppUtil.getKeyValueView("操作员",
			// GlobalData.listCustomerPackage.get(k).STAFF_NAME, mInFlater));
			layout_order_outer.setOnClickListener(this);
			layout_order_info_items.addView(itemOrder);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_order_outer:
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
