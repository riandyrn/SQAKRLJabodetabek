package com.sqakrljabodetabek.modules;

import java.net.URL;

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
        String resultText;
        
        if (result != null) {
            resultText = result.getBestFinalResultNoFiller();
        } else {
            resultText = "Mohon maaf, saya tidak mendengar apa yang Anda katakan";
        }

        return resultText;
	}
	
    public String transcribeAudio(String filename)
    {
        URL audioURL = getClass().getResource(RESOURCE_PATH + "resources/" + filename);
        
        AudioFileDataSource dataSource = (AudioFileDataSource) cm.lookup("audioFileDataSource");
        dataSource.setAudioFile(audioURL, null);
        
        Result result;
        String resultText = "";
        
        while((result = recognizer.recognize()) != null)
        {
        	resultText = result.getBestFinalResultNoFiller();
        	System.out.println(resultText);
        }
        
        return resultText;
        
    }
    
	public static void main(String[] args) {
		
		SpeechRecognizer recognizer = new SpeechRecognizer();
		System.out.println("Silakan mulai berbicara");
		while(true)
		{
			String result = recognizer.listen();
			
			if(result.length() > 0)
			{
				System.out.println(result);
			}
		}

	}

}
