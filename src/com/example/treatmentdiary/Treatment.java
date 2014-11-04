package com.example.treatmentdiary;

import java.util.ArrayList;
import java.util.List;

public class Treatment {
	
	private int id;
	private String name;
	private String method;
	private String started;
	private String expectedTime;
	protected List<Diary> list;
	
	public Treatment(){}
	
	public Treatment(String n, String m, String s, String et)
	{
		name = n;
		method = m;
		started = s;
		expectedTime = et;
		list = new ArrayList<Diary>();
	}
	
	public Treatment(int i, String n, String m, String s, String et)
	{
		id = i;
		name = n;
		method = m;
		started = s;
		expectedTime = et;
		list = new ArrayList<Diary>();
	}
	
	public List<Diary> getDiary()
	{
		return list;
	}
	
	public int getId()
	{
		return id;
	}
	
	public void setId(int i)
	{
		id = i;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String n)
	{
		name = n;
	}
	
	public String getMethod()
	{
		return method;
	}
	
	public void setMethod(String m)
	{
		method = m;
	}
	
	public String getStarted()
	{
		return started;
	}
	
	public void setStarted(String s)
	{
		started = s;
	}
	
	public String getExpectedTime()
	{
		return expectedTime;
	}
	
	public void setExpectedTime(String et)
	{
		expectedTime = et;
	}
	

}
