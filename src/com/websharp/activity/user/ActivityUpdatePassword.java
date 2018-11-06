package com.websharp.activity.user;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.websharp.activity.BaseActivity;
import com.websharp.activity.business.ActivityMain;
import com.websharp.activity.list.ActivityCzjl;
import com.websharp.activity.list.ActivityDbjl;
import com.websharp.activity.list.ActivityXfzd;
import com.websharp.activity.list.ActivityYbjdd;
import com.websharp.dao.EntityCustomerPackage;
import com.websharp.dao.EntityOrg;
import com.websharp.dao.EntityUser;
import com.websharp.data.Constant;
import com.websharp.data.GlobalData;
import com.websharp.http.AsyncHttpCallBack;
import com.websharp.http.SwzfHttpHandler;
import com.websharp.stb.R;
import com.websharputil.common.AppData;
import com.websharputil.common.LogUtil;
import com.websharputil.common.PrefUtil;
import com.websharputil.common.StringUtil;
import com.websharputil.common.Util;
import com.websharputil.json.JSONUtils;

public class ActivityUpdatePassword extends BaseActivity {

	LinearLayout layout_back;
	TextView tv_title;
	EditText et_old_password;
	EditText et_new_password;
	EditText et_new_password_repeat;
	Button btn_update_password;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_update_password:
			try {
				AsyncUpdatePassword();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break; 
		case R.id.layout_back:
			finish();
			break;
		}
	}

	@Override
	public void initLayout() {
		setContentView(R.layout.activity_update_password);
	}

	@Override
	public void init() {
		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		et_old_password = (EditText) findViewById(R.id.et_old_password);
		et_new_password = (EditText) findViewById(R.id.et_new_password);
		et_new_password_repeat = (EditText) findViewById(R.id.et_new_password_repeat);
		btn_update_password = (Button) findViewById(R.id.btn_update_password);
	}

	@Override
	public void bindData() {
		layout_back.setOnClickListener(this);
		tv_title.setText("修改密码");
		btn_update_password.setOnClickListener(this);

	}

	AsyncHttpCallBack cb = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);
				if (obj.optString("code").equals("0")) {
					String str = PrefUtil.getPref(ActivityUpdatePassword.this, "user", "");
					if (!str.isEmpty()) {
						JSONObject jsonUser = new JSONObject(str); 
						jsonUser.put("loginPwd", getText(et_new_password));
						PrefUtil.setPref(ActivityUpdatePassword.this, "user", jsonUser.toString());
						GlobalData.curUser = JSONUtils.fromJson(str, EntityUser.class);
					} 
					Util.createToast(ActivityUpdatePassword.this, R.string.common_update_password_success, Toast.LENGTH_SHORT)
							.show(); 
					finish();
				} else {
					Util.createToast(ActivityUpdatePassword.this, obj.optString("message"), 3000).show();
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

	private void AsyncUpdatePassword() throws Exception {
		String old_password = getText(et_old_password);
		String new_password = getText(et_new_password);
		String new_password_repeat = getText(et_new_password_repeat);

		if (old_password.isEmpty()) {
			Util.createToast(this, "旧密码不能为空!", Toast.LENGTH_LONG).show();
			return;
		}

		if (new_password.isEmpty()) {
			Util.createToast(this, "新密码不能为空!", Toast.LENGTH_LONG).show();
			return;
		}

		if (new_password_repeat.isEmpty()) {
			Util.createToast(this, "确认新密码不能为空!", Toast.LENGTH_LONG).show();
			return;
		}

		if (!new_password_repeat.equals(new_password)) {
			Util.createToast(this, "新密码两次输入不一致!", Toast.LENGTH_LONG).show();
			return;
		} 
		
		if (new_password.equals(old_password)) {
			Util.createToast(this, "新密码不能与旧密码一样!", Toast.LENGTH_LONG).show();
			return;
		}

		new SwzfHttpHandler(cb, ActivityUpdatePassword.this).updatePassword(ActivityUpdatePassword.this,
				GlobalData.curUser.loginCode, old_password, new_password);

	}

	public static String encode(String a) throws Exception {

		char[] ch = a.toCharArray();
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i <= ch.length; ++i) {
			int tmp = ch[(i - 1)];
			tmp = tmp * i + ch.length;
			tmp %= 96;
			tmp = (tmp + 32) % 128;
			sb.append((char) tmp);
		}
		LogUtil.d("encoding:%s,%s", a, sb.toString());
		return sb.toString();
	}

}
