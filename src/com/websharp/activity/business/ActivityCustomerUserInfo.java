package com.websharp.activity.business;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings.Global;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.textservice.TextInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.websharp.activity.BaseActivity;
import com.websharp.activity.fragment.FragmentChangeDevice;
import com.websharp.activity.fragment.FragmentOrderPackage;
import com.websharp.activity.fragment.FragmentOrderProduct;
import com.websharp.activity.fragment.FragmentWorkorderPage;
import com.websharp.activity.list.ActivityYbjdd;
import com.websharp.dao.EntityCustomerOrder;
import com.websharp.dao.EntityCustomerPackage;
import com.websharp.dao.EntityCustomerResource;
import com.websharp.dao.EntityCustomerUser;
import com.websharp.data.AppUtil;
import com.websharp.data.Constant;
import com.websharp.data.GlobalData;
import com.websharp.http.AsyncHttpCallBack;
import com.websharp.http.SwzfHttpHandler;
import com.websharp.stb.R;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;

public class ActivityCustomerUserInfo extends FragmentActivity implements View.OnClickListener {

	LinearLayout layout_back;
	TextView tv_title;
	TextView tv_name;
	ImageView iv_refresh;

	LinearLayout layout_order_product;
	LinearLayout layout_change_device;
	LinearLayout layout_send_auth;

	ImageView iv_order_product;
	ImageView iv_change_device;
	ImageView iv_send_auth;

	public FrameLayout container_fragment;
	FragmentOrderPackage fragOrderPackage;
	FragmentOrderProduct fragOrderProduct;
	FragmentChangeDevice fragChangeDevice;

	FragmentWorkorderPage fragPage;

	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub

			if (intent.getAction().equals(Constant.ACTION_REFRESH_CUSTOMER_USER)) {
				searchCustomerOrderPackage();
				searchCustomerResource();
			} else if (intent.getAction().equals(Constant.ACTION_HIDE_FRAGMENT)) {
				container_fragment.performClick();
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_order_product:
			if (GlobalData.curCustomerUser.SUBSCRIBER_TYPE.trim().indexOf("宽带") < 0) {
				showFragment(layout_order_product);
			} else {
				Util.createToast(this, "宽带业务不能进行此操作", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.layout_change_device:
			showFragment(layout_change_device);
			break;
		case R.id.layout_send_auth:
			authStb();
			break;
		case R.id.container_fragment:
			container_fragment.setVisibility(View.GONE);
			break;
		case R.id.layout_back:
			finish();
			break;
		case R.id.iv_refresh:
			searchCustomerOrderPackage();
			searchCustomerResource();
			break;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initLayout();
		init();
		bindData();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	public void initLayout() {
		setContentView(R.layout.activity_customer_user_info);
	}

	public void init() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.ACTION_REFRESH_CUSTOMER_USER);
		filter.addAction(Constant.ACTION_HIDE_FRAGMENT);
		registerReceiver(receiver, filter);
		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_name = (TextView) findViewById(R.id.tv_name);
		iv_refresh = (ImageView) findViewById(R.id.iv_refresh);
		layout_order_product = (LinearLayout) findViewById(R.id.layout_order_product);
		layout_change_device = (LinearLayout) findViewById(R.id.layout_change_device);
		layout_send_auth = (LinearLayout) findViewById(R.id.layout_send_auth);
		iv_order_product = (ImageView) findViewById(R.id.iv_order_product);
		iv_change_device = (ImageView) findViewById(R.id.iv_change_device);
		iv_send_auth = (ImageView) findViewById(R.id.iv_send_auth);
		
		container_fragment = (FrameLayout) findViewById(R.id.container_fragment);
		fragPage = (FragmentWorkorderPage) getSupportFragmentManager()
				.findFragmentById(R.id.frag_customer_user_info_page);
	}

	public void bindData() {
		
		for (int i = 0; i < GlobalData.listClientConfig.size(); i++) {
			// 1订购
			// 2,换设备
			// 3,余额
			int id = GlobalData.listClientConfig.get(i).ID;
			String value = GlobalData.listClientConfig.get(i).ParamValue.trim();
			
			if (id == 1) {
				if (value.equals("1")) {
					iv_order_product.setImageResource(R.drawable.icon_btn_cpdg);
					layout_order_product.setOnClickListener(this);
				} else {
					iv_order_product.setImageResource(R.drawable.icon_btn_cpdg_disabled);
					layout_order_product.setBackgroundResource(R.drawable.bg_btn_disabled);
				}
			} else if (id == 2) {
				if (value.equals("1")) {
					iv_change_device.setImageResource(R.drawable.icon_btn_sbhj);
					layout_change_device.setOnClickListener(this);
				} else {
					iv_change_device.setImageResource(R.drawable.icon_btn_sbhj_disabled);
					layout_order_product.setBackgroundResource(R.drawable.bg_btn_disabled);
				}
			} else if (id == 4) {
				if (value.equals("1")) {
					iv_send_auth.setImageResource(R.drawable.icon_btn_fssq);
					layout_send_auth.setOnClickListener(this);
				} else {
					iv_send_auth.setImageResource(R.drawable.icon_btn_fssq_disabled);
					layout_order_product.setBackgroundResource(R.drawable.bg_btn_disabled);
				}
			}
		}

		iv_refresh.setVisibility(View.VISIBLE);
		baseSetOnClickListener(this, layout_back, container_fragment, iv_refresh);

		tv_title.setText("用户信息及业务办理");
		tv_name.setText("机顶盒号 :" + GlobalData.curCustomerUser.BILL_ID);
		searchCustomerOrderPackage();
		// searchCustomerOrder();
		searchCustomerResource();
	}

	public void baseSetOnClickListener(View.OnClickListener listener, View... views) {
		for (int i = 0; i < views.length; i++) {
			views[i].setOnClickListener(listener);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		GlobalData.curCustomerUser = null;
		unregisterReceiver(receiver);
	}

	public void searchCustomerOrderPackage() {
		new SwzfHttpHandler(cb, this).callSQL("3", GlobalData.curCustomerUser.PROD_INST_ID);
	}

	public void searchCustomerResource() {
		new SwzfHttpHandler(cb2, this).callSQL("4", GlobalData.curCustomerUser.PROD_INST_ID);
	}

	public void searchCustomerLsdd() {
		if (GlobalData.listCustomerResources.size() > 0) {
			new SwzfHttpHandler(cb3, this).callSQL("14", "_f_" + fragPage.fragmentLsdd.curYear,
					GlobalData.listCustomerResources.get(0).RES_EQU_NO);
		}
	}

	public void searchCustomerWbjdd() {
		if (GlobalData.listCustomerResources.size() > 0) {
			new SwzfHttpHandler(cb4, this).callSQL("15", GlobalData.listCustomerResources.get(0).RES_EQU_NO);
		}
	}

	public void authStb() {
		new SwzfHttpHandler(cb5, this).authStb(this, GlobalData.curCustomerUser.PROD_INST_ID);
	}

	AsyncHttpCallBack cb = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			LogUtil.d("订购信息%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);

				if (obj.optInt("code") == 0) {
					Gson gson = new Gson();
					GlobalData.listCustomerPackage = gson.fromJson(obj.getJSONObject("data").getString("Result"),
							new com.google.gson.reflect.TypeToken<ArrayList<EntityCustomerPackage>>() {
							}.getType());
					fragPage.fragmentDg.bindCustomerOrder();
					// bindCustomerOrder();
				} else {
					Util.createToast(ActivityCustomerUserInfo.this, obj.optString("message"), 3000).show();
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
					GlobalData.listCustomerResources = gson.fromJson(obj.getJSONObject("data").getString("Result"),
							new com.google.gson.reflect.TypeToken<ArrayList<EntityCustomerResource>>() {
							}.getType());
					// bindCustomerResource();
					searchCustomerLsdd();
					fragPage.fragmentZyxx.bindCustomerResource();
				} else {
					Util.createToast(ActivityCustomerUserInfo.this, obj.optString("message"), 3000).show();
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
					GlobalData.listCustomerUserLsdd = gson.fromJson(obj.getJSONObject("data").getString("Result"),
							new com.google.gson.reflect.TypeToken<ArrayList<EntityCustomerOrder>>() {
							}.getType());
					// bindCustomerResource();
					fragPage.fragmentLsdd.bindCustomerOrder();
				} else {
					Util.createToast(ActivityCustomerUserInfo.this, obj.optString("message"), 3000).show();
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
					GlobalData.listCustomerUserWbjdd = gson.fromJson(obj.getJSONObject("data").getString("Result"),
							new com.google.gson.reflect.TypeToken<ArrayList<EntityCustomerOrder>>() {
							}.getType());
					// bindCustomerResource();
					fragPage.fragmentLsdd.bindCustomerOrder();
				} else {
					Util.createToast(ActivityCustomerUserInfo.this, obj.optString("message"), 3000).show();
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

	AsyncHttpCallBack cb5 = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {
			super.onSuccess(response);
			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);
				if (obj.optInt("code") == 0) {
					Util.createToast(ActivityCustomerUserInfo.this, R.string.msg_control_success, Toast.LENGTH_LONG)
							.show();
				} else {
					Util.createToast(ActivityCustomerUserInfo.this, obj.optString("message"), 3000).show();
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

	private void showFragment(View pressBtn) {
		FragmentManager fm = getFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();

		switch (pressBtn.getId()) {

		case R.id.layout_order_product:
			// if (fragOrderProduct == null) {
			// fragOrderProduct = new FragmentOrderProduct();
			// }
			fragOrderProduct = new FragmentOrderProduct();
			transaction.replace(R.id.container_fragment, fragOrderProduct);
			break;
		case R.id.layout_change_device:
			if (fragChangeDevice == null) {
				fragChangeDevice = new FragmentChangeDevice();
			}
			transaction.replace(R.id.container_fragment, fragChangeDevice);
			break;
		}

		// transaction.addToBackStack();
		// 事务提交
		transaction.commit();
		container_fragment.setVisibility(View.VISIBLE);
	}
}
