package com.websharp.http;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import org.apache.http.client.utils.URLEncodedUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateUtils;

import com.google.gson.JsonObject;
import com.loopj.android.http.RequestParams;
import com.websharp.dao.EntityUser;
import com.websharp.data.GlobalData;
import com.websharputil.code.DescUtil;
import com.websharputil.code.CodeUtil;
import com.websharputil.common.LogUtil;
import com.websharputil.common.PrefUtil;
import com.websharputil.common.Util;
import com.websharputil.date.DateUtil;
import com.websharputil.json.JSONUtils;

public class SwzfHttpHandler extends BaseHttpHandler {

	public static final String CLIENT = "android";

	public static int PAGEINDEX_CZJL = 0;
	public static int PAGEINDEX_DBJL = 0;
	public static int PAGEINDEX_WBJDD = 0;
	public static int PAGEINDEX_YBJDD = 0;
	public static int PAGEINDEX_XFJD = 0;
	public static int PAGEINDEX_CUSTOMER_LIST_SEARCH = 0;
	public static int PAGEINDEX_WORK_LOG = 0;

	public static int PAGEINDEX_NOTICE = 0;
	public static int PAGEINDEX_WORK_LOG_DG = 0;
	public static int PAGEINDEX_WORK_LOG_GH = 0;
	public static int PAGEINDEX_WORK_LOG_YE = 0;

	public static int PAGEINDEX_PAY_ORDER = 0;
	
	public static int PAGE_SIZE = 20;

	/**
	 * 接口中加密的密钥
	 */ 
	public final static String ENCRYPT_KEY = "JSCN_STB_API";

	/**
	 * 服务器地址
	 */
	
//	 public static final String BASE_URL = "http://58.241.13.54:6789";//宜兴广电
//	 public static final String BASE_URL = "http://122.97.216.20:30076";//南京广电
	 public static final String BASE_URL = "http://122.97.218.203:57005";//南京广电
	 

	/**
	 * 检查更新
	 */
	public static final String URL_CHECK_VERSION = "/Api/QueryData/Check";

	/**
	 * 登录接口
	 */
	public static final String URL_LOGIN = "/Api/QueryData/OperatorLogin";
	
	/**
	 *修改密码
	 */
	public static final String URL_UPDATE_PASSWORD = "/Api/QueryData/ChangePwd";
 
	/**
	 * 订购产品、套餐 stbNo,offerId,productId,userId,userName,orgCode
	 */
	public static final String URL_ORDER_PRODUCT_PACKAGE = "/Api/CallService/Subscribe";


	/**
	 * 更换设备 custCode,opId,oldStbNo,newStbNo,remark,userId,userName,orgCode
	 */
	public static final String URL_ORDER_CHANGE_DEVICE = "/Api/CallService/ChangeStb";
 
	/**
	 * 查询余额(帐本?) stbNo,userId,userName,orgCode
	 */
	public static final String URL_CUSTOMER_ACCOUNT_BOOK = "/Api/CallService/QueryBalance";
 
	
	/**
	 * 获取所有产品
	 */
	public static final String URL_ALL_PRODUCT = "/Api/QueryData/QueryProduct";

	/**
	 * 获取所有套餐
	 */
	public static final String URL_ALL_PACKAGE = "/Api/QueryData/QuerySuite";

	/**
	 * 查静态数据，如的有的营业厅，所有的枚举值，登录后调录
	 */
	public static final String URL_STATIC_DATA = "/Api/QueryData/QueryStaticData";

	/**
	 * 点播记录
	 */
	public static final String URL_DBJL = "/Api/QueryData/DianBo";
	
	/**
	 * 操作日志
	 */
	public static final String URL_WORKLOG_DG= "/Api/QueryData/QueryOrderLogs";

	/**
	 * 更换设备
	 */
	public static final String URL_WORKLOG_GH = "/Api/QueryData/QueryChangeLogs";

	/**
	 * 余额
	 */
	public static final String URL_WORKLOG_YE= "/Api/QueryData/QueryBalanceLogs";
	
	/**
	 * 客户端数据，也属于静态数据
	 */
	public static final String URL_CLIENT_CONFIG = "/Api/QueryData/QueryClientConfig"; 
	
	/**
	 * 支付过的订单列表
	 */
	public static final String URL_PAY_ORDER= "/Api/QueryData/QueryOrderPay";

	/**
	 * 调用设置好的ql ?id=&data= id=1,按cust_code查询客户 id=2,按cust_id查询用户
	 * id=3,按prod_inst_id查询订购产品及订购套餐 id=4,按prod_inst_id查询资源
	 * id=5,按prod_inst_id查询订单(包括历史及未办结)
	 */
	public static final String URL_CALL_SQL = "/Api/QueryData/Query";

	/**
	 * 选择用户营业厅
	 */
	public static final String URL_ChangeOrganization = "/Api/QueryData/ChangeOrganization";
	
	/**
	 * 没有用到
	 */
	public static final String URL_QUERY_COMMAND = "/Api/QueryData/QueryCommand";
	
	/**
	 * 客户查询
	 */
	public static final String URL_QUERY_CUST = "/Api/QueryData/QueryCust";
	
	/**
	 * 授权
	 */
	public static final String URL_AUTH_STB = "/Api/CallService/AuthStb";
	
	/**
	 * 现金 支付
	 */
	public static final String URL_CASH_SUBMIT= "/Api/CASH/Submit";
	
	/**
	 * 支付宝 支付
	 */
	public static final String URL_ALIPAY_SUBMIT= "/Api/Alipay/Submit";

	/**
	 * 支付宝 轮询
	 */
	public static final String URL_ALIPAY_QUERY= "/Api/Alipay/Query";
	

	/**
	 * 微信 支付
	 */
	public static final String URL_WECHAT_SUBMIT= "/Api/WeChat/Submit";

	/**
	 * 微信 轮询
	 */
	public static final String URL_WECHAT_QUERY= "/Api/WeChat/Query";


	public SwzfHttpHandler(AsyncHttpCallBack callback, Context context) {
		super(callback, context);

		if (GlobalData.curUser == null) {
			String str = PrefUtil.getPref(context, "user", "");
			if (!str.isEmpty()) {
				GlobalData.curUser = JSONUtils.fromJson(str, EntityUser.class);
			}
		}
	}

	/**
	 * 得到签名字符串
	 * 
	 * @param params
	 * @return
	 */
	public static String GetSignature(String... params) {
		StringBuilder sb = new StringBuilder();
		if (params != null && params.length != 0) {
			for (int i = 0; i < params.length; i++) {

				sb = sb.append(params[i]);

			}
		}
		sb = sb.append(CLIENT).append(ENCRYPT_KEY);
		String str = CodeUtil.MD5(sb.toString()).substring(8, 24);
		return str;
	}

	/**
	 * 登录
	 * 
	 * @param userName
	 *            登录名
	 * @param password
	 *            密码
	 * @throws Exception
	 */
	public void login(Context ctx, String userName, String password) throws Exception {
		JSONObject jobj = new JSONObject();
		jobj.put("loginCode", userName);
		jobj.put("loginPwd", password);
		new AsyncHttpUtil().postJson(ctx, BASE_URL + URL_LOGIN, jobj.toString(), handler);
	}
	
	public void updatePassword(Context ctx, String login_code, String old_pwd,String new_pwd) throws Exception {
		JSONObject jobj = new JSONObject();
		jobj.put("login_code", login_code);
		jobj.put("old_pwd", old_pwd);
		jobj.put("new_pwd", new_pwd);
		new AsyncHttpUtil().postJson(ctx, BASE_URL + URL_UPDATE_PASSWORD, jobj.toString(), handler);
	}

	/**
	 * 检查版本
	 */
	public void checkVersion(Context ctx) {
		JSONObject jobj = new JSONObject();
		try {
			jobj.put("token", GlobalData.curUser.token);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		new AsyncHttpUtil().get(BASE_URL + URL_CHECK_VERSION, null, handler);
	}

	/**
	 * 订购套餐/产品
	 * 
	 * @param stbNo
	 * @param offerId
	 * @param productId
	 */
	public void orderPackageProduct(Context ctx, String stbNo, String offerId, String productId) {
		JSONObject jobj = new JSONObject();
		try {
			jobj.put("stbNo", stbNo);
			jobj.put("offerId", offerId);
			jobj.put("productId", productId);
			jobj.put("userId", GlobalData.curUser.opId);
			jobj.put("userName", GlobalData.curUser.staffName);
			jobj.put("orgCode", GlobalData.curUserOrg.organization_code);
			jobj.put("token", GlobalData.curUser.token);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		new AsyncHttpUtil().postJson(ctx, BASE_URL + URL_ORDER_PRODUCT_PACKAGE, jobj.toString(), handler);
	}

	/**
	 * 更换设备
	 * 
	 * @param custCode
	 * @param opId
	 * @param oldStbNo
	 * @param newStbNo
	 * @param remark
	 */
	public void changeDevice(Context ctx, String custCode, String oldStbNo, String newStbNo, String remark,String changeType) {
		// custCode,opId,oldStbNo,newStbNo,remark,userId,userName,orgCode

		JSONObject jobj = new JSONObject();

		try {
			jobj.put("changeType", changeType);
			jobj.put("custCode", custCode);
			jobj.put("opId", GlobalData.curUser.opId);
			jobj.put("oldStbNo", oldStbNo);
			jobj.put("newStbNo", newStbNo);
			jobj.put("remark", remark);
			jobj.put("userId", GlobalData.curUser.opId);
			jobj.put("userName", GlobalData.curUser.staffName);
			jobj.put("orgCode", GlobalData.curUserOrg.organization_code);
			jobj.put("token", GlobalData.curUser.token);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		new AsyncHttpUtil().postJson(ctx, BASE_URL + URL_ORDER_CHANGE_DEVICE, jobj.toString(), handler);
	}

	/**
	 * 帐户余额
	 * 
	 * @param stbNo
	 */
	public void customerAccountBook(Context ctx, String stbNo) {
		JSONObject jobj = new JSONObject();
		try {
			jobj.put("custCode", stbNo);
			jobj.put("token", GlobalData.curUser.token);
			jobj.put("userId", GlobalData.curUser.opId);
			jobj.put("userName", GlobalData.curUser.staffName);
			jobj.put("orgCode", GlobalData.curUserOrg.organization_code);
			jobj.put("token", GlobalData.curUser.token);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		new AsyncHttpUtil().postJson(ctx, BASE_URL + URL_CUSTOMER_ACCOUNT_BOOK, jobj.toString(), handler);
	}

	/**
	 * 查询出所有的套餐
	 */
	public void getAllPackage() {
		RequestParams params = new RequestParams();
		params.add("Property", "99");
		params.add("token", GlobalData.curUser.token);
		new AsyncHttpUtil().get(BASE_URL + URL_ALL_PACKAGE, params, handler);
	}

	/**
	 * 查询出所有的产品
	 */
	public void getAllProduct() {
		RequestParams params = new RequestParams();
		params.add("Property", "99");
		params.add("token", GlobalData.curUser.token);
		new AsyncHttpUtil().get(BASE_URL + URL_ALL_PRODUCT, params, handler);
	}

	/**
	 * 调用设置好的sql ?id=&data=
	 * 
	 * @param param1
	 *            id=1,按cust_code查询客户
	 * @param param2id
	 *            =2,按cust_id查询用户
	 * @param param3
	 *            id=3,按prod_inst_id查询订购产品及订购套餐
	 * @param param4
	 *            id=4,按prod_inst_id查询资源
	 * @param param5
	 *            id=5,按custCode查询订单(未办结),indexStart,indexEnd
	 * @param param6
	 *            id=6,按_f_Year,custCode查询订单(包括历史),indexStart,indexEnd
	 * @param param7
	 *            id=7,按_f_Year,订单号,查询订单中product(包括历史及未办结)
	 * @param param8
	 *            id=8,按_f_Year,订单号,查询订单中资源(包括历史及未办结)
	 * @param param9
	 *            id=9,按_f_Year,订单号,查询订单中费用(包括历史及未办结)
	 * @param id
	 * @param strParams
	 */
	public void callSQL(String id, String... strParams) {
		RequestParams params = new RequestParams();
		String data = "";
		for (int i = 0; i < strParams.length; i++) {
			if (i > 0) {
				data += "|";
			}
			data += strParams[i];
		}
		params.add("id", id);
		if (!data.isEmpty()) {
			params.add("data", data);
		}
		params.add("token", GlobalData.curUser.token);
		params.add("opId", GlobalData.curUser.opId);
		new AsyncHttpUtil().get(BASE_URL + URL_CALL_SQL, params, handler);
	}

	public void getStaticData() {
		RequestParams params = new RequestParams();
		params.add("token", GlobalData.curUser.token);
		new AsyncHttpUtil().get(BASE_URL + URL_STATIC_DATA, params, handler);
	}

	public void getDbjl(String custCode, String startDate, String endDate, String startRowNum, String endRowNum) {
		RequestParams params = new RequestParams();
		// ?cust_code=%s&start_date=%s&end_date=%s&start_row_num=%s&end_row_num=%s
		params.add("cust_code", custCode);
		params.add("start_date", startDate);
		params.add("end_date", endDate);
		params.add("start_row_num", startRowNum);
		params.add("end_row_num", endRowNum);
		params.add("token", GlobalData.curUser.token);
		new AsyncHttpUtil().get(BASE_URL + URL_DBJL, params, handler);
	}

	// ?opId=XXX&organization_code=

	public void changeOrg(String org_code) {
		RequestParams params = new RequestParams();
		params.add("organization_code", org_code);
		params.add("opId", GlobalData.curUser.opId);
		params.add("token", GlobalData.curUser.token);
		new AsyncHttpUtil().get(BASE_URL + URL_ChangeOrganization, params, handler);
	}
	
	public void getWorkLogDg(String pageIndex) {
		RequestParams params = new RequestParams();
		params.add("pageIndex", pageIndex);
		params.add("pageSize", SwzfHttpHandler.PAGE_SIZE+"");
		params.add("opId", GlobalData.curUser.opId);
		params.add("token", GlobalData.curUser.token);
		new AsyncHttpUtil().get(BASE_URL + URL_WORKLOG_DG, params, handler);
	}
	
	public void getWorkLogGh(String pageIndex) {
		RequestParams params = new RequestParams();
		params.add("pageIndex", pageIndex);
		params.add("pageSize", SwzfHttpHandler.PAGE_SIZE+"");
		params.add("opId", GlobalData.curUser.opId);
		params.add("token", GlobalData.curUser.token);
		new AsyncHttpUtil().get(BASE_URL + URL_WORKLOG_GH, params, handler);
	}
	
	public void getWorkLogYe(String pageIndex) {
		RequestParams params = new RequestParams();
		params.add("pageIndex", pageIndex);
		params.add("pageSize", SwzfHttpHandler.PAGE_SIZE+"");
		params.add("opId", GlobalData.curUser.opId);
		params.add("token", GlobalData.curUser.token);
		new AsyncHttpUtil().get(BASE_URL + URL_WORKLOG_YE, params, handler);
	}
	
	public void queryPayOrder(String pageIndex,String data) {
		RequestParams params = new RequestParams();
		params.add("pageIndex", pageIndex);
		params.add("pageSize", SwzfHttpHandler.PAGE_SIZE+"");
		params.add("opId", GlobalData.curUser.opId);
		params.add("token", GlobalData.curUser.token);
		params.add("data", data);
		new AsyncHttpUtil().get(BASE_URL + URL_PAY_ORDER, params, handler);
	}
	
	public void  queryCommand(String auth_object_no,String start_date,String end_date) {
		RequestParams params = new RequestParams();
		params.add("auth_object_no", auth_object_no);
		params.add("start_date", start_date);
		params.add("end_date", end_date);
		params.add("opId", GlobalData.curUser.opId);
		params.add("token", GlobalData.curUser.token);
		new AsyncHttpUtil().get(BASE_URL + URL_QUERY_COMMAND, params, handler);
	}
	
	
	public void  queryCust(String data,String pageIndex) {
		RequestParams params = new RequestParams();
		params.add("data", data);
		params.add("pageIndex", pageIndex);
//		params.add("pageSize", SwzfHttpHandler.PAGE_SIZE+"");
		params.add("pageSize", "10");
		params.add("opId", GlobalData.curUser.opId);
		params.add("token", GlobalData.curUser.token);
		new AsyncHttpUtil().get(BASE_URL + URL_QUERY_CUST, params, handler);
	}
	
	
	public void  queryClientConfig() {
		RequestParams params = new RequestParams();
		params.add("opId", GlobalData.curUser.opId);
		params.add("token", GlobalData.curUser.token);
		new AsyncHttpUtil().get(BASE_URL + URL_CLIENT_CONFIG, params, handler);
	}
	
	
	public void  authStb(Context ctx, String prodInstId) {
		JSONObject jobj = new JSONObject();
		try {
			jobj.put("opId", GlobalData.curUser.opId);
			jobj.put("prodInstId", prodInstId);
			jobj.put("userId", GlobalData.curUser.opId);
			jobj.put("userName", GlobalData.curUser.staffName);
			jobj.put("orgCode", GlobalData.curUserOrg.organization_code);
			jobj.put("token", GlobalData.curUser.token);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		new AsyncHttpUtil().postJson(ctx, BASE_URL + URL_AUTH_STB, jobj.toString(), handler);
	}
	
	public void queryPayWeChat(String tradeID){
		RequestParams params = new RequestParams();
		params.add("tradeID", tradeID);
		new AsyncHttpUtil().get(BASE_URL + URL_WECHAT_QUERY, params, handler);
	}
	
	public void queryPayAlipay(String tradeID){
		RequestParams params = new RequestParams();
		params.add("tradeID", tradeID);
		new AsyncHttpUtil().get(BASE_URL + URL_ALIPAY_QUERY, params, handler);
	}
	
	public void  payAlipay(Context ctx, String custCode,double amt,String stbNo,String productCode,String prodId,String offerId) {
		JSONObject jobj = new JSONObject();
		try {
			jobj.put("custCode", custCode);
			jobj.put("amt", amt);
			jobj.put("productCode", productCode);
			jobj.put("userId", GlobalData.curUser.opId);
			jobj.put("userName", GlobalData.curUser.staffName);
			jobj.put("orgCode", GlobalData.curUserOrg.organization_code);
			jobj.put("stbNo", stbNo);
			jobj.put("prodId", prodId);
			jobj.put("offerId", offerId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		new AsyncHttpUtil().postJson(ctx, BASE_URL + URL_ALIPAY_SUBMIT, jobj.toString(), handler);
	}
	
	public void  payWeChat(Context ctx, String custCode,double amt,String stbNo,String productCode,String prodId,String offerId) {
		JSONObject jobj = new JSONObject();
		try {
			jobj.put("custCode", custCode);
			jobj.put("amt", amt);
			jobj.put("productCode", productCode);
			jobj.put("userId", GlobalData.curUser.opId);
			jobj.put("userName", GlobalData.curUser.staffName);
			jobj.put("orgCode", GlobalData.curUserOrg.organization_code);
			jobj.put("stbNo", stbNo);
			jobj.put("prodId", prodId);
			jobj.put("offerId", offerId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		new AsyncHttpUtil().postJson(ctx, BASE_URL + URL_WECHAT_SUBMIT, jobj.toString(), handler);
	}
	
	
	public void  payCash(Context ctx, String custCode,double amt,String stbNo,String productCode,String prodId,String offerId) {
		JSONObject jobj = new JSONObject();
		try {
			jobj.put("custCode", custCode);
			jobj.put("amt", amt); 
			jobj.put("productCode", productCode);
			jobj.put("userId", GlobalData.curUser.opId);
			jobj.put("userName", GlobalData.curUser.staffName);
			jobj.put("orgCode", GlobalData.curUserOrg.organization_code);
			jobj.put("stbNo", stbNo);
			jobj.put("prodId", prodId);
			jobj.put("offerId", offerId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		new AsyncHttpUtil().postJson(ctx, BASE_URL + URL_CASH_SUBMIT, jobj.toString(), handler);
	}
}
