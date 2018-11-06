package com.websharp.activity.business;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.websharp.activity.BaseActivity;
import com.websharp.activity.fragment.FragmentWorkLogDg;
import com.websharp.activity.fragment.FragmentWorkLogPage;
import com.websharp.activity.fragment.FragmentWorkorderPage;
import com.websharp.dao.EntityAccountBook;
import com.websharp.dao.EntityCustomerOrder;
import com.websharp.dao.EntityNotice;
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

public class ActivityWorkLog extends FragmentActivity  implements View.OnClickListener {
	LinearLayout layout_back;
	TextView tv_title;

	FragmentWorkLogPage fragPage;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_back:
			finish();
			break;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initLayout();
		init();
	}

	@Override
	protected void onStart() {
		super.onStart();
		bindData();
	}

	public void initLayout() {
		setContentView(R.layout.activity_work_log);
	}

	public void init() {
		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		fragPage = (FragmentWorkLogPage) getSupportFragmentManager()
				.findFragmentById(R.id.frag_work_log_page);
	}

	public void bindData() {
		baseSetOnClickListener(this, layout_back);
		tv_title.setText("操作日志");
	}

	public void baseSetOnClickListener(View.OnClickListener listener, View... views) {
		for (int i = 0; i < views.length; i++) {
			views[i].setOnClickListener(listener);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}


}
