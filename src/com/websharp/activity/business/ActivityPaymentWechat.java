package com.websharp.activity.business;

import org.json.JSONException;
import org.json.JSONObject;

import com.websharp.activity.BaseActivity;
import com.websharp.data.Constant;
import com.websharp.http.AsyncHttpCallBack;
import com.websharp.http.SwzfHttpHandler;
import com.websharp.stb.R;
import com.websharp.util.ZXingUtils;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityPaymentWechat extends BaseActivity {
	LinearLayout layout_back;
	TextView tv_title;
	ImageView iv_qr_pay;
	TextView tv_amount;
	String tradeID = "";
	String payCode = "";
	String amount = "";

	Handler handler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			// TODO Auto-generated method stub
			LogUtil.d("dispatchMessage");
			super.dispatchMessage(msg);
			if (msg.what == 0) {
				queryPayResult();
			}
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.layout_back:
			finish();
			break;
		}
	}

	@Override
	public void initLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_pay_wechat);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		layout_back = (LinearLayout) findViewById(R.id.layout_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		iv_qr_pay = (ImageView) findViewById(R.id.iv_qr_pay);
		tv_amount = (TextView) findViewById(R.id.tv_amount);

	}

	@Override
	public void bindData() {

		Bundle b = getIntent().getExtras();
		tradeID = b.getString("tradeID");
		payCode = b.getString("payCode");
		amount = b.getString("amount");
		layout_back.setOnClickListener(this);
		tv_title.setText("微信支付");
		tv_amount.setText("¥ " + amount);
		iv_qr_pay.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				iv_qr_pay.setImageBitmap(
						ZXingUtils.createQRImage(payCode, iv_qr_pay.getHeight(), iv_qr_pay.getHeight()));
			}
		});
		handler.sendEmptyMessageDelayed(0, 2000);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		handler.removeMessages(0);
	}
	// [1] 2.onSuccess:
	// 支付结果{"code":0,"message":"操作成功","data":{"PayState":9009,"PayMessage":"[微]等待支付"}}

	private void queryPayResult() {
		new SwzfHttpHandler(cbPayAlipay, ActivityPaymentWechat.this).queryPayAlipay(tradeID);
	}

	AsyncHttpCallBack cbPayAlipay = new AsyncHttpCallBack(false) {
		

		@Override
		public void onSuccess(String response) {

			super.onSuccess(response);
			// "tradeID":"8510021135392-18061210013600","payCode":"weixin://wxpay/bizpayurl?pr=XQDRNff"
			LogUtil.d("支付结果%s", response);
			JSONObject obj;
			try {
				obj = new JSONObject(response);
				if (obj.optInt("code") == 0) {
					System.err.println(obj.getJSONObject("data").optInt("PayState"));
					if (obj.getJSONObject("data").optInt("PayState") == 0) {
						handler.removeMessages(0);
						Util.createToast(ActivityPaymentWechat.this, "支付成功!", Toast.LENGTH_LONG).show();
						sendBroadcast(new Intent(Constant.ACTION_REFRESH_CUSTOMER_USER));
						sendBroadcast(new Intent(Constant.ACTION_HIDE_FRAGMENT));
						finish(); 
					} else if (obj.getJSONObject("data").optInt("PayState") == 9009) {
						sendBroadcast(new Intent(Constant.ACTION_HIDE_FRAGMENT));
						Util.createToast(ActivityPaymentWechat.this, "支付失败!", Toast.LENGTH_LONG).show();
						finish();
					}else if(obj.getJSONObject("data").optInt("PayState") == -1){
						handler.sendEmptyMessageDelayed(0, 2000);
					}
				} else {
					sendBroadcast(new Intent(Constant.ACTION_HIDE_FRAGMENT));
					Util.createToast(ActivityPaymentWechat.this, "支付失败!", Toast.LENGTH_LONG).show();
					finish();
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
