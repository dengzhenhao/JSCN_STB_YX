package com.websharp.data;

import com.websharp.stb.R;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AppUtil {

	public static String GetNullValue(String str){
		if(str == null)
			return "";
		else 
			return str;
	}
	
	public static  View getKeyValueView(String key, String value, LayoutInflater mInFlater) {
		View item1 = mInFlater.inflate(R.layout.item_attr_key_value, null);
		TextView tv_attr_key = (TextView) item1.findViewById(R.id.tv_attr_key);
		TextView tv_attr_value = (TextView) item1.findViewById(R.id.tv_attr_value);
		tv_attr_key.setText(key);
		tv_attr_value.setText(value);
		return item1;
	}
	

	
	public static  View getKeyValueViewLast(String key, String value, LayoutInflater mInFlater) {
		View item1 = mInFlater.inflate(R.layout.item_attr_key_value, null);
		TextView tv_attr_key = (TextView) item1.findViewById(R.id.tv_attr_key);
		TextView tv_attr_value = (TextView) item1.findViewById(R.id.tv_attr_value);
		LinearLayout layout_split = (LinearLayout) item1.findViewById(R.id.layout_split);
		layout_split.setVisibility(View.GONE);
		tv_attr_key.setText(key);
		tv_attr_value.setText(value);
		return item1;
	}
	
	public static  View getKeyValueView2(String key, String value, LayoutInflater mInFlater) {
		View item1 = mInFlater.inflate(R.layout.item_attr_key_value2, null);
		TextView tv_attr_key = (TextView) item1.findViewById(R.id.tv_attr_key);
		TextView tv_attr_value = (TextView) item1.findViewById(R.id.tv_attr_value);
		tv_attr_key.setText(key);
		tv_attr_value.setText(value);
		return item1;
	}
	
	public static  View getKeyValueView2Last(String key, String value, LayoutInflater mInFlater) {
		View item1 = mInFlater.inflate(R.layout.item_attr_key_value2, null);
		TextView tv_attr_key = (TextView) item1.findViewById(R.id.tv_attr_key);
		TextView tv_attr_value = (TextView) item1.findViewById(R.id.tv_attr_value);
		LinearLayout layout_split = (LinearLayout) item1.findViewById(R.id.layout_split);
		layout_split.setVisibility(View.GONE);
		tv_attr_key.setText(key);
		tv_attr_value.setText(value);
		return item1;
	}
	
	
	public static  View getKeyValueView3(String key, String value, LayoutInflater mInFlater) {
		View item1 = mInFlater.inflate(R.layout.item_attr_key_value3, null);
		TextView tv_attr_key = (TextView) item1.findViewById(R.id.tv_attr_key);
		TextView tv_attr_value = (TextView) item1.findViewById(R.id.tv_attr_value);
		tv_attr_key.setText(key);
		tv_attr_value.setText(value);
		return item1;
	}
	

	
	public static  View getKeyValueView3Last(String key, String value, LayoutInflater mInFlater) {
		View item1 = mInFlater.inflate(R.layout.item_attr_key_value3, null);
		TextView tv_attr_key = (TextView) item1.findViewById(R.id.tv_attr_key);
		TextView tv_attr_value = (TextView) item1.findViewById(R.id.tv_attr_value);
		LinearLayout layout_split = (LinearLayout) item1.findViewById(R.id.layout_split);
		layout_split.setVisibility(View.GONE);
		tv_attr_key.setText(key);
		tv_attr_value.setText(value);
		return item1;
	}
	
	
	
	public static  View getKeyValueView4(String key, String value, LayoutInflater mInFlater) {
		View item1 = mInFlater.inflate(R.layout.item_attr_key_value4, null);
		TextView tv_attr_key = (TextView) item1.findViewById(R.id.tv_attr_key);
		TextView tv_attr_value = (TextView) item1.findViewById(R.id.tv_attr_value);
		tv_attr_key.setText(key);
		tv_attr_value.setText(value);
		return item1;
	}
	
	public static  View getKeyValueView4Last(String key, String value, LayoutInflater mInFlater) {
		View item1 = mInFlater.inflate(R.layout.item_attr_key_value4, null);
		TextView tv_attr_key = (TextView) item1.findViewById(R.id.tv_attr_key);
		TextView tv_attr_value = (TextView) item1.findViewById(R.id.tv_attr_value);
		LinearLayout layout_split = (LinearLayout) item1.findViewById(R.id.layout_split);
		layout_split.setVisibility(View.GONE);
		tv_attr_key.setText(key);
		tv_attr_value.setText(value);
		return item1;
	}
	
}
