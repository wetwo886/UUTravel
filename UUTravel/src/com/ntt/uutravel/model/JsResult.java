package com.ntt.uutravel.model;

public class JsResult<T>  {
	
	public Boolean success;
	
	public  T  data;
	
	public Boolean getsuccess()
	{
		return success;
		
	}
	
	public T getdata()
	{
		return  data;
		
	}
	
	public void setdata(T data)
	{
		this.data=data;
		
	}
	
	public void setsuccess( Boolean success)
	{
		this.success=success;
		
	}
	
	
	public JsResult()
	{}
}
 