package com.sqakrljabodetabek.language_analysis_things;
import java.util.ArrayList;


public class Frame {
	
	private ArrayList<Slot> container;
	
	public Frame()
	{
		container = new ArrayList<>();
	}
	
	public void setValue(String key, String value)
	{
		int i = 0;
		boolean found = false;
		
		while(!found && i < container.size())
		{
			if(container.get(i).getKey().equals(key))
			{
				found = true;
			}
			else
			{
				i++;
			}
		}
		
		if(found)
		{
			container.remove(i);
			Slot slot = new Slot(key, value);
			container.add(i, slot);
		}
	}
	
	public void setValue(Slot slot)
	{
		int i = 0;
		boolean found = false;
		
		while(!found && i < container.size())
		{
			if(container.get(i).getKey().equals(slot.getKey()))
			{
				found = true;
			}
			else
			{
				i++;
			}
		}
		
		if(found)
		{
			container.remove(i);
			container.add(i, slot);
		}
	}	
	
	public void add(Slot slot) 
	{
		container.add(slot);
	}
	
	public ArrayList<Slot> getContent()
	{
		return container;
	}
	
	public String getValue(String key)
	{
		boolean found = false;
		int i = 0;
		
		String ret = "";
		
		while(i < container.size() && !found)
		{
			if(container.get(i).getKey().equals(key))
			{
				found = true;
			}
			else
			{
				i++;
			}
		}
		
		if(found)
		{
			ret = container.get(i).getValue();
		}
		
		return ret;
	}
	
	public ArrayList<String> getKeys()
	{
		ArrayList<String> ret = new ArrayList<>();
		
		for(Slot slot: container)
		{
			ret.add(slot.getKey());
		}
		
		return ret;
	}
	
	public boolean hasKey(String key)
	{
		return getKeys().contains(key);
	}
	
	public static void printFrame(Frame frame)
	{
		if(frame != null)
		{
			ArrayList<Slot> container = frame.getContent();
	
			System.out.println("{");
			
			for(Slot slot: container)
			{
				System.out.println("\t" + slot.getKey() + ": " + slot.getValue());
			}
			
			System.out.println("}");
		}
		else
		{
			System.out.println("null frame");
		}

	}
	
	public String toString()
	{
		StringBuilder str = new StringBuilder();
		
		if(container != null)
		{
	
			str.append("{");
			
			for(int i = 0; i < container.size(); i++)
			{
				Slot slot = container.get(i);
				str.append(slot.getKey() + ": " + slot.getValue());
				
				if(i < container.size() - 1)
				{
					str.append("; ");
				}
			}
			
			str.append("}");
		}
		else
		{
			str.append("null frame");
		}
		
		return str.toString();
	}
	

}
