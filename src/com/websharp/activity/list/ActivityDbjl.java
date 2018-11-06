package com.websharp.activity.list;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
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
import com.websharp.activity.list.ActivityCzjl.AdapterPayAmountRecord;
import com.websharp.activity.list.ActivityXfzd.AdapterBillRecord;
import com.websharp.dao.EntityBillRecord;
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

public class ActivityDbjl extends BaseActivity {
	LinearLayout layout_back;
	TextView tv_title;
	PullRefreshListView lv_info;
	AdapterPlayRecord adapterPlayRecord;
	ArrayList<String> listExpand = new ArrayList<String>();
	TextView tv_start_date, tv_end_date;
	// Spinner sp_start_date, sp_end_date;
	// String curYear = GlobalData.GetYearList().get(0);
	// String curMonth = "01";
	// String curDate = "31";
	boolean isRefresh = true;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_back:
			finish();
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

							try {
								LogUtil.d(DateUtil.daysBetween(tv_start_date.getText().toString(),
										tv_end_date.getText().toString()) + "天");
								if (DateUtil.daysBetween(tv_start_date.getText().toString(),
										tv_end_date.getText().toString()) > 7) {
									Util.createToast(ActivityDbjl.this, "起止时间不能大于7天", Toast.LENGTH_SHORT).show();
								} else if (DateUtil.daysBetween(tv_start_date.getText().toString(),
										tv_end_date.getText().toString()) < 0) {
									Util.createToast(ActivityDbjl.this, "结束日期不能早于开始日期", Toast.LENGTH_SHORT).show();
								}

							} catch (Exception e) {
								Util.createToast(ActivityDbjl.this, "结束日期不能早于开始日期", Toast.LENGTH_SHORT).show();
							}

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
								if (DateUtil.daysBetween(tv_start_date.getText().toString(),
										tv_end_date.getText().toString()) > 7) {
									Util.createToast(ActivityDbjl.this, "起止时间不能大于7天", Toast.LENGTH_SHORT).show();
								} else if (DateUtil.daysBetween(tv_start_date.getText().toString(),
										tv_end_date.getText().toString()) < 0) {
									Util.createToast(ActivityDbjl.this, "结束日期不能早于开始日期", Toast.LENGTH_SHORT).show();
								}

							} catch (Exception e) {
								Util.createToast(ActivityDbjl.this, "结束日期不能早于开始日期", Toast.LENGTH_SHORT).show();
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
		setContentView(R.layout.activity_list_dbjl);
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
	}

	@Override
	public void bindData() {
		layout_back.setOnClickListener(this);
		Bundle b = getIntent().getExtras();
		tv_title.setText(b == null ? "" : b.getString("title"));

		adapterPlayRecord = new AdapterPlayRecord(new ArrayList<EntityPlayRecord>());
		lv_info.setAdapter(adapterPlayRecord);
		tv_start_date.setOnClickListener(this);
		tv_end_date.setOnClickListener(this);
		lv_info.setCanLoadMore(false);
		lv_info.setCanRefresh(true);
		lv_info.setAutoLoadMore(true);
		lv_info.setMoveToFirstItemAfterRefresh(true);
		lv_info.setDoRefreshOnUIChanged(false);

		tv_end_date.setText(DateUtil.TimeParseNowToFormatString("yyyy-MM-dd"));
		tv_start_date.setText(DateUtil.TimeParseToFormatString(DateUtil.getDate(new Date(), -7), "yyyy-MM-dd"));

		// ArrayAdapter adapterSource = new ArrayAdapter<String>(this,
		// android.R.layout.simple_spinner_item,
		// GlobalData.GetYearList());
		// adapterSource.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// sp_year.setAdapter(adapterSource);
		// sp_year.setSelection(0, true);
		// sp_year.setOnItemSelectedListener(new
		// AdapterView.OnItemSelectedListener() {
		// @Override
		// public void onItemSelected(AdapterView<?> arg0, View arg1, int
		// position, long arg3) {
		// curYear = GlobalData.GetYearList().get(position);
		// lv_info.pull2RefreshManually();
		// }
		//
		// @Override
		// public void onNothingSelected(AdapterView<?> arg0) {
		//
		// }
		// });
		//
		// ArrayAdapter adapterSourceMonth = new ArrayAdapter<String>(this,
		// android.R.layout.simple_spinner_item,
		// GlobalData.GetMonthList());
		// adapterSourceMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// sp_month.setAdapter(adapterSourceMonth);
		// sp_month.setSelection(0, true);
		// sp_month.setOnItemSelectedListener(new
		// AdapterView.OnItemSelectedListener() {
		// @Override
		// public void onItemSelected(AdapterView<?> arg0, View arg1, int
		// position, long arg3) {
		// curMonth = GlobalData.GetMonthList().get(position);
		// curMonth = curMonth.length() == 1 ? "0" + curMonth : curMonth;
		// String start = curYear + "-" + curMonth + "-01";
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// Date date;
		// try {
		// date = sdf.parse(start);
		// Calendar calendar = Calendar.getInstance();
		// calendar.setTime(date);
		// calendar.add(Calendar.MONTH, 1);
		// calendar.add(Calendar.DATE, -1);
		// curDate = calendar.get(Calendar.DATE) + "";
		// } catch (ParseException e) {
		// e.printStackTrace();
		// }
		// lv_info.pull2RefreshManually();
		// }
		//
		// @Override
		// public void onNothingSelected(AdapterView<?> arg0) {
		//
		// }
		// });

		lv_info.setOnRefreshListener(new PullRefreshListView.OnRefreshListener() {
			@Override
			public void onRefresh() {
				adapterPlayRecord.mList.clear();
				adapterPlayRecord.notifyDataSetChanged();

				try {
					if (DateUtil.daysBetween(tv_start_date.getText().toString(),
							tv_end_date.getText().toString()) > 7) {
						Util.createToast(ActivityDbjl.this, "起止时间不能大于7天", Toast.LENGTH_SHORT).show();
						lv_info.onRefreshComplete();
						return;
					} else if (DateUtil.daysBetween(tv_start_date.getText().toString(),
							tv_end_date.getText().toString()) < 0) {
						Util.createToast(ActivityDbjl.this, "结束日期不能早于开始日期", Toast.LENGTH_SHORT).show();
						lv_info.onRefreshComplete();
						return;
					}
				} catch (Exception e) {
					Util.createToast(ActivityDbjl.this, "结束日期不能早于开始日期", Toast.LENGTH_SHORT).show();
					return;
				}

				isRefresh = true;
				SwzfHttpHandler.PAGEINDEX_DBJL = 1;
				// curYear + curMonth + "01" + "-" + curYear + curMonth +
				// curDate + ","

				// new SwzfHttpHandler(cb,
				// ActivityDbjl.this).getDbjl(GlobalData.curCustomer.CUST_CODE,
				// curYear + curMonth + "01", curYear + curMonth + curDate, "1",
				// "1000");
				new SwzfHttpHandler(cb, ActivityDbjl.this).callSQL("10",
						tv_start_date.getText().toString().replaceAll("-", "") + "-"
								+ tv_end_date.getText().toString().replaceAll("-", "") + ","
								+ GlobalData.curCustomer.CUST_CODE,
						(SwzfHttpHandler.PAGEINDEX_DBJL - 1) * SwzfHttpHandler.PAGE_SIZE + 1 + "", 1000 + "");
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
					ArrayList<EntityPlayRecord> listPlay;
					if (obj.getJSONObject("data").getString("Result").equals("null")) {
						return;
					}
					listPlay = gson.fromJson(obj.getJSONObject("data").getString("Result"),
							new com.google.gson.reflect.TypeToken<ArrayList<EntityPlayRecord>>() {
							}.getType());
					// if (listPlay.size() < SwzfHttpHandler.PAGE_SIZE) {
					// lv_info.setCanLoadMore(false);
					// } else {
					// lv_info.setCanLoadMore(true);
					// }
					// lv_info.changeEndViewByState();
					if (isRefresh) {
						GlobalData.listPlayRecord = (ArrayList<EntityPlayRecord>) listPlay.clone();
					} else {
						GlobalData.listPlayRecord.addAll(listPlay);
					}

					adapterPlayRecord.mList = GlobalData.listPlayRecord;
					adapterPlayRecord.notifyDataSetChanged();
				} else {
					Util.createToast(ActivityDbjl.this, obj.optString("message"), 3000).show();
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
		private TextView tv_stb_no;
		private TextView tv_start_time;
		private TextView tv_film_name;
		private TextView tv_charge1;
		private LinearLayout layout_container;
	}

	class AdapterPlayRecord extends BaseAdapter {
		ArrayList<EntityPlayRecord> mList;
		private LayoutInflater mInflater;

		public AdapterPlayRecord(ArrayList<EntityPlayRecord> list) {
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
				mInflater = LayoutInflater.from(ActivityDbjl.this);
			}
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.item_dbjl, null);
				holder = new ViewHolder();
				holder.tv_stb_no = (TextView) convertView.findViewById(R.id.tv_stb_no);
				holder.tv_start_time = (TextView) convertView.findViewById(R.id.tv_start_time);
				holder.tv_charge1 = (TextView) convertView.findViewById(R.id.tv_charge1);
				holder.tv_film_name = (TextView) convertView.findViewById(R.id.tv_film_name);
				holder.layout_container = (LinearLayout) convertView.findViewById(R.id.layout_container);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv_stb_no.setText(mList.get(position).USER_NUMBER);
			holder.tv_start_time
					.setText(DateUtil.TimeParseStringToFormatString(mList.get(position).START_TIME, "MM-dd HH:mm"));
			holder.tv_charge1.setText(ConvertUtil.ParsetDoubleStringToFormat(
					ConvertUtil.ParsetStringToDouble(mList.get(position).CHARGE1, 0) / 100 + "",
					ConvertUtil.FORMAT_DECIMAL_FORMAT_3)
					+ "/"
					+ ConvertUtil.ParsetDoubleStringToFormat(
							ConvertUtil.ParsetStringToDouble(mList.get(position).CHARGE1, 0) / 100 + "",
							ConvertUtil.FORMAT_DECIMAL_FORMAT_3));
			holder.tv_film_name.setText(mList.get(position).FILM_NAME);
			// holder.layout_container.removeAllViews();
			//
			// holder.layout_container.addView(AppUtil.getKeyValueView("机顶盒号",
			// mList.get(position).STB_NO, mInflater));
			// holder.layout_container.addView(AppUtil.getKeyValueView("开始时间",
			// mList.get(position).START_TIME, mInflater));
			// holder.layout_container.addView(AppUtil.getKeyValueView("原价",
			// ConvertUtil.ParsetDoubleStringToFormat(
			// ConvertUtil.ParsetStringToInt32(mList.get(position).STD_BASIC_CHARGE,
			// 0) / 100 + "",
			// ConvertUtil.FORMAT_DECIMAL_FORMAT_3),
			// mInflater));
			// holder.layout_container.addView(AppUtil.getKeyValueView("发生金额",
			// ConvertUtil.ParsetDoubleStringToFormat(
			// ConvertUtil.ParsetStringToInt32(mList.get(position).CHARGE1, 0) /
			// 100 + "",
			// ConvertUtil.FORMAT_DECIMAL_FORMAT_3),
			// mInflater));
			// holder.layout_container.addView(AppUtil.getKeyValueView("片名",
			// mList.get(position).FILM_NAME, mInflater));
			//
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
