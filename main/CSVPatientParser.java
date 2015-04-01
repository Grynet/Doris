package main;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * CSVPatientParser is used to parse Strings from csv files and to create patients from these Strings.  
 * 
 * @author Fredrik Nystad
 *
 */

public class CSVPatientParser implements Runnable {
	final public static String POISON_PILL = new String();
	final private BlockingQueue<String> WORK_QUEUE;	
	final private ConcurrentHashMap<Integer, Patient> OUTPUT;
	
	public CSVPatientParser(BlockingQueue<String> WORK_QUEUE, ConcurrentHashMap<Integer, Patient> OUTPUT){		
		this.WORK_QUEUE = WORK_QUEUE;
		this.OUTPUT = OUTPUT;
	}	

	@Override
	public void run(){
		Patient patient;
		String line;
		String[] values;
		int id;
		LocalDateTime time;
		String code;
		
		while(true){			
			try {	
				if((line = WORK_QUEUE.take()) == POISON_PILL){
					WORK_QUEUE.put(POISON_PILL);						
					break;
				}				
				values = line.split(",");	
				
				if(values[0].equals("") || values[1].equals("") || values[2].equals(""))
					break;
				
				id = Integer.parseInt(values[0]);
				time = stringToDateTime(values[1]);
				code = values[2];				
			
				patient = new Patient(id);
				patient.addCode(code, time);
				
				if((patient = OUTPUT.putIfAbsent(id, patient)) != null){
					patient.addCode(code, time);
				}			
				
			} catch (InterruptedException e) {				
				e.printStackTrace();
			} catch(DateTimeParseException d){
				d.printStackTrace();
			}
		}
	}	
	
	private LocalDateTime stringToDateTime (String time) throws DateTimeParseException{
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
		LocalDateTime dateTime = LocalDateTime.parse(time, formatter);		
		return dateTime;
		
	}

}
