package com.coolweather.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CoolWeatherOpenHelper extends SQLiteOpenHelper{

	/**
	 *  Province�������
	 */
	public static final String CREATE_PROVINCE = "create table Province ("
			+ "id integer primary key autoincrement, "
			+ "province_name txet, "
			+ "province_code txet)";
	
	/**
	 *  City�������
	 */
	public static final String CREATE_CITY = "create table City ("
			+ "id integer primary key autoincrement, "
			+ "city_name txet, "
			+ "city_code txet, "
			+ "province_id integer)";
	
	/**
	 *  County�������
	 */
	public static final String CREATE_COUNTY = "create table County ("
			+ "id integer primary key autoincrement, "
			+ "county_name txet, "
			+ "county_code txet, "
			+ "city_id integer)";
	
	public CoolWeatherOpenHelper(Context context,String name,CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_PROVINCE);//����Province��
		db.execSQL(CREATE_CITY);//����City��
		db.execSQL(CREATE_COUNTY);//����County��
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
