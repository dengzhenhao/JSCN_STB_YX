package com.websharp.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mslibs.utils.JSONUtils;
import com.mslibs.utils.NotificationsUtil;
import com.mslibs.widget.CActivity;
import com.mslibs.widget.CListView;
import com.mslibs.widget.CListViewParam;
import com.websharp.dao.EntityBillRecord;
import com.websharp.dao.EntityStaticData;
import com.websharp.data.GlobalData;
import com.websharp.http.AsyncHttpListCallBack;
import com.websharp.http.SwzfHttpHandler;
import com.websharp.stb.R;
import com.websharputil.common.ConvertUtil;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;
import com.websharputil.date.DateUtil;

/**
 *
 * 
 * @author dengzh
 * 
 */
public class AdapterBillRecordList extends CListView {

	public String caseSource = "0";

	public AdapterBillRecordList(PullToRefreshListView lv, Activity activity) {
		super(lv, activity);
		initListViewStart();
	}

	@Override
	public void initListItemResource() {
		// setHeaderResource(R.layout.headview_orderlist);
		setListItemResource(R.layout.item_xfzd);
	}

	@Override
	public void ensureUi() {
		mPerpage = 20;

		super.setGetMoreResource(R.layout.item_list_getmore,
				R.id.list_item_getmore_title, mActivity.getResources()
						.getString(R.string.title_pulltorefresh_get_more));
		super.ensureUi();
		mListViewPTR
				.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
					@Override
					public void onLastItemVisible() {
						if (actionType == IDLE && !mAdapter.isNotMore) {
							// 如果并没有在加载过程中,可以加载更多
							getmoreListViewStart();
						}
					}
				});
		// super.setGetMoreClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// getmoreListViewStart();
		// }
		// });
		
		super.setItemOnclickLinstener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//
				LogUtil.d("index:%s", v.getTag());
				LinearLayout layout_container = (LinearLayout) ((View) v.getParent()).findViewById(R.id.layout_container);
				LogUtil.d("container:%s", layout_container.toString());
				if (layout_container.getVisibility() == View.GONE) {
					layout_container.setVisibility(View.VISIBLE);
				} else {
					layout_container.setVisibility(View.GONE);
				}
			}
		});
	}

	@Override
	public ArrayList<CListViewParam> matchListItem(Object obj, int index) {
		LogUtil.d("%s", index);
		final EntityStaticData item = (EntityStaticData) obj;
		ArrayList<CListViewParam> LVP = new ArrayList<CListViewParam>();
		LVP.add(new CListViewParam(R.id.tv_acct_item_type, item.CODE_NAME, true));
		LVP.add(new CListViewParam(R.id.tv_total_amount, item.CODE_VALUE, true));
//		LVP.add(new CListViewParam(R.id.tv_Time, item.CreateTime, true));
//		CListViewParam paramBg = new CListViewParam(R.id.layout_bg, null, true);
//		if (index % 2 == 0) {
//			paramBg.setItemBackgroundDrawable(mActivity.getResources()
//					.getDrawable(R.drawable.selector_common_layout_list));
//		} else {
//			paramBg.setItemBackgroundDrawable(mActivity.getResources()
//					.getDrawable(R.drawable.selector_common_layout_trans));
//		}
//		LVP.add(paramBg);
		return LVP;
	}

	@Override
	public void asyncData() {
		super.asyncData();
		try {
			new SwzfHttpHandler(callback, mActivity).getStaticData();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		EntityBillRecord record = new EntityBillRecord();
//		record.ACCT_ITEM_TYPE = "www";
//		record.TOTAL_AMOUNT = "9999";
//		GlobalData.listBillRecord.add(record);
		//refreshListViewFinish();
	}

	AsyncHttpListCallBack<ArrayList<EntityStaticData>> callback = new AsyncHttpListCallBack<ArrayList<EntityStaticData>>(
			AdapterBillRecordList.this) {
		@Override
		public void setType() {
			myType = new TypeToken<ArrayList<EntityStaticData>>() {
			}.getType();
		}

		@Override
		public void addItems() {
			if (mT != null && !mT.isEmpty()) {
				for (int i = 0; i < mT.size(); i++) {
					mListItems.add(mT.get(i));
				}
				GlobalData.listStaticData = (ArrayList<EntityStaticData>) mListItems
						.clone();
				if (listener != null) {
					listener.listenerAferQuery();
				}
			} else {
				NotificationsUtil.ToastBottomMsg(mActivity, mActivity
						.getResources().getString(R.string.msg_no_result));
				return;
			}
		}
	};
	ListenerAfterQueryCaseList listener;

	public void setOnListenerAfterQueryCaseList(
			ListenerAfterQueryCaseList listenerAfter) {
		listener = listenerAfter;
	}

	public interface ListenerAfterQueryCaseList {
		public void listenerAferQuery();
	}

}
