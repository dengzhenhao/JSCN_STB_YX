package com.websharp.activity.business;

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
import com.websharp.dao.EntityAccountBook;
import com.websharp.dao.EntityCustomerOrder;
import com.websharp.dao.EntityNotice;
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

public class ActivityNotice extends BaseActivity {
	LinearLayout layout_back;
	TextView tv_title;
	PullRefreshListView lv_info;
	AdapterNotice adapterNotice;

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
		setContentView(R.layout.activity_notice);
	}

	@Override
	public void init() {
		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		lv_info = (PullRefreshListView) findViewById(R.id.lv_info);
	}

	@Override
	public void bindData() {
		layout_back.setOnClickListener(this);
		Bundle b = getIntent().getExtras();
		tv_title.setText("公告");

		adapterNotice = new AdapterNotice(new ArrayList<EntityNotice>());
		lv_info.setAdapter(adapterNotice);
		lv_info.setCanLoadMore(true);
		lv_info.setCanRefresh(true);
		lv_info.setAutoLoadMore(true);
		lv_info.setMoveToFirstItemAfterRefresh(true);
		lv_info.setDoRefreshOnUIChanged(false);

		lv_info.setOnRefreshListener(new PullRefreshListView.OnRefreshListener() {
			@Override
			public void onRefresh() {
				adapterNotice.mList.clear();
				adapterNotice.notifyDataSetChanged();
				isRefresh = true;
				SwzfHttpHandler.PAGEINDEX_NOTICE = 1;
				new SwzfHttpHandler(cb, ActivityNotice.this).callSQL("17",
						(SwzfHttpHandler.PAGEINDEX_NOTICE - 1) * SwzfHttpHandler.PAGE_SIZE + 1 + "",
						SwzfHttpHandler.PAGEINDEX_NOTICE * SwzfHttpHandler.PAGE_SIZE + "");
			}
		});

		lv_info.setOnLoadListener(new PullRefreshListView.OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				isRefresh = false;
				SwzfHttpHandler.PAGEINDEX_NOTICE++;
				new SwzfHttpHandler(cb, ActivityNotice.this).callSQL("17",
						(SwzfHttpHandler.PAGEINDEX_NOTICE - 1) * SwzfHttpHandler.PAGE_SIZE + 1 + "",
						SwzfHttpHandler.PAGEINDEX_NOTICE * SwzfHttpHandler.PAGE_SIZE + "");
			}
		});

		lv_info.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
				GlobalData.curNotice = GlobalData.listNotice.get(position - 1);
				Util.startActivity(ActivityNotice.this, ActivityNoticeContent.class, false);

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
					ArrayList<EntityNotice> listNotice;

					listNotice = gson.fromJson(obj.getJSONObject("data").getString("Result"),
							new com.google.gson.reflect.TypeToken<ArrayList<EntityNotice>>() {
							}.getType());
					if (listNotice.size() < SwzfHttpHandler.PAGE_SIZE) {
						lv_info.setCanLoadMore(false);
					} else {
						lv_info.setCanLoadMore(true);
					}
					lv_info.changeEndViewByState();
					if (isRefresh) {
						GlobalData.listNotice = (ArrayList<EntityNotice>) listNotice.clone();
					} else {
						GlobalData.listNotice.addAll(listNotice);
					}

					adapterNotice.mList = GlobalData.listNotice;
					adapterNotice.notifyDataSetChanged();
				} else {
					Util.createToast(ActivityNotice.this, obj.optString("message"), 3000).show();
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
		private TextView tv_title;
		private TextView tv_publish_time;
	}

	class AdapterNotice extends BaseAdapter {
		ArrayList<EntityNotice> mList;
		private LayoutInflater mInflater;

		public AdapterNotice(ArrayList<EntityNotice> list) {
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
				mInflater = LayoutInflater.from(ActivityNotice.this);
			}
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.item_notice, null);
				holder = new ViewHolder();
				holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
				holder.tv_publish_time = (TextView) convertView.findViewById(R.id.tv_publish_time);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv_title.setText(mList.get(position).AFFICHE_TITLE);
			holder.tv_publish_time.setText(mList.get(position).PUBLISH_DATE);

			return convertView;
		}
	}

}
