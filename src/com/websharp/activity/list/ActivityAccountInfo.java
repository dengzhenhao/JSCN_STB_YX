package com.websharp.activity.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.websharp.activity.BaseActivity;
import com.websharp.activity.list.ActivityDbjl.AdapterPlayRecord;
import com.websharp.dao.EntityAccountBook;
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

public class ActivityAccountInfo extends BaseActivity {
	LinearLayout layout_back;
	TextView tv_title;
	LinearLayout layout_account_book_items;

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
		setContentView(R.layout.activity_list_account_info);
	}

	@Override
	public void init() {
		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		layout_account_book_items = (LinearLayout) findViewById(R.id.layout_account_book_items);
	}

	@Override
	public void bindData() {
		layout_back.setOnClickListener(this);
		Bundle b = getIntent().getExtras();
		tv_title.setText(b == null ? "" : b.getString("title"));
		new SwzfHttpHandler(cbAccountBook, this).customerAccountBook(ActivityAccountInfo.this,
				GlobalData.curCustomer.CUST_CODE);

	}

	private void bindAccountBookDetail() {
		LayoutInflater mInFlater = LayoutInflater.from(this);
		layout_account_book_items.removeAllViews();
		for (int i = 0; i < GlobalData.listAccountBook.size(); i++) {
			if (i == GlobalData.listAccountBook.size() - 1) {
				layout_account_book_items
				.addView(
						AppUtil.getKeyValueView4Last(GlobalData.listAccountBook.get(i).balanceTypeName,
								ConvertUtil.ParsetDoubleStringToFormat(
										ConvertUtil.ParsetStringToDouble(
												GlobalData.listAccountBook.get(i).balance, 0) / 100 + "",
										ConvertUtil.FORMAT_DECIMAL_FORMAT_3),
								mInFlater));
			} else {
				layout_account_book_items
						.addView(
								AppUtil.getKeyValueView4(GlobalData.listAccountBook.get(i).balanceTypeName,
										ConvertUtil.ParsetDoubleStringToFormat(
												ConvertUtil.ParsetStringToDouble(
														GlobalData.listAccountBook.get(i).balance, 0) / 100 + "",
												ConvertUtil.FORMAT_DECIMAL_FORMAT_3),
										mInFlater));
			}
		}
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

	AsyncHttpCallBack cbAccountBook = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);
				if (obj.optString("code").equals("0")) {
					Gson gson = new Gson();
					GlobalData.listAccountBook = gson.fromJson(obj.getString("data"),
							new com.google.gson.reflect.TypeToken<ArrayList<EntityAccountBook>>() {
							}.getType());
					bindAccountBookDetail();
				} else {
					Util.createToast(ActivityAccountInfo.this, obj.optString("message"), 3000).show();
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
