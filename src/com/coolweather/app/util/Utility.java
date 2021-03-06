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
	 * 解析和处理服务器返回的省级数据类型
	 */
	public synchronized static boolean handleProvincesResponse(
			CoolWeatherDB coolWeatherDB, String response) {
		// TextUtils.isEmpty()可以一次性进行两种空值的判断。当传入的字符串等于null
		// 或者等于空字符串的时候，这个方法都会返回true，
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
				// 如果所有的省份信息都储存到了数据库中，返回true表示成功
				return true;
			}
		}
		return false;
	}

	/**
	 * 解析和处理服务器返回的市级数据
	 */
	public synchronized static boolean handleCitiesResponse(
			CoolWeatherDB coolWeatherDB, String response, int provinceId) {//provinceId为该城市对应的省份在Province表中的id
		// TextUtils.isEmpty()可以一次性进行两种空值的判断。当传入的字符串等于null
		// 或者等于空字符串的时候，这个方法都会返回true，
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
	 * 解析和处理服务器返回的市级数据
	 */
	public synchronized static boolean handleCountiesResponse(
			CoolWeatherDB coolWeatherDB, String response, int cityId) {
		// TextUtils.isEmpty()可以一次性进行两种空值的判断。当传入的字符串等于null
		// 或者等于空字符串的时候，这个方法都会返回true，
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
	* 解析服务器返回的JSON数据，并将解析出的数据存储到本地。
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
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
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
