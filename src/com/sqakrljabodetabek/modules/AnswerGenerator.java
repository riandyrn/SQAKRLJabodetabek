package com.sqakrljabodetabek.modules;
import com.sqakrljabodetabek.sql_things.SQLRow;
import com.sqakrljabodetabek.sql_things.SQLRows;


public class AnswerGenerator {
	
	public static String constructRoute(String start, String end, SQLRow row, String terminal_col)
	{
		String ret = "";
		
		StringBuilder str = new StringBuilder();
		
		if(row.isKeyExist(terminal_col))
		{
			String terminals = row.getValue(terminal_col);
			
			if(!terminals.isEmpty())
			{
				
				String[] tokens = terminals.split(",");
				str.append("->");
				
				for(String token: tokens)
				{
					str.append(token);
					str.append("->");
				}
				
			}
			else
			{
				str.append("->");
			}
		}
		
		if(row.isEmpty())
		{
			ret = "Tidak ada jalur dari " + start + " ke " +  end;
		}
		else
		{
			ret = "Rute dari " + start + " ke " + end + ": " + start + str.toString() + end;
		}
		
		return ret;
	}

	public static String constructNextScheduleAnswer(String start, String end, SQLRow row, String departure_col) 
	{
		return constructScheduleAnswer(start, end, row, departure_col, "berikutnya");
	}

	public static String constructMostEarlyScheduleAnswer(String start, String end, SQLRow row, String departure_col) 
	{
		return constructScheduleAnswer(start, end, row, departure_col, "paling awal");
	}
	
	public static String constructMostLateScheduleAnswer(String start, String end, SQLRow row, String departure_col) 
	{
		return constructScheduleAnswer(start, end, row, departure_col, "paling akhir");
	}
	
	private static String constructScheduleAnswer(String start, String end, SQLRow row, String departure_col, String keyword)
	{
		String ret = "Tidak ada jadwal kereta dari " + start + " menuju " + end;
		
		if(!row.isEmpty())
		{
			if(row.isKeyExist(departure_col))
			{
				String time = row.getValue(departure_col);
				time = time.substring(0, time.length() - 3);
				ret = "Kereta " + keyword + " berangkat dari " + start + " menuju " + end + " pada pukul " + time;
			}
		}
		
		return ret;
	}

	public static String constructScheduleAnswer(String start, String end, SQLRows rows, String departure_col) 
	{
		String ret = "Tidak ada jadwal kereta dari " + start + " menuju " + end;
		
		if(!rows.isEmpty())
		{
			StringBuilder str = new StringBuilder();
			str.append("Jadwal dari " + start + " menuju " + end + "\n");
			for(SQLRow row: rows.getContent())
			{
				if(!row.isEmpty())
				{
					if(row.isKeyExist(departure_col))
					{
						String time = row.getValue(departure_col);
						time = time.substring(0, time.length() - 3);
						str.append(time + "\n");
					}
				}
			}
			
			ret = str.toString();
		}
		
		return ret;
	}

	public static String handleMissingInformation(String key) 
	{
		String ret = "";
		
		if(key.equals("dari"))
		{
			ret = "dari stasiun apa Anda berangkat?";
		}
		else if(key.equals("ke"))
		{
			ret = "kemana Anda akan pergi?";
		}
		
		return ret;
	}

	public static String handleNullContextFrame() {
		return "";
	}
}
