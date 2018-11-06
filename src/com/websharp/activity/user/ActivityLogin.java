package com.websharp.activity.user;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class ActivityLogin extends BaseActivity {

	EditText et_username;
	EditText et_password;
	Button btn_login;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			try {
				AsyncLogin();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Util.startActivity(ActivityLogin.this, ActivityMain.class, true);
			break;
		}
	}

	@Override
	public void initLayout() { 
		setContentView(R.layout.activity_login);
	}

	@Override
	public void init() {
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);
		btn_login = (Button) findViewById(R.id.btn_login);
	}

	@Override
	public void bindData() {
		btn_login.setOnClickListener(this);

		Constant.InitDefaultDirs();
		AppData.InitAppData(this, Constant.APP_NAME);
		String str = PrefUtil.getPref(ActivityLogin.this, "user", "");
		if (!str.isEmpty()) {
			GlobalData.curUser = JSONUtils.fromJson(str, EntityUser.class);
			et_username.setText(GlobalData.curUser.loginCode);
			et_password.setText(GlobalData.curUser.loginPwd);
			btn_login.performClick();
		}
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
					JSONObject jsonUser = obj.getJSONObject("data");
					jsonUser.put("loginPwd", getText(et_password));
					PrefUtil.setPref(ActivityLogin.this, "user", jsonUser.toString());
					GlobalData.curUser = JSONUtils.fromJson(jsonUser.toString(), EntityUser.class);
					Gson gson = new Gson(); 
					GlobalData.listOrg = gson.fromJson(jsonUser.getString("organizations"),
							new com.google.gson.reflect.TypeToken<ArrayList<EntityOrg>>() {
							}.getType());
					initWithApiKey(ActivityLogin.this);
					Util.createToast(ActivityLogin.this, R.string.common_login_success, Toast.LENGTH_SHORT).show();
					Util.startActivity(ActivityLogin.this, ActivityMain.class, true);
				} else {
					Util.createToast(ActivityLogin.this, obj.optString("message"),
							3000).show();
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

	private void AsyncLogin() throws Exception {
		String userName = getText(et_username);
		String password = getText(et_password);
		
		if (password.isEmpty() || userName.isEmpty()) {
			Util.createToast(this, "用户名与密码不能为空!", Toast.LENGTH_LONG).show();
			return;
		}
 
		if (StringUtil.isContainChinese(password)) {
			Util.createToast(this, "密码不能包含中文", Toast.LENGTH_LONG).show();
		}
		
		new SwzfHttpHandler(cb, ActivityLogin.this).login(ActivityLogin.this,userName,URLEncoder.encode(password));
		
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
