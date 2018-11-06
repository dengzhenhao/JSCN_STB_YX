package com.websharp.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.websharp.dao.EntityAccountBook;
import com.websharp.dao.EntityBillRecord;
import com.websharp.dao.EntityClientConfig;
import com.websharp.dao.EntityCommand;
import com.websharp.dao.EntityCustomer;
import com.websharp.dao.EntityCustomerOrder;
import com.websharp.dao.EntityCustomerOrderFee;
import com.websharp.dao.EntityCustomerOrderProduct;
import com.websharp.dao.EntityCustomerOrderResource;
import com.websharp.dao.EntityCustomerPackage;
import com.websharp.dao.EntityCustomerResource;
import com.websharp.dao.EntityCustomerUser;
import com.websharp.dao.EntityNotice;
import com.websharp.dao.EntityOffer;
import com.websharp.dao.EntityOrg;
import com.websharp.dao.EntityPayAmountRecord;
import com.websharp.dao.EntityPayOrder;
import com.websharp.dao.EntityPlayRecord;
import com.websharp.dao.EntityProduct;
import com.websharp.dao.EntityStaticData;
import com.websharp.dao.EntityUser;
import com.websharp.dao.EntityWorkLogDg;
import com.websharp.dao.EntityWorkLogGh;
import com.websharp.dao.EntityWorkLogYe;

public class GlobalData {

	public static void clear() {
		curUser = null;
		curUserOrg = null;
		curCustomer = null;
		curCustomerUser = null;
		listOrg.clear();
		listStaticData.clear();
		listCustomerUser.clear();
		listCustomerPackage.clear();
		listCustomerResources.clear();
		listAccountBook.clear();
		listPlayRecord.clear();
		listPayAmountRecord.clear();
		listBillRecord.clear();
		listCustomerOrder.clear();
		curCustomerOrder = null;
		listCustomerOrderFee.clear();
		listCustomerOrderProduct.clear();
		listCustomerOrderResource.clear();
		listAllOffer.clear();
		listAllProduct.clear();
		listPayOrder.clear();
		System.gc();
	}

	public static ArrayList<String> listHistoryCustomerCode = new ArrayList<String>();

	public static EntityUser curUser = null;

	public static EntityOrg curUserOrg = null;

	/**
	 * 客户信息
	 */
	public static EntityCustomer curCustomer = null;

	public static EntityCustomerUser curCustomerUser = null;

	public static ArrayList<EntityOrg> listOrg = new ArrayList<EntityOrg>();
	/**
	 * 静态数据
	 */
	public static ArrayList<EntityStaticData> listStaticData = new ArrayList<EntityStaticData>();
	public static ArrayList<EntityClientConfig> listClientConfig = new ArrayList<EntityClientConfig>();

	public static String GetStaticDataName(String code_type, String code_value) {
		String result = "-";
		if (code_value == null)
			return result;
		for (int i = 0; i < listStaticData.size(); i++) {
			if (code_type.equals(listStaticData.get(i).CODE_TYPE)
					&& code_value.equals(listStaticData.get(i).CODE_VALUE)) {
				result = listStaticData.get(i).CODE_NAME;
				break;
			}
		}
		return result;
	}

	public static ArrayList<EntityCustomer> listCustomer = new ArrayList<EntityCustomer>();
	/**
	 * 客户下面的用户列表
	 */
	public static ArrayList<EntityCustomerUser> listCustomerUser = new ArrayList<EntityCustomerUser>();
	public static ArrayList<EntityCustomerUser> listCustomerUserPreInstall = new ArrayList<EntityCustomerUser>();

	/**
	 * 订购列表
	 */
	public static ArrayList<EntityCustomerPackage> listCustomerPackage = new ArrayList<EntityCustomerPackage>();

	/**
	 * 资源列表
	 */
	public static ArrayList<EntityCustomerResource> listCustomerResources = new ArrayList<EntityCustomerResource>();

	public static ArrayList<EntityCustomerOrder> listCustomerUserLsdd = new ArrayList<EntityCustomerOrder>();
	public static ArrayList<EntityCustomerOrder> listCustomerUserWbjdd = new ArrayList<EntityCustomerOrder>();

	/**
	 * 帐户余额列表
	 */
	public static ArrayList<EntityAccountBook> listAccountBook = new ArrayList<EntityAccountBook>();

	/**
	 * 点播记录
	 */
	public static ArrayList<EntityPlayRecord> listPlayRecord = new ArrayList<EntityPlayRecord>();

	/**
	 * 充值记录
	 */
	public static ArrayList<EntityPayAmountRecord> listPayAmountRecord = new ArrayList<EntityPayAmountRecord>();

	/**
	 * 帐单信息
	 */
	public static ArrayList<EntityBillRecord> listBillRecord = new ArrayList<EntityBillRecord>();

	/**
	 * 订单信息
	 */
	public static ArrayList<EntityCustomerOrder> listCustomerOrder = new ArrayList<EntityCustomerOrder>();

	public static EntityCustomerOrder curCustomerOrder = null;

	/**
	 * 订单中的费用列表
	 */
	public static ArrayList<EntityCustomerOrderFee> listCustomerOrderFee = new ArrayList<EntityCustomerOrderFee>();

	/**
	 * 订单中的产品列表
	 */
	public static ArrayList<EntityCustomerOrderProduct> listCustomerOrderProduct = new ArrayList<EntityCustomerOrderProduct>();

	/**
	 * 订单中的资源列表
	 */
	public static ArrayList<EntityCustomerOrderResource> listCustomerOrderResource = new ArrayList<EntityCustomerOrderResource>();

	/**
	 * 所有套餐列表
	 */
	public static ArrayList<EntityOffer> listAllOffer = new ArrayList<EntityOffer>();
	

	public static ArrayList<EntityCommand> listCommand = new ArrayList<EntityCommand>();

	/**
	 * 
	 */
	public static ArrayList<EntityProduct> listAllProduct = new ArrayList<EntityProduct>();

	public static ArrayList<EntityWorkLogDg> listWokLogDg = new ArrayList<EntityWorkLogDg>();
	public static ArrayList<EntityWorkLogYe> listWokLogYe = new ArrayList<EntityWorkLogYe>();
	public static ArrayList<EntityWorkLogGh> listWokLogGh = new ArrayList<EntityWorkLogGh>();

	public static ArrayList<EntityNotice> listNotice = new ArrayList<EntityNotice>();
	public static ArrayList<EntityPayOrder> listPayOrder = new ArrayList<EntityPayOrder>();
	public static EntityNotice curNotice = new EntityNotice();

	public static ArrayList<String> listYear = new ArrayList<String>();
	public static ArrayList<String> listMonth = new ArrayList<String>();

	public static ArrayList<String> GetYearList() {
		if (listYear.size() == 0) {
			int curYear = Calendar.getInstance().get(Calendar.YEAR);
			for (int i = 2011; i <= curYear; i++) {
				listYear.add(0, i + "");
			}
		}
		return listYear;
	}

	public static ArrayList<String> GetMonthList() {
		if (listMonth.size() == 0) {
			for (int i = 1; i <= 12; i++) {
				listMonth.add(i + "");
			}
		}
		return listMonth;
	}
}
