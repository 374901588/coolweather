package com.coolweather.app.util;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import android.util.Log;
import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;


public class Utility {
	private final static String FILE_NAME="WeatherInfo";
	
	/**
	 * �����ʹ�����������ص�ʡ����������
	 */
	public synchronized static boolean handleProvincesResponse(
			CoolWeatherDB coolWeatherDB, String response) {
		// TextUtils.isEmpty()����һ���Խ������ֿ�ֵ���жϡ���������ַ�������null
		// ���ߵ��ڿ��ַ�����ʱ������������᷵��true��
		if (!TextUtils.isEmpty(response)) {
			String[] allProvinces = response.split(",");
			if (allProvinces != null && allProvinces.length > 0) {
				for (String p : allProvinces) {
					String[] aProvince = p.split("\\|");
					Province province = new Province();
					province.setProvinceName(aProvince[1]);
					province.setProvinceCode(aProvince[0]);
					coolWeatherDB.saveProvince(province);
				}
				// ������е�ʡ����Ϣ�����浽�����ݿ��У�����true��ʾ�ɹ�
				return true;
			}
		}
		return false;
	}

	/**
	 * �����ʹ�����������ص��м�����
	 */
	public synchronized static boolean handleCitiesResponse(
			CoolWeatherDB coolWeatherDB, String response, int provinceId) {//provinceIdΪ�ó��ж�Ӧ��ʡ����Province���е�id
		// TextUtils.isEmpty()����һ���Խ������ֿ�ֵ���жϡ���������ַ�������null
		// ���ߵ��ڿ��ַ�����ʱ������������᷵��true��
		if (!TextUtils.isEmpty(response)) {
			String[] allCities = response.split(",");
			if (allCities != null && allCities.length > 0) {
				for (String c : allCities) {
					String[] aCity = c.split("\\|");
					City city = new City();
					city.setCityName(aCity[1]);
					city.setCityCode(aCity[0]);
					city.setProvinceId(provinceId);
					coolWeatherDB.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * �����ʹ�����������ص��м�����
	 */
	public synchronized static boolean handleCountiesResponse(
			CoolWeatherDB coolWeatherDB, String response, int cityId) {
		// TextUtils.isEmpty()����һ���Խ������ֿ�ֵ���жϡ���������ַ�������null
		// ���ߵ��ڿ��ַ�����ʱ������������᷵��true��
		if (!TextUtils.isEmpty(response)) {
			String[] allCounties = response.split(",");
			if (allCounties != null && allCounties.length > 0) {
				for (String c : allCounties) {
					String[] aCounty = c.split("\\|");
					County county = new County();
					county.setCountyName(aCounty[1]);
					county.setCountyCode(aCounty[0]);
					county.setCityId(cityId);
					coolWeatherDB.saveCounty(county);
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	* �������������ص�JSON���ݣ����������������ݴ洢�����ء�
	*/
	public static void handleWeatherResponse(Context context, String response) {
		try {
			JSONObject jsonObject=new JSONObject(response);
			JSONObject weatherinfo=jsonObject.getJSONObject("weatherinfo");
			String cityName=weatherinfo.getString("city");
			String weatherCode=weatherinfo.getString("cityid");
			String temp1=weatherinfo.getString("temp1");
			String temp2=weatherinfo.getString("temp2");
			String weatherDesp=weatherinfo.getString("weather");
			String publishTime=weatherinfo.getString("ptime");
			saveWetherInfo(context,cityName,weatherCode,temp1,temp2,weatherDesp,publishTime);
		}
		catch(JSONException e) {
			e.printStackTrace();
		}
	}

	private static void saveWetherInfo(Context context, String cityName,
			String weatherCode, String temp1, String temp2, String weatherDesp,
			String publishTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy��M��d��", Locale.CHINA);
		SharedPreferences.Editor editor = PreferenceManager
				.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("city_selected", true);
		editor.putString("city_name", cityName);
		editor.putString("weather_code", weatherCode);
		editor.putString("temp1", temp1);
		editor.putString("temp2", temp2);
		editor.putString("weather_desp", weatherDesp);
		editor.putString("publish_time", publishTime);
		editor.putString("current_date", sdf.format(new Date()));
		editor.commit();
	}
}
