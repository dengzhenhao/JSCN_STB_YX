package com.websharp.activity.list;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.websharp.activity.BaseActivity;
import com.websharp.activity.business.ActivityCustomerUserInfo;
import com.websharp.activity.fragment.FragmentChangeDevice;
import com.websharp.activity.fragment.FragmentOrderPackage;
import com.websharp.activity.fragment.FragmentOrderProduct;
import com.websharp.dao.EntityCustomerOrder;
import com.websharp.dao.EntityCustomerOrderFee;
import com.websharp.dao.EntityCustomerOrderProduct;
import com.websharp.dao.EntityCustomerOrderResource;
import com.websharp.dao.EntityCustomerPackage;
import com.websharp.dao.EntityCustomerResource;
import com.websharp.data.AppUtil;
import com.websharp.data.Constant;
import com.websharp.data.GlobalData;
import com.websharp.http.AsyncHttpCallBack;
import com.websharp.http.SwzfHttpHandler;
import com.websharp.stb.R;
import com.websharputil.common.ConvertUtil;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActivityDdDetail extends BaseActivity {

	LinearLayout layout_back;
	TextView tv_title;
	LinearLayout layout_order_info_items;
	LinearLayout layout_resource_info_items;
	LinearLayout layout_dd_items;
	LinearLayout layout_fee_info_items;
	LinearLayout layout_product, layout_resource, layout_fee;
	String year = null;
	String curCustomerOrderID = null;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_back:
			finish();
			break;
		case R.id.layout_order_outer:
			setItemVisibility(v);
			break;
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

	@Override
	public void initLayout() {
		setContentView(R.layout.activity_dd_detail);
	}

	@Override
	public void init() {

		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		layout_dd_items = (LinearLayout) findViewById(R.id.layout_dd_items);
		layout_order_info_items = (LinearLayout) findViewById(R.id.layout_order_info_items);
		layout_resource_info_items = (LinearLayout) findViewById(R.id.layout_resource_info_items);
		layout_fee_info_items = (LinearLayout) findViewById(R.id.layout_fee_info_items);
		layout_product = (LinearLayout) findViewById(R.id.layout_product);
		layout_resource = (LinearLayout) findViewById(R.id.layout_resource);
		layout_fee = (LinearLayout) findViewById(R.id.layout_fee);
	}

	@Override
	public void bindData() {
		layout_back.setOnClickListener(this);

		Bundle b = getIntent().getExtras();
		if (b != null) {
			year = b.getString("year", null);
			curCustomerOrderID = b.getString("curCustomerOrderID", null);
		}
		tv_title.setText("订单详情");
		if (curCustomerOrderID == null) {
			bindDdDetail();
		}else{
			GlobalData.curCustomerOrder = new EntityCustomerOrder();
			GlobalData.curCustomerOrder.CUST_ORDER_ID = curCustomerOrderID;
			searchCustomerOrder();
		}
		searchCustomerOrderProduct();
		searchCustomerOrderResource();
		searchCustomerOrderFee();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		GlobalData.curCustomerOrder = null;
	}

	private void bindDdDetail() {
		LayoutInflater mInFlater = LayoutInflater.from(this);
		layout_dd_items.removeAllViews();
		layout_dd_items
				.addView(AppUtil.getKeyValueView3("客户订单编号", GlobalData.curCustomerOrder.CUST_ORDER_ID, mInFlater));
		// layout_dd_items.addView(AppUtil.getKeyValueView3("订单ID",
		// GlobalData.curCustomerOrder.PROD_ORDER_ID, mInFlater));
		layout_dd_items.addView(AppUtil.getKeyValueView3("受理业务", GlobalData.curCustomerOrder.BUSI_NAME, mInFlater));
		layout_dd_items.addView(AppUtil.getKeyValueView3("营业厅", GlobalData.curCustomerOrder.ORGANIZE_NAME, mInFlater));
		layout_dd_items.addView(AppUtil.getKeyValueView3("操作员", GlobalData.curCustomerOrder.STAFF_NAME, mInFlater));
		layout_dd_items.addView(AppUtil.getKeyValueView3("发展人", GlobalData.curCustomerOrder.DEV_NAME, mInFlater));
		layout_dd_items.addView(AppUtil.getKeyValueView3("付款方式",
				GlobalData.GetStaticDataName("PAY_TYPE", GlobalData.curCustomerOrder.PAY_TYPE), mInFlater));
		layout_dd_items.addView(AppUtil.getKeyValueView3("创建日期", GlobalData.curCustomerOrder.CREATE_DATE, mInFlater));
		layout_dd_items.addView(AppUtil.getKeyValueView3("备注", GlobalData.curCustomerOrder.REMARK, mInFlater));
		layout_dd_items.addView(AppUtil.getKeyValueView3Last("状态", GlobalData.curCustomerOrder.ORDER_STATE, mInFlater));
		// 客户订单编号 CUST_ORDER_ID
		// 订单ID PROD_ORDER_ID
		// 受理业务 BUSINESS
		// 营业厅 ORGANIZE_NAME
		// 操作员 STAFF_NAME
		// 发展人 DEV_NAME
		// 付款方式 PAY_TYPE
		// 创建日期 CREATE_DATE
		// 备注 REMARK
		// 状态 STATUS

	}

	private void bindCustomerOrderProduct() {
		// // 订购列表
		layout_order_info_items.removeAllViews();
		LayoutInflater mInFlater = LayoutInflater.from(this);
		if (GlobalData.listCustomerOrderProduct.size() == 0) {
			layout_product.setVisibility(View.GONE);
		} else {
			layout_product.setVisibility(View.VISIBLE);
		}

		for (int k = 0; k < GlobalData.listCustomerOrderProduct.size(); k++) {
			View itemOrder = mInFlater.inflate(R.layout.item_customer_order, null);

			TextView tv_order_offer_name = (TextView) itemOrder.findViewById(R.id.tv_order_offer_name);

			TextView tv_order_offer_srvpkg_name = (TextView) itemOrder.findViewById(R.id.tv_order_offer_srvpkg_name);
			LinearLayout layout_split = (LinearLayout) itemOrder.findViewById(R.id.layout_split);
			if (k == GlobalData.listCustomerOrderProduct.size() - 1) {
				layout_split.setVisibility(View.GONE);
			}
			LinearLayout layout_order_outer = (LinearLayout) itemOrder.findViewById(R.id.layout_order_outer);
			LinearLayout layout_order_detail_contaimer = (LinearLayout) itemOrder
					.findViewById(R.id.layout_order_detail_contaimer);
			tv_order_offer_name.setText(GlobalData.listCustomerOrderProduct.get(k).OFFER_NAME);
			tv_order_offer_srvpkg_name.setText(GlobalData.listCustomerOrderProduct.get(k).SRVPKG_NAME);
			// if (k % 2 == 1) {
			// itemOrder.setBackgroundColor(getResources().getColor(
			// android.R.color.white));
			// }
			// 套餐ID OFFER_ID
			// 套餐名称 OFFER_NAME
			// 产品ID SRVPKG_ID
			// 产品名称 SRVPKG_NAME

			// 状态 OS_STATUS
			// 生效日期 VALID_DATE
			// 失效日期 EXPIRE_DATE
			// 最后受理日期 DONE_DATE
			// 受理营业厅 ORGANIZE_NAME
			// 操作员 STAFF_NAME

			// 套餐ID
			// 套餐名称
			// 产品ID
			// 产品名称
			// 状态
			// 生效日期
			// 失效日期

			// layout_order_detail_contaimer.addView(
			// AppUtil.getKeyValueView("套餐ID",
			// GlobalData.listCustomerOrderProduct.get(k).OFFER_ID, mInFlater));
			// layout_order_detail_contaimer.addView(
			// AppUtil.getKeyValueView("套餐名称",
			// GlobalData.listCustomerOrderProduct.get(k).OFFER_NAME,
			// mInFlater));
			// layout_order_detail_contaimer.addView(
			// AppUtil.getKeyValueView("产品ID",
			// GlobalData.listCustomerOrderProduct.get(k).SRVPKG_ID,
			// mInFlater));
			// layout_order_detail_contaimer.addView(
			// AppUtil.getKeyValueView("产品名称",
			// GlobalData.listCustomerOrderProduct.get(k).SRVPKG_NAME,
			// mInFlater));
			layout_order_detail_contaimer.addView(AppUtil.getKeyValueView("操作方式",
					GlobalData.GetStaticDataName("PRODUCT_STATE", GlobalData.listCustomerOrderProduct.get(k).STATE),
					mInFlater));
			layout_order_detail_contaimer.addView(
					AppUtil.getKeyValueView("生效日期", GlobalData.listCustomerOrderProduct.get(k).VALID_DATE, mInFlater));
			layout_order_detail_contaimer.addView(AppUtil.getKeyValueViewLast("失效日期",
					GlobalData.listCustomerOrderProduct.get(k).EXPIRE_DATE, mInFlater));
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

	private void bindCustomerOrderResource() {
		layout_resource_info_items.removeAllViews();
		LayoutInflater mInFlater = LayoutInflater.from(this);
		if (GlobalData.listCustomerOrderResource.size() == 0) {
			layout_resource.setVisibility(View.GONE);
		} else {
			layout_resource.setVisibility(View.VISIBLE);
		}
		// 资源列表
		for (int k = 0; k < GlobalData.listCustomerOrderResource.size(); k++) {
			View itemResource = mInFlater.inflate(R.layout.item_customer_resource, null);
			TextView tv_resource_code = (TextView) itemResource.findViewById(R.id.tv_resource_code);
			TextView tv_resource_name = (TextView) itemResource.findViewById(R.id.tv_resource_name);
			LinearLayout layout_split = (LinearLayout) itemResource.findViewById(R.id.layout_split);
			if (k == GlobalData.listCustomerOrderResource.size() - 1) {
				layout_split.setVisibility(View.GONE);
			}
			tv_resource_code.setText(GlobalData.listCustomerOrderResource.get(k).RES_EQU_NO);
			tv_resource_name.setText(GlobalData.listCustomerOrderResource.get(k).RES_NAME);

			LinearLayout layout_resource_outer = (LinearLayout) itemResource.findViewById(R.id.layout_resource_outer);
			LinearLayout layout_resource_detail_contaimer = (LinearLayout) itemResource
					.findViewById(R.id.layout_resource_detail_contaimer);

			// 资源型号 RES_CODE
			// 资源名称 RES_NAME
			// 资源编号 RES_EQU_NO
			// 生效日期 VALID_DATE
			// 资源用途 RES_USE_MODE

			// layout_resource_detail_contaimer.addView(
			// AppUtil.getKeyValueView("资源型号",
			// GlobalData.listCustomerOrderResource.get(k).RES_CODE,
			// mInFlater));
			// layout_resource_detail_contaimer.addView(
			// AppUtil.getKeyValueView("资源名称",
			// GlobalData.listCustomerOrderResource.get(k).RES_NAME,
			// mInFlater));
			layout_resource_detail_contaimer.addView(
					AppUtil.getKeyValueView("资源编号", GlobalData.listCustomerOrderResource.get(k).RES_EQU_NO, mInFlater));
			layout_resource_detail_contaimer.addView(
					AppUtil.getKeyValueView("生效日期", GlobalData.listCustomerOrderResource.get(k).VALID_DATE, mInFlater));
			layout_resource_detail_contaimer
					.addView(AppUtil.getKeyValueView("资源用途", GlobalData.GetStaticDataName("RES_USE_MODE",
							GlobalData.listCustomerOrderResource.get(k).RES_USE_MODE), mInFlater));
			layout_resource_detail_contaimer.addView(AppUtil.getKeyValueView("操作方式",
					GlobalData.GetStaticDataName("PRODUCT_STATE", GlobalData.listCustomerOrderResource.get(k).STATE),
					mInFlater)); 

			layout_resource_outer.setOnClickListener(this);
			layout_resource_info_items.addView(itemResource);
		}
	}

	private void bindCustomerOrderFee() {
		layout_fee_info_items.removeAllViews();
		LayoutInflater mInFlater = LayoutInflater.from(this);
		if (GlobalData.listCustomerOrderFee.size() == 0) {
			layout_fee.setVisibility(View.GONE);
		} else {
			layout_fee.setVisibility(View.VISIBLE);
		}
		// 资源列表
		for (int k = 0; k < GlobalData.listCustomerOrderFee.size(); k++) {
			View itemFee = mInFlater.inflate(R.layout.item_order_fee, null);
			TextView tv_price_plan_name = (TextView) itemFee.findViewById(R.id.tv_price_plan_name);
			TextView tv_should_money = (TextView) itemFee.findViewById(R.id.tv_should_money);
			TextView tv_cut_money = (TextView) itemFee.findViewById(R.id.tv_cut_money);
			TextView tv_fact_money = (TextView) itemFee.findViewById(R.id.tv_fact_money);
			LinearLayout layout_split = (LinearLayout) itemFee.findViewById(R.id.layout_split);
			if (k == GlobalData.listCustomerOrderProduct.size() - 1) {
				layout_split.setVisibility(View.GONE);
			}
			tv_price_plan_name.setText(GlobalData.listCustomerOrderFee.get(k).PRICE_PLAN_NAME);
			tv_should_money
					.setText(
							ConvertUtil.ParsetDoubleStringToFormat(
									ConvertUtil.ParsetStringToDouble(
											GlobalData.listCustomerOrderFee.get(k).SHOULD_MONEY, 0) / 100 + "",
									ConvertUtil.FORMAT_DECIMAL_FORMAT_3));
			tv_cut_money.setText(ConvertUtil.ParsetDoubleStringToFormat(
					ConvertUtil.ParsetStringToDouble(GlobalData.listCustomerOrderFee.get(k).CUT_MONEY, 0) / 100 + "",
					ConvertUtil.FORMAT_DECIMAL_FORMAT_3));
			tv_fact_money.setText(ConvertUtil.ParsetDoubleStringToFormat(
					ConvertUtil.ParsetStringToDouble(GlobalData.listCustomerOrderFee.get(k).FACT_MONEY, 0) / 100 + "",
					ConvertUtil.FORMAT_DECIMAL_FORMAT_3));
			layout_fee_info_items.addView(itemFee);
		}
	}
	
	private void searchCustomerOrder(){
		new SwzfHttpHandler(cb4, this).callSQL("19", curCustomerOrderID);
	}

	private void searchCustomerOrderProduct() {
		if (year != null) {
			new SwzfHttpHandler(cb, this).callSQL("7", year, GlobalData.curCustomerOrder.CUST_ORDER_ID);
		} else {
			new SwzfHttpHandler(cb, this).callSQL("7", "", GlobalData.curCustomerOrder.CUST_ORDER_ID);
		}
	}
 
	private void searchCustomerOrderResource() {
		if (year != null) {
			new SwzfHttpHandler(cb2, this).callSQL("8", year, GlobalData.curCustomerOrder.CUST_ORDER_ID);
		} else {
			new SwzfHttpHandler(cb2, this).callSQL("8", "", GlobalData.curCustomerOrder.CUST_ORDER_ID);
		}
	}
 
	private void searchCustomerOrderFee() {
		if (year != null) {
			new SwzfHttpHandler(cb3, this).callSQL("9", year, GlobalData.curCustomerOrder.CUST_ORDER_ID);
		} else {
			new SwzfHttpHandler(cb3, this).callSQL("9", "", GlobalData.curCustomerOrder.CUST_ORDER_ID);
		}
	}

	AsyncHttpCallBack cb = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);

				if (obj.optInt("code") == 0) {
					Gson gson = new Gson();
					GlobalData.listCustomerOrderProduct = gson.fromJson(obj.getJSONObject("data").getString("Result"),
							new com.google.gson.reflect.TypeToken<ArrayList<EntityCustomerOrderProduct>>() {
							}.getType());
					bindCustomerOrderProduct();
				} else {
					Util.createToast(ActivityDdDetail.this, obj.optString("message"), 3000).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(String message) {
			super.onFailure(message);
			LogUtil.d("%s", message);
		}

	};

	AsyncHttpCallBack cb2 = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);

				if (obj.optInt("code") == 0) {
					Gson gson = new Gson();
					GlobalData.listCustomerOrderResource = gson.fromJson(obj.getJSONObject("data").getString("Result"),
							new com.google.gson.reflect.TypeToken<ArrayList<EntityCustomerOrderResource>>() {
							}.getType());
					bindCustomerOrderResource();
				} else {
					Util.createToast(ActivityDdDetail.this, obj.optString("message"), 3000).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(String message) {
			super.onFailure(message);
			LogUtil.d("%s", message);
		}

	};

	AsyncHttpCallBack cb3 = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);

				if (obj.optInt("code") == 0) {
					Gson gson = new Gson();
					GlobalData.listCustomerOrderFee = gson.fromJson(obj.getJSONObject("data").getString("Result"),
							new com.google.gson.reflect.TypeToken<ArrayList<EntityCustomerOrderFee>>() {
							}.getType());
					bindCustomerOrderFee();
				} else {
					Util.createToast(ActivityDdDetail.this, obj.optString("message"), 3000).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(String message) {
			super.onFailure(message);
			LogUtil.d("%s", message);
		}

	};
	
	
	AsyncHttpCallBack cb4 = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);

				if (obj.optInt("code") == 0) {
					Gson gson = new Gson();
					GlobalData.curCustomerOrder = gson.fromJson(obj.getJSONObject("data").getJSONArray("Result").getString(0),
							EntityCustomerOrder.class);
					bindDdDetail();
				} else {
					Util.createToast(ActivityDdDetail.this, obj.optString("message"), 3000).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(String message) {
			super.onFailure(message);
			LogUtil.d("%s", message);
		}

	};

}
