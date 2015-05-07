package com.sqakrljabodetabek.modules;

import java.util.ArrayList;
import java.util.Arrays;

public class Visualizer 
{
	
	private static final String VISUALIZATION_RESOURCE_PATH = "/com/sqakrljabodetabek/visualization";
	private static final String MAP_FILENAME = "map.html";
	private static final String MAP_VARIABLES_FILENAME = "map_variables.js";
	private static final String SCHEDULES_FILENAME = "schedules.html";
	private static final String SCHEDULES_VARIABLES_FILENAME = "schedules_variables.js";
	
	public static void openMap(ArrayList<String> stations, ArrayList<String> lines)
	{
		writeMapVariables(stations, lines);
		openHTMLMap();
	}

	public static void openSchedules(ArrayList<String> stations, ArrayList<String> schedules)
	{
		writeSchedulesVariables(stations, schedules);
		openHTMLSchedules();
	}

	private static void openHTMLSchedules() 
	{
		CommonHelper.openFile(VISUALIZATION_RESOURCE_PATH, SCHEDULES_FILENAME);
	}

	private static void openHTMLMap() 
	{
		CommonHelper.openFile(VISUALIZATION_RESOURCE_PATH, MAP_FILENAME);	
	}
	
	private static void writeSchedulesVariables(ArrayList<String> stations, ArrayList<String> schedules) 
	{
		StringBuilder str = new StringBuilder();
		
		str.append("var stations = [");
		for(String station: stations)
		{
			str.append("'" + station + "', ");
		}
		str.append("];\n");
		
		str.append("var departure_hours = [");
		for(String schedule: schedules)
		{
			str.append("'" + schedule + "', ");
		}
		str.append("];");
		
		CommonHelper.writeToFile(str.toString(), VISUALIZATION_RESOURCE_PATH, SCHEDULES_VARIABLES_FILENAME);			
	}

	private static void writeMapVariables(ArrayList<String> stations, ArrayList<String> lines) 
	{
		StringBuilder str = new StringBuilder();
		
		str.append("var stations = [");
		for(String station: stations)
		{
			str.append("'" + station + "', ");
		}
		str.append("];\n");
		
		str.append("var jurusans = [");
		for(String line: lines)
		{
			str.append("'" + line+ "', ");
		}
		str.append("];");
		
		CommonHelper.writeToFile(str.toString(), VISUALIZATION_RESOURCE_PATH, MAP_VARIABLES_FILENAME);
	}
	
	public static void main(String args[])
	{
		ArrayList<String> stations = new ArrayList<>(Arrays.asList("Jakarta Kota", "Manggarai"));
		ArrayList<String> lines = new ArrayList<>(Arrays.asList("Bogor / Depok - Tanah Abang - Pasar Senen - Jatinegara (PP)", "Maja / Parung Panjang / Serpong - Tanah Abang (PP)"));
		openMap(stations, lines);
	}
}
