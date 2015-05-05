package com.sqakrljabodetabek.modules;
import java.util.ArrayList;

import com.mysql.fabric.xmlrpc.base.Array;
import com.sqakrljabodetabek.sql_things.SQL;
import com.sqakrljabodetabek.sql_things.SQLRow;
import com.sqakrljabodetabek.sql_things.SQLRows;


public class AnswerGenerator {
	
	public static String constructRoute(String start, String end, SQLRow row, String terminal_col)
	{
		/*
		 * Return answer of route in String and execute map
		 */
		
		//row.printRow();
		String ret = "";
		
		StringBuilder str = new StringBuilder();
		
		ArrayList<String> stations = new ArrayList<>();
		ArrayList<String> lines = new ArrayList<>();
		
		stations.add(start);
		
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
					stations.add(token);
					str.append("->");
				}
				
			}
			else
			{
				str.append("->");
			}
		}
		
		stations.add(end);
		
		if(row.isKeyExist("jurusan"))
		{
			String jurusans = row.getValue("jurusan");
			String[] tokens = jurusans.split(",");
			
			System.out.println(jurusans);
			
			for(String token: tokens)
			{
				lines.add(resolveLine(token));
			}
		}
		
		if(row.isEmpty())
		{
			ret = "Tidak ada jalur dari " + start + " ke " +  end;
		}
		else
		{
			ret = "Rute dari " + start + " ke " + end + ": " + start + str.toString() + end;
			Visualizer.openMap(stations, lines);
		}
		
		return ret;
	}

	private static String resolveLine(String line_id) 
	{
		SQL sql = new SQL();
		SQLRow row = sql.executeSelect("SELECT * FROM jurusan WHERE id='" + line_id + "'").getFirstRow();
		String ret = row.getValue("deskripsi");
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
