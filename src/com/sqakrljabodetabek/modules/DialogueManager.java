package com.sqakrljabodetabek.modules;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sqakrljabodetabek.language_analysis_things.Frame;
import com.sqakrljabodetabek.language_analysis_things.Slot;


public class DialogueManager {

	private Frame context_frame;
	
	private Frame route_asking;
	private Frame time_asking;
	private Frame schedule_asking;
	
	private final String ROUTE_ASKING_CATEGORY = "route_asking";
	private final String TIME_ASKING_CATEGORY = "time_asking";
	private final String SCHEDULE_ASKING_CATEGORY = "schedule_asking";
			
	private final String LIST_JADWAL_IDENTIFIER_KEY = "list_jadwal_identifier";
	private final String TIME_MODIFIER_KEY = "time_modifier";
	
	private final String START_STATION_FRAME_IDENTIFIER = "dari";
	private final String END_STATION_FRAME_IDENTIFIER = "ke";
	
	private final String TIME_NEXT_VALUE_IDENTIFIER = "berikutnya";
	private final String TIME_MOST_EARLY_VALUE_IDENTIFIER = "paling_awal";
	private final String TIME_MOST_LATE_VALUE_IDENTIFIER = "paling_akhir";
	
	private final String SCHEDULE_VALUE_IDENTIFIER = "kapan_saja";
	
	private SpeechRecognizer speechRecognizer;
	private LanguageUnderstanding languageUnderstanding;
	private RouteResolver routeResolver;
	private ScheduleResolver scheduleResolver;

	public DialogueManager()
	{
		initModules();
		constructReferenceFrames();
	}
	
	public void run()
	{
		/*
		 * fungsi ini yang akan melakukan eksekusi
		 * dari program
		 */
		
		welcomingUser();
		
		while(true)
		{
			System.out.print("> ");
			String recognized_sentence = speechRecognizer.listen();
			if(recognized_sentence.length() > 0)
			{
				System.out.println(recognized_sentence);
				Frame recognized_frame = languageUnderstanding.getFrame(recognized_sentence);
				Frame.printFrame(recognized_frame);
				this.fillContextFrame(recognized_frame);
				System.out.println(isContextFrameComplete());
				if(isContextFrameComplete())
				{
					doAppropriateAction();
					System.out.println();
					destroyContextFrame();
				}
			}
		}
	}
	
	public void runTranscriber(String filename)
	{
		/*
		 * fungsi ini yang akan melakukan eksekusi
		 * dari program
		 */
		
		//welcomingUser();
		
		System.out.print("> ");
		String recognized_sentence = speechRecognizer.transcribeAudio(filename);
		if(recognized_sentence.length() > 0)
		{
			System.out.println(recognized_sentence);
			Frame recognized_frame = languageUnderstanding.getFrame(recognized_sentence);
			Frame.printFrame(recognized_frame);
			this.fillContextFrame(recognized_frame);
			if(isContextFrameComplete())
			{
				doAppropriateAction();
				System.out.println();
				destroyContextFrame();
			}
		}
	}
	
	private void printString(String string)
	{
		System.out.println(string);
	}
	
	private void doAppropriateAction() 
	{
		String context_frame_category = getContextFrameCategory();
		
		String start_station = context_frame.getValue(START_STATION_FRAME_IDENTIFIER);
		String end_station = context_frame.getValue(END_STATION_FRAME_IDENTIFIER);
		
		if(context_frame_category.equals(TIME_ASKING_CATEGORY))
		{
			System.out.println("TIME_ASKING");
			String time_modifier = context_frame.getValue(TIME_MODIFIER_KEY);
			if(time_modifier.equals(TIME_MOST_LATE_VALUE_IDENTIFIER))
			{
				printString(scheduleResolver.getMostLateSchedule(start_station, end_station));
			}
			else if(time_modifier.equals(TIME_MOST_EARLY_VALUE_IDENTIFIER))
			{
				printString(scheduleResolver.getMostEarlySchedule(start_station, end_station));
			}
			else if(time_modifier.equals(TIME_NEXT_VALUE_IDENTIFIER))
			{
				printString(scheduleResolver.getNextSchedule(start_station, end_station));
			}
		}
		else if(context_frame_category.equals(SCHEDULE_ASKING_CATEGORY))
		{
			String list_jadwal_identifier = context_frame.getValue(LIST_JADWAL_IDENTIFIER_KEY);
			
			if(list_jadwal_identifier.equals(SCHEDULE_VALUE_IDENTIFIER))
			{
				printString(scheduleResolver.getSchedule(start_station, end_station));
			}
		}
		else
		{
			System.out.println("ROUTE_ASKING");
			printString(routeResolver.resolveRoute(start_station, end_station));
		}
		
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
									+ "-Kereta <selanjutnya | paling pagi | paling malam>";
		
		System.out.println(welcome_message);
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
		speechRecognizer = new SpeechRecognizer(true); //ganti line ini kalo mau pake microphone
		languageUnderstanding = new LanguageUnderstanding();
		routeResolver = new RouteResolver();
		scheduleResolver = new ScheduleResolver();
	}
	
	private void constructReferenceFrames()
	{
		constructFrameRouteAsking();
		constructFrameScheduleAsking();
		constructFrameTimeAsking();
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

		fillInformationToContextFrame(frame);
	}
	
	private Frame getMostAppropriateFrameTemplate(Frame frame) 
	{
		ArrayList<String> keys = frame.getKeys();
		Frame ret = null;
		
		if(keys.contains(TIME_MODIFIER_KEY))
		{
			ret = time_asking;
		}
		else if(keys.contains(LIST_JADWAL_IDENTIFIER_KEY))
		{
			ret = schedule_asking;
		}
		else
		{
			ret = route_asking;
		}
		
		return ret;
	}
	
	private String getContextFrameCategory()
	{
		ArrayList<String> keys = context_frame.getKeys();
		String ret;
		
		if(keys.contains(TIME_MODIFIER_KEY))
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

	public static void main(String[] args) 
	{
		DialogueManager dm = new DialogueManager();
		dm.runTranscriber("test_asr_1/1.wav");
		dm.runTranscriber("test_asr_1/9.wav");
	}

}
