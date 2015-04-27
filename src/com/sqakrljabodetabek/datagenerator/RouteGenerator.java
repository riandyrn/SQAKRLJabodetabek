package com.sqakrljabodetabek.datagenerator;

import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;

import com.sqakrljabodetabek.modules.CommonHelper;
import com.sqakrljabodetabek.sql_things.SQL;
import com.sqakrljabodetabek.sql_things.SQLRow;
import com.sqakrljabodetabek.sql_things.SQLRows;

public class RouteGenerator {

	private static final String RESOURCE_PATH = "/com/sqakrljabodetabek/datagenerator/";
	private static final String NOT_EXIST_JURUSAN_INTERMEDIARY = "-";
	private static final String ROUTE_TABLE_NAME = "rute";
	
	private ArrayList<String> rute;
	private ArrayList<String> jalur_antar_rute;
	private ArrayList<String> non_stop;
	
	private SQL sql;
	
	public RouteGenerator()
	{
		loadFiles();
		sql = new SQL();
	}
	
	private void loadFiles()
	{
		rute = CommonHelper.loadTextFile(RESOURCE_PATH, "rute");
		jalur_antar_rute = CommonHelper.loadTextFile(RESOURCE_PATH, "jalur_antar_rute");
		non_stop = CommonHelper.loadTextFile(RESOURCE_PATH, "non_stop");
	}
	
	public void generateData()
	{
		SQLRows container = new SQLRows();
		String station_origin;
		String station_destination;
		String jurusan_origin;
		String jurusan_destination;
		ArrayList<String> rute_found;
		String terminal = "";
		String stasiun_tetangga;
		ArrayList<String> data_jalur;
		String jurusan_intermediary = "";
		String jurusan = "";
		ArrayList<String> values;
		
		for(String row: rute)
		{
			station_origin = row.split("\\s")[1];
			
			if(!non_stop.contains(station_origin))
			{
				for(String col: rute)
				{
					station_destination = col.split("\\s")[1];
					
					if(!non_stop.contains(station_destination))
					{
						if(!station_origin.equals(station_destination))
						{
							if(!isInSameRoute(station_origin, station_destination))
							{
								rute_found = getRuteExist(station_origin, station_destination);
								
								if(rute_found.size() > 0)
								{		
									jurusan_origin = rute_found.get(0);
									jurusan_destination = rute_found.get(1);
									
									data_jalur = getDataJalur(jurusan_origin, jurusan_destination);
									jurusan_intermediary = data_jalur.get(1);
									
									if(!jurusan_intermediary.equals(NOT_EXIST_JURUSAN_INTERMEDIARY))
									{
										jurusan = jurusan_origin + "," + jurusan_intermediary + "," + jurusan_destination;
									}
									else
									{
										jurusan = jurusan_origin + "," + jurusan_destination;
									}
									
									terminal = data_jalur.get(0);
								}
							}
							else
							{
								// kalo ternyata ada di satu jalur
								jurusan = getJurusan(station_origin).get(0); // ambil jurusan yang pertama
							}
							
							stasiun_tetangga = getNextStoppableNeighbourStation(station_origin, station_destination);
							
							if(station_destination.equals(terminal))
							{
								terminal = "";
							}
							
							if(station_destination.equals(stasiun_tetangga))
							{
								stasiun_tetangga = "";
							}
							
							values = new ArrayList<>();
							values.add(station_origin);
							values.add(station_destination);
							values.add(terminal);
							values.add(stasiun_tetangga);
							values.add(jurusan);
							
							SQLRow entry = new SQLRow(values);
							container.add(entry);
						}
					}
				}
			}
		}
		
		//container.print();
		writeToDatabase(container);
		System.out.println("penulisan data rute selesai");
	}
	
	private boolean isInSameRoute(String station_origin, String station_destination)
	{
		
		boolean ret = false;
		ArrayList<String> jurusan_origin = getJurusan(station_origin);
		ArrayList<String> jurusan_destination = getJurusan(station_destination);
		
		for(String val_origin: jurusan_origin)
		{
			for(String val_destination: jurusan_destination)
			{
				if(val_origin.equals(val_destination))
				{
					ret = true;
					break;
				}
			}
		}
		
		return ret;
	}
	
	private void writeToDatabase(SQLRows container) 
	{
		sql.executeInsert(container, ROUTE_TABLE_NAME);	
	}
	
	private ArrayList<String> getRuteExist(String station_origin, String station_destination)
	{
		/*
		 * ini return cuma 1 rute yang exist di jalur_antar_rute
		 * misalkan 1,2
		 */
		
		ArrayList<String> ret = new ArrayList<>();
		ArrayList<String> jurusan_origins = getJurusan(station_origin);
		ArrayList<String> jurusan_destinations = getJurusan(station_destination);
		
		for(String jurusan_origin: jurusan_origins)
		{
			for(String jurusan_destination: jurusan_destinations)
			{
				String key = jurusan_origin + "," + jurusan_destination;
				
				for(String row: jalur_antar_rute)
				{
					String rute = row.split("\\s")[0];
					if(rute.equals(key))
					{
						ret.add(jurusan_origin);
						ret.add(jurusan_destination);
						return ret;
					}
				}
			}
		}
		
		return ret;
		
	}
	
	private ArrayList<String> getJurusan(String station)
	{
		/*
		 * Perhatikan bahwa ini untuk multi-jurusan
		 */
		
		ArrayList<String> ret = new ArrayList<>();
		
		for(String row: rute)
		{
			String[] data = row.split("\\s");
			String jurusan = data[0];
			String stasiun = data[1];
			
			if(stasiun.equals(station))
			{
				ret.add(jurusan);
			}
			
		}
		
		return ret;
	}
	
	private ArrayList<String> getDataJalur(String jurusan_origin, String jurusan_destination)
	{
		ArrayList<String> ret = new ArrayList<>();
		
		String key = jurusan_origin + "," + jurusan_destination;
		
		for(String row: jalur_antar_rute)
		{
			String[] data = row.split("\\s");
			
			if(data[0].equals(key))
			{
				ret.add(data[1]); //data stasiun intersection
				ret.add(data[2]); //data intermediary jurusan
				break;
			}
		}
		
		return ret;
	}
	
	private int getIndexOfStasiunInRute(String jurusan, String stasiun)
	{
		
		int ret = -1;
		
		for(int i = 0; i < rute.size(); i++)
		{
			String[] data = rute.get(i).split("\\s");
			if(data[0].equals(jurusan) && data[1].equals(stasiun))
			{
				ret = i;
				break;
			}
		}
		
		return ret;
	}
	
	private String getFirstSameJurusan(String station_origin, String station_destination)
	{
		String ret = "";
		
		ArrayList<String> jurusan_origin = getJurusan(station_origin);
		ArrayList<String> jurusan_destination = getJurusan(station_destination);
		
		for(String val_origin: jurusan_origin)
		{
			for(String val_destination: jurusan_destination)
			{
				if(val_destination.equals(val_origin))
				{
					ret = val_destination;
				}
			}
		}
		
		return ret;
	}
	
	private String getNextStoppableNeighbourStation(String station_origin, String station_destination)
	{
		String ret = "";
		String jurusan;
		String intersection_station;
		int index_origin = -1;
		int index_destination = -1;
		
		if(isInSameRoute(station_origin, station_destination))
		{
			jurusan = getFirstSameJurusan(station_origin, station_destination);
			index_origin = getIndexOfStasiunInRute(jurusan, station_origin);
			index_destination = getIndexOfStasiunInRute(jurusan, station_destination);
		}
		else
		{
			ArrayList<String> tmp = getRuteExist(station_origin, station_destination);
			intersection_station = getDataJalur(tmp.get(0), tmp.get(1)).get(0).split(",")[0]; // ini kayak gini karena station formatnya bisa tanah_abang,duri
			jurusan = getFirstSameJurusan(station_origin, intersection_station);
			index_origin = getIndexOfStasiunInRute(jurusan, station_origin);
			index_destination = getIndexOfStasiunInRute(jurusan, intersection_station);
		}
		
		if(index_origin < index_destination)
		{
			ret = getNearestStoppableStation(index_origin, "forward");
		}
		else
		{
			ret = getNearestStoppableStation(index_origin, "backward");
		}
		
		return ret;
	}
	
	private String getNearestStoppableStation(int index_origin, String mode) {
		
		String ret = "";
		boolean foundStop = false;
		int i;
		
		if(mode.equals("forward"))
		{
			i = 1;
		}
		else
		{
			i = -1;
		}
		
		while(!foundStop)
		{
			String station = rute.get(index_origin + i).split("\\s")[1];
			if(!non_stop.contains(station))
			{
				ret = station;
				foundStop = true;
			}
			else
			{
				if(mode.equals("forward"))
				{
					i++;
				}
				else if(mode.equals("backward"))
				{
					i--;
				}
			}
		}
		
		return ret;
	}

	public static void main(String args[])
	{
		RouteGenerator rg = new RouteGenerator();
		rg.generateData();
	}
	
}
