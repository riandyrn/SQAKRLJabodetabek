package com.sqakrljabodetabek.modules;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class CommonHelper {

	public static ArrayList<String> loadTextFile(String RESOURCE_PATH, String filename)
	{
		ArrayList<String> ret = new ArrayList<>();
		
		URL file = CommonHelper.class.getResource(RESOURCE_PATH + filename);
        BufferedReader in;
		try {
			in = new BufferedReader(new InputStreamReader(file.openStream()));
			
			String inputLine;
	        while ((inputLine = in.readLine()) != null)
	        {
	        	if(inputLine.charAt(0) != '#')
	        	{
	        		ret.add(inputLine);
	        	}
	        }
	            
	        in.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.toString());
		}

		return ret;
	}
	
}
