package util;

import java.util.Iterator;

import android.text.TextUtils;
import model.City;
import model.CoolWeatherDB;
import model.Country;
import model.Province;

public class Utility
{
	/**
	 * 解析和处理服务器返回的省级数据
	 * @param coolWeatherDB
	 * @param response
	 * @return
	 */
	public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB,String response)
	{
		if (!TextUtils.isEmpty(response))
		{
			String[] allProvinces=response.split(",");
			if (allProvinces !=null &&allProvinces.length>0);
			{
				/*
				 * p表示的其实就是v中得任意一个元素，allProvice是数组，也可以是集合，如list或set。
				 * 这种语句叫做foreach语句。其实就是Iterator迭代器的简化
				 * 意思就是循环的从v中拿出一个元素s进行操作
				 */
				for(String p:allProvinces)
				{
					String[]arry=p.split("\\|");
					Province province=new Province();
					province.setProvinceCode(arry[0]);
					province.setProvinceName(arry[1]);
					//将解析出来的数据存储到Province表中
					coolWeatherDB.saveProvince(province);
				}
				return true;
			}
		}
		return false;
	}
	/**
	 * 解析服务器返回的市级数据
	 * @param coolWeatherDB
	 * @param response
	 * @param provinceId
	 * @return
	 */
	public static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB,String response,int provinceId)
	{
		if (!TextUtils.isEmpty(response))
		{
			String[] allCities=response.split(",");
			if (allCities!=null&& allCities.length>0)
			{
				for(String c:allCities)
				{
					String []array=c.split("\\|");
					City city=new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					//将解析出来的数据存储到City表
					coolWeatherDB.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}
	//解析和处理服务器返回的县级数据
	public static boolean handleCountriesResponse(CoolWeatherDB coolWeatherDB,String response,int cityId)
	{
		if (!TextUtils.isEmpty(response))
		{
			String []allCountries=response.split(",");
			if (allCountries!=null&&allCountries.length>0)
			{
				for(String c:allCountries)
				{
					String []array=c.split("\\|");
					Country country=new Country();
					country.setCountryCode(array[0]);
					country.setCountryName(array[1]);
					country.setCityId(cityId);
					//将解析出来的数据存储到Country表
					coolWeatherDB.saveCountry(country);
				}
				return true;
			}
		}
		return false;
		
	}
	
	
}
