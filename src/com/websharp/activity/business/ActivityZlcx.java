package com.websharp.activity.business;

import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.websharp.activity.BaseActivity;
import com.websharp.activity.list.ActivityCzjl;
import com.websharp.activity.list.ActivityDbjl;
import com.websharp.activity.qr.CaptureActivity;
import com.websharp.dao.EntityBillRecord;
import com.websharp.dao.EntityCommand;
import com.websharp.dao.EntityPayAmountRecord;
import com.websharp.dao.EntityPlayRecord;
import com.websharp.data.AppUtil;
import com.websharp.data.Constant;
import com.websharp.data.GlobalData;
import com.websharp.http.AsyncHttpCallBack;
import com.websharp.http.SwzfHttpHandler;
import com.websharp.stb.R;
import com.websharp.widget.PullRefreshListView;
import com.websharputil.common.ConvertUtil;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;
import com.websharputil.date.DateUtil;

public class ActivityZlcx extends BaseActivity {
	LinearLayout layout_back;
	TextView tv_title;
	PullRefreshListView lv_info;
	AdapterCommand adapterCommand;
	ArrayList<String> listExpand = new ArrayList<String>();
	TextView tv_start_date, tv_end_date;
	// Spinner sp_start_date, sp_end_date;
	// String curYear = GlobalData.GetYearList().get(0);
	// String curMonth = "01";
	// String curDate = "31";
	boolean isRefresh = true;
	EditText et_search_keyword;
	ImageView btn_qr;
	Button btn_search;
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == Constant.QR_REQUEST_CODE_ZLCX) {
				et_search_keyword.setText(data.getExtras().getString("data"));
				btn_search.performClick();
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_back:
			finish();
			break;
		case R.id.btn_qr:
			Intent intent = new Intent(this, CaptureActivity.class);
			startActivityForResult(intent, Constant.QR_REQUEST_CODE_ZLCX);
			break;
		case R.id.btn_search:
			lv_info.pull2RefreshManually();
			break;
		case R.id.tv_start_date:
			Calendar c = Calendar.getInstance(Locale.CHINA);
			DatePickerDialog d = new DatePickerDialog(this, DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT,
					// 绑定监听器
					new DatePickerDialog.OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
							tv_start_date.setText(
									year + "-" + ((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : (monthOfYear + 1))
											+ "-" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "");
						}
					}
					// 设置初始日期
					, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
			d.show();
			break;
		case R.id.tv_end_date:
			Calendar c2 = Calendar.getInstance(Locale.CHINA);
			DatePickerDialog d2 = new DatePickerDialog(this, DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT,
					// 绑定监听器
					new DatePickerDialog.OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
							tv_end_date.setText(
									year + "-" + ((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : (monthOfYear + 1))
											+ "-" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "");
							try {
								LogUtil.d(DateUtil.daysBetween(tv_start_date.getText().toString(),
										tv_end_date.getText().toString()) + "天");
								// if
								// (DateUtil.daysBetween(tv_start_date.getText(),
								// tv_end_date.getText())) {
								//
								// }

							} catch (Exception e) {
								Util.createToast(ActivityZlcx.this, "结束日期不能早于开始日期", Toast.LENGTH_SHORT).show();
							}
						}
					}
					// 设置初始日期
					, c2.get(Calendar.YEAR), c2.get(Calendar.MONTH), c2.get(Calendar.DAY_OF_MONTH));
			d2.show();
			break;
		}
	}

	@Override
	public void initLayout() {
		setContentView(R.layout.activity_list_zlcx);
	}

	@Override
	public void init() {
		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		lv_info = (PullRefreshListView) findViewById(R.id.lv_info);
		// sp_year = (Spinner) findViewById(R.id.sp_year);
		// sp_month = (Spinner) findViewById(R.id.sp_month);
		// GlobalData.curCustomer.CUST_CODE = "8512001703471";
		tv_start_date = (TextView) findViewById(R.id.tv_start_date);
		tv_end_date = (TextView) findViewById(R.id.tv_end_date);	
		 et_search_keyword= (EditText) findViewById(R.id.et_keyword);	
		 btn_qr= (ImageView) findViewById(R.id.btn_qr);	
		 btn_search= (Button) findViewById(R.id.btn_search);	
	}

	@Override
	public void bindData() {
		tv_title.setText("指令查询");
		layout_back.setOnClickListener(this);
		btn_qr.setOnClickListener(this);
		btn_search.setOnClickListener(this);
		tv_start_date.setOnClickListener(this);
		tv_end_date.setOnClickListener(this);
		
		adapterCommand = new AdapterCommand(new ArrayList<EntityCommand>());
		lv_info.setAdapter(adapterCommand);
		lv_info.setCanLoadMore(false);
		lv_info.setCanRefresh(true);
		// lv_info.setAutoLoadMore(true);
		lv_info.setMoveToFirstItemAfterRefresh(true);
		lv_info.setDoRefreshOnUIChanged(false);

		tv_end_date.setText(DateUtil.TimeParseNowToFormatString("yyyy-MM-dd"));
		tv_start_date.setText(DateUtil.TimeParseToFormatString(DateUtil.getDate(new Date(), -7), "yyyy-MM-dd"));

		

		lv_info.setOnRefreshListener(new PullRefreshListView.OnRefreshListener() {
			@Override
			public void onRefresh() {
				listExpand.clear();
				adapterCommand.mList.clear();
				adapterCommand.notifyDataSetChanged();
				try {
					if (DateUtil.daysBetween(tv_start_date.getText().toString(),
							tv_end_date.getText().toString()) > 7) {
						Util.createToast(ActivityZlcx.this, "起止时间不能大于7天", Toast.LENGTH_SHORT).show();
						lv_info.onRefreshComplete();
						return;
					} else if (DateUtil.daysBetween(tv_start_date.getText().toString(),
							tv_end_date.getText().toString()) < 0) {
						Util.createToast(ActivityZlcx.this, "结束日期不能早于开始日期", Toast.LENGTH_SHORT).show();
						lv_info.onRefreshComplete();
						return;
					}
				} catch (Exception e) {
					Util.createToast(ActivityZlcx.this, "结束日期不能早于开始日期", Toast.LENGTH_SHORT).show();
					return;
				}

				isRefresh = true;
//				new SwzfHttpHandler(cb, ActivityZlcx.this).callSQL(id, strParams);(getText(et_search_keyword),
//						tv_start_date.getText().toString(), tv_end_date.getText().toString());
				new SwzfHttpHandler(cb, ActivityZlcx.this).callSQL("20", getText(et_search_keyword)+"|"+tv_start_date.getText().toString()+"|"+tv_end_date.getText().toString());
			}
		});

		// lv_info.setOnLoadListener(new
		// PullRefreshListView.OnLoadMoreListener() {
		//
		// @Override
		// public void onLoadMore() {
		// isRefresh = false;
		// SwzfHttpHandler.PAGEINDEX_DBJL++;
		// new SwzfHttpHandler(cb, ActivityDbjl.this).callSQL("10",
		// curYear+curMonth+"01"+"-"+ curYear+curMonth+curDate,
		// GlobalData.curCustomer.CUST_CODE,
		// (SwzfHttpHandler.PAGEINDEX_DBJL - 1) * SwzfHttpHandler.PAGE_SIZE + 1
		// + "",
		// SwzfHttpHandler.PAGEINDEX_DBJL * SwzfHttpHandler.PAGE_SIZE + "");
		// }
		// });

		lv_info.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {

				LinearLayout layout_container = (LinearLayout) v.findViewById(R.id.layout_container);
				LogUtil.d("container:%s", position - 1);
				layout_container.removeAllViews();
				if (layout_container.getVisibility() == View.GONE) {
					LayoutInflater mInflater = LayoutInflater.from(ActivityZlcx.this);
					layout_container.addView(AppUtil.getKeyValueView("授权对象编号",
							adapterCommand.mList.get(position).AUTH_OBJECT_NO, mInflater));
					layout_container.addView(AppUtil.getKeyValueView("指令名称",
							adapterCommand.mList.get(position).NE_COMMAND_NAME, mInflater));
					layout_container.addView(AppUtil.getKeyValueView("处理状态",
							GlobalData.GetStaticDataName("DEAL_STATE", adapterCommand.mList.get(position).DEAL_STATE),
							mInflater));
					layout_container.addView(AppUtil.getKeyValueView("指令状态",
							adapterCommand.mList.get(position).CMD_STATUS_INFO, mInflater));
					layout_container.addView(AppUtil.getKeyValueView("指令确认时间",
							adapterCommand.mList.get(position).CMD_CONFIRM_DATE, mInflater));
					layout_container.addView(AppUtil.getKeyValueView("所属机构",
							adapterCommand.mList.get(position).ORGANIZE_NAME, mInflater));
					listExpand.add(position - 1 + "");
					layout_container.setVisibility(View.VISIBLE);
				} else {
					layout_container.setVisibility(View.GONE);
					listExpand.remove(position - 1 + "");
				}
			}

		});
		//lv_info.pull2RefreshManually();
		// new SwzfHttpHandler(cb, this).getStaticData();

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
					ArrayList<EntityCommand> listCommand;

					listCommand = gson.fromJson(obj.getJSONObject("data").getJSONArray("Result").toString(),
							new com.google.gson.reflect.TypeToken<ArrayList<EntityCommand>>() {
							}.getType());
					// if (listPlay.size() < SwzfHttpHandler.PAGE_SIZE) {
					// lv_info.setCanLoadMore(false);
					// } else {
					// lv_info.setCanLoadMore(true);
					// }
					// lv_info.changeEndViewByState();
					if (isRefresh) {
						GlobalData.listCommand = (ArrayList<EntityCommand>) listCommand.clone();
					} else {
						GlobalData.listCommand.addAll(listCommand);
					}

					adapterCommand.mList = GlobalData.listCommand;
					adapterCommand.notifyDataSetChanged();
				} else {
					Util.createToast(ActivityZlcx.this, obj.optString("message"), 3000).show();
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
		private TextView tv_bus_name;
		private TextView tv_staff_name;
		private TextView tv_create_time;
		private LinearLayout layout_container;
	}

	class AdapterCommand extends BaseAdapter {
		ArrayList<EntityCommand> mList;
		private LayoutInflater mInflater;

		public AdapterCommand(ArrayList<EntityCommand> list) {
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
				mInflater = LayoutInflater.from(ActivityZlcx.this);
			}
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.item_zlcx, null);
				holder = new ViewHolder();
				holder.tv_bus_name = (TextView) convertView.findViewById(R.id.tv_bus_name);
				holder.tv_staff_name = (TextView) convertView.findViewById(R.id.tv_staff_name);
				holder.tv_create_time = (TextView) convertView.findViewById(R.id.tv_create_time);
				holder.layout_container = (LinearLayout) convertView.findViewById(R.id.layout_container);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.tv_bus_name.setText(mList.get(position).BUSI_COMMAND_NAME);
			holder.tv_staff_name.setText(mList.get(position).STAFF_NAME);
			holder.tv_create_time.setText(mList.get(position).CREATE_DATE);

			holder.layout_container.removeAllViews();

			if (listExpand.contains(position + "")) {
				holder.layout_container
						.addView(AppUtil.getKeyValueView("授权对象编号", mList.get(position).AUTH_OBJECT_NO, mInflater));
				holder.layout_container
						.addView(AppUtil.getKeyValueView("指令名称", mList.get(position).NE_COMMAND_NAME, mInflater));
				holder.layout_container.addView(AppUtil.getKeyValueView("处理状态",
						GlobalData.GetStaticDataName("DEAL_STATE", mList.get(position).DEAL_STATE), mInflater));
				holder.layout_container
						.addView(AppUtil.getKeyValueView("指令状态", mList.get(position).CMD_STATUS_INFO, mInflater));
				holder.layout_container
						.addView(AppUtil.getKeyValueView("指令确认时间", mList.get(position).CMD_CONFIRM_DATE, mInflater));
				holder.layout_container
						.addView(AppUtil.getKeyValueView("所属机构", mList.get(position).ORGANIZE_NAME, mInflater));
				holder.layout_container.setVisibility(View.VISIBLE);
			} else {
				holder.layout_container.setVisibility(View.GONE);
			}

			return convertView;
		}
	}
}
