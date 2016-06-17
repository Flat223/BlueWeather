package com.example.blueweather.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.blueweather.model.City;
import com.example.blueweather.model.Country;
import com.example.blueweather.model.Province;

public class BlueDAO {
	
	public BlueOpenHelper helper;
	private SQLiteDatabase database;
	
	public BlueDAO(Context context){
		helper = new BlueOpenHelper(context, 1);
		database = helper.getReadableDatabase();
	}
	
	public void insertProvince(Province province){
		
		ContentValues values = new ContentValues();
		values.put("province_name", province.getName());
		values.put("province_code", province.getCode());
		
		long id = database.insert("province", null, values);
	}
	
	public List<Province> loadProvinceData(){
		List<Province> list = new ArrayList<Province>();
		Province province = null;
		Cursor cursor = database.query("province", null, null, null, null, null, null);
		while(cursor.moveToNext()){
			province = new Province();
			province.setId(cursor.getInt(0));
			province.setName(cursor.getString(1));
			province.setCode(cursor.getString(2));
			
			list.add(province);
		}
		if (cursor != null) {
			cursor.close();
		}
		return list;
	}
	
	public void insertCity(City city){
		
		ContentValues values = new ContentValues();
		values.put("city_name", city.getName());
		values.put("city_code", city.getCode());
		values.put("province_id", city.getProvinceId());
		
		long id = database.insert("city", null, values);
	}
	
	public List<City> loadCityData(int provinceId){
		List<City> list = new ArrayList<City>();
		City city = null;
		Cursor cursor = database.query("city", null, null, null, null, null, null);
		while(cursor.moveToNext()){
			city = new City();
			city.setId(cursor.getInt(cursor.getColumnIndex("_id")));
			city.setName(cursor.getString(cursor.getColumnIndex("city_name")));
			city.setCode(cursor.getString(cursor.getColumnIndex("city_code")));
			city.setProvinceId(provinceId);
			
			list.add(city);
		}
		if (cursor != null) {
			cursor.close();
		}
		return list;
	}
	
	public void insertCountry(Country country){
		
		ContentValues values = new ContentValues();
		values.put("country_name", country.getName());
		values.put("country_code", country.getCode());
		values.put("city_id", country.getCityId());
		
		long id = database.insert("country", null, values);
	}

	public List<Country> loadCountryData(int cityId){
		List<Country> list = new ArrayList<Country>();
		Country country = null;
		Cursor cursor = database.query("country", null, null, null, null, null, null);
		while(cursor.moveToNext()){
			country = new Country();
			country.setId(cursor.getInt(cursor.getColumnIndex("_id")));
			country.setName(cursor.getString(cursor.getColumnIndex("country_name")));
			country.setCode(cursor.getString(cursor.getColumnIndex("country_code")));
			country.setCityId(cityId);
			
			list.add(country);
		}
		if (cursor != null) {
			cursor.close();
		}
		return list;
	}
}
