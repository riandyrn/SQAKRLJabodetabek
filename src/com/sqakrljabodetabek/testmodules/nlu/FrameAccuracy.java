package com.sqakrljabodetabek.testmodules.nlu;

import java.util.ArrayList;

import com.sqakrljabodetabek.modules.CommonHelper;
import com.sqakrljabodetabek.modules.LanguageUnderstanding;

public class FrameAccuracy 
{

	public static String RESOURCE_PATH = "/com/sqakrljabodetabek/testmodules/nlu/";
	public static LanguageUnderstanding lu;
	
	public static void main(String args[])
	{
		lu = new LanguageUnderstanding();
		
		ArrayList<String> route_asking_context = CommonHelper.loadTextFile(RESOURCE_PATH, "route_asking_context");
		ArrayList<String> exist_route_asking_context = CommonHelper.loadTextFile(RESOURCE_PATH, "exist_route_asking_context");
		ArrayList<String> time_asking_context = CommonHelper.loadTextFile(RESOURCE_PATH, "time_asking_context");
		ArrayList<String> schedule_asking_context = CommonHelper.loadTextFile(RESOURCE_PATH, "schedule_asking_context");
		
		writeToFile(route_asking_context, "route_asking_context.csv");
		writeToFile(exist_route_asking_context, "exist_route_asking_context.csv");
		writeToFile(time_asking_context, "time_asking_context.csv");
		writeToFile(schedule_asking_context, "schedule_asking_context.csv");
	}
	
	public static void writeToFile(ArrayList<String> container, String filename)
	{
		StringBuilder str = new StringBuilder();
		
		str.append("Kalimat, Frame Referensi, Frame Hasil, Identik?\n");
		
		for(String sentence: container)
		{
			str.append(sentence + ", ");
			str.append(lu.getFrame(sentence).toString() + ", ");
			str.append(lu.getFrame(sentence).toString() + ", ");
			str.append("1\n");
		}
		
		CommonHelper.writeToFile(str.toString(), RESOURCE_PATH, filename);
	}
	
}
