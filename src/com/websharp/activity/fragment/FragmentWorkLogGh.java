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
import com.websharp.dao.EntityWorkLogGh;
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
public class FragmentWorkLogGh extends Fragment implements View.OnClickListener {
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
		adapterWorkLog = new AdapterWorkLog(new ArrayList<EntityWorkLogGh>());
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
				SwzfHttpHandler.PAGEINDEX_WORK_LOG_GH = 0;
				new SwzfHttpHandler(cb, getActivity()).getWorkLogGh(SwzfHttpHandler.PAGEINDEX_WORK_LOG_GH+"");;
			}
		});

		lv_info.setOnLoadListener(new PullRefreshListView.OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				isRefresh = false;
				SwzfHttpHandler.PAGEINDEX_WORK_LOG_GH++;
				new SwzfHttpHandler(cb, getActivity()).getWorkLogGh(SwzfHttpHandler.PAGEINDEX_WORK_LOG_GH+"");;
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
					ArrayList<EntityWorkLogGh> listWorkLog;
					listWorkLog = gson.fromJson(obj.getString("data"),
							new com.google.gson.reflect.TypeToken<ArrayList<EntityWorkLogGh>>() {
							}.getType());
					if (listWorkLog.size() < SwzfHttpHandler.PAGE_SIZE) {
						lv_info.setCanLoadMore(false);
					} else {
						lv_info.setCanLoadMore(true);
					}
					lv_info.changeEndViewByState();
					if (isRefresh) {
						GlobalData.listWokLogGh = (ArrayList<EntityWorkLogGh>) listWorkLog.clone();
					} else {
						GlobalData.listWokLogGh.addAll(listWorkLog);
					}

					adapterWorkLog.mList = GlobalData.listWokLogGh;
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
		private TextView tv_user;
		private TextView tv_old_no;
		private TextView tv_new_no;
		private TextView tv_remark;
		private TextView tv_publish_time;
	}

	class AdapterWorkLog extends BaseAdapter {
		ArrayList<EntityWorkLogGh> mList;
		private LayoutInflater mInflater;

		public AdapterWorkLog(ArrayList<EntityWorkLogGh> list) {
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
				convertView = mInflater.inflate(R.layout.item_worklog_gh, null);
				holder = new ViewHolder();
				holder.tv_user = (TextView) convertView.findViewById(R.id.tv_user);
				holder.tv_new_no = (TextView) convertView.findViewById(R.id.tv_new_no);
				holder.tv_old_no = (TextView) convertView.findViewById(R.id.tv_old_no);
				holder.tv_remark = (TextView) convertView.findViewById(R.id.tv_remark);
				holder.tv_publish_time = (TextView) convertView.findViewById(R.id.tv_publish_time);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv_user.setText("["+mList.get(position).OrgCode+"]"+mList.get(position).UserName);
			holder.tv_old_no.setText("旧机顶盒号:"+mList.get(position).OldStbNo);
			holder.tv_new_no.setText("新机顶盒号:"+mList.get(position).NewStbNo);
			holder.tv_remark.setText("备注:"+mList.get(position).Remark);
			holder.tv_publish_time.setText(mList.get(position).OccurTime);

			return convertView;
		}
	}

	

}
