package com.websharp.activity.business;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.websharp.activity.BaseActivity;
import com.websharp.activity.fragment.FragmentPaymentInfo;
import com.websharp.dao.EntityOffer;
import com.websharp.dao.EntityProduct;
import com.websharp.data.Constant;
import com.websharp.data.EnumPayType;
import com.websharp.data.GlobalData;
import com.websharp.http.AsyncHttpCallBack;
import com.websharp.http.SwzfHttpHandler;
import com.websharp.stb.R;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;

public class ActivityPaymentInfo extends BaseActivity implements View.OnClickListener {

	LinearLayout layout_back;
	TextView tv_title;
	// LinearLayout layout_info_search;

	FragmentPaymentInfo frag_payment_info;
	RelativeLayout layout_pay_line, layout_free_pay;
	LinearLayout layout_pay;
	ImageView iv_pay_wechat, iv_pay_alibaba, iv_pay_cash;

	String customerCode = "";
	public String allowEditPrice = "N";
	public EntityOffer offer = null;
	public double total_price = 0;
	public ArrayList<EntityProduct> listProductSelected = new ArrayList<EntityProduct>();

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_pay_wechat:
			payOrder(EnumPayType.WECHAT);
			// Util.startActivity(this, ActivityPaymentWechat.class, false);
			break;
		case R.id.iv_pay_alibaba:
			payOrder(EnumPayType.ALIPAY);
			// Util.startActivity(this, ActivityPaymentAlibaba.class, false);
			break;
		case R.id.iv_pay_cash:
			payOrder(EnumPayType.CASH);
			break;
		case R.id.layout_free_pay:
			payFreeOrder();
			break;
		case R.id.layout_back:
			finish();
			break;
		}
	}

	@Override
	public void initLayout() {
		setContentView(R.layout.activity_pay_info);
	}

	@Override
	public void init() {
		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		iv_pay_wechat = (ImageView) findViewById(R.id.iv_pay_wechat);
		iv_pay_alibaba = (ImageView) findViewById(R.id.iv_pay_alibaba);
		iv_pay_cash = (ImageView) findViewById(R.id.iv_pay_cash);
		layout_pay_line = (RelativeLayout) findViewById(R.id.layout_pay_line);
		layout_free_pay = (RelativeLayout) findViewById(R.id.layout_free_pay);
		layout_pay = (LinearLayout) findViewById(R.id.layout_pay);
		frag_payment_info = (FragmentPaymentInfo) getFragmentManager().findFragmentById(R.id.frag_payment_info);
	}

	@Override
	public void bindData() {
		offer = (EntityOffer) getIntent().getSerializableExtra("offer");
		listProductSelected = (ArrayList<EntityProduct>) getIntent().getSerializableExtra("listProductSelected");
		total_price = getIntent().getDoubleExtra("total_price", 0);
		allowEditPrice = getIntent().getStringExtra("allow_edit_price");
		frag_payment_info.offer = offer;
		frag_payment_info.listProductSelected = listProductSelected;
		frag_payment_info.total_price = total_price;
		frag_payment_info.allowEditPrice = allowEditPrice;
		// Bundle b = getIntent().getExtras();
		// customerCode = b.getString("customerCode", "");
		iv_pay_wechat.setOnClickListener(this);
		iv_pay_alibaba.setOnClickListener(this);
		iv_pay_cash.setOnClickListener(this);
		layout_back.setOnClickListener(this);
		layout_free_pay.setOnClickListener(this);
		if (total_price == 0) {
			layout_free_pay.setVisibility(View.VISIBLE);
			layout_pay_line.setVisibility(View.GONE);
			layout_pay.setVisibility(View.GONE);
		} else {
			layout_free_pay.setVisibility(View.GONE);
			layout_pay_line.setVisibility(View.VISIBLE);
			layout_pay.setVisibility(View.VISIBLE);
		}
		tv_title.setText("确认订单");
		frag_payment_info.bindData();

	}

	private void payFreeOrder() {
		Util.createDialog(this, null, R.string.msg_dialog_title, R.string.msg_confirm_order_product, null, true, false,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String productID = "-1";
						String offerID = offer.OfferId;
						String productCode = "-1";
						if (listProductSelected.size() == 1) {
							// 订购产品
							productCode = listProductSelected.get(0).ProdId;
							productID = listProductSelected.get(0).ProdId;
						} else {
							// 订购套餐
							productCode = offer.OfferId;
						}
						
						new SwzfHttpHandler(cb, ActivityPaymentInfo.this).payCash(
								ActivityPaymentInfo.this, GlobalData.curCustomer.CUST_CODE, 0,
								GlobalData.curCustomerUser.BILL_ID, productCode, productID, offerID);
					}
				}).show();
	}

	private void payOrder(final EnumPayType type) {
		if (frag_payment_info.et_order_pay_amount.getText().toString().trim().isEmpty()) {
			Util.createToast(this, "订单金额不能为空!", Toast.LENGTH_LONG).show();
			return;
		}

		if (Double.parseDouble(frag_payment_info.et_order_pay_amount.getText().toString().trim()) <= 0) {
			Util.createToast(this, "订单金额必须大于0!", Toast.LENGTH_LONG).show();
			return;
		}

		Util.createDialog(this, null, R.string.msg_dialog_title, R.string.msg_confirm_order_product, null, true, false,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String productID = "-1";
						String offerID = offer.OfferId;
						String productCode = "-1";
						if (listProductSelected.size() == 1) {
							// 订购产品
							productCode = listProductSelected.get(0).ProdId;
							productID = listProductSelected.get(0).ProdId;
						} else {
							// 订购套餐
							productCode = offer.OfferId;
						}
						dialog.dismiss();
						double price = Double
								.parseDouble(frag_payment_info.et_order_pay_amount.getText().toString().trim());
						if (type == EnumPayType.CASH) {
//							new SwzfHttpHandler(cb, ActivityPaymentInfo.this).orderPackageProduct(
//									ActivityPaymentInfo.this, GlobalData.curCustomerUser.BILL_ID, offerID, productID);
							new SwzfHttpHandler(cb, ActivityPaymentInfo.this).payCash(
									ActivityPaymentInfo.this, GlobalData.curCustomer.CUST_CODE, price,
									GlobalData.curCustomerUser.BILL_ID, productCode, productID, offerID);
						} else if (type == EnumPayType.ALIPAY) {
							// Util.startActivity(ActivityPaymentInfo.this,
							// ActivityPaymentAlibaba.class, false);
							new SwzfHttpHandler(cbPayAlipay, ActivityPaymentInfo.this).payAlipay(
									ActivityPaymentInfo.this, GlobalData.curCustomer.CUST_CODE, price,
									GlobalData.curCustomerUser.BILL_ID, productCode, productID, offerID);
						} else if (type == EnumPayType.WECHAT) {
							// Util.startActivity(ActivityPaymentInfo.this,
							// ActivityPaymentWechat.class, false);
							new SwzfHttpHandler(cbPayWeChat, ActivityPaymentInfo.this).payWeChat(
									ActivityPaymentInfo.this, GlobalData.curCustomer.CUST_CODE, price,
									GlobalData.curCustomerUser.BILL_ID, productCode, productID, offerID);
						}
					}
				}).show();
	}

	AsyncHttpCallBack cbPayAlipay = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			// "tradeID":"8510021135392-18061210013600","payCode":"weixin://wxpay/bizpayurl?pr=XQDRNff"
			LogUtil.d("支付结果%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);
				if (obj.optInt("code") == 0) {
					JSONObject dataObj = obj.getJSONObject("data");
					String tradeID = dataObj.getString("tradeID");
					String payCode = dataObj.getString("payCode");
					Bundle b = new Bundle();
					b.putString("tradeID", tradeID);
					b.putString("payCode", payCode);
					b.putString("amount", frag_payment_info.et_order_pay_amount.getText().toString().trim());
					Util.createToast(ActivityPaymentInfo.this, R.string.msg_control_success, Toast.LENGTH_LONG).show();
					Util.startActivity(ActivityPaymentInfo.this, ActivityPaymentAlibaba.class, b, true);
				} else {
					Util.createToast(ActivityPaymentInfo.this, obj.optString("message"), 3000).show();
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

	AsyncHttpCallBack cbPayWeChat = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			// "tradeID":"8510021135392-18061210013600","payCode":"weixin://wxpay/bizpayurl?pr=XQDRNff"
			LogUtil.d("支付结果%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);
				if (obj.optInt("code") == 0) {
					JSONObject dataObj = obj.getJSONObject("data");
					String tradeID = dataObj.getString("tradeID");
					String payCode = dataObj.getString("payCode");
					Bundle b = new Bundle();
					b.putString("tradeID", tradeID);
					b.putString("payCode", payCode);
					b.putString("amount", frag_payment_info.et_order_pay_amount.getText().toString().trim());
					Util.createToast(ActivityPaymentInfo.this, R.string.msg_control_success, Toast.LENGTH_LONG).show();
					Util.startActivity(ActivityPaymentInfo.this, ActivityPaymentWechat.class, b, true);
				} else {
					Util.createToast(ActivityPaymentInfo.this, obj.optString("message"), 3000).show();
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

	AsyncHttpCallBack cb = new AsyncHttpCallBack() {

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			LogUtil.d("%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);
				if (obj.optInt("code") == 0) {
					Util.createToast(ActivityPaymentInfo.this, R.string.msg_control_success, Toast.LENGTH_LONG).show();
					sendBroadcast(new Intent(Constant.ACTION_REFRESH_CUSTOMER_USER));
					sendBroadcast(new Intent(Constant.ACTION_HIDE_FRAGMENT));
					finish();
				} else {
					Util.createToast(ActivityPaymentInfo.this, obj.optString("message"), 3000).show();
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
	public void onDestroy() {
		super.onDestroy();
	}

}
