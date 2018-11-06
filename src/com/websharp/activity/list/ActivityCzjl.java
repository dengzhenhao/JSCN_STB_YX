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
import com.websharp.activity.list.ActivityDbjl.AdapterPlayRecord;
import com.websharp.dao.EntityAccountBook;
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

public class ActivityCzjl extends BaseActivity {
	LinearLayout layout_back;
	TextView tv_title;
	PullRefreshListView lv_info;
	AdapterPayAmountRecord adapterPayAmountRecord;
	ArrayList<String> listExpand = new ArrayList<String>();
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
		setContentView(R.layout.activity_list_czjl);
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

		adapterPayAmountRecord = new AdapterPayAmountRecord(new ArrayList<EntityPayAmountRecord>());
		lv_info.setAdapter(adapterPayAmountRecord);
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
				listExpand.clear();
				adapterPayAmountRecord.mList.clear();
				adapterPayAmountRecord.notifyDataSetChanged();
				isRefresh = true;
				SwzfHttpHandler.PAGEINDEX_CZJL = 1;
				// http://mboss.sz96296.com/Handlers/Boss/QueryDataHandler.ashx?data=201701-201712,8512003007619|1|20&id=11&token=a11dcf7941b8bad5d94324a43fb25583c84f2a9157ff3cca

				new SwzfHttpHandler(cb, ActivityCzjl.this).callSQL("11",
						curYear + "01-" + curYear + "12" + "," + GlobalData.curCustomer.CUST_CODE,
						(SwzfHttpHandler.PAGEINDEX_CZJL - 1) * SwzfHttpHandler.PAGE_SIZE + 1 + "",
						SwzfHttpHandler.PAGEINDEX_CZJL * SwzfHttpHandler.PAGE_SIZE + "");
			}
		});

		lv_info.setOnLoadListener(new PullRefreshListView.OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				isRefresh = false;
				SwzfHttpHandler.PAGEINDEX_CZJL++;
				new SwzfHttpHandler(cb, ActivityCzjl.this).callSQL("11",
						curYear + "01-" + curYear + "12" + "," + GlobalData.curCustomer.CUST_CODE,
						(SwzfHttpHandler.PAGEINDEX_CZJL - 1) * SwzfHttpHandler.PAGE_SIZE + 1 + "",
						SwzfHttpHandler.PAGEINDEX_CZJL * SwzfHttpHandler.PAGE_SIZE + "");
			}
		});

		lv_info.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {

				LinearLayout layout_container = (LinearLayout) v.findViewById(R.id.layout_container);
				LogUtil.d("container:%s", position - 1);
				layout_container.removeAllViews();
				if (layout_container.getVisibility() == View.GONE) {
					LayoutInflater mInflater = LayoutInflater.from(ActivityCzjl.this);
//					layout_container.addView(AppUtil.getKeyValueView("缴费金额",
//							ConvertUtil.ParsetDoubleStringToFormat(
//									ConvertUtil.ParsetStringToDouble(
//											adapterPayAmountRecord.mList.get(position - 1).AMOUNT, 0) / 100 + "",
//									ConvertUtil.FORMAT_DECIMAL_FORMAT_3),
//							mInflater));
//					layout_container.addView(AppUtil.getKeyValueView("缴费时间",
//							adapterPayAmountRecord.mList.get(position - 1).PAYMENT_DATE, mInflater));
//					layout_container
//							.addView(
//									AppUtil.getKeyValueView("交费类型",
//											GlobalData.GetStaticDataName("OPERATION_TYPE",
//													adapterPayAmountRecord.mList.get(position - 1).OPERATION_TYPE),
//											mInflater));
					layout_container.addView(AppUtil.getKeyValueView("营业点",
							adapterPayAmountRecord.mList.get(position - 1).ORGANIZE_NAME, mInflater));
					layout_container.addView(AppUtil.getKeyValueView("操作员",
							adapterPayAmountRecord.mList.get(position - 1).STAFF_NAME, mInflater));
					layout_container.addView(AppUtil.getKeyValueView("银行名称",
							adapterPayAmountRecord.mList.get(position - 1).BANK_NAME, mInflater));
					layout_container
							.addView(
									AppUtil.getKeyValueView("凭证类型",
											GlobalData.GetStaticDataName("CERTIFIED_TYPE",
													adapterPayAmountRecord.mList.get(position - 1).CERTIFIED_TYPE),
											mInflater));
					layout_container.addView(AppUtil.getKeyValueViewLast("备注",
							adapterPayAmountRecord.mList.get(position - 1).REMARK, mInflater));
					listExpand.add(position - 1 + "");
					layout_container.setVisibility(View.VISIBLE);
				} else {
					layout_container.setVisibility(View.GONE);
					listExpand.remove(position - 1 + "");
				}
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
					ArrayList<EntityPayAmountRecord> listPay;

					listPay = gson.fromJson(obj.getJSONObject("data").getString("Result"),
							new com.google.gson.reflect.TypeToken<ArrayList<EntityPayAmountRecord>>() {
							}.getType());
					if (listPay.size() < SwzfHttpHandler.PAGE_SIZE) {
						lv_info.setCanLoadMore(false);
					} else {
						lv_info.setCanLoadMore(true);
					}
					lv_info.changeEndViewByState();
					if (isRefresh) {
						GlobalData.listPayAmountRecord = (ArrayList<EntityPayAmountRecord>) listPay.clone();
					} else {
						GlobalData.listPayAmountRecord.addAll(listPay);
					}

					adapterPayAmountRecord.mList = GlobalData.listPayAmountRecord;
					adapterPayAmountRecord.notifyDataSetChanged();
				} else {
					Util.createToast(ActivityCzjl.this, obj.optString("message"), 3000).show();
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
		private TextView tv_amount;
		private TextView tv_payment_date;
		private TextView tv_payment_method;
		private TextView tv_operation_type;
		private LinearLayout layout_container;
	}

	class AdapterPayAmountRecord extends BaseAdapter {
		ArrayList<EntityPayAmountRecord> mList;
		private LayoutInflater mInflater;

		public AdapterPayAmountRecord(ArrayList<EntityPayAmountRecord> list) {
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
				mInflater = LayoutInflater.from(ActivityCzjl.this);
			}
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.item_czjl, null);
				holder = new ViewHolder();
				holder.tv_amount = (TextView) convertView.findViewById(R.id.tv_amount);
				holder.tv_payment_date = (TextView) convertView.findViewById(R.id.tv_payment_date);
				holder.tv_operation_type = (TextView) convertView.findViewById(R.id.tv_operation_type);
				holder.layout_container = (LinearLayout) convertView.findViewById(R.id.layout_container);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv_amount.setText(ConvertUtil.ParsetDoubleStringToFormat(
					ConvertUtil.ParsetStringToDouble(mList.get(position).AMOUNT, 0) / 100 + "",
					ConvertUtil.FORMAT_DECIMAL_FORMAT_3));
			holder.tv_payment_date.setText(mList.get(position).PAYMENT_DATE);
			// holder.tv_payment_method
			// .setText(GlobalData.GetStaticDataName("PAYMENT_METHOD",
			// mList.get(position).PAYMENT_METHOD));
			holder.tv_operation_type
					.setText(GlobalData.GetStaticDataName("OPERATION_TYPE", mList.get(position).OPERATION_TYPE));

			holder.layout_container.removeAllViews();

			if (listExpand.contains(position + "")) {
				holder.layout_container
						.addView(AppUtil.getKeyValueView("营业点", mList.get(position).ORGANIZE_NAME, mInflater)); 
				holder.layout_container
						.addView(AppUtil.getKeyValueView("操作员", mList.get(position).STAFF_NAME, mInflater));
				holder.layout_container
						.addView(AppUtil.getKeyValueView("银行名称", mList.get(position).BANK_NAME, mInflater));
				holder.layout_container.addView(AppUtil.getKeyValueView("凭证类型",
						GlobalData.GetStaticDataName("CERTIFIED_TYPE", mList.get(position).CERTIFIED_TYPE), mInflater));
				holder.layout_container.addView(AppUtil.getKeyValueView("备注", mList.get(position).REMARK, mInflater));
				holder.layout_container.setVisibility(View.VISIBLE);
			} else {
				holder.layout_container.setVisibility(View.GONE);
			}
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
