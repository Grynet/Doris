package main;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Patient {
	private final int ID;	
	private final ConcurrentHashMap<String, LocalDateTime[]> ATC_CODES;
	private final ConcurrentHashMap<String, LocalDateTime[]> ICD_CODES;
	
	public Patient(final int ID){
		this.ID = ID;		
		ATC_CODES = new ConcurrentHashMap<String,LocalDateTime[]>(); 
		ICD_CODES = new ConcurrentHashMap<String,LocalDateTime[]>(); 
	}
	
	public void addCode(String code, LocalDateTime time){		
		LocalDateTime[] times = null;
		ConcurrentHashMap<String, LocalDateTime[]> codeType = null;
		
		if(code.endsWith("_ICD")){
			codeType = ICD_CODES;
		}else if(code.endsWith("_ATC")){
			codeType = ATC_CODES;
		}
		
		if(codeType != null){		
			if((times = codeType.putIfAbsent(code, new LocalDateTime[]{time,time})) != null){
				synchronized(times){
					times[0] = (time.isBefore(times[0])) ? time : times[0];
					times[1] = (time.isAfter(times[1])) ? time : times[1];
				}
			}
		}
	}

	public int getID() {
		return ID;
	}
	
	public int getNumICDs(){
		return ICD_CODES.size();
	}
	
	public int getNumATCs(){
		return ATC_CODES.size();
	}
	
	
	public Set<String> getATCs(){
		return new HashSet<String>(ATC_CODES.keySet());
	}
	
	public Set<String> getIDCs(){
		return new HashSet<String>(ICD_CODES.keySet());
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ID;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Patient other = (Patient) obj;
		if (ID != other.ID)
			return false;
		return true;
	}

	public String toString(){
		return String.valueOf(ID);
	}
	
	
}
