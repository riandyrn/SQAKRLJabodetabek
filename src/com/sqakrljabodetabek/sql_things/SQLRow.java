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
		for(int i = 0; i < columns.size(); i++)
		{
			System.out.print(columns.get(i) + ": " + values.get(i) + "; ");
		}
	}
	
	public ArrayList<String> getColumns()
	{
		return columns;
	}
	
	public String getValue(String key)
	{
		int index = columns.indexOf(key);
		return values.get(index);
	}
	
	public boolean isKeyExist(String key)
	{
		return columns.indexOf(key) != -1;
	}
	
	public boolean isEmpty()
	{
		return columns.size() == 0;
	}
}
