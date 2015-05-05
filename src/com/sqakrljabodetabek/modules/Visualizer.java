package com.sqakrljabodetabek.modules;

import java.util.ArrayList;
import java.util.Arrays;

public class Visualizer 
{
	
	private static final String MAP_RESOURCE_PATH = "/com/sqakrljabodetabek/map";
	private static final String MAP_FILENAME = "index.html";
	private static final String MAP_VARIABLES_FILENAME = "variables.js";
	
	public static void openMap(ArrayList<String> stations, ArrayList<String> lines)
	{
		writeVariables(stations, lines);
		openHTMLMap();
	}

	private static void openHTMLMap() 
	{
		CommonHelper.openFile(MAP_RESOURCE_PATH, MAP_FILENAME);
		
	}

	private static void writeVariables(ArrayList<String> stations, ArrayList<String> lines) 
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
		
		CommonHelper.writeToFile(str.toString(), MAP_RESOURCE_PATH, MAP_VARIABLES_FILENAME);
	}
	
	public static void main(String args[])
	{
		ArrayList<String> stations = new ArrayList<>(Arrays.asList("Jakarta Kota", "Manggarai"));
		ArrayList<String> lines = new ArrayList<>(Arrays.asList("Bogor / Depok - Tanah Abang - Pasar Senen - Jatinegara (PP)", "Maja / Parung Panjang / Serpong - Tanah Abang (PP)"));
		openMap(stations, lines);
	}
}
