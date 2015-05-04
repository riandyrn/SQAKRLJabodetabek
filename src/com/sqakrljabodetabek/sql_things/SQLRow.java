package com.sqakrljabodetabek.sql_things;
import java.util.ArrayList;

public class SQLRow {
	
	private ArrayList<String> values;
	private ArrayList<String> columns;
	
	public SQLRow()
	{
		columns = new ArrayList<>();
		values = new ArrayList<>();
	}
	
	public SQLRow(ArrayList<String> values)
	{
		this.values = values;
	}
	
	public SQLRow(ArrayList<String> values, ArrayList<String> columns)
	{
		this.columns = columns;
		this.values = values;
	}
	
	public ArrayList<String> getContent()
	{
		return values;
	}
	
	public void printRow()
	{
		for(int i = 0; i < values.size(); i++)
		{
			if(columns != null)
			{
				System.out.print(columns.get(i) + ": " + values.get(i) + "; ");
			}
			else
			{
				System.out.print(values.get(i) + "; ");
			}
		}	
	}
	
	public ArrayList<String> getColumns()
	{
		return columns;
	}
	
	public String getValue(String key)
	{
		String ret = null;
		
		if(columns != null)
		{
			if(isKeyExist(key))
			{
				int index = columns.indexOf(key);
				ret = values.get(index);
			}
		}
		
		return ret;
	}
	
	public boolean isKeyExist(String key)
	{
		boolean ret = false;
		
		if(columns != null)
		{
			ret = columns.indexOf(key) != -1;
		}
		
		return ret;
	}
	
	public boolean isEmpty()
	{
		return values.size() == 0;
	}
}
