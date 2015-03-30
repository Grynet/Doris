package main;
import java.time.LocalDateTime;
import java.util.HashSet;


public class Group {
	private final HashSet<Patient> patients;
	
	public Group(){
		patients = new HashSet<Patient>();
	}
	
	public Group(final HashSet<Patient> patients){
		this.patients = patients;
	}
	
	public boolean addPatient(Patient patient){
		return patients.add(patient);
	}
	
	
	
	public HashSet<Patient> getPatientsWithCode(String code){
		HashSet<Patient> result = new HashSet<Patient>();
		for(Patient p : patients){
			if(p.containsCode(code)){
				result.add(p);
				break;
			}
				
			
		}
		return result; 
	}
	
	public HashSet<Patient> getPatientsWithCodeAfter(String code, LocalDateTime time){
		HashSet<Patient> result = new HashSet<Patient>();
		for(Patient p : patients){
			if(p.containsCodeAfter(code, time)){
				result.add(p);
				break;
			}
		}
		return result; 
	}
	
	public HashSet<Patient> getPatientsWithCodeBefore(String code, LocalDateTime time){
		HashSet<Patient> result = new HashSet<Patient>();
		for(Patient p : patients){
			if(p.containsCodeBefore(code, time)){
				result.add(p);
				break;
			}
		}
		return result; 
	}

}
