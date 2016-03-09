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

	private ProgressDialog progressDialog;// �����Ǻ���
	private TextView titleText;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private CoolWeatherDB coolWeatherDB;
	private List<String> dataList = new ArrayList<String>();
	/*
	 * ʡ�б�
	 */
	private List<Province> provinceList;
	// ���б�
	private List<City> cityList;
	// ���б�
	private List<Country> countryList;
	// ѡ�е�ʡ��
	private Province selectedProvince;
	// ѡ�еĳ���
	private City selectedCity;
	// ѡ�е���
	private Country selectedCountry;
	/*
	 * ��ǰѡ�еļ���
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
		 * ���ܳ�������
		 */
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);

		
	}

}
