package com.example.blueweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.example.blueweather.db.BlueDAO;
import com.example.blueweather.model.BlueInfo;
import com.example.blueweather.model.City;
import com.example.blueweather.model.Country;
import com.example.blueweather.model.Province;
import com.example.blueweather.model.BlueInfo.WeatherInfo;
import com.google.gson.Gson;

public class Utility {

//	public SharedPreferences sp;

	public static void handleProvinceResult(String result, BlueDAO dao) {
		if (!TextUtils.isEmpty(result)) {
			String[] allProvinces = result.split(",");
			if (allProvinces != null && allProvinces.length > 0) {
				for (String p : allProvinces) {
					String[] strs = p.split("\\|");
					String code = strs[0];
					String name = strs[1];
					Province province = new Province(-1, name, code);
					dao.insertProvince(province);
				}
			}
		}
	}

	public static void handleCityResult(String result, BlueDAO dao,
			int provinceId) {
		if (!TextUtils.isEmpty(result)) {
			String[] allCities = result.split(",");
			if (allCities != null && allCities.length > 0) {
				for (String c : allCities) {
					String[] strs = c.split("\\|");
					String code = strs[0];
					String name = strs[1];
					City city = new City(-1, name, code, provinceId);
					dao.insertCity(city);
				}
			}
		}
	}

	public static void handleCountryResult(String result, BlueDAO dao,
			int cityId) {
		if (!TextUtils.isEmpty(result)) {
			String[] allCountries = result.split(",");
			if (allCountries != null && allCountries.length > 0) {
				for (String c : allCountries) {
					String[] strs = c.split("\\|");
					String code = strs[0];
					String name = strs[1];
					Country country = new Country(-1, name, code, cityId);
					dao.insertCountry(country);
				}
			}
		}
	}

	public static void handleWeatherResult(Context context, String jsonData) {
		SharedPreferences sp = context.getSharedPreferences(
				"BlueWeather", Context.MODE_PRIVATE);
		Gson gson = new Gson();
		BlueInfo blueInfo = gson.fromJson(jsonData, BlueInfo.class);
		WeatherInfo mInfo = blueInfo.weatherinfo;

		String countryName = mInfo.city;
		String weatherCode = mInfo.cityid;
		String mTime = mInfo.ptime;
		String mFromTemp = mInfo.temp2;
		String mToTemp = mInfo.temp1;
		String weatherInfo = mInfo.weather;
		
		sp.edit()
			.putBoolean("country_selected", true)
			.putString("countryName", countryName)
			.putString("weatherCode", weatherCode)
			.putString("mTime", mTime).putString("mFromTemp", mFromTemp)
			.putString("mToTemp", mToTemp)
			.putString("weatherInfo", weatherInfo)
			.commit();
	}
}
