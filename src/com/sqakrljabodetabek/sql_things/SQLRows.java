package com.sqakrljabodetabek.sql_things;
import java.util.ArrayList;


public class SQLRows {

	ArrayList<SQLRow> container;
	
	public SQLRows()
	{
		container = new ArrayList<>();
	}
	
	public void add(SQLRow row)
	{
		container.add(row);
	}
	
	public ArrayList<SQLRow> getContent()
	{
		return container;
	}
	
	public SQLRow getFirstRow()
	{
		SQLRow ret = new SQLRow();
	
		if(container.size() > 0)
		{
			ret = container.get(0);
		}
		
		return ret;
	}
	
	public SQLRow getLastRow()
	{
		SQLRow ret = new SQLRow();
		
		if(container.size() > 0)
		{
			ret = container.get(container.size() - 1);
		}
		
		return ret;
	}
	
	public int getCount()
	{
		return container.size();
	}
	
	public boolean isEmpty()
	{
		return container.size() == 0;
	}
}
