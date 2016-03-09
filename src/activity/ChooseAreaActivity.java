package activity;

import java.util.ArrayList;
import java.util.List;

import android.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import model.City;
import model.CoolWeatherDB;
import model.Country;
import model.Province;

public class ChooseAreaActivity extends Activity
{
	public static final int LEVEL_PROVINCE = 0;
	public static final int LEVEL_CITY = 1;
	public static final int LEVEL_COUNTRY = 2;

	private ProgressDialog progressDialog;// 还不是很熟
	private TextView titleText;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private CoolWeatherDB coolWeatherDB;
	private List<String> dataList = new ArrayList<String>();
	/*
	 * 省列表
	 */
	private List<Province> provinceList;
	// 市列表
	private List<City> cityList;
	// 县列表
	private List<Country> countryList;
	// 选中的省份
	private Province selectedProvince;
	// 选中的城市
	private City selectedCity;
	// 选中的县
	private Country selectedCountry;
	/*
	 * 当前选中的级别
	 */
	private int currentLevel;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(com.coolweather.app.R.layout.choose_area);
		listView = (ListView) findViewById(com.coolweather.app.R.id.list_view);
		titleText = (TextView) findViewById(com.coolweather.app.R.id.title_text);
		/**
		 * 可能出现问题
		 */
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);

		
	}

}
