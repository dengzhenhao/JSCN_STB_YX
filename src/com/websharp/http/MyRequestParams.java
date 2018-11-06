package com.websharp.http;

import com.loopj.android.http.RequestParams;

public class MyRequestParams extends RequestParams {

	public static synchronized MyRequestParams getInstance() {
		MyRequestParams params = new MyRequestParams();
		params.add("CLIENT", SwzfHttpHandler.CLIENT);
		return params;
	}
}
