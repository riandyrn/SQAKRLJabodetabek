package com.sqakrljabodetabek.testmodules.asr;

import java.util.ArrayList;
import java.util.Arrays;

import com.sqakrljabodetabek.modules.CommonHelper;

public class WordAccuracyTest {

	public static String RESOURCE_PATH = "/com/sqakrljabodetabek/testmodules/nlu/";
	
	public static double getWordAccuracy(String reference, String recognized)
	{

		
		String[] reference_words = reference.split("\\s");
		String[] recognized_words = recognized.split("\\s");
		
		double H = 0;
		double I = 0;
		double N = reference_words.length;
				
		for(String recognized_word: recognized_words)
		{
			ArrayList<String> selected_words = new ArrayList<>();
			for(String reference_word: reference_words)
			{
				if(recognized_word.equals(reference_word))
				{
					if(!selected_words.contains(reference_word))
					{
						H++;
						selected_words.add(reference_word);
					}
				}
			}
		}
		
		I = recognized_words.length - H;
		
		System.out.println("H: " + H);
		System.out.println("I: " + I);
		System.out.println("N: " + N);
		
		return (H - I) / N;
	}
	
	public static void main(String[] args) 
	{	
		ArrayList<String> filenames = new ArrayList<>(Arrays.asList("jaenuri", "mahdi", "riandy", "rifqi", "rosin", "arini", "ina", "kak_azul", "kharisma", "kiki"));
		ArrayList<String> reference_file = CommonHelper.loadTextFile(RESOURCE_PATH, "test_reference");
		StringBuilder str = new StringBuilder();
		
		for(String filename: filenames)
		{	
			ArrayList<String> test_file = CommonHelper.loadTextFile(RESOURCE_PATH, filename);
			
			str.append("FILENAME: " + filename + "\n");
			for(int i = 0; i < test_file.size(); i++)
			{
				str.append(getWordAccuracy(reference_file.get(i), test_file.get(i)) + "\n");
			}
			str.append("-------------------------------------------------------------\n");
		}
		
		CommonHelper.writeToFile(str.toString(), RESOURCE_PATH, "test_result");
	}

}
