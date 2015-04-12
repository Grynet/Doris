package doris.backend;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import doris.backend.support.*;

/**
 *Group.BEHAVIOUR determines what type of codes are stored in SUBGROUPS.
 * 
 * @author Fredrik Nystad
 *
 */

public class Group {
	final protected String GROUP_CLASSIFIER;
	final protected LinkedList<Integer> PATIENTS;	
	final private HashMap<String, HashSet<Integer>> SUBGROUPS = new HashMap<>(); 
	final private CodeTypeBehaviour BEHAVIOUR;
	
	public Group(String groupCode, HashSet<Integer> patientIDs) throws IllegalArgumentException{
		GROUP_CLASSIFIER = groupCode;
		PATIENTS = new LinkedList<Integer>(patientIDs);	
		if(groupCode.endsWith("_ICD"))
			BEHAVIOUR = new ICDBehaviour();
		else if (groupCode.endsWith("_ATC"))
			BEHAVIOUR = new ATCBehaviour();			
		else 
			throw new IllegalArgumentException("Unknown code type: "+groupCode);
		setSubgroups(patientIDs);
	}
	
	private void setSubgroups(HashSet<Integer> patientIDs){		
		for(int patientID : patientIDs){
			Patient patient = Population.getPatient(patientID);
			HashSet<String> codes = BEHAVIOUR.getCodes(patient);
			for(String code : codes){
				HashSet<Integer> groupPatients;
				if((groupPatients = SUBGROUPS.get(code)) != null)
					groupPatients.add(patientID);
				else{
					groupPatients = new HashSet<Integer>();
					groupPatients.add(patientID);
					SUBGROUPS.put(code, groupPatients);
				}					
			}
		}		
	}		
	
	public String getClassifier(){
		return GROUP_CLASSIFIER;
	}
	
	public int getSize(){		
		return PATIENTS.size();
	}	
	
	public double getCorrelationToGroup(Group group){		
		double observed = PATIENTS.size();
		double expected = Population.getCodeGroupPercentage(GROUP_CLASSIFIER) * group.getSize(); 
		
		return Math.log((observed+0.5)/(expected+0.5)) / Math.log(2);		
	}
	
	public double getAverageNumATCs(){
		double totalATCs = 0;
		for(Integer i : PATIENTS){
			totalATCs += Population.getPatient(i).getNumATCs();
		}
		return totalATCs/PATIENTS.size();
	}
	
	public double getAverageNumICDs(){
		double totalICDs = 0;
		for(Integer i : PATIENTS){
			totalICDs += Population.getPatient(i).getNumICDs();
		}
		return totalICDs/PATIENTS.size();
	}
	
	public int getNumSubgroups(){
		return SUBGROUPS.size();
	}
	
	public LinkedList<Group> getSubgroups(){
		LinkedList<Group> subgroups = new LinkedList<>();
		for(String groupCode : SUBGROUPS.keySet()){
			Group group = new Group(groupCode, new HashSet<Integer>(SUBGROUPS.get(groupCode)));
			subgroups.add(group);
		}		
		return subgroups;
	}
}
