package com.sqakrljabodetabek.modules;
import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.sqakrljabodetabek.language_analysis_things.Frame;
import com.sqakrljabodetabek.language_analysis_things.Slot;


public class DialogueManager {

	private Frame context_frame;
	
	private Frame route_asking;
	private Frame time_asking;
	private Frame schedule_asking;
	private Frame exist_route_asking;
	private Frame exist_schedule_asking;
	
	private final String ROUTE_ASKING_CATEGORY = "route_asking";
	private final String TIME_ASKING_CATEGORY = "time_asking";
	private final String SCHEDULE_ASKING_CATEGORY = "schedule_asking";
	private final String EXIST_ROUTE_ASKING_CATEGORY = "exist_route_asking";
	private final String EXIST_SCHEDULE_ASKING_CATEGORY = "exist_schedule_asking";
			
	private final String LIST_JADWAL_IDENTIFIER_KEY = "list_jadwal_identifier";
	private final String TIME_MODIFIER_KEY = "time_modifier";
	private final String PLACE_IDENTIFIER_ORIGIN_KEY = "dari";
	private final String PLACE_IDENTIFIER_DESTINATION_KEY = "ke";
	private final String EXIST_ROUTE_IDENTIFIER_KEY = "ada_kereta";
	private final String EXIST_SCHEDULE_IDENTIFIER_KEY = "ada_jadwal";
	
	private final String START_STATION_FRAME_IDENTIFIER = "dari";
	private final String END_STATION_FRAME_IDENTIFIER = "ke";
	
	private final String TIME_NEXT_VALUE_IDENTIFIER = "berikutnya";
	private final String TIME_MOST_EARLY_VALUE_IDENTIFIER = "paling_awal";
	private final String TIME_MOST_LATE_VALUE_IDENTIFIER = "paling_akhir";
	
	private final String SCHEDULE_VALUE_IDENTIFIER = "jadwal";
	
	private SpeechRecognizer speechRecognizer;
	private LanguageUnderstanding languageUnderstanding;
	private RouteResolver routeResolver;
	private ScheduleResolver scheduleResolver;
	
	private final String RESOURCE_PATH = "/com/sqakrljabodetabek/scenario_files/";

	public DialogueManager()
	{
		initModules();
		constructReferenceFrames();
	}
	
	public DialogueManager(boolean isTranscribeFile)
	{
		initModules(isTranscribeFile);
		constructReferenceFrames();
	}
	
	private String getFirstEmptyAttributeKeyInContextFrame() 
	{
		String ret = "";
		ArrayList<String> keys = context_frame.getKeys();
		for(String key: keys)
		{
			if(context_frame.getValue(key).isEmpty())
			{
				ret = key;
			}
		}
		
		return ret;
	}
	
	private void printString(String string)
	{
		System.out.println(string);
	}
	
	private String doAppropriateAction() 
	{
		String context_frame_category = getContextFrameCategory();
		
		String start_station = context_frame.getValue(START_STATION_FRAME_IDENTIFIER);
		String end_station = context_frame.getValue(END_STATION_FRAME_IDENTIFIER);
		
		String ret = "";
		
		if(context_frame_category.equals(EXIST_ROUTE_ASKING_CATEGORY))
		{
			
		}
		else if(context_frame_category.equals(EXIST_SCHEDULE_ASKING_CATEGORY))
		{
			
		}
		else if(context_frame_category.equals(TIME_ASKING_CATEGORY))
		{
			//System.out.println("TIME_ASKING");
			String time_modifier = context_frame.getValue(TIME_MODIFIER_KEY);
			if(time_modifier.equals(TIME_MOST_LATE_VALUE_IDENTIFIER))
			{
				ret = scheduleResolver.getMostLateSchedule(start_station, end_station);
			}
			else if(time_modifier.equals(TIME_MOST_EARLY_VALUE_IDENTIFIER))
			{
				ret = scheduleResolver.getMostEarlySchedule(start_station, end_station);
			}
			else if(time_modifier.equals(TIME_NEXT_VALUE_IDENTIFIER))
			{
				ret = scheduleResolver.getNextSchedule(start_station, end_station);
			}
		}
		else if(context_frame_category.equals(SCHEDULE_ASKING_CATEGORY))
		{
			String list_jadwal_identifier = context_frame.getValue(LIST_JADWAL_IDENTIFIER_KEY);
			
			if(list_jadwal_identifier.equals(SCHEDULE_VALUE_IDENTIFIER))
			{
				ret = scheduleResolver.getSchedule(start_station, end_station);
			}
		}
		else
		{
			//System.out.println("ROUTE_ASKING");
			ret = routeResolver.resolveRoute(start_station, end_station);
		}
		
		return ret;
		
		//destroyContextFrame();
	}

	private void welcomingUser()
	{
		cls();
		
		try 
		{
			Thread.sleep(1000);
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		
		String welcome_message = "Layanan informasi yang bisa ditanyakan: \n"
									+ "-Kereta dari <stasiun> ke <stasiun> \n"
									+ "-Kereta <selanjutnya | paling pagi | paling akhir> dari <place> ke <place> \n"
									+ "-Jadwal kereta dari <stasiun> ke <stasiun>";
		
		System.out.println(welcome_message);
		System.out.println();
	}
	
	private void cls()
	{
		/*
		 * fungsi untuk 'membersihkan' layar
		 */
		
		for(int i = 0; i < 100; i++)
		{
			System.out.println();
		}
	}
	
	private void initModules()
	{
		speechRecognizer = new SpeechRecognizer(false); //ini buat input pake mikrofon
		languageUnderstanding = new LanguageUnderstanding();
		routeResolver = new RouteResolver();
		scheduleResolver = new ScheduleResolver();
	}
	
	private void initModules(boolean isTranscribeAudioFile)
	{
		speechRecognizer = new SpeechRecognizer(isTranscribeAudioFile); //ini untuk transcribe audio file
		languageUnderstanding = new LanguageUnderstanding();
		routeResolver = new RouteResolver();
		scheduleResolver = new ScheduleResolver();		
	}
	
	private void constructReferenceFrames()
	{
		constructFrameRouteAsking();
		constructFrameScheduleAsking();
		constructFrameTimeAsking();
		constructFrameExistRouteAsking();
		constructFrameExistScheduleAsking();
	}
	
	public boolean isContextFrameComplete()
	{
		boolean ret = true;
		
		for(Slot slot: context_frame.getContent())
		{
			if(slot.getValue().length() == 0)
			{
				ret = false;
				break;
			}
		}
		
		return ret;
	}
	
	public void fillContextFrame(Frame frame)
	{
		if(context_frame == null)
		{
			context_frame = getMostAppropriateFrameTemplate(frame);
		}
		
		if(context_frame != null)
		{
			fillInformationToContextFrame(frame);
		}
	}
	
	private Frame getMostAppropriateFrameTemplate(Frame frame) 
	{
		ArrayList<String> keys = frame.getKeys();
		Frame ret = null;
		
		if(keys.contains(EXIST_ROUTE_IDENTIFIER_KEY))
		{
			ret = exist_route_asking;
		}
		else if(keys.contains(EXIST_SCHEDULE_IDENTIFIER_KEY))
		{
			ret = exist_schedule_asking;
		}
		else if(keys.contains(TIME_MODIFIER_KEY))
		{
			ret = time_asking;
		}
		else if(keys.contains(LIST_JADWAL_IDENTIFIER_KEY))
		{
			ret = schedule_asking;
		}
		else if(keys.contains(PLACE_IDENTIFIER_DESTINATION_KEY) || keys.contains(PLACE_IDENTIFIER_ORIGIN_KEY))
		{
			ret = route_asking;
		}
		
		return ret;
	}
	
	private String getContextFrameCategory()
	{
		ArrayList<String> keys = context_frame.getKeys();
		String ret;
		
		if(keys.contains(EXIST_ROUTE_IDENTIFIER_KEY))
		{
			ret = EXIST_ROUTE_ASKING_CATEGORY;
		}
		else if(keys.contains(EXIST_SCHEDULE_IDENTIFIER_KEY))
		{
			ret = EXIST_SCHEDULE_ASKING_CATEGORY;
		}
		else if(keys.contains(TIME_MODIFIER_KEY))
		{
			ret = TIME_ASKING_CATEGORY;
		}
		else if(keys.contains(LIST_JADWAL_IDENTIFIER_KEY))
		{
			ret = SCHEDULE_ASKING_CATEGORY;
		} 
		else
		{
			ret = ROUTE_ASKING_CATEGORY;
		}
		
		return ret;
	}

	private void fillInformationToContextFrame(Frame frame) {
		
		for(Slot slot:frame.getContent())
		{
			context_frame.setValue(slot);
		}
		
	}

	public void destroyContextFrame()
	{
		context_frame = null;
		constructReferenceFrames();
	}
	
	private void constructFrameScheduleAsking() 
	{
		schedule_asking = initializeFrame(Arrays.asList("dari", "ke", "list_jadwal_identifier"));		
	}

	private void constructFrameTimeAsking() 
	{
		time_asking = initializeFrame(Arrays.asList("dari", "ke", "time_modifier"));	
	}

	private void constructFrameRouteAsking() 
	{
		route_asking = initializeFrame(Arrays.asList("dari", "ke"));
	}
	
	private void constructFrameExistRouteAsking()
	{
		exist_route_asking = initializeFrame(Arrays.asList("ke", "exist_route_identifier"));
	}
	
	private void constructFrameExistScheduleAsking()
	{
		exist_schedule_asking = initializeFrame(Arrays.asList("dari", "ke", "exist_schedule_identifier"));
	}
	
	private Frame initializeFrame(List<String> keys)
	{
		Frame ret = new Frame();
		for(String key: keys)
		{
			Slot slot = new Slot(key, "");
			ret.add(slot);
		}
		
		return ret;
	}
	
	public void run()
	{
		/*
		 * fungsi ini yang akan melakukan eksekusi
		 * dari program
		 */
		
		boolean isBlank = false;
		welcomingUser();
		
		while(true)
		{
			if(!isBlank) 
			{
				System.out.print("> ");			
			}
			
			String recognized_sentence = speechRecognizer.listen();
			if(recognized_sentence.length() > 0)
			{
				executeDMBehavior(recognized_sentence);
				isBlank = false;
			}
			else
			{
				isBlank = true;
			}
		}
	}
	
	public String executeDMBehavior(String recognized_sentence)
	{
		/*
		 * Prosedur ini berisi behavior dari komponen DM
		 * atau dalam kata lain berisi algoritma penanganan
		 * teks yang diterima oleh sistem hingga pemrosesan
		 * query dari teks tersebut
		 * 
		 * Dengan adanya prosedur ini diharapkan bisa
		 * memperpendek penulisan utk fitur running
		 * skenario dari file
		 */
		
		String ret = "";
		
		if(!recognized_sentence.isEmpty())
		{
			//System.out.println(recognized_sentence);
			Frame recognized_frame = languageUnderstanding.getFrame(recognized_sentence);
			//Frame.printFrame(recognized_frame);
			this.fillContextFrame(recognized_frame);
				
			if(context_frame != null)
			{
				if(isContextFrameComplete())
				{
					ret = doAppropriateAction();
					destroyContextFrame();
				}
				else
				{
					ret = AnswerGenerator.handleMissingInformation(getFirstEmptyAttributeKeyInContextFrame());
				}
			}
			else
			{
				ret = AnswerGenerator.handleNullContextFrame();
			}
		}
		
		return ret;
	}

	public void runScenario(String filename)
	{
		ArrayList<String> sentences = CommonHelper.loadTextFile(RESOURCE_PATH, filename);
		
		welcomingUser();
		
		for(String sentence: sentences)
		{
			System.out.print("> ");
			printString(executeDMBehavior(sentence));
		}
	}
	
	public void runScenarioFromInput()
	{
		Scanner stdin = new Scanner(new BufferedInputStream(System.in));
		
		while(true)
		{
			System.out.print("Masukkan nama file skenario: ");
			runScenario(stdin.next());
		}
		
	}
	
	public void runTranscriber(String continous_audio_filename)
	{
		/*
		 * fungsi ini yang akan melakukan eksekusi
		 * dari program
		 */
		
		//welcomingUser();
		
		ArrayList<String> sentences = speechRecognizer.transcribeAudio(continous_audio_filename);
		
		welcomingUser();
		
		for(String sentence: sentences)
		{
			System.out.print("> ");
			printString(executeDMBehavior(sentence));
		}
	}
	
	public String listenUtterance()
	{
		/*
		 * possible returns null
		 */
		
		return speechRecognizer.listen();
	}
	
	public static void main(String[] args) 
	{
		/*	UNTUK MIKROFON dan SKENARIO */
			DialogueManager dm = new DialogueManager(false);
			//dm.run();
			dm.runScenario("skenario_rute");
			//dm.runScenarioFromInput();
		
		/* UNTUK FILE AUDIO
		
		DialogueManager dm = new DialogueManager(true);
		dm.runTranscriber("skenario_rute.wav");
		//dm.runTranscriber("skenario_jadwal.wav");*/
		
	}

}
