package com.websharp.activity.business;

import java.util.ArrayList;

import com.websharp.activity.BaseActivity;
import com.websharp.activity.business.ActivityNotice.AdapterNotice;
import com.websharp.dao.EntityNotice;
import com.websharp.data.GlobalData;
import com.websharp.http.SwzfHttpHandler;
import com.websharp.stb.R;
import com.websharp.widget.PullRefreshListView;

import android.os.Bundle;
import android.view.View;
import android.view.textservice.TextInfo;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActivityNoticeContent extends BaseActivity {

	LinearLayout layout_back;
	TextView tv_title;
	TextView tv_notice_title;
	TextView tv_publish_time;
	TextView tv_content;

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
		setContentView(R.layout.activity_notice_content);
	}

	@Override
	public void init() {
		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_notice_title = (TextView) findViewById(R.id.tv_notice_title);
		tv_publish_time = (TextView) findViewById(R.id.tv_publish_time);
		tv_content = (TextView) findViewById(R.id.tv_content);
	}

	@Override
	public void bindData() {
		layout_back.setOnClickListener(this);
		tv_title.setText("公告内容");
		tv_notice_title.setText(GlobalData.curNotice.AFFICHE_TITLE);
		tv_publish_time.setText(GlobalData.curNotice.PUBLISH_DATE);
		tv_content.setText(GlobalData.curNotice.AFFICHE_CONTENT);
	}

}
