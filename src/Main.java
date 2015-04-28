import java.net.URL;

import edu.cmu.sphinx.frontend.util.AudioFileDataSource;
import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;


public class Main {
	
	static ConfigurationManager cm;
	static Recognizer recognizer;
	private static final String RESOURCE_PATH = "/com/sqakrljabodetabek/resources/";
	
	public static void main(String[] args) {
		
		/*
		initConfig("recognition_ina.xml");
		listenToMicrophone();
		*/
		
		
		initConfig("transcriber_ina.xml");
		transcribeAudio("test_stations_not_in_lm.wav");
		
	}
	
	public static void initConfig(String config_filename)
	{
		cm = new ConfigurationManager(Main.class.getResource(RESOURCE_PATH + "configs/" + config_filename));
		
		// allocate the recognizer
        System.out.println("Loading...");
        recognizer = (Recognizer) cm.lookup("recognizer");
        recognizer.allocate();
	}
	
	public static void listenToMicrophone()
	{
        // start the microphone or exit if the programm if this is not possible
        Microphone microphone = (Microphone) cm.lookup("microphone");
        if (!microphone.startRecording()) {
            System.out.println("Cannot start microphone.");
            recognizer.deallocate();
            System.exit(1);
        }

        // loop the recognition until the programm exits.
        while (true) {
            System.out.println("Start speaking. Press Ctrl-C to quit.\n");

            Result result = recognizer.recognize();

            if (result != null) {
                String resultText = result.getBestResultNoFiller();
                System.out.println("You said: " + resultText + '\n');
            } else {
                System.out.println("I can't hear what you said.\n");
            }
        }
        
	}
	
    public static void transcribeAudio(String filename)
    {
        URL audioURL = Main.class.getResource(RESOURCE_PATH + "resources/" + filename);
        
        AudioFileDataSource dataSource = (AudioFileDataSource) cm.lookup("audioFileDataSource");
        dataSource.setAudioFile(audioURL, null);
        
        Result result;
        
        while((result = recognizer.recognize()) != null)
        {
        	String resultText = result.getBestResultNoFiller();
        	System.out.println(resultText);
        }
        
    }

}
