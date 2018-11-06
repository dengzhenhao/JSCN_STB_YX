package com.websharp.activity.business;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.websharp.activity.BaseActivity;
import com.websharp.activity.fragment.FragmentCustomerBaseInfo;
import com.websharp.activity.fragment.FragmentCustomerUserList;
import com.websharp.activity.list.ActivityAccountInfo;
import com.websharp.activity.list.ActivityCzjl;
import com.websharp.activity.list.ActivityDbjl;
import com.websharp.activity.list.ActivityWbjdd;
import com.websharp.activity.list.ActivityXfzd;
import com.websharp.activity.list.ActivityYbjdd;
import com.websharp.activity.qr.CaptureActivity;
import com.websharp.activity.user.ActivityLogin;
import com.websharp.dao.EntityCustomer;
import com.websharp.dao.EntityCustomerUser;
import com.websharp.dao.EntityOffer;
import com.websharp.dao.EntityProduct;
import com.websharp.dao.EntityStaticData;
import com.websharp.data.Constant;
import com.websharp.data.GlobalData;
import com.websharp.http.AsyncHttpCallBack;
import com.websharp.http.SwzfHttpHandler;
import com.websharp.stb.R;
import com.websharputil.common.AppData;
import com.websharputil.common.LogUtil;
import com.websharputil.common.PrefUtil;
import com.websharputil.common.Util;
import com.websharputil.json.JSONUtils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityCustomerInfo extends BaseActivity {
	LinearLayout layout_back;
	TextView tv_title;
//	LinearLayout layout_info_search;

	FragmentCustomerBaseInfo frag_customer_base_info;
	FragmentCustomerUserList frag_customer_user_list;
	FragmentCustomerUserList frag_customer_user_list_preinstall;
	
	LinearLayout layout_ybjdd;
	LinearLayout layout_wbjdd;
	LinearLayout layout_zhxx;
	LinearLayout layout_czjl;
	LinearLayout layout_dbjl;
	LinearLayout layout_xfzd;

	String customerCode = "";

	@Override
	public void onClick(View v) {
		Bundle b = new Bundle();
		if (v.getTag() != null) {
			b.putString("title", v.getTag().toString());
		}
		switch (v.getId()) {
//		case R.id.layout_info_search:
//			Util.startActivity(this, ActivitySearchCustomerInfo.class, false);
//			break;
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
		setContentView(R.layout.activity_customer_info);
	}

	@Override
	public void init() {
		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
//		layout_info_search = (LinearLayout) findViewById(R.id.layout_info_search);
		frag_customer_base_info = (FragmentCustomerBaseInfo) getFragmentManager()
				.findFragmentById(R.id.frag_customer_base_info);
		frag_customer_user_list = (FragmentCustomerUserList) getFragmentManager()
				.findFragmentById(R.id.frag_customer_user_list);
		frag_customer_user_list_preinstall = (FragmentCustomerUserList) getFragmentManager()
				.findFragmentById(R.id.frag_customer_user_list_preinstall);
		
		layout_ybjdd = (LinearLayout) findViewById(R.id.layout_ybjdd);
		layout_wbjdd = (LinearLayout) findViewById(R.id.layout_wbjdd);
		layout_zhxx = (LinearLayout)findViewById(R.id.layout_zhxx);
		layout_czjl = (LinearLayout) findViewById(R.id.layout_czjl);
		layout_dbjl = (LinearLayout) findViewById(R.id.layout_dbjl);
		layout_xfzd = (LinearLayout) findViewById(R.id.layout_xfzd);
	}

	@Override
	public void bindData() {
		
		for(int i = 0; i<GlobalData.listClientConfig.size();i++){
			//01订购
			//02,余额
			//03,换设备
			String code = GlobalData.listClientConfig.get(i).ParamTypeCode.trim();
			String value =  GlobalData.listClientConfig.get(i).ParamValue.trim();
			if(code.equals("03")){
				if(value.equals("1")){
					layout_zhxx.setVisibility(View.VISIBLE);
				}else{
					layout_zhxx.setVisibility(View.INVISIBLE);
				}
			}
		} 
		
		Bundle b = getIntent().getExtras();
		customerCode = b.getString("customerCode", "");
		baseSetOnClickListener(this, layout_back, layout_ybjdd, layout_wbjdd,layout_zhxx, layout_czjl, layout_dbjl, layout_xfzd);
		tv_title.setText("客户信息");
		frag_customer_user_list.title = "用户列表";
		frag_customer_user_list_preinstall.title = "预安装用户列表";
		searchCustomer();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void searchCustomer() {
		new SwzfHttpHandler(cb, this).callSQL("1", customerCode);
	}

	private void searchCustomerUserList(String cust_id) {
		new SwzfHttpHandler(cb2, this).callSQL("2", cust_id);
		new SwzfHttpHandler(cb3, this).callSQL("18", cust_id);
	}

	AsyncHttpCallBack cb = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				frag_customer_base_info.clear();
				frag_customer_user_list.clear();
				frag_customer_user_list_preinstall.clear();
				obj = new JSONObject(response);
				if (obj.optInt("code") == 0) {
					JSONArray arrResult = obj.getJSONObject("data").getJSONArray("Result");
					if (arrResult.length() > 0) {
						GlobalData.curCustomer = JSONUtils.fromJson(arrResult.getJSONObject(0).toString(),
								EntityCustomer.class);
					} else {
						Util.createToast(ActivityCustomerInfo.this, "找不到对应的客户信息", Toast.LENGTH_SHORT).show();
						GlobalData.curCustomer = new EntityCustomer();
						finish();
					}
					
					
					String str_hisstory_customer_code = PrefUtil.getPref(ActivityCustomerInfo.this, "history_customer_code",
							"");
					if (!GlobalData.listHistoryCustomerCode.contains(customerCode+"["+GlobalData.curCustomer.CUST_NAME+"]")) {
						GlobalData.listHistoryCustomerCode.add(0, customerCode+"["+GlobalData.curCustomer.CUST_NAME+"]");
						if (GlobalData.listHistoryCustomerCode.size() > 5) {
							GlobalData.listHistoryCustomerCode.remove(5);
						}
						str_hisstory_customer_code = "";
						for (int i = 0; i < GlobalData.listHistoryCustomerCode.size(); i++) {
							if (i > 0) {
								str_hisstory_customer_code += "####";
							}
							str_hisstory_customer_code += GlobalData.listHistoryCustomerCode.get(i);
						}
						PrefUtil.setPref(ActivityCustomerInfo.this, "history_customer_code", str_hisstory_customer_code);
					}
					
					frag_customer_base_info.bindData();
//					layout_info_search.setEnabled(true);
//					layout_info_search.setBackgroundResource(R.drawable.bg_btn_system);
					searchCustomerUserList(GlobalData.curCustomer.CUST_ID);
				} else {
//					layout_info_search.setEnabled(false);
//					layout_info_search.setBackgroundResource(R.drawable.bg_btn_disabled);
					Util.createToast(ActivityCustomerInfo.this, obj.optString("message"), 3000).show();
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
					GlobalData.listCustomerUser = gson.fromJson(obj.getJSONObject("data").getString("Result"),
							new com.google.gson.reflect.TypeToken<ArrayList<EntityCustomerUser>>() {
							}.getType());
					frag_customer_user_list.listUser = GlobalData.listCustomerUser;
					frag_customer_user_list.bindData();
				} else {
					Util.createToast(ActivityCustomerInfo.this, obj.optString("message"), 3000).show();
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
					GlobalData.listCustomerUserPreInstall = gson.fromJson(obj.getJSONObject("data").getString("Result"),
							new com.google.gson.reflect.TypeToken<ArrayList<EntityCustomerUser>>() {
							}.getType());

					frag_customer_user_list.listUser = GlobalData.listCustomerUserPreInstall;
					FragmentTransaction transaction = getFragmentManager().beginTransaction();
					if (GlobalData.listCustomerUserPreInstall.size() > 0) {
						transaction.show(frag_customer_user_list_preinstall);
						frag_customer_user_list_preinstall.bindData();
					} else {
						transaction.hide(frag_customer_user_list_preinstall);
						// frag_customer_user_list_preinstall.
					}
					transaction.commit();
				} else {
					Util.createToast(ActivityCustomerInfo.this, obj.optString("message"), 3000).show();
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
