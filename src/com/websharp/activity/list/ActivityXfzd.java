package com.websharp.activity.list;

import android.os.AsyncTask;
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
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.websharp.activity.BaseActivity;
import com.websharp.activity.business.ActivityCustomerUserInfo;
import com.websharp.activity.business.ActivityMain;
import com.websharp.activity.list.ActivityDbjl.AdapterPlayRecord;
import com.websharp.activity.user.ActivityLogin;
import com.websharp.adapter.AdapterBillRecordList;
import com.websharp.dao.EntityBillRecord;
import com.websharp.dao.EntityCustomerPackage;
import com.websharp.dao.EntityOrg;
import com.websharp.dao.EntityPlayRecord;
import com.websharp.dao.EntityStaticData;
import com.websharp.dao.EntityUser;
import com.websharp.data.AppUtil;
import com.websharp.data.GlobalData;
import com.websharp.http.AsyncHttpCallBack;
import com.websharp.http.AsyncHttpListCallBack;
import com.websharp.http.SwzfHttpHandler;
import com.websharp.stb.R;
import com.websharp.widget.PullRefreshListView;
import com.websharputil.common.ConvertUtil;
import com.websharputil.common.LogUtil;
import com.websharputil.common.PrefUtil;
import com.websharputil.common.Util;
import com.websharputil.json.JSONUtils;

public class ActivityXfzd extends BaseActivity {

	LinearLayout layout_back;
	TextView tv_title;
	PullRefreshListView lv_info;
	AdapterBillRecord adapterBillRecord;
	// ArrayList<String> listExpand = new ArrayList<String>();
	Spinner sp_year, sp_month;
	String curYear = GlobalData.GetYearList().get(0);
	String curMonth = "01";
	String curDate = "31";
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
		setContentView(R.layout.activity_list_xfzd);
	}

	@Override
	public void init() {
		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		lv_info = (PullRefreshListView) findViewById(R.id.lv_info);
		sp_year = (Spinner) findViewById(R.id.sp_year);
		sp_month = (Spinner) findViewById(R.id.sp_month);
		// GlobalData.curCustomer.CUST_CODE = "8512001703471";
	}

	@Override
	public void bindData() {
		layout_back.setOnClickListener(this);
		Bundle b = getIntent().getExtras();
		tv_title.setText(b == null ? "" : b.getString("title"));

		adapterBillRecord = new AdapterBillRecord(new ArrayList<EntityBillRecord>());
		lv_info.setAdapter(adapterBillRecord);
		lv_info.setCanLoadMore(false);
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

		ArrayAdapter adapterSourceMonth = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				GlobalData.GetMonthList());
		adapterSourceMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_month.setAdapter(adapterSourceMonth);
		sp_month.setSelection(0, true);
		sp_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				curMonth = GlobalData.GetMonthList().get(position);
				curMonth = curMonth.length() == 1 ? "0" + curMonth : curMonth;
				String start = curYear + "-" + curMonth + "-01";
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date date;
				try {
					date = sdf.parse(start);
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(date);
					calendar.add(Calendar.MONTH, 1);
					calendar.add(Calendar.DATE, -1);
					curDate = calendar.get(Calendar.DATE) + "";
				} catch (ParseException e) {
					e.printStackTrace();
				}
				lv_info.pull2RefreshManually();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		lv_info.setOnRefreshListener(new PullRefreshListView.OnRefreshListener() {
			@Override
			public void onRefresh() {
				adapterBillRecord.mList.clear();
				adapterBillRecord.notifyDataSetChanged();
				isRefresh = true;
				SwzfHttpHandler.PAGEINDEX_XFJD = 1;
				new SwzfHttpHandler(cb, ActivityXfzd.this).callSQL("12",
						curYear + curMonth + "01" + "-" + curYear + curMonth + curDate + ","
								+ GlobalData.curCustomer.CUST_CODE,
						GlobalData.curCustomer.ACCT_ID.substring(GlobalData.curCustomer.ACCT_ID.length() - 1),
						GlobalData.curCustomer.CUST_CODE,
						(SwzfHttpHandler.PAGEINDEX_XFJD - 1) * SwzfHttpHandler.PAGE_SIZE + 1 + "", 1000 + "");
			}
		});

		// lv_info.setOnLoadListener(new
		// PullRefreshListView.OnLoadMoreListener() {
		//
		// @Override
		// public void onLoadMore() {
		// isRefresh = false;
		// SwzfHttpHandler.PAGEINDEX_XFJD++;
		// new SwzfHttpHandler(cb, ActivityXfzd.this).callSQL("13", curYear,
		// curMonth,
		// GlobalData.curCustomer.CUST_CODE,
		// (SwzfHttpHandler.PAGEINDEX_XFJD - 1) * SwzfHttpHandler.PAGE_SIZE + 1
		// + "",
		// SwzfHttpHandler.PAGEINDEX_XFJD * SwzfHttpHandler.PAGE_SIZE + "");
		// }
		// });

		lv_info.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {

				// LinearLayout layout_container = (LinearLayout)
				// v.findViewById(R.id.layout_container);
				// LogUtil.d("container:%s", position);
				// if (layout_container.getVisibility() == View.GONE) {
				// layout_container.setVisibility(View.VISIBLE);
				// listExpand.add(position - 1 + "");
				// } else {
				// layout_container.setVisibility(View.GONE);
				// listExpand.remove(position - 1 + "");
				// }
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
					ArrayList<EntityBillRecord> listBillRecord;

					listBillRecord = gson.fromJson(obj.getJSONObject("data").getString("Result"),
							new com.google.gson.reflect.TypeToken<ArrayList<EntityBillRecord>>() {
							}.getType());
					// if (listPlay.size() < SwzfHttpHandler.PAGE_SIZE) {
					// lv_info.setCanLoadMore(false);
					// } else {
					// lv_info.setCanLoadMore(true);
					// }
					// lv_info.changeEndViewByState();
					if (isRefresh) {
						GlobalData.listBillRecord = (ArrayList<EntityBillRecord>) listBillRecord.clone();
					} else {
						GlobalData.listBillRecord.addAll(listBillRecord);
					}

					adapterBillRecord.mList = GlobalData.listBillRecord;
					adapterBillRecord.notifyDataSetChanged();
				} else {
					Util.createToast(ActivityXfzd.this, obj.optString("message"), 3000).show();
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
		private TextView tv_acct_item_type;
		private TextView tv_total_amount;
		private TextView tv_billing_cycle_id;
		private TextView tv_unpay_amount;
		private TextView tv_ppy_amount;
		private LinearLayout layout_container;
	}

	class AdapterBillRecord extends BaseAdapter {
		ArrayList<EntityBillRecord> mList;
		private LayoutInflater mInflater;

		public AdapterBillRecord(ArrayList<EntityBillRecord> list) {
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
				mInflater = LayoutInflater.from(ActivityXfzd.this);
			}
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.item_xfzd, null);
				holder = new ViewHolder();

				holder.tv_acct_item_type = (TextView) convertView.findViewById(R.id.tv_acct_item_type);
				holder.tv_total_amount = (TextView) convertView.findViewById(R.id.tv_total_amount);
				holder.tv_billing_cycle_id = (TextView) convertView.findViewById(R.id.tv_billing_cycle_id);
				holder.tv_unpay_amount = (TextView) convertView.findViewById(R.id.tv_unpay_amount);
				holder.tv_ppy_amount = (TextView) convertView.findViewById(R.id.tv_ppy_amount);
				holder.layout_container = (LinearLayout) convertView.findViewById(R.id.layout_container);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv_acct_item_type.setText(mList.get(position).ACCT_ITEM_TYPE);
			holder.tv_total_amount.setText(ConvertUtil.ParsetDoubleStringToFormat(
					(ConvertUtil.ParsetStringToDouble(mList.get(position).UNPAY_AMOUNT, 0)
							+ ConvertUtil.ParsetStringToDouble(mList.get(position).PPY_AMOUNT, 0)) / 100 + "",
					ConvertUtil.FORMAT_DECIMAL_FORMAT_3));
			holder.tv_billing_cycle_id.setText(mList.get(position).BILLING_CYCLE_ID);
			holder.tv_unpay_amount.setText(ConvertUtil.ParsetDoubleStringToFormat(
					ConvertUtil.ParsetStringToDouble(mList.get(position).UNPAY_AMOUNT, 0) / 100 + "",
					ConvertUtil.FORMAT_DECIMAL_FORMAT_3));
			holder.tv_ppy_amount.setText(ConvertUtil.ParsetDoubleStringToFormat(
					ConvertUtil.ParsetStringToDouble(mList.get(position).PPY_AMOUNT, 0) / 100 + "",
					ConvertUtil.FORMAT_DECIMAL_FORMAT_3));
			// holder.layout_container.removeAllViews();
			//
			// holder.layout_container
			// .addView(AppUtil.getKeyValueView("帐单科目",
			// mList.get(position).ACCT_ITEM_TYPE, mInflater));
			// holder.layout_container.addView(AppUtil.getKeyValueView("账单金额",
			// ConvertUtil.ParsetDoubleStringToFormat(
			// ConvertUtil.ParsetStringToInt32(mList.get(position).TOTAL_AMOUNT,
			// 0) / 100 + "",
			// ConvertUtil.FORMAT_DECIMAL_FORMAT_3),
			// mInflater));
			// holder.layout_container.addView(AppUtil.getKeyValueView("费帐期",
			// ConvertUtil.ParsetDoubleStringToFormat(
			// ConvertUtil.ParsetStringToInt32(mList.get(position).BILLING_CYCLE_ID,
			// 0) / 100 + "",
			// ConvertUtil.FORMAT_DECIMAL_FORMAT_3),
			// mInflater));
			// holder.layout_container.addView(AppUtil.getKeyValueView("未销金额",
			// ConvertUtil.ParsetDoubleStringToFormat(
			// ConvertUtil.ParsetStringToInt32(mList.get(position).UNPAY_AMOUNT,
			// 0) / 100 + "",
			// ConvertUtil.FORMAT_DECIMAL_FORMAT_3),
			// mInflater));
			// holder.layout_container.addView(AppUtil.getKeyValueView("已结清",
			// ConvertUtil.ParsetDoubleStringToFormat(
			// ConvertUtil.ParsetStringToInt32(mList.get(position).PPY_AMOUNT,
			// 0) / 100 + "",
			// ConvertUtil.FORMAT_DECIMAL_FORMAT_3),
			// mInflater));
			// if (listExpand.contains(position + "")) {
			// holder.layout_container.setVisibility(View.VISIBLE);
			// } else {
			// holder.layout_container.setVisibility(View.GONE);
			// }
			// holder.layout_container.setOnClickListener(new
			// View.OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// // TODO Auto-generated method stub
			//
			// }
			// });
			return convertView;
		}
	}
}
