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
	 * �����ʹ�����������ص�ʡ������
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
				 * p��ʾ����ʵ����v�е�����һ��Ԫ�أ�allProvice�����飬Ҳ�����Ǽ��ϣ���list��set��
				 * ����������foreach��䡣��ʵ����Iterator�������ļ�
				 * ��˼����ѭ���Ĵ�v���ó�һ��Ԫ��s���в���
				 */
				for(String p:allProvinces)
				{
					String[]arry=p.split("\\|");
					Province province=new Province();
					province.setProvinceCode(arry[0]);
					province.setProvinceName(arry[1]);
					//���������������ݴ洢��Province����
					coolWeatherDB.saveProvince(province);
				}
				return true;
			}
		}
		return false;
	}
	/**
	 * �������������ص��м�����
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
					//���������������ݴ洢��City��
					coolWeatherDB.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}
	//�����ʹ�����������ص��ؼ�����
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
					//���������������ݴ洢��Country��
					coolWeatherDB.saveCountry(country);
				}
				return true;
			}
		}
		return false;
		
	}
	
	
}
