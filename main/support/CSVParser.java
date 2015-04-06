package main.support;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;


public class CSVParser implements Runnable {

	final public static String POISON_PILL = new String();
	final private BlockingQueue<String> WORK_QUEUE;	
	final private ConcurrentHashMap<Integer, HashMap<String, LocalDateTime>> PATIENT_MAP;
	final private ConcurrentHashMap<String, HashSet<Integer>> CODE_GROUPS;
	final private CountDownLatch LATCH;
	
	public CSVParser(BlockingQueue<String> WORK_QUEUE, CountDownLatch LATCH, ConcurrentHashMap<Integer, HashMap<String, LocalDateTime>> PATIENT_MAP, ConcurrentHashMap<String, HashSet<Integer>> CODE_GROUPS){		
		this.WORK_QUEUE = WORK_QUEUE;
		this.LATCH = LATCH;
		this.PATIENT_MAP = PATIENT_MAP;
		this.CODE_GROUPS = CODE_GROUPS;
	}	

	@Override
	public void run(){		
		String line;
		String[] values;
		int patientID;
		LocalDateTime time;
		String code;		
			
		try {
			while (true) {
				if ((line = WORK_QUEUE.take()) == POISON_PILL) {
					terminate();
					break;
				}
				values = line.split(",");

				if (values[0].equals("") || values[1].equals("")
						|| values[2].equals(""))
					break;

				patientID = Integer.parseInt(values[0]);
				time = stringToDateTime(values[1]);
				code = values[2];
			
				addValues(patientID, time, code);
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			LATCH.countDown();
		}
	}
		
	private void addValues(int patientID, LocalDateTime time, String code) {
		
		//Add patientID and the code and its time
		HashMap<String, LocalDateTime> patientCodes = PATIENT_MAP.get(patientID);
		if (patientCodes == null) {
			PATIENT_MAP.putIfAbsent(patientID, new HashMap<String, LocalDateTime>());
			patientCodes = PATIENT_MAP.get(patientID);
		}
		synchronized (patientCodes) {
			LocalDateTime value = patientCodes.get(code);
			if (value == null)
				patientCodes.put(code, time);
			else if (value.isAfter(time))
				patientCodes.put(code, time);
		}
		
		//Add code to codeGroup and add the patientID to that group
		HashSet<Integer> codeGroupValues = CODE_GROUPS.get(code);
		if(codeGroupValues == null){
			CODE_GROUPS.putIfAbsent(code, new HashSet<Integer>());
			codeGroupValues = CODE_GROUPS.get(code);			
		}
		
		synchronized(codeGroupValues){
			codeGroupValues.add(patientID);
		}
	}
	
	
	private LocalDateTime stringToDateTime (String time) throws DateTimeParseException{
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
		LocalDateTime dateTime = LocalDateTime.parse(time, formatter);		
		return dateTime;
		
	}
	
	private void terminate() throws InterruptedException{		
		try{
			WORK_QUEUE.put(POISON_PILL);
		}catch(InterruptedException e){		
			Random r = new Random();
			int sleep = r.nextInt(1000)+50;
			Thread.sleep(sleep);
			WORK_QUEUE.put(POISON_PILL);				
		}
	}

}
