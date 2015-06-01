package com.sqakrljabodetabek.modules;

import java.io.IOException;

public class SpeechSynthesizer {
	
	private static final String FESTIVAL_PATH = "C:\\festival\\";
	private static final String SCRIPT_FILENAME = "ucapan.scm";
	private static final String FESTIVAL_FILENAME = "festival";
	
	public void say(String sentence)
	{
		writeToFile(sentence);
		try {
			Runtime.getRuntime().exec(FESTIVAL_PATH + FESTIVAL_FILENAME + " " + FESTIVAL_PATH + SCRIPT_FILENAME);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void writeToFile(String sentence)
	{
		StringBuilder str = new StringBuilder();
		str.append("(SayText \"" + cleanupSentence(sentence) + "\")");
		CommonHelper.writeToFileAbsolutePath(str.toString(), FESTIVAL_PATH + SCRIPT_FILENAME);
	}
	
	private String cleanupSentence(String sentence)
	{
		String tmp = sentence.replaceAll("/", "atau").replaceAll("-", "").replace(">", " ").replace("_", " ");
		return tmp;
	}
	
	public static void main(String args[])
	{
		SpeechSynthesizer sythesizer = new SpeechSynthesizer();
		sythesizer.say("Anda bisa menuju kota. Melalui rute: jakarta_kota->bogor->gondangdia");
	}

}
