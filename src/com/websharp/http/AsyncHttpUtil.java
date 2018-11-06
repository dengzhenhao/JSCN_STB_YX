package com.websharp.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.http.HttpClientConnection;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.os.AsyncTask;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.websharp.data.GlobalData;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;

public class AsyncHttpUtil {

	private static final int TIME_OUT_INTERVAL = 60000;

	/**
	 * Important! This method execute the get action!
	 * 
	 * @param url
	 * @param method
	 * @param params
	 * @param responseHandler
	 */
	public void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		post(url, params, responseHandler, TIME_OUT_INTERVAL);
	}

	public void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler, int timeout) {

		AsyncHttpClient client = new AsyncHttpClient();
		client.setTimeout(timeout);
		client.setMaxConnections(10);

		client.getHttpClient().getParams().setParameter("http.protocol.allow-circular-redirects", true);

		LogUtil.d("%s?%s", url, params.toString());

		RequestHandle rh = client.post(url, params, responseHandler);
	}

	public void postJson(Context ctx,String url, String jsonString, AsyncHttpResponseHandler responseHandler) {
		AsyncHttpClient client = new AsyncHttpClient();
		client.setTimeout(TIME_OUT_INTERVAL);
		client.setMaxConnections(10);
		if (GlobalData.curUser != null && GlobalData.curUser.token != null)
				client.addHeader("token", GlobalData.curUser.token);
		
		client.getHttpClient().getParams().setParameter("http.protocol.allow-circular-redirects", true);

		ByteArrayEntity entity = null;
		try {
			entity = new ByteArrayEntity(jsonString.getBytes("UTF-8"));
			entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		LogUtil.d("%s?%s", url, jsonString.toString());
		client.post(ctx, url, entity, "application/json", responseHandler);
	}

	/**
	 * Important! This method execute the post action!
	 * 
	 * @param url
	 * @param method
	 * @param params
	 * @param responseHandler
	 */
	public void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		get(url, params, responseHandler, TIME_OUT_INTERVAL);
	}

	public void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler, int timeOut) {
		AsyncHttpClient client = new AsyncHttpClient();
		client.setTimeout(timeOut);
     
		if (GlobalData.curUser != null && GlobalData.curUser.token != null)
				client.addHeader("token", GlobalData.curUser.token);
		client.getHttpClient().getParams().setParameter("http.protocol.allow-circular-redirects", true);
		LogUtil.d("%s?%s", url, params == null ? "" : params.toString());

		client.get(url, params, responseHandler);
	}

}
