package com.websharp.http;

public class AsyncHttpCallBack {
	private Object extra;
	private boolean showDialog = true;

	public AsyncHttpCallBack() {

	}

	public AsyncHttpCallBack(boolean showDialog) {
		this.showDialog = showDialog;
	}

	public void onPrea() {

	}

	public void onPost() {

	}

	public void onSuccess(String response) {

	}
	
	

	public void onFailure(String message) {

	}

	public Object getExtra() {
		return extra;
	}

	public void setExtra(Object extra) {
		this.extra = extra;
	}

	public boolean isShowDialog() {
		return showDialog;
	}

	public void setShowDialog(boolean showDialog) {
		this.showDialog = showDialog;
	}

}
