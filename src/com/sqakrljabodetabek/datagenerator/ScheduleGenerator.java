package com.sqakrljabodetabek.datagenerator;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Random;

import com.sqakrljabodetabek.modules.CommonHelper;
import com.sqakrljabodetabek.sql_things.SQL;
import com.sqakrljabodetabek.sql_things.SQLRow;
import com.sqakrljabodetabek.sql_things.SQLRows;

public class ScheduleGenerator {
	
	private static final String RESOURCE_PATH = "/com/sqakrljabodetabek/datagenerator/";
	private static final String SCHEDULE_TABLE_NAME = "jadwal_keberangkatan";
	private static final int MAX_NUMBER_OF_JADWAL = 20;
	
	private SQL sql;
	
	private ArrayList<String> rute;
	private ArrayList<String> non_stop;
	
	public ScheduleGenerator()
	{
		sql = new SQL();
		loadFiles();
	}
	
	private void loadFiles()
	{
		rute = CommonHelper.loadTextFile(RESOURCE_PATH, "rute");
		non_stop = CommonHelper.loadTextFile(RESOURCE_PATH, "non_stop");
	}
	
	public void generateData()
	{
		SQLRows container = new SQLRows();
		
		for(int i = 0; i < rute.size() - 1; i++)
		{
			String[] origin_arr = rute.get(i).split("\\s");
			String[] destination_arr = rute.get(i+1).split("\\s");
			
			String origin = origin_arr[1];
			String jurusan_origin = origin_arr[0];
			
			String destination = destination_arr[1];
			String jurusan_destination = destination_arr[0];
			
			if(jurusan_origin.equals(jurusan_destination))
			{
				if(!non_stop.contains(origin) && !non_stop.contains(destination))
				{
					for(int j = 0; j < MAX_NUMBER_OF_JADWAL; j++)
					{
						container.add(generateRowSchedule(jurusan_destination, origin, destination));
						container.add(generateRowSchedule(jurusan_destination, destination, origin)); //ini untuk generate schedule ke arah sebaliknya
					}
				}
			}
		}
		
		//container.print();
		writeToDatabase(container);
		System.out.println("penulisan data jadwal selesai");
	}
	
	private void writeToDatabase(SQLRows container) 
	{
		sql.executeInsert(container, SCHEDULE_TABLE_NAME);	
	}

	private SQLRow generateRowSchedule(String jurusan, String origin, String destination)
	{
		
		ArrayList<String> values = new ArrayList<>();
		values.add(jurusan);
		values.add(getRandomDepartureTime());
		values.add(origin);
		values.add(destination);
		
		SQLRow row = new SQLRow(values);
		
		return row;
	}
	
	private String getRandomDepartureTime() {
		
		Random rand = new Random();
		Time time = new Time(rand.nextLong());
		
		return time.toString();
	}
	
	public static void main(String args[])
	{
		ScheduleGenerator dg = new ScheduleGenerator();
		dg.generateData();
	}

}
