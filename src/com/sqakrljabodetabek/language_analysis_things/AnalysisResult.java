package com.sqakrljabodetabek.language_analysis_things;

import java.util.ArrayList;


public class AnalysisResult {
	
	private ArrayList<String[]> mapping;
	private String[] tokens;
	private ArrayList<Integer> consecutive_mapping;
	
	public AnalysisResult(String[] tokens,
			ArrayList<String[]> mapping, ArrayList<Integer> consecutive_mapping) {
		
		this.tokens = tokens;
		this.mapping = mapping;
		this.consecutive_mapping = consecutive_mapping;
	}

	public ArrayList<String[]> getMapping() 
	{
		return mapping;
	}

	public String[] getTokens() 
	{
		return tokens;
	}
	
	public ArrayList<Integer> getConsecutiveMapping()
	{
		return consecutive_mapping;
	}

}
