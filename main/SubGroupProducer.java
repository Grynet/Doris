package main;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Used to produce subgroups from a collection of patients. 
 * 
 * Subgroup can be of type IDC groups or ATC groups, specified with CodeRetriever
 * @author Fredrik Nystad
 *
 */

public class SubGroupProducer implements Runnable {
	final private BlockingQueue<Patient> WORK_QUEUE;
	final private ConcurrentHashMap<String, List<Patient>> OUTPUT; //String is the codeClassifier, List is the patients associated with that code
	final private CodeRetriever codeRetriever;
	
	public SubGroupProducer(CodeRetriever retriever, BlockingQueue<Patient> WORK_QUEUE, ConcurrentHashMap<String, List<Patient>> OUTPUT){
		this.codeRetriever = retriever;
		this.WORK_QUEUE = WORK_QUEUE;
		this.OUTPUT = OUTPUT;
	}
	
	@Override
	public void run(){
		List<Patient> patients;
		List<Patient> oldList;
		Patient patient;	
		while(true){
			try {
				patient = WORK_QUEUE.take();	
				patients = new LinkedList<Patient>();
				patients.add(patient);				
				for(String code : codeRetriever.retriveCodes(patient)){
					if((oldList = OUTPUT.putIfAbsent(code, patients)) != null){
						synchronized(oldList){
							oldList.add(patient);
						}
					}
				}
				
			} catch (InterruptedException e) {				
				e.printStackTrace();
			}			
		}
	}	
}
