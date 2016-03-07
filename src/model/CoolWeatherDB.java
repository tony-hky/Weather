package model;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
/**
 * ���һЩ���õ����ݿ������װ����
 * @author Administrator
 *
 */
import android.database.sqlite.SQLiteDatabase;
import db.CoolWeatherOpenHelper;

public class CoolWeatherDB
{
	// ���ݿ���
	public static final String DB_NAME = "cool_weather";
	// ���ݿ�汾
	public static final int VERSION = 1;

	private static CoolWeatherDB coolWeatherDB;
	private SQLiteDatabase db;

	/**
	 * �����췽��˽�л�
	 * 
	 * @param context
	 */
	private CoolWeatherDB(Context context)
	{
		CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context, DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase();
	}

	/*
	 * ��ȡCoolWeatheDB��ʵ��
	 * ����synchronized����ʾͬʱֻ��һ�����̿��Է���,getInstance��������һ���̷߳������˲ŵ���һ������
	 */
	public synchronized static CoolWeatherDB getInstance(Context context)
	{
		if (coolWeatherDB == null)
		{
			coolWeatherDB = new CoolWeatherDB(context);
		}
		return coolWeatherDB;

	}

	/*
	 * ��provinceʵ���洢�����ݿ�
	 */
	public void saveProvince(Province province)
	{
		if (province != null)
		{
			ContentValues values = new ContentValues();
			values.put("province_name", province.getProvinceName());
			values.put("province_code", province.getProvinceCode());
			db.insert("Province", null, values);
		}
	}

	/*
	 * �����ݿ��ȡȫ�����е�ʡ����Ϣ
	 */
	public List<Province> loadProvinces()
	{
		// ArrayList�Ƕ�list��ڵĵ���ʵ��
		List<Province> list = new ArrayList<Province>();
		/*
		 * ͬ����һ������Ϊ����,�ڶ�������Ϊ�������ֶ� ����������Ϊ����,���ĸ�����Ϊ����ֵ,���������Ϊ�������
		 * ����������Ϊ���˲���,���߸�����Ϊ�������
		 */
		Cursor cursor = db.query("Province", null, null, null, null, null, null);
		/*
		 * һ��ͨ�ж�cursor.moveToFirst()ֵtrue��falseȷ����ѯ����cursor.moveToNext()����ѭ�����ã�
		 * while(cursor.moveToNext()){ }
		 */
		if (cursor.moveToFirst())
		{
			do
			{
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
				list.add(province);
			} while (cursor.moveToNext());
		}
		if (cursor != null)
		{
			cursor.close();
		}
		return list;
	}

	/*
	 * ��Cityʵ���洢�����ݿ�
	 */
	public void saveCity(City city)
	{
		if (city != null)
		{
			ContentValues values = new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("city_code", city.getCityCode());
			values.put("provinced_id", city.getProvinceId());
			db.insert("City", null, values);
		}
	}

	/*
	 * �����ݿ��ȡĳʡ�����еĳ�����Ϣ
	 */
	public List<City> loadcities(int provinceId)
	{
		List<City> list = new ArrayList<City>();
		/*
		 * ͬ����һ������Ϊ����,�ڶ�������Ϊ����,���ֶ� ����������Ϊ����,���ĸ�����Ϊ����ֵ,���������Ϊ�������
		 * ����������Ϊ���˲���,���߸�����Ϊ�������
		 */
		Cursor cursor = db.query("City", null, "provinceID=?", new String[]
		{ String.valueOf(provinceId) }, null, null, null);
		if (cursor.moveToFirst())
		{
			do
			{
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setProvinceId(provinceId);
				list.add(city);
			} while (cursor.moveToNext());
		}
		if (cursor != null)
		{
			cursor.close();
		}
		return list;
	}

	/*
	 * ��countryʵ���洢�����ݿ�
	 */
	public void saveCountry(Country country)
	{
		if (country != null)
		{
			ContentValues values = new ContentValues();
			values.put("country_name", country.getCountryName());
			values.put("country_code", country.getCountryCode());
			values.put("city_id", country.getCityId());
			db.insert("Country", null, values);
		}
	}

	/*
	 * �����ݿ��ȡĳ���������е�����Ϣ
	 * 
	 */
	public List<Country> loadCountries(int cityId)
	{
		List<Country> list=new ArrayList<Country>();
		Cursor cursor = db.query("Country", null, "cityI_id=?", new String[]
				{ String.valueOf(cityId) }, null, null, null);
				if (cursor.moveToFirst())
				{
					do
					{
						Country country = new Country();
						country.setId(cursor.getInt(cursor.getColumnIndex("id")));
						country.setCountryName(cursor.getString(cursor.getColumnIndex("country_name")));
						country.setCountryCode(cursor.getString(cursor.getColumnIndex("country_code")));
						country.setCityId(cityId);
						list.add(country);
					} while (cursor.moveToNext());
				}
				if (cursor != null)
				{
					cursor.close();
				}
		return list;
	}
}
