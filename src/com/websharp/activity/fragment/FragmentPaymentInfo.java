package com.websharp.activity.fragment;

import java.util.ArrayList;

import com.websharp.activity.BaseActivity;
import com.websharp.dao.EntityOffer;
import com.websharp.dao.EntityProduct;
import com.websharp.data.AppUtil;
import com.websharp.data.GlobalData;
import com.websharp.stb.R;
import com.websharputil.common.ConvertUtil;
import com.websharputil.date.DateUtil;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 客户基本信息
 * 
 * @author dengzh
 * 
 */
public class FragmentPaymentInfo extends Fragment {

	TextView tv_order_new_package;
	TextView tv_order_new_product;
	TextView tv_effective_time;
	TextView tv_expire_time;
	TextView tv_username;
	TextView tv_customer_attr;
	TextView tv_customer_level;
	TextView tv_device_num;
	TextView tv_customer_num;
	TextView tv_default_contact_info;
	TextView tv_order_pay_status;
	TextView tv_order_pay_amount;
	public EditText et_order_pay_amount;

	public String allowEditPrice = "Y";
	public EntityOffer offer = null;
	public double total_price = 0;
	public ArrayList<EntityProduct> listProductSelected = new ArrayList<EntityProduct>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.widget_payment_info, container, false);
		init(view);

		return view;

	}

	private void init(View view) {
		tv_order_new_package = (TextView) view.findViewById(R.id.tv_order_new_package);
		tv_order_new_product = (TextView) view.findViewById(R.id.tv_order_new_product);
		tv_effective_time = (TextView) view.findViewById(R.id.tv_effective_time);
		tv_expire_time = (TextView) view.findViewById(R.id.tv_expire_time);
		tv_username = (TextView) view.findViewById(R.id.tv_username);
		tv_customer_attr = (TextView) view.findViewById(R.id.tv_customer_attr);
		tv_customer_level = (TextView) view.findViewById(R.id.tv_customer_level);
		tv_device_num = (TextView) view.findViewById(R.id.tv_device_num);
		tv_customer_num = (TextView) view.findViewById(R.id.tv_customer_num);
		tv_default_contact_info = (TextView) view.findViewById(R.id.tv_default_contact_info);
		tv_order_pay_status = (TextView) view.findViewById(R.id.tv_order_pay_status);
		tv_order_pay_amount = (TextView) view.findViewById(R.id.tv_order_pay_amount);
		et_order_pay_amount = (EditText) view.findViewById(R.id.et_order_pay_amount);
	}

	public void bindData() {
		// 绑定基本信息 
		try {
			// 新套餐名称
			tv_order_new_package.setText(offer.ProdName);
			// 生效时间
			tv_effective_time.setText(DateUtil.TimeParseNowToFormatString("yyyy-MM-dd"));
			// 失效时间
			tv_expire_time.setText("2099-12-31");
			// 客户名称
			tv_username.setText(GlobalData.curCustomer.CUST_NAME);
			// 客户属性
			tv_customer_attr.setText(GlobalData.GetStaticDataName("CUST_PROP", GlobalData.curCustomer.CUST_PROP));

			// 客户级别
			tv_customer_level.setText(GlobalData.GetStaticDataName("CUST_LEVEL", GlobalData.curCustomer.CUST_LEVEL));
			// 设备号
			tv_device_num.setText(GlobalData.curCustomerUser.BILL_ID);

			// 客户证号
			tv_customer_num.setText(GlobalData.curCustomer.CUST_CODE);
			// 联系方式
			tv_default_contact_info.setText(
					(GlobalData.curCustomer.CONT_MOBILE1 == null ? "" : GlobalData.curCustomer.CONT_MOBILE1) + "  "
							+ (GlobalData.curCustomer.CONT_MOBILE2 == null ? "" : GlobalData.curCustomer.CONT_MOBILE2));
			String str = "";
			for (int i = 0; i < listProductSelected.size(); i++) {
				if (i > 0) {
					str += "\r\n";
				}
				str += (i + 1) + ". " + listProductSelected.get(i).ProdName;

			}
			tv_order_new_product.setText(str);
			// 支付状态
			tv_order_pay_status.setText("待支付");
			// 支付金额
			et_order_pay_amount.setText(total_price+"");
			tv_order_pay_amount.setText(total_price+"");
			if(allowEditPrice!= null && allowEditPrice.equals("Y")){
				et_order_pay_amount.setVisibility(View.VISIBLE);
				tv_order_pay_amount.setVisibility(View.GONE);
			}else{
				et_order_pay_amount.setVisibility(View.GONE);
				tv_order_pay_amount.setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void clear() {
		if (getActivity() instanceof BaseActivity) {
			((BaseActivity) getActivity()).baseSetTextEmpty(tv_order_new_package, tv_effective_time, tv_expire_time,
					tv_username, tv_customer_attr, tv_customer_level, tv_device_num, tv_customer_num,
					tv_default_contact_info, tv_order_pay_status, et_order_pay_amount);
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
