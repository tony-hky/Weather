package model;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
/**
 * 会把一些常用的数据库操作封装起来
 * @author Administrator
 *
 */
import android.database.sqlite.SQLiteDatabase;
import db.CoolWeatherOpenHelper;

public class CoolWeatherDB
{
	// 数据库名
	public static final String DB_NAME = "cool_weather";
	// 数据库版本
	public static final int VERSION = 1;

	private static CoolWeatherDB coolWeatherDB;
	private SQLiteDatabase db;

	/**
	 * 将构造方法私有化
	 * 
	 * @param context
	 */
	private CoolWeatherDB(Context context)
	{
		CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context, DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase();
	}

	/*
	 * 获取CoolWeatheDB的实例
	 * 加了synchronized，表示同时只有一个进程可以访问,getInstance方法，待一个线程访问完了才到下一个进程
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
	 * 将province实例存储到数据库
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
	 * 从数据库读取全国所有的省份信息
	 */
	public List<Province> loadProvinces()
	{
		// ArrayList是对list借口的典型实现
		List<Province> list = new ArrayList<Province>();
		/*
		 * 同样第一个参数为表名,第二个参数为列名即字段 第三个参数为条件,第四个参数为条件值,第五个参数为分组操作
		 * 第六个参数为过滤操作,第七个参数为排序操作
		 */
		Cursor cursor = db.query("Province", null, null, null, null, null, null);
		/*
		 * 一般通判断cursor.moveToFirst()值true或false确定查询结否空cursor.moveToNext()用做循环般用：
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
	 * 将City实例存储到数据库
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
	 * 从数据库读取某省下所有的城市信息
	 */
	public List<City> loadcities(int provinceId)
	{
		List<City> list = new ArrayList<City>();
		/*
		 * 同样第一个参数为表名,第二个参数为列名,即字段 第三个参数为条件,第四个参数为条件值,第五个参数为分组操作
		 * 第六个参数为过滤操作,第七个参数为排序操作
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
	 * 将country实例存储到数据库
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
	 * 从数据库读取某城市下所有的县信息
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
