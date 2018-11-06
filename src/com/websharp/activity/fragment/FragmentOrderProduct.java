package com.websharp.activity.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.websharp.activity.business.ActivityCustomerUserInfo;
import com.websharp.activity.business.ActivityPaymentInfo;
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
import android.provider.Settings.Global;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 产品订购
 * 
 * @author dengzh
 * 
 */
public class FragmentOrderProduct extends Fragment implements View.OnClickListener {

	Spinner sp_offer;
	ListView lv_product;
	TextView tv_valid_date;
	TextView tv_expire_date;
	Button btn_order_product, btn_cancel;
	TextView tv_price_total;

	List<String> listSpinnerName = null;
	ArrayList<EntityProduct> listProductFromOffer = new ArrayList<EntityProduct>();
	ArrayList<EntityProduct> listProductSelected = new ArrayList<EntityProduct>();
	AdapterCourse adapterProduct = null;
	double total_price = 0;
	boolean is_new_offer = false;
	int cur_check_cbx_index = -1;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.widget_order_product, container, false);
		init(view);
		return view;
	}

	private void init(View view) {
		sp_offer = (Spinner) view.findViewById(R.id.sp_offer);
		lv_product = (ListView) view.findViewById(R.id.lv_product);
		tv_valid_date = (TextView) view.findViewById(R.id.tv_valid_date);
		tv_expire_date = (TextView) view.findViewById(R.id.tv_expire_date);
		btn_order_product = (Button) view.findViewById(R.id.btn_order_product);
		tv_price_total = (TextView) view.findViewById(R.id.tv_price_total);
		btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
	}

	private void bindData() {
		tv_valid_date.setOnClickListener(this);
		tv_expire_date.setOnClickListener(this);
		btn_order_product.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);

		listSpinnerName = new ArrayList<String>();
		for (int i = 0; i < GlobalData.listAllOffer.size(); i++) {
			listSpinnerName.add(GlobalData.listAllOffer.get(i).ProdName);
		}

		ArrayAdapter adapterSource = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
				listSpinnerName);
		adapterSource.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_offer.setAdapter(adapterSource);
		try {
			sp_offer.setSelection(0, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		HandlerProduct();
		sp_offer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				cur_check_cbx_index = -1;
				HandlerProduct();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	private void HandlerProduct() {
		listProductSelected.clear();
		total_price = 0;
		tv_price_total.setText("¥ " + total_price);

		String offerId = GlobalData.listAllOffer.get(sp_offer.getSelectedItemPosition()).OfferId;
		// listview中列出这个套餐下所有的产品
		LogUtil.d(offerId);
		listProductFromOffer.clear();

		for (int i = 0; i < GlobalData.listAllProduct.size(); i++) {
			if (offerId.equals(GlobalData.listAllProduct.get(i).OfferId)) {
				listProductFromOffer.add(GlobalData.listAllProduct.get(i));
			}
		}

		adapterProduct = new AdapterCourse(listProductFromOffer);
		adapterProduct.mList = listProductFromOffer;
		lv_product.setAdapter(adapterProduct);
		adapterProduct.notifyDataSetChanged();

		if (adapterProduct.mList.size() > 0) {
			lv_product.setSelection(0);
		}

		if (GlobalData.listAllOffer.get(sp_offer.getSelectedItemPosition()).OrderMode == 0) {
			lv_product.setOnItemClickListener(null);
			for (int i = 0; i < listProductFromOffer.size(); i++) {
				total_price += listProductFromOffer.get(i).Price;
				listProductSelected.add(listProductFromOffer.get(i));
			}
			tv_price_total.setText("¥ " + total_price);
		} else {
			lv_product.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
					cur_check_cbx_index = position;
					listProductSelected.clear();
					
					for (int i = 0; i < lv_product.getChildCount(); i++) {
						CheckBox cbx = (CheckBox) lv_product.getChildAt(i).findViewById(R.id.cbx_product_select);
						int index = (Integer)cbx.getTag();
						if (index== position) { 
							cbx.setChecked(!cbx.isChecked());
							if(cbx.isChecked()){ 
								 total_price = listProductFromOffer.get(index).Price;
								 listProductSelected.add(listProductFromOffer.get(index));
							}else{
								total_price = 0;
							}
							 tv_price_total.setText("¥ " + total_price);
						}else{
							cbx.setChecked(false);
						}
					}
					
					// 设置setchecked后，会自动触 发cbx的 setOnCheckedChange监听
					// if (cbx.isChecked()) {
					// total_price += adapterProduct.mList.get(position).Price;
					// } else {
					// total_price -= adapterProduct.mList.get(position).Price;
					// }
					//tv_price_total.setText("¥ " + total_price);
				}
			});
		}

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
		case R.id.btn_order_product:
			submitOrderProduct();
			break;
		case R.id.btn_cancel:
			((ActivityCustomerUserInfo) this.getActivity()).container_fragment.setVisibility(View.GONE);
			break;
		}

	}

	private void submitOrderProduct() {
		// String validDate = tv_valid_date.getText().toString();
		// String expireDate = tv_expire_date.getText().toString();
		// String productID =
		// GlobalData.listAllProduct.get(sp_offer.getSelectedItemPosition()).ProdId;
		// String offerID =
		// GlobalData.listAllProduct.get(sp_offer.getSelectedItemPosition()).OfferId;

		// new SwzfHttpHandler(cb,
		// getActivity()).orderPackageProduct(getActivity(),
		// GlobalData.curCustomerUser.BILL_ID,
		// offerID, productID);
		
		if (listProductSelected.size() == 0) {
			Util.createToast(getActivity(), "请至少选择一个产品", Toast.LENGTH_LONG).show();
			return;
		}
		Intent intent = new Intent(getActivity(), ActivityPaymentInfo.class);
		intent.putExtra("listProductSelected", (Serializable) listProductSelected);
		intent.putExtra("total_price", total_price);  
		intent.putExtra("allow_edit_price", listProductSelected.get(0).AllowEditPrice);
		intent.putExtra("offer", (Serializable) GlobalData.listAllOffer.get(sp_offer.getSelectedItemPosition()));
		((ActivityCustomerUserInfo) this.getActivity()).container_fragment.setVisibility(View.GONE);
		startActivity(intent); 
	}

	class ViewHolder {
		private CheckBox cbx_product_select;
		private TextView tv_product_name;
	}

	private class AdapterCourse extends BaseAdapter {
		private LayoutInflater mInflater;
		private ArrayList<EntityProduct> mList;

		public AdapterCourse(ArrayList<EntityProduct> list) {
			this.mList = list;
		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (getCount() == 0)
				return null;
			ViewHolder holder = null;

			try {
				if (mInflater == null) {
					mInflater = LayoutInflater.from(getActivity());
				}
				if (convertView == null) {
					convertView = mInflater.inflate(R.layout.item_offer_product_cbx, null);

					holder = new ViewHolder();
					holder.cbx_product_select = (CheckBox) convertView.findViewById(R.id.cbx_product_select);
					holder.tv_product_name = (TextView) convertView.findViewById(R.id.tv_product_name);

					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}

				holder.cbx_product_select.setTag(position);
//				holder.cbx_product_select.setOnCheckedChangeListener(null);
				
				if(cur_check_cbx_index == position){
					holder.cbx_product_select.setChecked(true);
				}else{
					holder.cbx_product_select.setChecked(false);
				}
				
				if (GlobalData.listAllOffer.get(sp_offer.getSelectedItemPosition()).OrderMode == 0) {
					holder.cbx_product_select.setEnabled(false);
					holder.cbx_product_select.setChecked(true);
				} else {
//					holder.cbx_product_select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//						@Override
//						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//							int index = (Integer) buttonView.getTag();
//							if (isChecked) {
//								cur_check_cbx_index = index;
//								for (int i = 0; i < lv_product.getChildCount(); i++) {
//									CheckBox cbx = (CheckBox) lv_product.getChildAt(i).findViewById(R.id.cbx_product_select);
//									if (i != index) {
//										cbx.setChecked(false);
//									}
//								}
//								total_price += mList.get(index).Price;
//								listProductSelected.add(mList.get(index));
//							} else {
//								total_price -= mList.get(index).Price;
//								String product_id = mList.get(index).ProdId;
//								for (int i = 0; i < listProductSelected.size(); i++) {
//									if (product_id.equals(listProductSelected.get(i).ProdId)) {
//										listProductSelected.remove(i);
//										break;
//									}
//								}
//							}
//							tv_price_total.setText("¥ " + total_price);
//						}
//					});
				}
				holder.tv_product_name.setText(mList.get(position).ProdName);
				return convertView;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

		}
	}

}
