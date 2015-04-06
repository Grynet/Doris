package doris.backend.support;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import doris.backend.Patient;

public class Producer {
	final private ExecutorService XCUTOR; 
	final private int CPU_COUNT;
	final private int BUFFER_SIZE = 100000;
	
	public Producer(){
		CPU_COUNT = Runtime.getRuntime().availableProcessors();
		XCUTOR = Executors.newFixedThreadPool(CPU_COUNT);
	}
	
	//make more effective and increase readability
	
	public void parseCSVFile(String filePath, ConcurrentHashMap<Integer,Patient> patientMap, ConcurrentHashMap<String, HashSet<Integer>> codeGroupMap) throws InterruptedException, FileNotFoundException, IOException{
		BlockingQueue<String> work = new ArrayBlockingQueue<String>(BUFFER_SIZE);			
		ConcurrentHashMap<Integer, HashMap<String, LocalDateTime>> patientCodes = new ConcurrentHashMap<>();
		CountDownLatch doneSignal = new CountDownLatch(CPU_COUNT);	
		
		for(int i = 0; i < CPU_COUNT; i++){
			XCUTOR.execute(new CSVParser(work, doneSignal, patientCodes, codeGroupMap));
		}
		
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = "";
		br.readLine(); //reads past first line which is a format description line
		while((line = br.readLine()) != null){			
			work.put(line);			
		}	
		
		br.close();
		
		work.put(CSVParser.POISON_PILL);		
		doneSignal.await();
		
		for(Integer patientID : patientCodes.keySet()){
			HashMap<String, LocalDateTime> codes = patientCodes.get(patientID);
		
			for(String code: codes.keySet()){
				if(code.trim().equals(""))
					System.out.printf("Empty code!");
				
			}
			Patient patient = new Patient(patientID, patientCodes.get(patientID));
			patientMap.put(patientID, patient);
		}			
	}

}
