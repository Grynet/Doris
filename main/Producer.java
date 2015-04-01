package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Producer {
	final private int BUFFER_SIZE = 100000;
	
	final private ExecutorService XCUTOR; //GuardedBy synchronized(this) (only one method can use the service at a time)
	final private int CPU_COUNT;
	
	public Producer(){
		CPU_COUNT = Runtime.getRuntime().availableProcessors();
		XCUTOR = Executors.newFixedThreadPool(CPU_COUNT);		
	}
	
	public synchronized Group parseCSV(String filePath) throws IOException, InterruptedException{		
		BlockingQueue<String> work = new ArrayBlockingQueue<String>(BUFFER_SIZE);
		ConcurrentHashMap<Integer, Patient> result = new ConcurrentHashMap<>(BUFFER_SIZE);		
		
		
		for(int i = 0; i < CPU_COUNT+1; i++){
			XCUTOR.execute(new CSVPatientParser(work, result));
		}	
		
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = "";
		br.readLine(); //reads past first line which is a format description line
		while((line = br.readLine()) != null){
			work.put(line);			
		}
		
		br.close();
		work.put(CSVPatientParser.POISON_PILL);
		
		XCUTOR.shutdown();
		if(!XCUTOR.awaitTermination(2, TimeUnit.MINUTES)) //if timedOut and threads didn't finish
			return null;
		
		
		return new Group("Main Group", new LinkedList<Patient>(result.values()));
	}

}
