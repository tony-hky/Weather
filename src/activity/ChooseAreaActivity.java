package activity;

import java.util.ArrayList;
import java.util.List;

import com.coolweather.app.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import model.City;
import model.CoolWeatherDB;
import model.Country;
import model.Province;
import util.HttpCallbackListener;
import util.HttpUtil;
import util.Utility;

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
		setContentView(R.layout.choose_area);
		listView = (ListView) findViewById(R.id.list_view);
		titleText = (TextView) findViewById(R.id.title_text);
		/**
		 * 可能出现问题
		 */
		// 注意我们使用了android.R.layout.simple_list_item_1作为ListView子项布局的id，这是
		// 一个android内置的布局文件，里面只有一个TextView,可用于简单的显示一段文本
		adapter = new ArrayAdapter<String>(ChooseAreaActivity.this, android.R.layout.simple_list_item_1, dataList);
		listView.setAdapter(adapter);
		// 在主活动实例并创建一个数据库
		coolWeatherDB = coolWeatherDB.getInstance(this);
		listView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int index, long arg3)
			{
				// 如果点到省的话，就去列举城市名称
				if (currentLevel == LEVEL_PROVINCE)
				{
					// 获取provinceList数组第index个位置
					selectedProvince = provinceList.get(index);
					queryCities();
				} else if (currentLevel == LEVEL_CITY)
				{
					selectedCity = cityList.get(index);
					querycountries();
				}
			}
		});
		queryprovinces();
	}

	private void queryprovinces()
	{
		provinceList = coolWeatherDB.loadProvinces();
		if (provinceList.size() > 0)
		{
			dataList.clear();
			for (Province province : provinceList)
			{
				dataList.add(province.getProvinceName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText("中国");
			currentLevel = LEVEL_PROVINCE;
		} else
		{
			queryFromServer(null, "province");
		}
	}

	/**
	 * 查询选中市内所有的县，有限从数据库查询，如果没有查询到再去服务器上查询
	 */
	private void querycountries()
	{
		countryList = coolWeatherDB.loadCountries(selectedCountry.getId());
		if (countryList.size() > 0)
		{
			// 一处列表中所有元素
			dataList.clear();
			for (Country country : countryList)
			{
				dataList.add(country.getCountryName());
			}
			// notifyDataSetChanged方法通过一个外部的方法控制如果适配器的内容改变时需要强制调用getView
			// 来刷新每个Item的内容
			adapter.notifyDataSetChanged();
			// listview.setselection(position)，表示将列表移动到指定的Position处
			listView.setSelection(0);
			titleText.setText(selectedCountry.getCountryName());
			currentLevel = LEVEL_COUNTRY;
		} else
		{
			queryFromServer(selectedCity.getCityCode(), "country");
		}
	}

	/**
	 * 查询选中省所在的市，优先从数据库查询，如果没有查询到再去服务器上查询
	 */
	private void queryCities()
	{
		cityList = coolWeatherDB.loadcities(selectedCity.getId());
		if (cityList.size() > 0)
		{
			// 移除列表中所有元素
			dataList.clear();
			for (City city : cityList)
			{
				// 当执行完下面那一句后，程序自动再加载多dataList一次然后刷新城市名
				dataList.add(city.getCityName());
			}
			// notifyDataSetChanged方法通过一个外部的方法控制如果适配器的内容改变时需要强制调用getView
			// 来刷新每个Item的内容
			adapter.notifyDataSetChanged();
			// listview.setselection(position)，表示将列表移动到指定的Position处
			listView.setSelection(0);
			titleText.setText(selectedProvince.getProvinceName());
			currentLevel = LEVEL_CITY;
		} else
		{
			queryFromServer(selectedProvince.getProvinceCode(), "city");
		}
	}

	/**
	 * 根据传入的代号和类型从服务器上查询省市县数据
	 * 
	 * @param code
	 * @param type
	 */
	private void queryFromServer(final String code, final String type)
	{
		String address;
		if (!TextUtils.isEmpty(code))
		{
			address = "http://www.weather.com.cn/data/list3/city" + code + ".xml";
		} else
		{
			address = "http://www.weather.com.cn/data/list3/city.xml";
		}
		showProgressDialog();
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener()
		{

			@Override
			public void onFinish(String response)
			{
				// TODO Auto-generated method stub
				boolean result = false;
				if ("province".equals(type))
				{
					result = Utility.handleProvincesResponse(coolWeatherDB, response);
				} else if ("city".equals(type))
				{
					result = Utility.handleCitiesResponse(coolWeatherDB, response, selectedProvince.getId());

				} else if ("country".equals(type))
				{
					result = Utility.handleCountriesResponse(coolWeatherDB, response, selectedCity.getId());
				}
				if (result)
				{
					runOnUiThread(new Runnable()
					{

						@Override
						public void run()
						{
							// TODO Auto-generated method stub
							closeProgressDialog();
							if ("province".equals(type))
							{
								queryprovinces();
							} else if ("city".equals(type))
							{
								queryCities();
							} else if ("country".equals(type))
							{
								querycountries();
							}
						}
					});
				}
			}

			@Override
			public void onError(Exception e)
			{
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable()
				{
					
					@Override
					public void run()
					{
						// TODO Auto-generated method stub
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}

	/**
	 * 显示进度对话框
	 */
	private void showProgressDialog()
	{
		if (progressDialog == null)
		{
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("正在加载......");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}

	/**
	 * 关闭对话框
	 */
	private void closeProgressDialog()
	{
		if (progressDialog != null)
		{
			progressDialog.dismiss();
		}
	}

	@Override
	public void onBackPressed()
	{
		if (currentLevel == LEVEL_COUNTRY)
		{
			queryCities();
		} else if (currentLevel == LEVEL_CITY)
		{
			queryprovinces();
		} else
		{
			finish();
		}
		super.onBackPressed();
	}
}
