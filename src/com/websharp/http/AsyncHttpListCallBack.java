package com.websharp.http;

import java.lang.reflect.Type;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.mslibs.utils.NotificationsUtil;
import com.mslibs.utils.VolleyLog;
import com.mslibs.widget.CActivity;
import com.mslibs.widget.CListView;
import com.websharputil.common.LogUtil;

public class AsyncHttpListCallBack<T> extends AsyncHttpCallBack {
	protected T mT;

	protected Type myType;

	protected CListView mListView;

	public AsyncHttpListCallBack(CListView listView) {
		mListView = listView;
	}

	public void setType() {

	}

	public String preProcess(String response) {
		return response;
	}

	public void addItems() {

	}

	@Override
	public void onSuccess(String response) {
		super.onSuccess(response);
		//((CActivity) mListView.mActivity).dismissProgress();
		response = preProcess(response);

		setType();

		Gson gson = new Gson();
		LogUtil.d("列表数据:%s", response);
		try {
			JSONObject obj = new JSONObject(response);
			if (obj.optString("code", "").equals("0")) {
				mT = gson.fromJson(obj.getJSONArray("data").toString(), myType);
			}
		} catch (Exception e) {
			VolleyLog.e(e, e.toString());
		}

		if (CListView.GETMORE != mListView.actionType) {
			mListView.mListItems.clear();
			mListView.mDateList.clear();
		}

		addItems();

		switch (mListView.actionType) {
		case CListView.INIT:
			mListView.initListViewFinish();
			break;
		case CListView.REFRESH:
			mListView.refreshListViewFinish();
			break;
		case CListView.GETMORE:
			mListView.getmoreListViewFinish();
			break;
		}

		mListView.actionType = CListView.IDLE;
	}

	@Override
	public void onFailure(String message) {
		//((CActivity) mListView.mActivity).dismissProgress();
		mListView.refreshListViewFinish();
		mListView.actionType = CListView.IDLE;
	}
}
