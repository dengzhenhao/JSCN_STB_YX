package com.websharp.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;

import com.websharp.data.Constant;
import com.websharp.stb.R;
import com.websharputil.common.AppData;
import com.websharputil.common.Util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @类名称：BaseActivity
 * @包名：com.websharp.mix.activity
 * @描述： TODO
 * @创建人： dengzh
 * @创建时间:2014-12-10 下午4:45:02
 * @版本 V1.0
 * @Copyright (c) 2014 by 苏州威博世网络科技有限公司.
 */
public abstract class BaseActivity extends Activity implements
		View.OnClickListener { 

	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		try {
			// StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
			// .detectDiskReads().detectDiskWrites().detectNetwork()
			// .penaltyLog().build());
			// StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
			// .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
			// .penaltyLog().penaltyDeath().build());

		} catch (Exception e) {
		}

		super.onCreate(savedInstanceState);
		setLockPatternEnabled(false);
		initLayout();
		init();
		bindData();
	}

	public void baseSetOnClickListener(View.OnClickListener listener,
			View... views) {
		for (int i = 0; i < views.length; i++) {
			views[i].setOnClickListener(listener);
		}
	}

	public void baseSetTextEmpty(View... views) {
		for (int i = 0; i < views.length; i++) {
			if (views[i] instanceof TextView) {
				((TextView) views[i]).setText("");
			} else if (views[i] instanceof EditText) {
				((EditText) views[i]).setText("");
			}
		}
	}

	// 以apikey的方式绑定
	public void initWithApiKey(Context ctx) {
		// Push: 无账号初始化，用api key绑定

	}

	public abstract void initLayout();

	public abstract void init();

	public abstract void bindData();

	public String getText(EditText et) {
		return et.getText().toString().trim();
	}

	public String getText(TextView tv) {
		return tv.getText().toString().trim();
	}

	public boolean isEmpty(EditText et) {
		return et.getText().toString().trim().isEmpty();
	}

	public void setLockPatternEnabled(boolean enabled) {
		setBoolean(android.provider.Settings.System.LOCK_PATTERN_ENABLED,
				enabled);
	}

	private void setBoolean(String systemSettingKey, boolean enabled) {
		android.provider.Settings.System.putInt(getContentResolver(),
				systemSettingKey, enabled ? 1 : 0);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Util.finishActivity(this);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void initParam() {
		AppData.InitAppData(this, Constant.APP_NAME);
	}

	/**
	 * 收起软键盘并设置提示文字
	 */
	public void collapseSoftInputMethod(EditText et) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(et.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * 显示软键盘并设置提示文字
	 */
	public void showSoftInputMethod(EditText et) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
	}

	public boolean isExistShortCut() {
		boolean isInstallShortcut = false;
		final ContentResolver cr = BaseActivity.this.getContentResolver();
		final String AUTHORITY = "com.android.launcher.settings";
		final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
				+ "/favorites?notify=true");
		Cursor c = cr.query(CONTENT_URI,
				new String[] { "title", "iconResource" }, "title=?",
				new String[] { getString(R.string.app_name) }, null);
		if (c != null && c.getCount() > 0) {
			isInstallShortcut = true;
		}
		return isInstallShortcut;
	}

	/**
	 * 为程序创建桌面快捷方式
	 */
	public void createShortcut(Activity act) {
		Intent shortcut = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,
				getString(R.string.app_name));
		shortcut.putExtra("duplicate", false);// 设置是否重复创建
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setClass(this, act.getClass());// 设置第一个页面
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
		ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(
				this, R.drawable.ic_launcher);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
		sendBroadcast(shortcut);
	}

	/**
	 * 删除程序的快捷方式
	 */
	public void delShortcut() {
		Intent shortcut = new Intent(
				"com.android.launcher.action.UNINSTALL_SHORTCUT");
		// 快捷方式的名称
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,
				getString(R.string.app_name));

		// 指定当前的Activity为快捷方式启动的对象: 如 com.everest.video.VideoPlayer
		// 注意: ComponentName的第二个参数必须是完整的类名（包名+类名），否则无法删除快捷方式
		String appClass = this.getPackageName() + "."
				+ this.getLocalClassName();
		ComponentName comp = new ComponentName(this.getPackageName(), appClass);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(
				Intent.ACTION_MAIN).setComponent(comp));
		sendBroadcast(shortcut);
	}

	public void showDialogListView(final ArrayList<String> list,
			final EditText et) {

		Dialog alertDialog = new AlertDialog.Builder(this,
				AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
				.setTitle(R.string.msg_dialog_options)
				// .setPositiveButton("确定", null)
				// .setNegativeButton("取消", null)
				.setItems(list.toArray(new String[list.size()]),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								et.setText(list.get(which));
								et.setSelection(et.getText().length());
							}
						}).create();
		alertDialog.show();
	}

}
