package com.websharp.activity.business;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.websharp.activity.BaseActivity;
import com.websharp.dao.EntityBillRecord;
import com.websharp.dao.EntityCustomer;
import com.websharp.dao.EntityCustomerOrder;
import com.websharp.dao.EntityPayAmountRecord;
import com.websharp.dao.EntityPlayRecord;
import com.websharp.data.AppUtil;
import com.websharp.data.GlobalData;
import com.websharp.http.AsyncHttpCallBack;
import com.websharp.http.SwzfHttpHandler;
import com.websharp.stb.R;
import com.websharp.widget.PullRefreshListView;
import com.websharputil.common.ConvertUtil;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;
import com.websharputil.date.DateUtil;

public class ActivityCustomerListSearch extends BaseActivity {
	LinearLayout layout_back;
	TextView tv_title;
	PullRefreshListView lv_info;
	AdapterCustomer adapterCustomer;
	EditText et_search_keyword_name;
	EditText et_search_keyword_cust_cert;
	EditText et_search_keyword_cust_code;
	EditText et_search_keyword_cust_address;
	EditText et_search_keyword_cust_mobile;
	Button btn_search;
	boolean isRefresh = true;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_back:
			finish();
			break;
		case R.id.btn_search:
			if (getText(et_search_keyword_name).isEmpty() && getText(et_search_keyword_cust_cert).isEmpty()
					&& getText(et_search_keyword_cust_code).isEmpty()
					&& getText(et_search_keyword_cust_address).isEmpty()
					&& getText(et_search_keyword_cust_mobile).isEmpty()) {
				Util.createToast(this, "至少要输入一个查询条件", Toast.LENGTH_SHORT).show();
				return;
			}
			lv_info.pull2RefreshManually();
			break;
		}
	}

	@Override
	public void initLayout() {
		setContentView(R.layout.activity_list_customer_search);
	}

	@Override
	public void init() {
		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		lv_info = (PullRefreshListView) findViewById(R.id.lv_info);
		et_search_keyword_name = (EditText) findViewById(R.id.et_search_keyword_name);
		et_search_keyword_cust_cert = (EditText) findViewById(R.id.et_search_keyword_cust_cert);
		et_search_keyword_cust_code = (EditText) findViewById(R.id.et_search_keyword_cust_code);
		et_search_keyword_cust_address = (EditText) findViewById(R.id.et_search_keyword_cust_address);
		et_search_keyword_cust_mobile = (EditText) findViewById(R.id.et_search_keyword_cust_mobile);
		btn_search = (Button) findViewById(R.id.btn_search);
	}

	@Override
	public void bindData() {
		layout_back.setOnClickListener(this);
		btn_search.setOnClickListener(this);
		Bundle b = getIntent().getExtras();
		tv_title.setText(b == null ? "客户查询" : b.getString("title"));

		adapterCustomer = new AdapterCustomer(new ArrayList<EntityCustomer>());
		lv_info.setAdapter(adapterCustomer);
		lv_info.setCanLoadMore(false);
		lv_info.setCanRefresh(true);
		lv_info.setAutoLoadMore(false);
		lv_info.setMoveToFirstItemAfterRefresh(true);
		lv_info.setDoRefreshOnUIChanged(false);
		// lv_info.mEndRootView.setVisibility(View.GONE);

		lv_info.setOnRefreshListener(new PullRefreshListView.OnRefreshListener() {
			@Override
			public void onRefresh() {
				adapterCustomer.mList.clear();
				adapterCustomer.notifyDataSetChanged();
				isRefresh = true;
				SwzfHttpHandler.PAGEINDEX_CUSTOMER_LIST_SEARCH = 1;
				JSONObject jsonObj = new JSONObject();
				try {
					if (!getText(et_search_keyword_name).isEmpty()) {
						jsonObj.put("cust_name", URLEncoder.encode(getText(et_search_keyword_name)));
					}
					if (!getText(et_search_keyword_cust_cert).isEmpty()) {
						jsonObj.put("cust_cert_no", URLEncoder.encode(getText(et_search_keyword_cust_cert)));
					}
					if (!getText(et_search_keyword_cust_code).isEmpty()) {
						jsonObj.put("cust_code", URLEncoder.encode(getText(et_search_keyword_cust_code)));
					}
					if (!getText(et_search_keyword_cust_mobile).isEmpty()) {
						jsonObj.put("cont_mobile", URLEncoder.encode(getText(et_search_keyword_cust_mobile)));
					}
					if (!getText(et_search_keyword_cust_address).isEmpty()) {
						jsonObj.put("std_addr_name", URLEncoder.encode(getText(et_search_keyword_cust_address)));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				// new SwzfHttpHandler(cb,
				// ActivityCustomerListSearch.this).callSQL("16",
				// jsonObj.toString(),
				// (SwzfHttpHandler.PAGEINDEX_CUSTOMER_LIST_SEARCH - 1) *
				// SwzfHttpHandler.PAGE_SIZE + 1 + "",
				// SwzfHttpHandler.PAGE_SIZE + "");
				new SwzfHttpHandler(cb, ActivityCustomerListSearch.this).queryCust(jsonObj.toString(),
						SwzfHttpHandler.PAGEINDEX_CUSTOMER_LIST_SEARCH + "");
			}
		});

		lv_info.setOnLoadListener(new PullRefreshListView.OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				isRefresh = false;
				SwzfHttpHandler.PAGEINDEX_CUSTOMER_LIST_SEARCH++;
				// new SwzfHttpHandler(cb, ActivityDbjl.this).callSQL("10",
				// curYear + curMonth + "01" + "-" + curYear + curMonth +
				// curDate,
				// GlobalData.curCustomer.CUST_CODE,
				// (SwzfHttpHandler.PAGEINDEX_DBJL - 1) *
				// SwzfHttpHandler.PAGE_SIZE + 1 + "",
				// SwzfHttpHandler.PAGEINDEX_DBJL * SwzfHttpHandler.PAGE_SIZE +
				// "");
				// new SwzfHttpHandler(cb,
				// ActivityCustomerListSearch.this).callSQL("16",
				// URLEncoder.encode(getText(et_search_keyword_name)) + "|"
				// + URLEncoder.encode(getText(et_search_keyword_cust_cert)) +
				// "|"
				// + URLEncoder.encode(getText(et_search_keyword_cust_code)) +
				// "|"
				// + URLEncoder.encode(getText(et_search_keyword_cust_mobile)) +
				// "|"
				// + URLEncoder.encode(getText(et_search_keyword_cust_address)),
				// (SwzfHttpHandler.PAGEINDEX_CUSTOMER_LIST_SEARCH - 1) *
				// SwzfHttpHandler.PAGE_SIZE + 1 + "",
				// SwzfHttpHandler.PAGEINDEX_CUSTOMER_LIST_SEARCH *
				// SwzfHttpHandler.PAGE_SIZE + "");
				JSONObject jsonObj = new JSONObject();
				try {
					if (!getText(et_search_keyword_name).isEmpty()) {
						jsonObj.put("cust_name", URLEncoder.encode(getText(et_search_keyword_name)));
					}
					if (!getText(et_search_keyword_cust_cert).isEmpty()) {
						jsonObj.put("cust_cert_no", URLEncoder.encode(getText(et_search_keyword_cust_cert)));
					}
					if (!getText(et_search_keyword_cust_code).isEmpty()) {
						jsonObj.put("cust_code", URLEncoder.encode(getText(et_search_keyword_cust_code)));
					}
					if (!getText(et_search_keyword_cust_mobile).isEmpty()) {
						jsonObj.put("cont_mobile", URLEncoder.encode(getText(et_search_keyword_cust_mobile)));
					}
					if (!getText(et_search_keyword_cust_address).isEmpty()) {
						jsonObj.put("std_addr_name", URLEncoder.encode(getText(et_search_keyword_cust_address)));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				new SwzfHttpHandler(cb, ActivityCustomerListSearch.this).queryCust(jsonObj.toString(),
						SwzfHttpHandler.PAGEINDEX_CUSTOMER_LIST_SEARCH + "");
			}
		});

		lv_info.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
				String cust_code = adapterCustomer.mList.get(position - 1).CUST_CODE;
				Bundle b = new Bundle();
				b.putString("customerCode", cust_code);
				Util.startActivity(ActivityCustomerListSearch.this, ActivityCustomerInfo.class, b, false);
			}

		});

	}

	AsyncHttpCallBack cb = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			if (isRefresh) {
				lv_info.onRefreshComplete();
			} else {
				lv_info.onLoadMoreComplete();
			}
			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);
				if (obj.optString("code").equals("0")) {
					Gson gson = new Gson();
					ArrayList<EntityCustomer> listPlay;

					listPlay = gson.fromJson(obj.getJSONArray("data").toString(),
							new com.google.gson.reflect.TypeToken<ArrayList<EntityCustomer>>() {
							}.getType());
					if (listPlay.size() < SwzfHttpHandler.PAGE_SIZE) {
						lv_info.setCanLoadMore(false);
					} else {
						lv_info.setCanLoadMore(true);
					}
					lv_info.changeEndViewByState();
					// if (listPlay.size() < SwzfHttpHandler.PAGE_SIZE) {
					// lv_info.setCanLoadMore(false);
					// } else {
					// lv_info.setCanLoadMore(true);
					// }
					// lv_info.changeEndViewByState();
					if (isRefresh) {
						GlobalData.listCustomer = (ArrayList<EntityCustomer>) listPlay.clone();
					} else {
						GlobalData.listCustomer.addAll(listPlay);
					}

					adapterCustomer.mList = GlobalData.listCustomer;
					adapterCustomer.notifyDataSetChanged();
				} else {
					Util.createToast(ActivityCustomerListSearch.this, obj.optString("message"), 3000).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(String message) {
			super.onFailure(message);
			LogUtil.d("%s", message);
			if (isRefresh) {
				lv_info.onRefreshComplete();
			} else {
				lv_info.onLoadMoreComplete();
			}
		}

	};

	private static class ViewHolder {
		private TextView tv_customer_code;
		private TextView tv_customer_name;
		private TextView tv_customer_address;
		private LinearLayout layout_order_outer;
	}

	class AdapterCustomer extends BaseAdapter {
		ArrayList<EntityCustomer> mList;
		private LayoutInflater mInflater;

		public AdapterCustomer(ArrayList<EntityCustomer> list) {
			this.mList = list;
		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (getCount() == 0)
				return null;
			ViewHolder holder = null;

			if (mInflater == null) {
				mInflater = LayoutInflater.from(ActivityCustomerListSearch.this);
			}
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.item_list_customer, null);
				holder = new ViewHolder();
				holder.layout_order_outer = (LinearLayout) convertView.findViewById(R.id.layout_order_outer);
				holder.tv_customer_code = (TextView) convertView.findViewById(R.id.tv_customer_code);
				holder.tv_customer_name = (TextView) convertView.findViewById(R.id.tv_customer_name);
				holder.tv_customer_address = (TextView) convertView.findViewById(R.id.tv_customer_address);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv_customer_code.setText(mList.get(position).CUST_CODE);
			holder.tv_customer_name.setText(mList.get(position).CUST_NAME);
			// holder.tv_customer_address
			// .setText(mList.get(position).REGION_NAME1 +
			// AppUtil.GetNullValue(mList.get(position).REGION_NAME2)
			// + AppUtil.GetNullValue(mList.get(position).REGION_NAME3)
			// + AppUtil.GetNullValue(mList.get(position).LOUDONG)
			// + AppUtil.GetNullValue(mList.get(position).DOOR_DESC));
			holder.tv_customer_address.setText(mList.get(position).STD_ADDR_NAME);
			String c = mList.get(position).C;
			// Util.createToast(ActivityCustomerListSearch.this, c,
			// 1500).show();
			if (c == null || c.isEmpty() || ConvertUtil.ParsetStringToInt32(c, 0) <= 0) {
				// Util.createToast(ActivityCustomerListSearch.this, "设置为灰色",
				// 1500).show();
				holder.layout_order_outer.setBackgroundColor(getResources().getColor(R.color.color_border));
			}

			return convertView;
		}
	}

}
