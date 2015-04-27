package com.sqakrljabodetabek.modules;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.sqakrljabodetabek.sql_things.SQL;


public class ScheduleResolver {

	private SQL sql;
	
	private final String DEPARTURE_COL = "waktu_berangkat";
	private final String START_STATION_COL = "stasiun_asal";
	private final String END_STATION_COL = "stasiun_tujuan";
	private final String SCHEDULE_TABLE = "jadwal_keberangkatan";
	
	public ScheduleResolver()
	{
		sql = new SQL();
	}
	
	public String getNextSchedule(String start_station, String end_station)
	{
		String currentTime = getCurrentTime();
		String next_station = getNextStation(start_station, end_station);
		
		String query = "SELECT " + DEPARTURE_COL + " FROM " + SCHEDULE_TABLE
						+ " WHERE " +  START_STATION_COL + "='" + start_station + "'" 
						+ " AND " + END_STATION_COL + "='" + next_station + "'"
						+ " AND " + DEPARTURE_COL + " >= '" + currentTime + "'";
		
		return AnswerGenerator.constructNextScheduleAnswer(start_station, end_station, sql.executeSelect(query).getFirstRow(), DEPARTURE_COL);		
	}
	
	public String getMostEarlySchedule(String start_station, String end_station)
	{
		String next_station = getNextStation(start_station, end_station);
		
		String query = "SELECT " + DEPARTURE_COL + " FROM " + SCHEDULE_TABLE
						+ " WHERE " +  START_STATION_COL + "='" + start_station + "'" 
						+ " AND " + END_STATION_COL + "='" + next_station + "'"
						+ " ORDER BY " + DEPARTURE_COL + " ASC LIMIT 1";
		
		return AnswerGenerator.constructMostEarlyScheduleAnswer(start_station, end_station, sql.executeSelect(query).getFirstRow(), DEPARTURE_COL);		
	}
	
	public String getMostLateSchedule(String start_station, String end_station)
	{
		String next_station = getNextStation(start_station, end_station);
		
		String query = "SELECT " + DEPARTURE_COL + " FROM " + SCHEDULE_TABLE
						+ " WHERE " +  START_STATION_COL + "='" + start_station + "'" 
						+ " AND " + END_STATION_COL + "='" + next_station + "'"
						+ " ORDER BY " + DEPARTURE_COL + " DESC LIMIT 1";
		
		return AnswerGenerator.constructMostLateScheduleAnswer(start_station, end_station, sql.executeSelect(query).getFirstRow(), DEPARTURE_COL);		
	}
	
	public String getSchedule(String start_station, String end_station) {
		
		String next_station = getNextStation(start_station, end_station);
		
		String query = "SELECT " + DEPARTURE_COL + " FROM " + SCHEDULE_TABLE
						+ " WHERE " +  START_STATION_COL + "='" + start_station + "'" 
						+ " AND " + END_STATION_COL + "='" + next_station + "'"
						+ " ORDER BY " + DEPARTURE_COL + " ASC";
		
		return AnswerGenerator.constructScheduleAnswer(start_station, end_station, sql.executeSelect(query), DEPARTURE_COL);		

		
	}
	
	private String getNextStation(String start_station, String end_station) {
		
		RouteResolver resolver = new RouteResolver();
		String neighbour_station = resolver.getNeighbourStation(start_station, end_station);
		String ret = end_station;
		
		if(!neighbour_station.isEmpty())
		{
			ret = neighbour_station;
		}
		
		return ret;
	}

	private String getCurrentTime()
	{
		Date dNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat ("HH:mm");
		
		String ret = ft.format(dNow) + ":00";
	    return ret;
	}
	
	public static void main(String args[])
	{
		ScheduleResolver resolver = new ScheduleResolver();
		System.out.println(resolver.getNextSchedule("bogor", "depok"));
		System.out.println(resolver.getMostEarlySchedule("bogor", "depok"));
		System.out.println(resolver.getMostLateSchedule("bogor", "depok"));
	}

}
