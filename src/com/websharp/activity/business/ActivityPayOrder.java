package com.websharp.activity.business;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import com.google.gson.Gson;
import com.websharp.activity.BaseActivity;
import com.websharp.dao.EntityAccountBook;
import com.websharp.dao.EntityCustomerOrder;
import com.websharp.dao.EntityNotice;
import com.websharp.dao.EntityPayAmountRecord;
import com.websharp.dao.EntityPayOrder;
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

public class ActivityPayOrder extends BaseActivity {
	LinearLayout layout_back;
	TextView tv_title, tv_total_amt, tv_cash_amt, tv_wx_amt, tv_alipay_amt;
	PullRefreshListView lv_info;
	AdapterPayOrder adapterPayOrder;
	TextView tv_date;
	boolean isRefresh = true;
	Calendar ca = Calendar.getInstance();
	int mYear = ca.get(Calendar.YEAR);
	int mMonth = ca.get(Calendar.MONTH);
	int mDay = ca.get(Calendar.DAY_OF_MONTH);

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_back:
			finish();
			break;
		case R.id.tv_date:
			new DatePickerDialog(ActivityPayOrder.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,onDateSetListener, mYear, mMonth, mDay).show();
			break;
		}
	}

	/**
	 * 日期选择器对话框监听
	 */
	private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			String days;
			if (mMonth + 1 < 10) {
				if (mDay < 10) {
					days = new StringBuffer().append(mYear).append("-").append("0").append(mMonth + 1).append("-")
							.append("0").append(mDay).toString();
				} else {
					days = new StringBuffer().append(mYear).append("-").append("0").append(mMonth + 1).append("-")
							.append(mDay).toString();
				}

			} else {
				if (mDay < 10) {
					days = new StringBuffer().append(mYear).append("-").append(mMonth + 1).append("-").append("0")
							.append(mDay).toString();
				} else {
					days = new StringBuffer().append(mYear).append("-").append(mMonth + 1).append("-").append(mDay)
							.toString();
				}

			}
			tv_date.setText(days);
			lv_info.pull2RefreshManually();
		}
	};

	@Override
	public void initLayout() {
		setContentView(R.layout.activity_pay_order);
	}

	@Override
	public void init() {
		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		lv_info = (PullRefreshListView) findViewById(R.id.lv_info);
		tv_total_amt = (TextView) findViewById(R.id.tv_total_amt);
		tv_cash_amt = (TextView) findViewById(R.id.tv_cash_amt);
		tv_wx_amt = (TextView) findViewById(R.id.tv_wx_amt);
		tv_alipay_amt = (TextView) findViewById(R.id.tv_alipay_amt);
		tv_date = (TextView) findViewById(R.id.tv_date);
	}

	@Override
	public void bindData() {
		layout_back.setOnClickListener(this);
		Bundle b = getIntent().getExtras();
		tv_title.setText("支付对帐");
		tv_date.setText(DateUtil.TimeParseNowToFormatString("yyyy-MM-dd"));
		tv_date.setOnClickListener(this);

		adapterPayOrder = new AdapterPayOrder(new ArrayList<EntityPayOrder>());
		lv_info.setAdapter(adapterPayOrder);
		lv_info.setCanLoadMore(true);
		lv_info.setCanRefresh(true);
		lv_info.setAutoLoadMore(true);
		lv_info.setMoveToFirstItemAfterRefresh(true);
		lv_info.setDoRefreshOnUIChanged(false);

		lv_info.setOnRefreshListener(new PullRefreshListView.OnRefreshListener() {
			@Override
			public void onRefresh() {
				adapterPayOrder.mList.clear();
				adapterPayOrder.notifyDataSetChanged();
				isRefresh = true;
				SwzfHttpHandler.PAGEINDEX_PAY_ORDER = 0;
				JSONObject jobj = new JSONObject();
				try {
					// jobj.put("OccurTime",
					// DateUtil.TimeParseNowToFormatString("yyyy-MM-dd
					// 00:00:00"));
					jobj.put("OccurTime", tv_date.getText().toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				new SwzfHttpHandler(cb, ActivityPayOrder.this).queryPayOrder(SwzfHttpHandler.PAGEINDEX_PAY_ORDER + "",
						jobj.toString());
			}
		});

		lv_info.setOnLoadListener(new PullRefreshListView.OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				isRefresh = false;
				SwzfHttpHandler.PAGEINDEX_PAY_ORDER++;
				JSONObject jobj = new JSONObject();
				try {
					jobj.put("OccurTime", tv_date.getText().toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				new SwzfHttpHandler(cb, ActivityPayOrder.this).queryPayOrder(SwzfHttpHandler.PAGEINDEX_PAY_ORDER + "",
						jobj.toString());
			}
		});

		// lv_info.setOnItemClickListener(new AdapterView.OnItemClickListener()
		// {
		//
		// @Override
		// public void onItemClick(AdapterView<?> arg0, View v, int position,
		// long arg3) {
		// Util.startActivity(ActivityPayOrder.this,
		// ActivityNoticeContent.class, false);
		//
		// }
		// });

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
					ArrayList<EntityPayOrder> listPayOrder;

					listPayOrder = gson.fromJson(obj.getJSONObject("data").getString("Logs"),
							new com.google.gson.reflect.TypeToken<ArrayList<EntityPayOrder>>() {
							}.getType());
					tv_total_amt.setText("总金额: " + obj.getJSONObject("data").getString("TotalAmt") + " 元");
					tv_cash_amt.setText("现金: " + obj.getJSONObject("data").getString("CashAmt") + " 元");
					tv_wx_amt.setText("微信: " + obj.getJSONObject("data").getString("WxAmt") + " 元");
					tv_alipay_amt.setText("支付宝: " + obj.getJSONObject("data").getString("AlipayAmt") + " 元");

					if (listPayOrder.size() < SwzfHttpHandler.PAGE_SIZE) {
						lv_info.setCanLoadMore(false);
					} else {
						lv_info.setCanLoadMore(true);
					}
					lv_info.changeEndViewByState();
					if (isRefresh) {
						GlobalData.listPayOrder = (ArrayList<EntityPayOrder>) listPayOrder.clone();
					} else {
						GlobalData.listPayOrder.addAll(listPayOrder);
					}

					adapterPayOrder.mList = GlobalData.listPayOrder;
					adapterPayOrder.notifyDataSetChanged();
				} else {
					Util.createToast(ActivityPayOrder.this, obj.optString("message"), 3000).show();
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
		private TextView tv_cust_code;
		private TextView tv_offerid;
		private TextView tv_prodid;
		private TextView tv_publish_time;
		private TextView tv_trade_id;
		private TextView tv_pay_way;
		private TextView tv_amount;
		private TextView tv_pay_result;
		private TextView tv_staff_name;
	}

	class AdapterPayOrder extends BaseAdapter {
		ArrayList<EntityPayOrder> mList;
		private LayoutInflater mInflater;

		public AdapterPayOrder(ArrayList<EntityPayOrder> list) {
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
				mInflater = LayoutInflater.from(ActivityPayOrder.this);
			}

			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.item_pay_order, null);
				holder = new ViewHolder();
				holder = new ViewHolder();
				holder.tv_cust_code = (TextView) convertView.findViewById(R.id.tv_cust_code);
				holder.tv_offerid = (TextView) convertView.findViewById(R.id.tv_offerid);
				holder.tv_prodid = (TextView) convertView.findViewById(R.id.tv_prodid);
				holder.tv_publish_time = (TextView) convertView.findViewById(R.id.tv_publish_time);
				holder.tv_trade_id = (TextView) convertView.findViewById(R.id.tv_trade_id);
				holder.tv_pay_way = (TextView) convertView.findViewById(R.id.tv_pay_way);
				holder.tv_amount = (TextView) convertView.findViewById(R.id.tv_amount);
				holder.tv_pay_result = (TextView) convertView.findViewById(R.id.tv_pay_result);
				holder.tv_staff_name = (TextView) convertView.findViewById(R.id.tv_staff_name);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.tv_cust_code.setText("客户证号: " + mList.get(position).CustCode);
			holder.tv_offerid
					.setText("套餐名称: " + mList.get(position).OfferName + "(" + mList.get(position).OfferID + ")");
			holder.tv_prodid.setText("产品名称: " + mList.get(position).ProdName + "(" + mList.get(position).ProdID + ")");
			holder.tv_trade_id.setText("订单编号: " + mList.get(position).TradeID);
			holder.tv_pay_way.setText("支付方式: " + (mList.get(position).PayWay.equals("1") ? "微信" : "支付宝"));
			holder.tv_amount.setText("支付金额: " + mList.get(position).Amt);
			holder.tv_pay_result.setText("支付状态: " + mList.get(position).PayMessage);
			holder.tv_staff_name
					.setText("经办人员: " + mList.get(position).StaffName + "[" + mList.get(position).DeptName + "]");
			holder.tv_publish_time.setText(mList.get(position).OccurTime);

			return convertView;
		}
	}

}
