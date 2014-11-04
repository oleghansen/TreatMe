package com.example.treatmentdiary;

public class Diary {
	
	private int id, treatmentid;
	private String date;
	private String title;
	private String text;
	private String timeofday;
	private String rate;
	
	public Diary(){}
	
	public Diary(String t, String d, String tx, String tod, String r)
	{
		title = t;
		date = d;
		text = tx;
		rate = r;
	}
	
	public Diary(int i, String d, String t, String tx, String tod, String r, int tid)
	{
		id = i;
		treatmentid = tid;
		title = t;
		date = d;
		text = tx;
		rate = r;
	}
	
	
	public int getId()
	{
		return id;
	}
	
	public void setId(int i)
	{
		id = i;
	}
	
	
	public int getTreatmentId()
	{
		return treatmentid;
	}
	
	public void setTreatmentId(int tid)
	{
		treatmentid = tid;
	}
	
	
	public String getDate()
	{
		return date;
	}
	
	public void setDate(String d)
	{
		date = d;
	}
	
	public String getTimeOfDay()
	{
		return timeofday;
	}
	
	public void setTimeOfDay(String tod)
	{
		timeofday = tod;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String t)
	{
		title = t;
	}
	
	public String getDescription()
	{
		return text;
	}
	
	public void setDescription(String tx)
	{
		text = tx;
	}
	
	public String getRate()
	{
		return rate;
	}
	
	public void setRate(String r)
	{
		rate = r;
	}
}
