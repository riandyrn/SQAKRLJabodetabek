package com.sqakrljabodetabek.modules;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
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
	
	public static void openFile(String path, String filename)
	{
		try {
			Desktop.getDesktop().browse(CommonHelper.class.getResource(path + "/" + filename).toURI());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void writeToFile(String content, String path, String filename)
	{
		try {
			
			String current_path = CommonHelper.class.getResource(path).getPath()  + "/" + filename;
			File file = new File(current_path);
 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
 
			System.out.println("Done");
 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void writeToFileAbsolutePath(String content, String absolute_path)
	{
		try {
			
			String current_path = absolute_path;
			File file = new File(current_path);
 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
 
			System.out.println("Done");
 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[])
	{
		CommonHelper.writeToFile("Yuhu!", "/com/sqakrljabodetabek/map", "test.txt");
		openFile("/com/sqakrljabodetabek/map", "test.txt");
	}
}
