package com.example.blueweather.test;

import java.util.List;

import com.example.blueweather.db.BlueDAO;
import com.example.blueweather.model.City;
import com.example.blueweather.model.Country;
import com.example.blueweather.model.Province;

import android.test.AndroidTestCase;
import android.util.Log;

public class TestDAO extends AndroidTestCase {
	
	public void tsetInsertProvince(){
		BlueDAO dao = new BlueDAO(getContext());
		Province province = new Province(1, "jiangsu", "19");
		dao.insertProvince(province);
	}
	
	public void testLoadProvinceData(){
		BlueDAO dao = new BlueDAO(getContext());
		List<Province> list = dao.loadProvinceData();
		Log.i("TAG", list.toString());
	}
	
	public void tsetInsertCity(){
		BlueDAO dao = new BlueDAO(getContext());
		City city = new City(1, "suzhou", "1904", 04);
		dao.insertCity(city);
	}
	
	public void testLoadCityData(){
		BlueDAO dao = new BlueDAO(getContext());
		List<City> list = dao.loadCityData(1);
		Log.i("TAG", list.toString());
	}
	
	public void tsetInsertCountry(){
		BlueDAO dao = new BlueDAO(getContext());
		Country country = new Country(1, "kunshan", "190404", 04);
		dao.insertCountry(country);
	}
	
	public void testLoadCountryData(){
		BlueDAO dao = new BlueDAO(getContext());
		List<Country> list = dao.loadCountryData(1);
		Log.i("TAG", list.toString());
	}
}
