package com.websharp.activity.fragment;

import com.websharp.activity.BaseActivity;
import com.websharp.data.AppUtil;
import com.websharp.data.GlobalData;
import com.websharp.stb.R;
import com.websharputil.common.ConvertUtil;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 客户基本信息
 * 
 * @author dengzh
 * 
 */
public class FragmentCustomerBaseInfo extends Fragment {

	TextView tv_username;
	TextView tv_create_date;
	TextView tv_customer_attr;
	TextView tv_id_type;
	TextView tv_customer_status;
	TextView tv_customer_type;
	TextView tv_customer_level;
	TextView tv_account_bussiness_staff;
	TextView tv_id_address;
	TextView tv_account_bussiness_lobby;
	TextView tv_customer_num;
	TextView tv_contact_name;
	TextView tv_company_belong;
	TextView tv_default_contact_info;
	TextView tv_id_num;
	TextView tv_customer_mobile;
	TextView tv_remark;
	TextView tv_payment_method;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.widget_customer_base_info, container, false);
		init(view);

		return view;

	}

	private void init(View view) {
		tv_username = (TextView) view.findViewById(R.id.tv_username);
		tv_create_date = (TextView) view.findViewById(R.id.tv_create_date);
		tv_customer_attr = (TextView) view.findViewById(R.id.tv_customer_attr);
		tv_id_type = (TextView) view.findViewById(R.id.tv_id_type);
		tv_customer_status = (TextView) view.findViewById(R.id.tv_customer_status);
		tv_customer_type = (TextView) view.findViewById(R.id.tv_customer_type);
		tv_customer_level = (TextView) view.findViewById(R.id.tv_customer_level);
		tv_account_bussiness_staff = (TextView) view.findViewById(R.id.tv_account_bussiness_staff);
		tv_id_address = (TextView) view.findViewById(R.id.tv_id_address);
		tv_account_bussiness_lobby = (TextView) view.findViewById(R.id.tv_account_bussiness_lobby);
		tv_customer_num = (TextView) view.findViewById(R.id.tv_customer_num);
		tv_contact_name = (TextView) view.findViewById(R.id.tv_contact_name);
		tv_company_belong = (TextView) view.findViewById(R.id.tv_company_belong);
		tv_default_contact_info = (TextView) view.findViewById(R.id.tv_default_contact_info);
		tv_id_num = (TextView) view.findViewById(R.id.tv_id_num);
		tv_customer_mobile = (TextView) view.findViewById(R.id.tv_customer_mobile);
		tv_remark = (TextView) view.findViewById(R.id.tv_remark);
		tv_payment_method = (TextView)view.findViewById(R.id.tv_payment_method);
	}

	public void bindData() {
		// 绑定基本信息
		try {
			// 客户名称
			tv_username.setText(GlobalData.curCustomer.CUST_NAME);
			// 开户日期
			tv_create_date.setText(GlobalData.curCustomer.CREATE_DATE);
			// 客户属性
			;
			tv_customer_attr.setText(GlobalData.GetStaticDataName("CUST_PROP", GlobalData.curCustomer.CUST_PROP));
			// 证件类型
			tv_id_type.setText(GlobalData.GetStaticDataName("CERT_TYPE", GlobalData.curCustomer.CUST_CERT_TYPE));
			// 客户状态
			tv_customer_status.setText(GlobalData.GetStaticDataName("CUST_STATUS", GlobalData.curCustomer.CUST_STATUS));

			// 客户类型
			tv_customer_type.setText(GlobalData.GetStaticDataName("CUST_TYPE",GlobalData.curCustomer.CUST_TYPE));
			// 客户级别
			tv_customer_level.setText(GlobalData.GetStaticDataName("CUST_LEVEL",GlobalData.curCustomer.CUST_LEVEL));
			// 证件地址
//			tv_id_address.setText(AppUtil.GetNullValue(GlobalData.curCustomer.REGION_NAME1)
//					+ AppUtil.GetNullValue(GlobalData.curCustomer.REGION_NAME2)
//					+ AppUtil.GetNullValue(GlobalData.curCustomer.REGION_NAME3)
//					+ AppUtil.GetNullValue(GlobalData.curCustomer.LOUDONG)
//					+ AppUtil.GetNullValue(GlobalData.curCustomer.DOOR_DESC));
			tv_id_address.setText(AppUtil.GetNullValue(GlobalData.curCustomer.STD_ADDR_NAME));
			// 开户营业厅
			tv_account_bussiness_lobby.setText(GlobalData.curCustomer.ORGANIZE_NAME);
			// 开户营业员
			tv_account_bussiness_staff.setText(GlobalData.curCustomer.STAFF_NAME);
			// 客户证号
			tv_customer_num.setText(GlobalData.curCustomer.CUST_CODE);
			// 联系人名称
			tv_contact_name.setText(GlobalData.curCustomer.CONT_NAME);
			// 所属分公司
			tv_company_belong.setText(GlobalData.curCustomer.OWN_CORP_ORG);
			// 联系方式
			tv_default_contact_info.setText(AppUtil.GetNullValue(GlobalData.curCustomer.CONT_PHONE1) + "  "
					+ AppUtil.GetNullValue(GlobalData.curCustomer.CONT_PHONE2));
			// 证件号
			tv_id_num.setText(GlobalData.curCustomer.CUST_CERT_NO);
			// 移动电话
			tv_customer_mobile
					.setText((GlobalData.curCustomer.CONT_MOBILE1 == null ? "" : GlobalData.curCustomer.CONT_MOBILE1)+ "  " 
			+ (GlobalData.curCustomer.CONT_MOBILE2 == null ? "" : GlobalData.curCustomer.CONT_MOBILE2));
			// 备注
			tv_remark.setText(GlobalData.curCustomer.REMARK);
			tv_payment_method.setText(GlobalData.curCustomer.PAYMENT_METHOD);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void clear() {
		if (getActivity() instanceof BaseActivity) {
			((BaseActivity) getActivity()).baseSetTextEmpty(tv_username, tv_create_date, tv_customer_attr, tv_id_type,
					tv_customer_status, tv_customer_type, tv_customer_level, tv_account_bussiness_staff, tv_id_address,
					tv_account_bussiness_lobby, tv_customer_num, tv_contact_name, tv_company_belong,
					tv_default_contact_info, tv_id_num, tv_customer_mobile, tv_remark);
		}

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// bindData();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
