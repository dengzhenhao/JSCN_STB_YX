package com.websharp.data;

import java.io.File;


import android.content.Context;
import android.os.Environment;

public class Constant {
	
	public static final String RESULT_SUCCESS = "success";
	public static final String RESULT_FAILED = "fail";

	public static final String APP_NAME = "JSCN_STB";

	public static final String ROOT_SRC = Environment
			.getExternalStorageDirectory().getAbsolutePath();
	public static final String SAVE_PATH = ROOT_SRC + "/JSCN_STB/"; // 本地存放资源路径
	public static final String SDCARD_IMAGE_DIR = SAVE_PATH + "image/";// 图片存放
	public static final String SDCARD_ATTACH_DIR = SAVE_PATH+"attach/";//附件下载存放路径 
	public static final String DOWNLOAD_APK_NAME = SAVE_PATH + "JSCN_STB.apk";
	public static final String IMAGE_DIR_TAKE_PHOTO = SAVE_PATH + "image_take/";
	
	
	public static final String ACTION_FINISH_CASEINFO = "finish_caseinfo";
	public static final String ACTION_REFRESH_CASE_LIST = "refresh_case_list";
	

	public static final String ACTION_SHOW_LOADING = "show_loading";
	public static final String ACTION_HIDE_LOADING = "hide_loading";
	public static final String ACTION_OPEN_ATTACH = "open_attach";
	
	public static final String ACTION_REFRESH_CUSTOMER_USER = "refresh_customer_user";
	public static final String ACTION_HIDE_FRAGMENT = "hide_fragment"; 
	public static final String ACTION_PAY_SUCCESS = "pay_success";  
	public static final String ACTION_PAY_FAILED = "pay_failed"; 
	
	public static final int QR_REQUEST_CODE_MAIN = 1;
	public static final int QR_REQUEST_CODE_ORDER_CHANGE_DEVICE = 2;
	public static final int QR_REQUEST_CODE_ZLCX = 3;
	
//	public static String cur_trade_id = "";
//	public static String cur_pay_qr_url_alipay = "";
//	public static String cur_pay_qr_url_wechat = "";
	
	public static void InitDefaultDirs() {
		createDir(SAVE_PATH);
		createDir(SDCARD_IMAGE_DIR);
		createDir(SDCARD_ATTACH_DIR);
		createDir(IMAGE_DIR_TAKE_PHOTO);
	}
	
	public static boolean createDir(String path) {
		boolean ret = false;
		try {
			File file = new File(path);
			if (!file.exists())
				file.mkdirs();
			if (!file.isDirectory()) {
				ret = file.mkdirs();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	

	
}
