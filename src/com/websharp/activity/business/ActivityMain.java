package com.websharp.activity.business;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.websharp.activity.BaseActivity;
import com.websharp.activity.fragment.FragmentChangeDevice;
import com.websharp.activity.fragment.FragmentCustomerBaseInfo;
import com.websharp.activity.fragment.FragmentCustomerUserList;
import com.websharp.activity.fragment.FragmentOrderPackage;
import com.websharp.activity.fragment.FragmentOrderProduct;
import com.websharp.activity.qr.CaptureActivity;
import com.websharp.activity.user.ActivityLogin;
import com.websharp.activity.user.ActivityUpdatePassword;
import com.websharp.dao.EntityClientConfig;
import com.websharp.dao.EntityCustomer;
import com.websharp.dao.EntityCustomerUser;
import com.websharp.dao.EntityOffer;
import com.websharp.dao.EntityProduct;
import com.websharp.dao.EntityStaticData;
import com.websharp.dao.EntityUser;
import com.websharp.data.Constant;
import com.websharp.data.GlobalData;
import com.websharp.http.AsyncHttpCallBack;
import com.websharp.http.SwzfHttpHandler;
import com.websharp.stb.R;
import com.websharputil.common.AppData;
import com.websharputil.common.LogUtil;
import com.websharputil.common.PrefUtil;
import com.websharputil.common.Util;
import com.websharputil.json.JSONUtils;
import com.websharputil.network.HttpUtil;

public class ActivityMain extends BaseActivity {

	LinearLayout layout_back;
	TextView tv_title, tv_dept;
	// EditText et_search_keyword;
	AutoCompleteTextView autotext;
	ImageView btn_qr;
	Button btn_search;

	ArrayAdapter arrayAdapter;

	LinearLayout layout_zlcx, layout_khcx, layout_ggck, layout_cancel, layout_work_log,layout_pay_order, layout_update_password;
	TextView tv_cancel;

	private LinearLayout layout_update;
	private ProgressBar pb_progress;
	private TextView tv_progress;
	private Button btn_update_download_exit;
	AsyncDownloadFile asyncDownloadFile;

	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
				long downloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
				validDownloadStatus(downloadID);
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_qr:
			Intent intent = new Intent(this, CaptureActivity.class);
			startActivityForResult(intent, Constant.QR_REQUEST_CODE_MAIN);
			break;
		case R.id.btn_search:
			
			if (!getText(autotext).isEmpty()) {
				new SwzfHttpHandler(cb, this).callSQL("1", getText(autotext));
			} else {
				Util.createToast(this, "请输入客户证号/机顶盒号/智能卡号/CMMAC 进行查询", Toast.LENGTH_LONG).show();
			}
			
			break;
		// case R.id.layout_info_search:
		// Util.startActivity(this, ActivitySearchCustomerInfo.class, false);
		// break;
		case R.id.layout_cancel:
			Util.createDialog(this, null, R.string.msg_dialog_title, R.string.msg_confirm_cancel, null, true, false,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							logout();
							dialog.dismiss();
						}
					}).show();
			break;
		case R.id.layout_pay_order:
			Util.startActivity(this, ActivityPayOrder.class, false);
			break;
		case R.id.tv_logout:
			showOrgSelectDialog();
			break;
		case R.id.layout_khcx:
			Util.startActivity(this, ActivityCustomerListSearch.class, false);
			break;
		case R.id.layout_ggck:
			Util.startActivity(this, ActivityNotice.class, false);
			break;
		case R.id.layout_work_log:
			Util.startActivity(this, ActivityWorkLog.class, false);
			break;
		case R.id.layout_zlcx:
			Bundle b = new Bundle();
			b.putString("auth_object_no", getText(autotext));
			Util.startActivity(this, ActivityZlcx.class, b, false);
			break;
		case R.id.layout_update_password:
			Util.startActivity(this, ActivityUpdatePassword.class, false);
			break;
		case R.id.btn_update_download_exit:
			try {
				asyncDownloadFile.stop = true;
				asyncDownloadFile.cancel(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

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
				if (obj.optInt("code") == 0) {
					JSONArray arrResult = obj.getJSONObject("data").getJSONArray("Result");
					if (arrResult.length() > 0) {
						Bundle b = new Bundle();
						b.putString("customerCode", getText(autotext));
						Util.startActivity(ActivityMain.this, ActivityCustomerInfo.class, b, false);
					} else {
						Util.createToast(ActivityMain.this, "找不到对应的客户信息", Toast.LENGTH_SHORT).show();
					}
				} else {
					Util.createToast(ActivityMain.this, obj.optString("message"), 3000).show();
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

	private void logout() {
		GlobalData.clear();
		PrefUtil.getEditor(this).clear().commit();
		Util.startActivity(this, ActivityLogin.class, true);
	}

	@Override
	public void initLayout() {
		setContentView(R.layout.activity_main);
	}

	@Override
	public void init() {

		IntentFilter filter = new IntentFilter();
		filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
		registerReceiver(receiver, filter);

		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_dept = (TextView) findViewById(R.id.tv_logout);
		btn_qr = (ImageView) findViewById(R.id.btn_qr);
		btn_search = (Button) findViewById(R.id.btn_search);
		layout_zlcx = (LinearLayout) findViewById(R.id.layout_zlcx);
		layout_khcx = (LinearLayout) findViewById(R.id.layout_khcx);
		layout_ggck = (LinearLayout) findViewById(R.id.layout_ggck);
		layout_cancel = (LinearLayout) findViewById(R.id.layout_cancel);
		layout_work_log = (LinearLayout) findViewById(R.id.layout_work_log);
		layout_pay_order = (LinearLayout) findViewById(R.id.layout_pay_order);
		layout_update_password = (LinearLayout)findViewById(R.id.layout_update_password);
		// et_search_keyword = (EditText) findViewById(R.id.et_search_keyword);
		autotext = (AutoCompleteTextView) findViewById(R.id.autotext);
		tv_cancel = (TextView) findViewById(R.id.tv_cancel);

		layout_update = (LinearLayout) findViewById(R.id.layout_update);
		pb_progress = (ProgressBar) findViewById(R.id.pb_progress);
		tv_progress = (TextView) findViewById(R.id.tv_progress);
		btn_update_download_exit = (Button) findViewById(R.id.btn_update_download_exit);
	}

	@Override
	public void bindData() {

		layout_back.setVisibility(View.GONE);
		tv_dept.setVisibility(View.VISIBLE);
		baseSetOnClickListener(this, layout_back, btn_qr, btn_search, tv_dept, layout_cancel, layout_ggck, layout_khcx,
				layout_zlcx, layout_work_log,layout_pay_order, layout_update_password, btn_update_download_exit);
		tv_title.setText("查询客户信息"); 
		tv_dept.setText("选择部门");
		tv_cancel.setText("注销\n" + GlobalData.curUser.staffName);
		// autotext.setText("8512003007619");
		// autotext.setText("04334943349433491");
		// autotext.setText("9999 9999 9999 9292");

		if (GlobalData.listStaticData.size() == 0) {
			new SwzfHttpHandler(cb3, this).getStaticData();
		}

		if (GlobalData.listAllOffer.size() == 0) {
			new SwzfHttpHandler(cb4, this).getAllPackage();
		}

		if (GlobalData.listAllProduct.size() == 0) {
			new SwzfHttpHandler(cb5, this).getAllProduct();
		}

		new SwzfHttpHandler(cb7, this).queryClientConfig();

		AppData.InitAppData(this, Constant.APP_NAME);

		String str_hisstory_customer_code = PrefUtil.getPref(this, "history_customer_code", "");
		GlobalData.listHistoryCustomerCode = new ArrayList<String>(
				Arrays.asList(str_hisstory_customer_code.split("####")));

		arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
				GlobalData.listHistoryCustomerCode);
		autotext.setAdapter(arrayAdapter);

		autotext.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				autotext.showDropDown();
			}
		});

		autotext.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				LogUtil.d("onItemClick:" + position);
				String content = GlobalData.listHistoryCustomerCode.get(position);
				if (content.indexOf("[") > 0) {
					try {
						content = content.substring(0, content.indexOf("["));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				autotext.setText(content);
				autotext.setSelection(autotext.getText().toString().length());
			}
		});

		showOrgSelectDialog();
		checkUpdateApk();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == Constant.QR_REQUEST_CODE_MAIN) {
				autotext.setText(data.getExtras().getString("data"));
				autotext.setSelection(autotext.getText().toString().length());// 将光标追踪到内容的最后
				btn_search.performClick();
			}
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiver);
	}

	AsyncHttpCallBack cb3 = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);

				if (obj.optInt("code") == 0) {
					Gson gson = new Gson();
					GlobalData.listStaticData = gson.fromJson(obj.getString("data"),
							new com.google.gson.reflect.TypeToken<ArrayList<EntityStaticData>>() {
							}.getType());
					// 1现金 2银行托收 3充值卡 4支票 5POS 6银行卡
					EntityStaticData ent1 = new EntityStaticData("PAY_TYPE", "1", "现金");
					EntityStaticData ent2 = new EntityStaticData("PAY_TYPE", "2", "银行托收");
					EntityStaticData ent3 = new EntityStaticData("PAY_TYPE", "3", "充值卡");
					EntityStaticData ent4 = new EntityStaticData("PAY_TYPE", "4", "支票");
					EntityStaticData ent5 = new EntityStaticData("PAY_TYPE", "5", "POS");
					EntityStaticData ent6 = new EntityStaticData("PAY_TYPE", "6", "银行卡");
					// 订单状态STATE
					// 1 新增
					// 2 修改
					// 3 退订
					// 4 正常
					EntityStaticData ent7 = new EntityStaticData("PRODUCT_STATE", "1", "新增");
					EntityStaticData ent8 = new EntityStaticData("PRODUCT_STATE", "2", "修改");
					EntityStaticData ent9 = new EntityStaticData("PRODUCT_STATE", "3", "取消");
					EntityStaticData ent10 = new EntityStaticData("PRODUCT_STATE", "4", "正常");

					EntityStaticData ent11 = new EntityStaticData("DEAL_STATE", "0", "待处理，默认");
					EntityStaticData ent12 = new EntityStaticData("DEAL_STATE", "1", "处理中");
					EntityStaticData ent13 = new EntityStaticData("DEAL_STATE", "2", "处理成功");
					EntityStaticData ent14 = new EntityStaticData("DEAL_STATE", "3", "处理失败");

					GlobalData.listStaticData.add(ent1);
					GlobalData.listStaticData.add(ent2);
					GlobalData.listStaticData.add(ent3);
					GlobalData.listStaticData.add(ent4);
					GlobalData.listStaticData.add(ent5);
					GlobalData.listStaticData.add(ent6);
					GlobalData.listStaticData.add(ent7);
					GlobalData.listStaticData.add(ent8);
					GlobalData.listStaticData.add(ent9);
					GlobalData.listStaticData.add(ent10);
					GlobalData.listStaticData.add(ent11);
					GlobalData.listStaticData.add(ent12);
					GlobalData.listStaticData.add(ent13);
					GlobalData.listStaticData.add(ent14);

				} else {
					Util.createToast(ActivityMain.this, obj.optString("message"), 3000).show();
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

	AsyncHttpCallBack cb4 = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);

				if (obj.optInt("code") == 0) {
					Gson gson = new Gson();
					GlobalData.listAllOffer = gson.fromJson(obj.getString("data"),
							new com.google.gson.reflect.TypeToken<ArrayList<EntityOffer>>() {
							}.getType());
				} else {
					Util.createToast(ActivityMain.this, obj.optString("message"), 3000).show();
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

	AsyncHttpCallBack cb5 = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);

				if (obj.optInt("code") == 0) {
					Gson gson = new Gson();
					GlobalData.listAllProduct = gson.fromJson(obj.getString("data"),
							new com.google.gson.reflect.TypeToken<ArrayList<EntityProduct>>() {
							}.getType());
				} else {
					Util.createToast(ActivityMain.this, obj.optString("message"), 3000).show();
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

	/**
	 * 让登录人必须选择一个所属机构
	 */
	private void showOrgSelectDialog() {
		String[] arrayOrg = new String[GlobalData.listOrg.size()];
		for (int i = 0; i < GlobalData.listOrg.size(); i++) {
			arrayOrg[i] = "[" + GlobalData.listOrg.get(i).organization_code + "]"
					+ GlobalData.listOrg.get(i).organization_name;
		}

		if (GlobalData.listOrg.size() == 1) {

			GlobalData.curUserOrg = GlobalData.listOrg.get(0);
			LogUtil.d("orgcode:" + GlobalData.curUserOrg.organization_code);
			tv_dept.setText(GlobalData.curUserOrg.organization_name);
			new SwzfHttpHandler(cb6, ActivityMain.this).changeOrg(GlobalData.curUserOrg.organization_code);
		} else {
			Dialog alertDialog = new AlertDialog.Builder(this).setTitle(GlobalData.curUser.staffName + ",请选择所属机构")
					.setIcon(null).setItems(arrayOrg, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

							GlobalData.curUserOrg = GlobalData.listOrg.get(which);
							LogUtil.d("orgcode:" + GlobalData.curUserOrg.organization_code);
							tv_dept.setText(GlobalData.curUserOrg.organization_name);
							new SwzfHttpHandler(cb6, ActivityMain.this)
									.changeOrg(GlobalData.curUserOrg.organization_code);
							dialog.dismiss();
						}
					}).create();
			alertDialog.setCancelable(false);
			alertDialog.setCanceledOnTouchOutside(false);
			alertDialog.show();
		}
	}

	AsyncHttpCallBack cb6 = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			LogUtil.d("%s", response);

		}

		@Override
		public void onFailure(String message) {
			super.onFailure(message);
			LogUtil.d("%s", message);
		}

	};

	AsyncHttpCallBack cb7 = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);

				if (obj.optInt("code") == 0) {
					Gson gson = new Gson();
					GlobalData.listClientConfig = gson.fromJson(obj.getString("data"),
							new com.google.gson.reflect.TypeToken<ArrayList<EntityClientConfig>>() {
							}.getType());
					// Util.createToast(ActivityMain.this,
					// "ClientConfig,Size:"+GlobalData.listClientConfig.size(),
					// 3000).show();
				} else {
					Util.createToast(ActivityMain.this, obj.optString("message"), 3000).show();
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

	/*
	 * 查询版本更新相关
	 */
	public static boolean IS_NEED_UPDATE = false;
	public static String URL_NEW_VERSION = "";
	public static String UPDATE_CONTENT = "";

	DownloadManager downloadmanager = null;

	public void checkUpdateApk() {
		new SwzfHttpHandler(cbCheckVersion, ActivityMain.this).checkVersion(this);
	}

	AsyncHttpCallBack cbCheckVersion = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);

				if (obj.optInt("code") == 0) {
					JSONObject json = obj.getJSONObject("data");
					int versionCode = json.optInt("VersionCode", 0);
					IS_NEED_UPDATE = AppData.VERSION_CODE < versionCode ? true : false;
					URL_NEW_VERSION = SwzfHttpHandler.BASE_URL + json.optString("DownloadURL");
					UPDATE_CONTENT = json.optString("UpdateContent", "");
					if (IS_NEED_UPDATE) {
						showUpdateApkDialog(ActivityMain.this, Constant.DOWNLOAD_APK_NAME, URL_NEW_VERSION,
								UPDATE_CONTENT);
					}
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

	public void showUpdateApkDialog(final Context context, final String savePath, final String url_download,
			final String update_content) {

		StringBuffer sb = new StringBuffer();
		sb.append("更新内容:\n" + update_content);
		Dialog dialog = new AlertDialog.Builder(context).setTitle(R.string.welcome_dialog_version_msg)
				.setMessage(sb.toString())
				.setPositiveButton(R.string.welcome_dialog_version_confirm, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						startDownloadApk(savePath, url_download);
					}
				}).setNegativeButton(R.string.welcome_dialog_version_cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
					}
				}).create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		// }
	}

	// public long startDownloadApk(Context context, String savePath, String
	// url_download) {
	// if (!new File(Constant.SAVE_PATH).exists()) {
	// new File(Constant.SAVE_PATH).mkdirs();
	// }
	// if (new File(savePath).exists()) {
	// new File(savePath).delete();
	// }
	// downloadmanager = (DownloadManager)
	// context.getSystemService(DOWNLOAD_SERVICE);
	// Request req = new DownloadManager.Request(Uri.parse(url_download));
	// req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE |
	// DownloadManager.Request.NETWORK_WIFI);
	// req.setAllowedOverRoaming(false);
	// req.setNotificationVisibility(Request.VISIBILITY_VISIBLE);
	// req.setDestinationUri(Uri.fromFile(new File(savePath)));
	// long tmp = downloadmanager.enqueue(req);
	// return tmp;
	// }

	private void startDownloadApk(String savePath, String url_download) {
		if (!new File(Constant.SAVE_PATH).exists()) {
			new File(Constant.SAVE_PATH).mkdirs();
		}
		if (new File(savePath).exists()) {
			new File(savePath).delete();
		}
		asyncDownloadFile = new AsyncDownloadFile();
		asyncDownloadFile.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url_download);

	}

	/**
	 * 监听是否下载apk成功,这个方法放在receiver中用来监听 android.intent.action.DOWNLOAD_COMPLETE
	 * android.intent.action.DOWNLOAD_NOTIFICATION_CLICKED
	 * 
	 * @param downloadmanager
	 * @param downloadID
	 * @param context
	 * @param apkPath
	 */
	public void validDownloadStatus(long downloadID) {
		DownloadManager.Query query = new DownloadManager.Query();
		query.setFilterById(downloadID);
		Cursor c = downloadmanager.query(query);
		int status = -1;
		if (c != null && c.moveToFirst()) {
			status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
		}
		c.close();
		if (status == 8) {
			LogUtil.d("%s", "下载成功");
			// 下载成功,调用系统的安装程序
			updateAndInstall();
		}
	}

	/**
	 * 安装/覆盖软件
	 * 
	 * @param context
	 * @param apkPath
	 */
	public void updateAndInstall() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.fromFile(new File(Constant.DOWNLOAD_APK_NAME)),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}

	class AsyncDownloadFile extends AsyncTask<String, String, String> {
		boolean stop = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// showDialog(DIALOG_DOWNLOAD_PROGRESS);
			layout_update.setVisibility(View.VISIBLE);
		}

		@Override
		protected String doInBackground(String... aurl) {
			int count;

			try {
				URL url = new URL(aurl[0]);
				URLConnection conexion = url.openConnection();
				conexion.connect();

				int lenghtOfFile = conexion.getContentLength();
				// Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);

				InputStream input = new BufferedInputStream(url.openStream());
				OutputStream output = new FileOutputStream(Constant.DOWNLOAD_APK_NAME);

				byte data[] = new byte[1024];
				long total = 0;

				while ((count = input.read(data)) != -1 && !stop) {
					total += count;
					publishProgress("" + (int) ((total * 100) / lenghtOfFile));
					output.write(data, 0, count);
				}
				output.flush();
				output.close();
				input.close();

			} catch (Exception e) {
				e.printStackTrace();
				return Constant.RESULT_FAILED;
			}
			return Constant.RESULT_SUCCESS;
		}

		protected void onProgressUpdate(String... progress) {
			pb_progress.setProgress(Integer.parseInt(progress[0]));
			tv_progress.setText(Integer.parseInt(progress[0]) + "%");
		}

		@Override
		protected void onCancelled(String result) {
			super.onCancelled(result);
			layout_update.setVisibility(View.GONE);
			if (stop) {
				File f = new File(Constant.DOWNLOAD_APK_NAME);
				if (f.exists()) {
					f.delete();
				}
			}
		}

		@Override
		protected void onPostExecute(String unused) {
			layout_update.setVisibility(View.GONE);
			if (unused.equals(Constant.RESULT_SUCCESS)) {
				ActivityMain.this.finish();
				updateAndInstall();
			} else {
				Toast.makeText(ActivityMain.this, "下载失败!", 2000).show();
			}
		}
	}

}
