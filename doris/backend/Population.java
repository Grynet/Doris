package doris.backend;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

import doris.backend.support.Producer;

public final class Population {

	final static private ConcurrentHashMap<Integer, Patient> PATIENTS = new ConcurrentHashMap<>();
	final static private ConcurrentHashMap<String, HashSet<Integer>> CODE_GROUPS = new ConcurrentHashMap<>();
	final static private Producer PRODUCER = new Producer();
	
	
	private Population(){}
	
	public static void init(String filePath) throws FileNotFoundException, InterruptedException, IOException {	
		if(!PATIENTS.isEmpty()){		
			PATIENTS.clear();	
			CODE_GROUPS.clear();			
		}		
		PRODUCER.parseCSVFile(filePath, PATIENTS, CODE_GROUPS);		
	}
	
	public static int getSize(){
		return PATIENTS.size();
	}
	
	public static double getAverageNumATCs(){
		double sum = 0;
		for(Integer patientID : PATIENTS.keySet()){
			sum += PATIENTS.get(patientID).getNumATCs();
		}
		
		return sum /PATIENTS.size();
	}
	
	public static double getAverageNumICDs(){
		double sum = 0;
		for(Integer patientID : PATIENTS.keySet()){
			sum += PATIENTS.get(patientID).getNumICDs();
		}
		
		return sum /PATIENTS.size();
	}
	
	public static Patient getPatient(int id){
		Patient patient = null;
		if((patient = PATIENTS.get(id)) != null)
				return new Patient(patient);  
		return null;
	}
	
	public static Group getCodeGroup(String code){		
		HashSet<Integer> codeGroup = null;
		if((codeGroup = CODE_GROUPS.get(code)) != null)
			return new Group(code,codeGroup);		
		return null;
	}
	
	public static int getCodeGroupSize(String code){
		return CODE_GROUPS.get(code).size();
	}
	
	public static double getCodeGroupPercentage(String code){
		HashSet<Integer> codeGroup = null;
		double percentage = 0;
		
		if((codeGroup = CODE_GROUPS.get(code))!= null)
			percentage = (double) codeGroup.size() / getSize();
		
		return percentage;		
	}

}
