package model;

import android.R.id;

public class Province
{
	private int id;
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public String getProvinceName()
	{
		return provinceName;
	}
	public void setProvinceName(String provinceName)
	{
		this.provinceName = provinceName;
	}
	public String getProvinceCode()
	{
		return provinceCode;
	}
	public void setProvinceCode(String provinceCode)
	{
		this.provinceCode = provinceCode;
	}
	private String provinceName;
	private String provinceCode;
	
}