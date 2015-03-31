package main;

import java.util.LinkedList;
import java.util.List;

public class Group {	
	final private LinkedList<Patient> PATIENTS;	
	
	public Group(final LinkedList<Patient> patients){
		this.PATIENTS = patients;		
	}
	
	public int getSize(){
		return PATIENTS.size();
	}
	
	public double getAverageAge(){
		int sum = 0;
		for(Patient patient : PATIENTS){
			sum +=patient.getAge();
		}		
		return sum/PATIENTS.size();
	}
	
	public double getAverageNumOfDiseases(){
		int sum = 0;
		for(Patient p : PATIENTS){
			sum += p.getNumIDCs();
		}
		return sum/PATIENTS.size();
	}
	
	public double getAverageNumOfDrugs(){
		int sum = 0;
		for(Patient p : PATIENTS){
			sum += p.getNumATCs();
		}
		return sum/PATIENTS.size();
	}
	
	public List<Group> getATCSubgroups(){
		return null;
	}
	
	public List<Group> getIDCSubgroups(){
		return null;
	}	
}
