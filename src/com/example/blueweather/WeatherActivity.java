package com.example.blueweather;

import java.text.SimpleDateFormat;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.blueweather.db.BlueDAO;
import com.example.blueweather.model.BlueInfo;
import com.example.blueweather.model.BlueInfo.WeatherInfo;
import com.example.blueweather.util.Utility;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class WeatherActivity extends Activity implements OnClickListener {

	private LinearLayout ll_temp;
	private LinearLayout mLayout;																													
	private TextView tv_title;
	private TextView tv_publish;
	private TextView tv_time;
	private TextView tv_desc;
	private TextView tv_fromTemp;
	private TextView tv_toTemp;
	private ImageView iv_home;
	private ImageView iv_refresh;

	public BlueDAO dao;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather);

		initView();

		initData();
		
	}

	private void initView() {
		mLayout = (LinearLayout) findViewById(R.id.ll_weatherInfo);
		ll_temp = (LinearLayout) findViewById(R.id.ll_temp);

		tv_title = (TextView) findViewById(R.id.tv_weather_title);
		tv_publish = (TextView) findViewById(R.id.tv_publish);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_desc = (TextView) findViewById(R.id.tv_weatherDesc);
		tv_fromTemp = (TextView) findViewById(R.id.tv_fromTemp);
		tv_toTemp = (TextView) findViewById(R.id.tv_toTemp);
		
		iv_home = (ImageView) findViewById(R.id.iv_home);
		iv_refresh = (ImageView) findViewById(R.id.iv_refresh);
		
		iv_home.setOnClickListener(this);
		iv_refresh.setOnClickListener(this);
	}

	private void initData() {
		Intent intent = getIntent();

		String code = intent.getStringExtra("code");

		dao = new BlueDAO(this);
		sp = getSharedPreferences("BlueWeather", MODE_PRIVATE);

		if (!TextUtils.isEmpty(code)) {
			getDataFromServer(code, "country");
		} else {
			showWeather();
		}
	}

	private void getDataFromServer(final String code, final String type) {
		String url = null;
		if (type.equals("country")) {
			url = "http://www.weather.com.cn/data/list3/city" + code + ".xml";
		} else if (type.equals("weather")) {
			url = "http://www.weather.com.cn/adat/cityinfo/" + code + ".html";
		}
		tv_publish.setText("同步中...");
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configTimeout(5000);
		httpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				Log.e("SUN", "result :\n" + result);
				if (type.equals("country")) {
					String[] strs = result.split("\\|");
					String code = strs[1];
					getDataFromServer(code, "weather");
				} else if (type.equals("weather")) {
					Utility.handleWeatherResult(WeatherActivity.this,result);
					showWeather();
			 	}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Log.e("SUN", "connect error");
//				mLayout.setVisibility(View.GONE);
				tv_publish.setText("同步失败");
			}
		});
	}

	public void showWeather() {
		String countryName = sp.getString("countryName", "");
		String mTime = sp.getString("mTime", "");
		String mFromTemp = sp.getString("mFromTemp", "");
		String mToTemp = sp.getString("mToTemp", "");
		String weatherInfo = sp.getString("weatherInfo", "");

		if (TextUtils.isEmpty(mTime)) {
			tv_publish.setVisibility(View.GONE);
			ll_temp.setVisibility(View.GONE);
		} else {
			Date date = new Date(System.currentTimeMillis());
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
			String time = formatter.format(date);

			tv_title.setText(countryName);
			tv_publish.setText("今天" + mTime + "发布");
			tv_time.setText(time);
			tv_desc.setText(weatherInfo);
			tv_fromTemp.setText(mFromTemp + " ~ ");
			tv_toTemp.setText(mToTemp);
		}
		
		startService(new Intent(this,UpdateWeatherService.class));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_home:
			Intent intent = new Intent(this, AreaActivity.class);
			intent.putExtra("isFromWeather", true);
			startActivity(intent);
			finish();
			break;
		case R.id.iv_refresh:
			String weatherCode = sp.getString("weatherCode", "");
			getDataFromServer(weatherCode,"weather");
			break;
		default:
			break;
		}
	}
}
