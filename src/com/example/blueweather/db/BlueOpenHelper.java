package com.example.blueweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BlueOpenHelper extends SQLiteOpenHelper {

	public static final String CREATE_PROVINCE = "create table province("
			+ "_id integer primary key autoincrement,"
			+ "province_name text,"
			+ "province_code text)";
	
	public static final String CREATE_CITY = "create table city("
			+ "_id integer primary key autoincrement,"
			+ "city_name text,"
			+ "city_code text,"
			+ "province_id integer)";
	
	public static final String CREATE_COUNTRY = "create table country("
			+ "_id integer primary key autoincrement,"
			+ "country_name text,"
			+ "country_code text,"
			+ "city_id integer)";
	
	public BlueOpenHelper(Context context,int version) {
		super(context, "blue.db", null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_PROVINCE);
		db.execSQL(CREATE_CITY);
		db.execSQL(CREATE_COUNTRY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
}
