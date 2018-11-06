package com.websharp.activity.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.websharp.activity.business.ActivityMain;
import com.websharp.dao.EntityProduct;
import com.websharp.data.Constant;
import com.websharp.data.GlobalData;
import com.websharp.http.AsyncHttpCallBack;
import com.websharp.http.SwzfHttpHandler;
import com.websharp.stb.R;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 套餐订购
 * 
 * @author dengzh
 * 
 */
public class FragmentOrderPackage extends Fragment implements View.OnClickListener {

	Spinner sp_package;
	TextView tv_valid_date;
	TextView tv_expire_date;
	Button btn_order_package;

	List<String> listSpinnerName = null;
	List<String> listSpinnerValue = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.widget_order_package, container, false);
		init(view);
		return view;
	}

	private void init(View view) {
		sp_package = (Spinner) view.findViewById(R.id.sp_package);
		tv_valid_date = (TextView) view.findViewById(R.id.tv_valid_date);
		tv_expire_date = (TextView) view.findViewById(R.id.tv_expire_date);
		btn_order_package = (Button) view.findViewById(R.id.btn_order_package);
	}

	private void bindData() {
		tv_valid_date.setOnClickListener(this);
		tv_expire_date.setOnClickListener(this);
		btn_order_package.setOnClickListener(this);

		listSpinnerName = new ArrayList<String>();
		listSpinnerValue = new ArrayList<String>();
		for (int i = 0; i < GlobalData.listAllOffer.size(); i++) {
			listSpinnerName.add(GlobalData.listAllOffer.get(i).ProdName);
			listSpinnerValue.add(GlobalData.listAllOffer.get(i).OfferId);
		}

		ArrayAdapter adapterSource = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
				listSpinnerName);
		adapterSource.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_package.setAdapter(adapterSource);
		try {
			sp_package.setSelection(0, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// sp_package
		// .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		// @Override
		// public void onItemSelected(AdapterView<?> arg0, View arg1,
		// int position, long arg3) {
		// arg0.setVisibility(View.VISIBLE);
		// LogUtil.d("onItemSelected");
		//// caseSource = arrWorkorderSourceValue[position];
		//// adapterCaseList.caseSource = caseSource;
		//// refreshWorkorder();
		// }
		//
		// @Override
		// public void onNothingSelected(AdapterView<?> arg0) {
		//
		// }
		// });
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		bindData();
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_valid_date:
			new Util().createDatePickerDialog(getActivity(), tv_valid_date);
			break;
		case R.id.tv_expire_date:
			new Util().createDatePickerDialog(getActivity(), tv_expire_date);
			break;
		case R.id.btn_order_package:
			submitOrderPackage();
			break;
		}

	}

	private void submitOrderPackage() {
		String validDate = tv_valid_date.getText().toString();
		String expireDate = tv_expire_date.getText().toString();
		String offerID = listSpinnerValue.get(sp_package.getSelectedItemPosition());
		new SwzfHttpHandler(cb, getActivity()).orderPackageProduct(getActivity(), GlobalData.curCustomerUser.BILL_ID,
				offerID, "-1");
	}

	AsyncHttpCallBack cb = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);
				getActivity().getApplicationContext().sendBroadcast(new Intent(Constant.ACTION_HIDE_FRAGMENT));
				if (obj.optInt("code") == 0) {
					Util.createToast(getActivity(), R.string.msg_control_success, Toast.LENGTH_LONG).show();
					getActivity().getApplicationContext()
							.sendBroadcast(new Intent(Constant.ACTION_REFRESH_CUSTOMER_USER));
				} else {
					Util.createToast(getActivity(), obj.optString("message"), 3000).show();
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

}
