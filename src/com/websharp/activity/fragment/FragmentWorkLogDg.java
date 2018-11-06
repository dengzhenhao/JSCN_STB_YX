package com.websharp.activity.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.websharp.activity.business.ActivityCustomerUserInfo;
import com.websharp.activity.business.ActivityNotice;
import com.websharp.activity.list.ActivityDdDetail;
import com.websharp.activity.list.ActivityYbjdd;
import com.websharp.dao.EntityCustomerOrder;
import com.websharp.dao.EntityCustomerUser;
import com.websharp.dao.EntityNotice;
import com.websharp.dao.EntityUser;
import com.websharp.dao.EntityWorkLogDg;
import com.websharp.data.GlobalData;
import com.websharp.http.AsyncHttpCallBack;
import com.websharp.http.SwzfHttpHandler;
import com.websharp.stb.R;
import com.websharp.widget.PullRefreshListView;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 客户下的用户列表
 * 
 * @author dengzh
 * 
 */
public class FragmentWorkLogDg extends Fragment implements View.OnClickListener {
	public String title = "";
	public int type = -1;
	public boolean isRefresh = false;
	PullRefreshListView lv_info;
	AdapterWorkLog adapterWorkLog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.widget_work_log, container, false);
		init(view);
		return view;
	}

	private void init(View view) {
		lv_info = (PullRefreshListView) view.findViewById(R.id.lv_info);
		adapterWorkLog = new AdapterWorkLog(new ArrayList<EntityWorkLogDg>());
		lv_info.setAdapter(adapterWorkLog);
		lv_info.setCanLoadMore(true);
		lv_info.setCanRefresh(true);
		lv_info.setAutoLoadMore(true);
		lv_info.setMoveToFirstItemAfterRefresh(true);
		lv_info.setDoRefreshOnUIChanged(false);

		lv_info.setOnRefreshListener(new PullRefreshListView.OnRefreshListener() {
			@Override
			public void onRefresh() {
				adapterWorkLog.mList.clear();
				adapterWorkLog.notifyDataSetChanged();
				isRefresh = true;
				SwzfHttpHandler.PAGEINDEX_WORK_LOG_DG = 0;
				new SwzfHttpHandler(cb, getActivity()).getWorkLogDg(SwzfHttpHandler.PAGEINDEX_WORK_LOG_DG + "");
				;
			}
		});

		lv_info.setOnLoadListener(new PullRefreshListView.OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				isRefresh = false;
				SwzfHttpHandler.PAGEINDEX_WORK_LOG_DG++;
				new SwzfHttpHandler(cb, getActivity()).getWorkLogDg(SwzfHttpHandler.PAGEINDEX_WORK_LOG_DG + "");
				;
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
					ArrayList<EntityWorkLogDg> listWorkLog;
					listWorkLog = gson.fromJson(obj.getString("data"),
							new com.google.gson.reflect.TypeToken<ArrayList<EntityWorkLogDg>>() {
							}.getType());
					if (listWorkLog.size() < SwzfHttpHandler.PAGE_SIZE) {
						lv_info.setCanLoadMore(false);
					} else {
						lv_info.setCanLoadMore(true);
					}
					lv_info.changeEndViewByState();
					if (isRefresh) {
						GlobalData.listWokLogDg = (ArrayList<EntityWorkLogDg>) listWorkLog.clone();
					} else {
						GlobalData.listWokLogDg.addAll(listWorkLog);
					}

					adapterWorkLog.mList = GlobalData.listWokLogDg;
					adapterWorkLog.notifyDataSetChanged();
				} else {
					Util.createToast(getActivity(), obj.optString("message"), 3000).show();
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

	public void bindData() {
		lv_info.pull2RefreshManually();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		bindData();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	private static class ViewHolder {
		private TextView tv_offerid;
		private TextView tv_prodid;
		private TextView tv_publish_time;
		private TextView tv_trade_id;
		private TextView tv_order_status;
		private TextView tv_staff_name;
		private TextView tv_message;

	}

	class AdapterWorkLog extends BaseAdapter {
		ArrayList<EntityWorkLogDg> mList;
		private LayoutInflater mInflater;

		public AdapterWorkLog(ArrayList<EntityWorkLogDg> list) {
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
				mInflater = LayoutInflater.from(getActivity());
			}
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.item_worklog_dg, null);
				holder = new ViewHolder();
				holder.tv_offerid = (TextView) convertView.findViewById(R.id.tv_offerid);
				holder.tv_prodid = (TextView) convertView.findViewById(R.id.tv_prodid);
				holder.tv_publish_time = (TextView) convertView.findViewById(R.id.tv_publish_time);
				holder.tv_order_status = (TextView) convertView.findViewById(R.id.tv_order_status);
				holder.tv_trade_id = (TextView) convertView.findViewById(R.id.tv_trade_id);
				holder.tv_staff_name = (TextView) convertView.findViewById(R.id.tv_staff_name);
				holder.tv_message = (TextView) convertView.findViewById(R.id.tv_message);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv_offerid.setText("套餐名称: " + mList.get(position).OfferName + "(" + mList.get(position).OfferID + ")");
			holder.tv_prodid.setText("产品名称: " + mList.get(position).ProdName + "(" + mList.get(position).ProdID + ")");
			holder.tv_trade_id.setText("订单编号: " + mList.get(position).TradeID);
			holder.tv_order_status.setText("订购状态: " + (mList.get(position).State.equals("0") ? "订购成功" : "订购失败"));
			holder.tv_staff_name.setText("经办人员: " + mList.get(position).StaffName+"["+mList.get(position).DeptName+"]");
			holder.tv_message.setText("办理结果: " + mList.get(position).Message);
			holder.tv_publish_time.setText(mList.get(position).OccurTime);

			return convertView;
		}
	}

}
