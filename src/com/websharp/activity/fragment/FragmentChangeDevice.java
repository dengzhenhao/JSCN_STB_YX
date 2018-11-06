package com.websharp.activity.fragment;

import java.util.Arrays;

import org.json.JSONException;
import org.json.JSONObject;

import com.websharp.activity.qr.CaptureActivity;
import com.websharp.data.Constant;
import com.websharp.data.GlobalData;
import com.websharp.http.AsyncHttpCallBack;
import com.websharp.http.SwzfHttpHandler;
import com.websharp.stb.R;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;
import com.websharputil.date.DateUtil;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 设备换机
 * 
 * @author dengzh
 * 
 */
public class FragmentChangeDevice extends Fragment implements View.OnClickListener {

	// Spinner sp_device;
	TextView tv_old_device_num;
	EditText et_device_num;
	ImageView btn_qr;
	EditText et_reason_change;
	TextView tv_change_date;
	Button btn_order_change_device;
	RadioGroup rg_change_type;
	String[] arrReason,arrCleanLevel;
	LinearLayout layout_reason_repair,layout_reason_change;
	Spinner sp_reason_change,sp_clean_level;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.widget_order_change_device, container, false);
		init(view);
		return view;
	}

	private void init(View view) {
		// sp_device = (Spinner) view.findViewById(R.id.sp_device);
		tv_old_device_num = (TextView) view.findViewById(R.id.tv_old_device_num);
		et_device_num = (EditText) view.findViewById(R.id.et_device_num);
		btn_qr = (ImageView) view.findViewById(R.id.btn_qr);
		et_reason_change = (EditText) view.findViewById(R.id.et_reason_change);
		tv_change_date = (TextView) view.findViewById(R.id.tv_change_date);
		btn_order_change_device = (Button) view.findViewById(R.id.btn_order_change_device);
		rg_change_type = (RadioGroup) view.findViewById(R.id.rg_change_type);
		arrCleanLevel = getResources().getStringArray(R.array.arr_clean_level);
		arrReason = getResources().getStringArray(R.array.arr_change_reason);
		layout_reason_repair = (LinearLayout) view.findViewById(R.id.layout_reason_repair);
		layout_reason_change = (LinearLayout) view.findViewById(R.id.layout_reason_change);
		sp_clean_level = (Spinner) view.findViewById(R.id.sp_clean_level);
		sp_reason_change = (Spinner) view.findViewById(R.id.sp_reason_change);
	}

	private void bindData() {
		tv_old_device_num.setText(GlobalData.curCustomerUser.BILL_ID);
		tv_change_date.setOnClickListener(this);
		btn_qr.setOnClickListener(this);
		btn_order_change_device.setOnClickListener(this);
		tv_change_date.setText(DateUtil.TimeParseNowToFormatString("yyyy-MM-dd"));
		rg_change_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				// 获取变更后的选中项的ID
				int radioButtonId = arg0.getCheckedRadioButtonId();
				// 根据ID获取RadioButton的实例
				RadioButton rb = (RadioButton) getActivity().findViewById(radioButtonId);
				// 更新文本内容，以符合选中项
				LogUtil.d(rb.getTag().toString());
				if (rb.getTag().toString().equals("repair")) {
					layout_reason_repair.setVisibility(View.VISIBLE);
					layout_reason_change.setVisibility(View.GONE);
				} else {
					layout_reason_repair.setVisibility(View.GONE);
					layout_reason_change.setVisibility(View.VISIBLE);
				}
			}
		});

		// ArrayAdapter adapterSource = new ArrayAdapter<String>(getActivity(),
		// android.R.layout.simple_spinner_item,
		// arrReason);
		
		SpinnerAdapter adapterSourceClearnLevel = new SpinnerAdapter(getActivity(), R.layout.multiline_spinner_dropdown_item,
				arrCleanLevel);
		adapterSourceClearnLevel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_clean_level.setAdapter(adapterSourceClearnLevel);
		sp_clean_level.setSelection(0, true);
		sp_clean_level.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				LogUtil.d(arrCleanLevel[position]);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		
		
		SpinnerAdapter adapterSource = new SpinnerAdapter(getActivity(), R.layout.multiline_spinner_dropdown_item,
				arrReason);
		adapterSource.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_reason_change.setAdapter(adapterSource);
		sp_reason_change.setSelection(0, true);
		sp_reason_change.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				LogUtil.d(arrReason[position]);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

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
		case R.id.tv_change_date:
			new Util().createDatePickerDialog(getActivity(), tv_change_date);
			break;
		case R.id.btn_qr:
			Intent intent = new Intent(getActivity(), CaptureActivity.class);
			startActivityForResult(intent, Constant.QR_REQUEST_CODE_ORDER_CHANGE_DEVICE);
			break;
		case R.id.btn_order_change_device:
			submitChangeDevice();
			break;
		}

	}

	private void submitChangeDevice() {
		String oldStbNo = tv_old_device_num.getText().toString().trim();
		String newStbNo = et_device_num.getText().toString().trim();
		String reason_change = et_reason_change.getText().toString().trim();
		String reason_clean_level = "";
		String reason_repair="";
		String reason = "";
		if (newStbNo.isEmpty()) {
			Util.createToast(getActivity(), "请填写新的机顶盒号!", Toast.LENGTH_LONG).show();
			return;
		}
		String changeType = "";
		RadioButton rb = (RadioButton) getActivity().findViewById(rg_change_type.getCheckedRadioButtonId());
		// 更新文本内容，以符合选中项
		changeType = rb.getTag().toString();
		if (changeType.equals("repair")) {
			reason_clean_level = arrCleanLevel[sp_clean_level.getSelectedItemPosition()];
			reason_repair = arrReason[sp_reason_change.getSelectedItemPosition()];
			reason = reason_clean_level+","+reason_repair;
		} else {
			reason = reason_change;
		}
		LogUtil.d(reason);
		if(GlobalData.curCustomer == null){
			Util.createToast(getActivity(), "用户不存在，无法进行此操作", 3000).show();
			return;
		}
		new SwzfHttpHandler(cb, getActivity()).changeDevice(getActivity(), GlobalData.curCustomer.CUST_CODE, oldStbNo,
				newStbNo, reason,changeType);
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == Constant.QR_REQUEST_CODE_ORDER_CHANGE_DEVICE) {
				et_device_num.setText(data.getExtras().getString("data"));
				et_device_num.setSelection(et_device_num.getText().toString().length());// 将光标追踪到内容的最后
			}
		}
	}

	private class SpinnerAdapter extends ArrayAdapter<String> {
		Context context;
		String[] items = new String[] {};

		public SpinnerAdapter(final Context context, final int textViewResourceId, final String[] objects) {
			super(context, textViewResourceId, objects);
			this.items = objects;
			this.context = context;
		}

		@Override
		public View getDropDownView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(context);
				convertView = inflater.inflate(R.layout.multiline_spinner_dropdown_item, parent, false);
			}

			TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
			tv.setSingleLine(false);
			tv.setText(items[position]);
			tv.setTextColor(Color.BLACK);
			// tv.setTextSize(20);
			return convertView;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(context);
				convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
			}

			// android.R.id.text1 is default text view in resource of the
			// android.
			// android.R.layout.simple_spinner_item is default layout in
			// resources of android.

			TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
			tv.setSingleLine(false);
			tv.setText(items[position]);
			tv.setTextColor(Color.BLACK);
			// tv.setTextSize(20);
			return convertView;
		}
	}

}
