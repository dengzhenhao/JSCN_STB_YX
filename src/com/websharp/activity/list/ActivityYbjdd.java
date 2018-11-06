package com.websharp.activity.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.websharp.activity.BaseActivity;
import com.websharp.activity.list.ActivityCzjl.AdapterPayAmountRecord;
import com.websharp.dao.EntityCustomer;
import com.websharp.dao.EntityCustomerOrder;
import com.websharp.dao.EntityPayAmountRecord;
import com.websharp.data.AppUtil;
import com.websharp.data.GlobalData;
import com.websharp.http.AsyncHttpCallBack;
import com.websharp.http.SwzfHttpHandler;
import com.websharp.stb.R;
import com.websharp.widget.PullRefreshListView;
import com.websharputil.common.ConvertUtil;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;

public class ActivityYbjdd extends BaseActivity {

	LinearLayout layout_back;
	TextView tv_title;
	PullRefreshListView lv_info;
	AdapterDingDan adapterDingDan; 
	Spinner sp_year;
	String curYear = GlobalData.GetYearList().get(0);
	boolean isRefresh = true;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_back:
			finish();
			break;
		}
	}

	@Override
	public void initLayout() {
		setContentView(R.layout.activity_list_ybjdd);
	}

	@Override
	public void init() {
		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		lv_info = (PullRefreshListView) findViewById(R.id.lv_info);
		sp_year = (Spinner) findViewById(R.id.sp_year);
	}

	@Override
	public void bindData() {
		layout_back.setOnClickListener(this);
		Bundle b = getIntent().getExtras();
		tv_title.setText(b == null ? "" : b.getString("title"));

		adapterDingDan = new AdapterDingDan(new ArrayList<EntityCustomerOrder>());
		lv_info.setAdapter(adapterDingDan);
		lv_info.setCanLoadMore(true);
		lv_info.setCanRefresh(true);
		lv_info.setAutoLoadMore(true);
		lv_info.setMoveToFirstItemAfterRefresh(true);
		lv_info.setDoRefreshOnUIChanged(false);

		ArrayAdapter adapterSource = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				GlobalData.GetYearList());
		adapterSource.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_year.setAdapter(adapterSource);
		sp_year.setSelection(0, true);
		sp_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				curYear = GlobalData.GetYearList().get(position);
				lv_info.pull2RefreshManually();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		lv_info.setOnRefreshListener(new PullRefreshListView.OnRefreshListener() {
			@Override
			public void onRefresh() {
				adapterDingDan.mList.clear();
				adapterDingDan.notifyDataSetChanged();
				isRefresh = true;
				SwzfHttpHandler.PAGEINDEX_YBJDD = 1;
				
				new SwzfHttpHandler(cb, ActivityYbjdd.this).callSQL("6", "_f_" + curYear,
						GlobalData.curCustomer.CUST_CODE,
						(SwzfHttpHandler.PAGEINDEX_YBJDD - 1) * SwzfHttpHandler.PAGE_SIZE + 1 + "",
						SwzfHttpHandler.PAGEINDEX_YBJDD * SwzfHttpHandler.PAGE_SIZE + "");
			}
		});

		lv_info.setOnLoadListener(new PullRefreshListView.OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				isRefresh = false;
				SwzfHttpHandler.PAGEINDEX_YBJDD++;
				new SwzfHttpHandler(cb, ActivityYbjdd.this).callSQL("6", "_f_" + curYear,
						GlobalData.curCustomer.CUST_CODE,
						(SwzfHttpHandler.PAGEINDEX_YBJDD - 1) * SwzfHttpHandler.PAGE_SIZE + 1 + "",
						SwzfHttpHandler.PAGEINDEX_YBJDD * SwzfHttpHandler.PAGE_SIZE + "");
			}
		}); 

		lv_info.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
				// 跳转到订单详情
				Bundle b = new Bundle();
				b.putString("year", "_f_" + curYear);
				GlobalData.curCustomerOrder = GlobalData.listCustomerOrder.get(position-1);
				Util.startActivity(ActivityYbjdd.this, ActivityDdDetail.class, b,false);
			}

		});
		lv_info.pull2RefreshManually();
		
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
					ArrayList<EntityCustomerOrder> listOrder;

					listOrder = gson.fromJson(obj.getJSONObject("data").getString("Result"),
							new com.google.gson.reflect.TypeToken<ArrayList<EntityCustomerOrder>>() {
							}.getType());
					if (listOrder.size() < SwzfHttpHandler.PAGE_SIZE) {
						lv_info.setCanLoadMore(false);
					} else {
						lv_info.setCanLoadMore(true);
					}
					lv_info.changeEndViewByState();
					if (isRefresh) {
						GlobalData.listCustomerOrder = (ArrayList<EntityCustomerOrder>) listOrder.clone();
					} else {
						GlobalData.listCustomerOrder.addAll(listOrder);
					}

					adapterDingDan.mList = GlobalData.listCustomerOrder;
					adapterDingDan.notifyDataSetChanged();
				} else {
					Util.createToast(ActivityYbjdd.this, obj.optString("message"),
							3000).show();
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
		private TextView tv_order_staff_name;
		private TextView tv_order_org_name;
		private TextView tv_business;
		private TextView tv_create_date;
	}

	class AdapterDingDan extends BaseAdapter {
		ArrayList<EntityCustomerOrder> mList;
		private LayoutInflater mInflater;

		public AdapterDingDan(ArrayList<EntityCustomerOrder> list) {
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
				mInflater = LayoutInflater.from(ActivityYbjdd.this);
			}
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.item_bjdd, null);
				holder = new ViewHolder();
				holder.tv_order_staff_name = (TextView) convertView.findViewById(R.id.tv_order_staff_name);
				holder.tv_order_org_name = (TextView) convertView.findViewById(R.id.tv_order_org_name);
				holder.tv_business = (TextView) convertView.findViewById(R.id.tv_business);
				holder.tv_create_date = (TextView) convertView.findViewById(R.id.tv_create_date);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.tv_order_staff_name.setText(mList.get(position).STAFF_NAME);
			holder.tv_order_org_name.setText(mList.get(position).ORGANIZE_NAME);
			holder.tv_business.setText(mList.get(position).BUSI_NAME);
			holder.tv_create_date.setText(mList.get(position).CREATE_DATE);
			return convertView;
		}
	}

}
