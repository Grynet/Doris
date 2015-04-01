package main;

import java.util.LinkedList;
import java.util.List;

public class Group {	
	final private LinkedList<Patient> PATIENTS;	
	
	public Group(final LinkedList<Patient> patients){
		this.PATIENTS = patients;		
	}
	
	//OBS getPatient() endast för testning, kommer tas bort
	public Patient getPatient(int id){
		for(Patient p : PATIENTS){
			if(p.getID() == id)
				return p;
		}
		return null;
	}
	
	public int getSize(){
		return PATIENTS.size();
	}	
	
	public double getAverageNumOfDiseases(){
		double sum = 0;
		for(Patient p : PATIENTS){
			sum += p.getNumICDs();
		}
		return sum/PATIENTS.size();
	}
	
	public double getAverageNumOfDrugs(){
		double sum = 0;
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
