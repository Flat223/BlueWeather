package com.example.blueweather;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blueweather.db.BlueDAO;
import com.example.blueweather.model.City;
import com.example.blueweather.model.Country;
import com.example.blueweather.model.Province;
import com.example.blueweather.util.Utility;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class AreaActivity extends Activity {

	public TextView mTextView;
	public ListView mListView;
	private ProgressDialog dialog;

	public ArrayAdapter<String> adapter;
	public List<String> list;
	public List<Province> mProvinceArray;
	public List<City> mCityArray;
	public List<Country> mCountryArray;

	public Province selectedProvince;
	public City selectedCity;
	public Country selectedCountry;

	public BlueDAO dao;

	private static final int LEVEL_PROVINCE = 1;
	private static final int LEVEL_CITY = 2;
	private static final int LEVEL_COUNTRY = 3;
	private int mCurrentLevel;
	private SharedPreferences sp;
	private boolean mIsFromWeather;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//要在setContentView之前干这件事
		sp = getSharedPreferences("BlueWeather", MODE_PRIVATE);
		Intent intent = getIntent();
		mIsFromWeather = intent.getBooleanExtra("isFromWeather", false);
		if (!mIsFromWeather && sp.getBoolean("country_selected", false)) {
			startActivity(new Intent(this, WeatherActivity.class));
			finish();
		}
		
		setContentView(R.layout.activity_area);
		
		init();
	}

	private void init() {
		mTextView = (TextView) findViewById(R.id.tv_base_title);
		mListView = (ListView) findViewById(R.id.lv_main);

		dao = new BlueDAO(this);
		list = new ArrayList<String>();
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, list);
		mProvinceArray = new ArrayList<Province>();
		mCityArray = new ArrayList<City>();
		mCountryArray = new ArrayList<Country>();

		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (mCurrentLevel == LEVEL_PROVINCE) {
					selectedProvince = mProvinceArray.get(position);
					queryCitis();
				} else if (mCurrentLevel == LEVEL_CITY) {
					selectedCity = mCityArray.get(position);
					queryCountries();
				}else if (mCurrentLevel == LEVEL_COUNTRY) {
					selectedCountry = mCountryArray.get(position);
					Intent intent = new Intent(AreaActivity.this, WeatherActivity.class);
					intent.putExtra("code", selectedCountry.getCode());
					startActivity(intent);
					finish();
				}
			}
		});
		queryProvinces();
	}

	private void queryProvinces() {
		mProvinceArray = dao.loadProvinceData();
		if (mProvinceArray.size() > 0) {
			Log.e("SUN", "从数据库加载");
			list.clear();
			for (Province p : mProvinceArray) {
				list.add(p.getName());
			}
			mTextView.setText("中国");
			mListView.setSelection(0);
			adapter.notifyDataSetChanged();
			mCurrentLevel = LEVEL_PROVINCE;
		} else {
			getDataFromServer(null, "province");
		}
	}

	private void queryCitis() {
		mCityArray = dao.loadCityData(selectedProvince.getId());
		if (mCityArray.size() > 0) {
			Log.e("SUN", "从数据库加载");
			list.clear();
			for (City c : mCityArray) {
				list.add(c.getName());
			}
			mTextView.setText(selectedProvince.getName());
			mListView.setSelection(0);
			adapter.notifyDataSetChanged();
			mCurrentLevel = LEVEL_CITY;
		} else {
			getDataFromServer(selectedProvince.getCode(), "city");
		}
	}

	private void queryCountries() {
		mCountryArray = dao.loadCountryData(selectedCity.getId());
		if (mCountryArray.size() > 0) {
			Log.e("SUN", "从数据库加载");
			list.clear();
			for (Country c : mCountryArray) {
				list.add(c.getName());
			}
			mTextView.setText(selectedCity.getName());
			mListView.setSelection(0);
			adapter.notifyDataSetChanged();
			mCurrentLevel = LEVEL_COUNTRY;
		} else {
			getDataFromServer(selectedCity.getCode(), "country");
		}
	}

	public void getDataFromServer(final String code, final String type) {
		Log.e("SUN", "联网加载");
		String url;
		if (TextUtils.isEmpty(code)) {
			url = "http://www.weather.com.cn/data/list3/city.xml";
		} else {
			url = "http://www.weather.com.cn/data/list3/city" + code + ".xml";
		}
		disPlayDialog();
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configTimeout(5000);
		httpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String data = responseInfo.result;
				if (type.equals("province")) {
					Log.e("SUN", "province_data :\n" + data);
					Utility.handleProvinceResult(data, dao);
					queryProvinces();
				} else if (type.equals("city")) {
					Log.e("SUN", "city_data :\n" + data);
					Utility.handleCityResult(data, dao,
							selectedProvince.getId());
					queryCitis();
				} else if (type.equals("country")) {
					Log.e("SUN", "country_data :\n" + data);
					Utility.handleCountryResult(data, dao, selectedCity.getId());
					queryCountries();
				}
				cancelDialog();
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				cancelDialog();
				Log.e("SUN", "connect error");
				cancelDialog();
				Toast.makeText(AreaActivity.this, "加载失败", 0).show();
			}
		});
	}

	private void disPlayDialog() {
		if (dialog == null) {
			dialog = new ProgressDialog(this);
			dialog.setMessage("正在加载");
			// 设置点击对话框外部时对话框是否可以取消,此时不能取消。但点击back键可以取消;
			dialog.setCanceledOnTouchOutside(false);
		}
		dialog.show();
	}

	private void cancelDialog() {
		if (dialog != null) {
			dialog.dismiss();
		}
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (mCurrentLevel == LEVEL_CITY) {
				queryProvinces();
			} else if (mCurrentLevel == LEVEL_COUNTRY) {
				queryCitis();
			} else {
				if (mIsFromWeather) {
					startActivity(new Intent(AreaActivity.this, WeatherActivity.class));
				}
				finish();
			}

			return true;
		}
		return super.onKeyUp(keyCode, event);
	}
}
