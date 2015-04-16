package com.sqakrljabodetabek.modules;

import java.net.URL;
import java.util.ArrayList;

import edu.cmu.sphinx.frontend.util.AudioFileDataSource;
import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;


public class SpeechRecognizer {

	private ConfigurationManager cm;
	private Recognizer recognizer;
	private final String RESOURCE_PATH = "/com/sqakrljabodetabek/resources/";
	
	public SpeechRecognizer()
	{
		initConfig("recognition_ina.xml");
	}
	
	public SpeechRecognizer(boolean isTranscribeMode)
	{
		if(isTranscribeMode)
		{
			initConfig("transcriber_ina.xml");
		}
		else 
		{
			initConfig("recognition_ina.xml");
		}
	}
	
	private void initConfig(String config_filename)
	{
		cm = new ConfigurationManager(getClass().getResource(RESOURCE_PATH + "configs/" + config_filename));
		
		// allocate the recognizer
        System.out.println("Loading...");
        recognizer = (Recognizer) cm.lookup("recognizer");
        recognizer.allocate();
	}
	
	public String listen()
	{
		Microphone microphone = (Microphone) cm.lookup("microphone");
		
		microphone.startRecording();
		
        Result result = recognizer.recognize();
        String resultText = "";
        
        if (result != null) {
            resultText = result.getBestResultNoFiller();
        } else {
            resultText = "Mohon maaf, saya tidak mendengar apa yang Anda katakan";
        }
        
        return resultText;
	}
	
    public ArrayList<String> transcribeAudio(String filename)
    {
        URL audioURL = getClass().getResource(RESOURCE_PATH + "resources/" + filename);
        
        AudioFileDataSource dataSource = (AudioFileDataSource) cm.lookup("audioFileDataSource");
        dataSource.setAudioFile(audioURL, null);
        
        ArrayList<String> ret = new ArrayList<>();
        Result result;
        String resultText;
        
        while((result = recognizer.recognize()) != null)
        {
        	resultText = result.getBestFinalResultNoFiller();
        	
        	if(!resultText.isEmpty())
        	{
        		//System.out.println(resultText);
        		ret.add(resultText);
        	}
        }
        
        return ret;
        
    }
    
	public static void main(String[] args) {
		
		SpeechRecognizer recognizer = new SpeechRecognizer(true);
		/*System.out.println("Silakan mulai berbicara");
		while(true)
		{
			String result = recognizer.listen();
			
			if(result != null)
			{
				if(result.length() > 0)
				{
					System.out.println(result);
				}
			}
		}*/
		
		recognizer.transcribeAudio("skenario_jadwal.wav");

	}

}
