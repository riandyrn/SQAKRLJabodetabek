package com.sqakrljabodetabek.modules;
import com.sqakrljabodetabek.sql_things.SQL;
import com.sqakrljabodetabek.sql_things.SQLRow;

public class RouteResolver {

	private SQL sql;
	
	private final String START_STATION_COL = "stasiun_asal";
	private final String END_STATION_COL = "stasiun_tujuan";
	private final String ROUTE_TABLE = "rute";
	private final String NON_STOP_STATIONS_TABLE = "non_stop_stations";
	private final String NON_STOP_STATION_COL = "station";
	private final String TERMINALS_COL = "terminal";
	private final String NEIGHBOUR_STATION_COL = "stasiun_tetangga";
	
	public RouteResolver()
	{
		sql = new SQL();
	}
	
	public String resolveRoute(String start_station, String end_station)
	{
		String query = "SELECT * FROM " + ROUTE_TABLE  + " WHERE " + START_STATION_COL + "='" 
						+ start_station + "' AND " + END_STATION_COL + "='" + end_station + "'";
		
		return AnswerGenerator.constructRoute(start_station, end_station, sql.executeSelect(query).getFirstRow(), TERMINALS_COL);
	}
	
	public String getNeighbourStation(String start_station, String end_station)
	{
		String query = "SELECT * FROM " + ROUTE_TABLE  + " WHERE " + START_STATION_COL + "='" 
				+ start_station + "' AND " + END_STATION_COL + "='" + end_station + "' LIMIT 1";
		
		SQLRow row = sql.executeSelect(query).getFirstRow();
		
		String ret = "";
		
		if(!row.isEmpty())
		{
			ret = row.getValue(NEIGHBOUR_STATION_COL);
			if(ret == null)
			{
				ret = "";
			}
		}
		
		return ret;
	}
	
	public static void main(String[] args) 
	{
		RouteResolver resolver = new RouteResolver();
		//System.out.println(resolver.resolveRoute("bogor", "citayam"));
		//System.out.println(resolver.resolveRoute("bogor", "cilebut"));
		//System.out.println(resolver.resolveRoute("bogor", "tangerang"));
		System.out.println(resolver.resolveRoute("tanah_abang", "pasar_senen"));
		//System.out.println(resolver.getNeighbourStation("bogor", "depok"));*/
	}

	public boolean isDestinationAvailable(String end_station) {
		
		boolean ret = true;
		
		String query = "SELECT * FROM " + NON_STOP_STATIONS_TABLE + " WHERE " + NON_STOP_STATION_COL + "='" + end_station + "'";
		SQLRow row = (new SQL()).executeSelect(query).getFirstRow();
		
		if(row.getValue(NON_STOP_STATION_COL) != null)
		{
			ret = false;
		}
		
		return ret;
	}

}
