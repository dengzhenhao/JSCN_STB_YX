package com.websharp.http;

import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.apache.http.Header;
import org.apache.http.client.HttpResponseException;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.websharp.activity.user.ActivityLogin;
import com.websharputil.common.LogUtil;
import com.websharputil.widget.LoadingProgressDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Looper;

public class BaseHttpHandler {

	public static final String EXCEPTION_UNKNOWNHOST = "unknownhost";
	public static final String EXCEPTION_CONNECT = "connect";
	public static final String EXCEPTION_SOCKET = "socket";
	public static final String EXCEPTION_SOCKET_TIMEOUT = "socket_timeout";

	protected AsyncHttpCallBack mCallBack;
	protected Context mContext;
	protected Object extra;

	LoadingProgressDialog loadingDialog;

	private static boolean mDialogShowing;

	private void showProgressDialog() {
		if (loadingDialog == null) {
			loadingDialog = LoadingProgressDialog.createDialog(mContext);
			loadingDialog.setCanceledOnTouchOutside(false);
			loadingDialog.setCancelable(false);
			// progressDialog.setMessage("正在进行操作...");
		}
		try {
			loadingDialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void hideProgressDialog() {
		try {
			if (loadingDialog != null) {
				loadingDialog.dismiss();
				loadingDialog = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public BaseHttpHandler(AsyncHttpCallBack callback, Context context) {
		mCallBack = callback;
		mContext = context;
	}

	public void setExtra(Object extra) {
		this.extra = extra;
	}

	protected AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

		public void onStart() {
			if (mCallBack.isShowDialog()) {
				showProgressDialog();
			}
			mCallBack.onPrea();

		};

		public void onFinish() {
			if (mCallBack.isShowDialog()) {
				hideProgressDialog();
			}
			mCallBack.onPost();
		};

		@Override
		public void onSuccess(int status, Header[] arg1, byte[] arg2) {
			LogUtil.d("onSuccess:%s", "onSuccess" + status);
			String response = "";
			try {
				response = new String(arg2, "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (null == mCallBack || null == mContext) {
				return;
			}

			if (extra != null) {
				mCallBack.setExtra(extra);
			}

			mCallBack.onSuccess(response);
		}

		@Override
		public void onFailure(final int status, Header[] arg1, byte[] arg2, Throwable e) {
			LogUtil.d("onFailure:%s", "onFailure" + status);
			if (e instanceof UnknownHostException) {
				LogUtil.e(e, EXCEPTION_UNKNOWNHOST);
			} else if (e instanceof ConnectException) {
				LogUtil.e(e, EXCEPTION_CONNECT);
			} else if (e instanceof SocketException) {
				LogUtil.e(e, EXCEPTION_SOCKET);
			} else if (e instanceof SocketTimeoutException) {
				LogUtil.e(e, EXCEPTION_SOCKET_TIMEOUT);
			} else if (e instanceof HttpResponseException) {

			}

			if (null == mCallBack || null == mContext) {
				return;
			}

			mCallBack.onFailure(e.getClass().getSimpleName());
			try {
				if (!((Activity) mContext).isFinishing()) {
					if (!mDialogShowing) {
						mDialogShowing = true;
						String message = "1.检查您的网络设置\n2.当前服务器不稳定";
						String title = "网络故障";
						String btn_string = "网络设置";
						if (status == 401) {
							message = "帐号已在别处登录，请重新登录";
							title = "帐号异常";
							btn_string = "确定";
						}
						new AlertDialog.Builder(mContext).setTitle(title).setMessage(message)
								.setPositiveButton(btn_string, new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										mDialogShowing = false;
										if (status != 401) {
											Intent intent = null;
											// 判断手机系统的版本 即API大于10 就是3.0或以上版本
											if (android.os.Build.VERSION.SDK_INT > 10) {
												intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
											} else { 
												intent = new Intent();
												ComponentName component = new ComponentName("com.android.settings",
														"com.android.settings.WirelessSettings");
												intent.setComponent(component);
												intent.setAction("android.intent.action.VIEW");
											}
											mContext.startActivity(intent);
										}else{
											Intent intent = new Intent(mContext, ActivityLogin.class);
											intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
											mContext.startActivity(intent);
										}
										dialog.dismiss();
									}
								}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int whichButton) {
										mDialogShowing = false;
										dialog.dismiss();
									}
								}).setCancelable(false).show();

					}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	};

}
