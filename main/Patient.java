package main;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Patient is a class representation of patients in EHRs.
 * 
 * The class is made thread-safe with immutable state and a ConcurrentHashMap
 * where the values in the map are protected by value.this
 * 
 * @author Fredrik Nystad
 *
 */
public class Patient {
	private final int ID;
	private final int AGE;
	/**
	 * ehrs key is the IDC-10/ATC code associated with the patient and the
	 * LocalDateTime[] values represents the first time(index 0) the code was associated with the patient and
	 * the last time(index 1) the code was associated witht the patient.
	 */
	private final ConcurrentHashMap<String, LocalDateTime[]> ehrs;
	
	public Patient(final int ID, final int AGE){
		this.ID = ID;
		this.AGE = AGE;
		ehrs = new ConcurrentHashMap<String, LocalDateTime[]>();
	}
	
	public void addEHR(String code, LocalDateTime time){		
		LocalDateTime[] times;
		if((times = ehrs.putIfAbsent(code, new LocalDateTime[]{time,time})) != null){
			synchronized(times){
				times[0] = (time.isBefore(times[0])) ? time : times[0];
				times[1] = (time.isAfter(times[1])) ? time : times[1];
			}
		}		
	}

	public int getID() {
		return ID;
	}

	public int getAGE() {
		return AGE;
	}
	
	public boolean containsCode(String code){
		return ehrs.containsKey(code);
	}	
	
	public boolean containsCodeBefore(String code, LocalDateTime time){
		LocalDateTime[] times;
		if((times = ehrs.get(code)) != null){
			synchronized(times){
				return time.isAfter(times[0]);
			}
		}
		return false;
	}
	
	public boolean containsCodeAfter(String code, LocalDateTime time){
		LocalDateTime[] times;
		if((times = ehrs.get(code)) != null){
			synchronized(times){
				return time.isBefore(times[1]);
			}
		}
		return false;
	}
	
	public ArrayList<String> getCodes(){
		Set<String> keys = ehrs.keySet();
		return new ArrayList<String>(keys);
	}
	
	/**	 * 
	 * @param code
	 * @return Returns all codes that appear after code or null if none found
	 */
	
	public ArrayList<String> getCodesAfterCode(String code){		
		if(containsCode(code)){
			LocalDateTime time = ehrs.get(code).clone()[0];
			return getCodesAfter(time);
		}
		return null;
	}
	
	/**	 * 
	 * @param code
	 * @return Returns all codes that appear before code or null if none found
	 */
	
	public ArrayList<String> getCodesBeforeCode(String code){		
		if(containsCode(code)){
			LocalDateTime time = ehrs.get(code).clone()[1];
			return getCodesBefore(time);
		}
		return null;
	}
	
	/** 
	 * 
	 * @param time 
	 * @return ArrayList<String> with all codes that appears before time. 
	 */
	public ArrayList<String> getCodesBefore(LocalDateTime time){
		ArrayList<String> codes = new ArrayList<String>();
		Set<String> keySet = ehrs.keySet();
		LocalDateTime[] times;
		for(String key : keySet){
			times = ehrs.get(key);
			if(time.isAfter(times[0])){
				codes.add(key);
			}
		}
		return codes;
	}	
	
	public ArrayList<String> getCodesAfter(LocalDateTime time){
		ArrayList<String> codes = new ArrayList<String>();
		Set<String> keySet = ehrs.keySet();
		LocalDateTime[] times;
		for(String key : keySet){
			times = ehrs.get(key);
			if(time.isBefore(times[1])){
				codes.add(key);
			}
		}
		return codes;
	}
	
	
	//add getIDC/ATC after code
		
	
	public ArrayList<String> getIDC(){
		return getCodesWithSuffix("_IDC");
	}
	
	public ArrayList<String> getIDCAfter(LocalDateTime time){
		return getCodesWithSuffixAfter("_IDC", time);
	}
	
	public ArrayList<String> getIDCBefore(LocalDateTime time){
		return getCodesWithSuffixBefore("_IDC", time);
	}
	
	public ArrayList<String> getIDCBeforeCode(String code){
		return getCodesWithSuffixBeforeCode("_IDC", code);
	}
	
	public ArrayList<String> getIDCAfterCode(String code){
		return getCodesWithSuffixAfterCode("_IDC", code);
	}
	
	public ArrayList<String> getATC(){
		return getCodesWithSuffix("_ATC");
	}
	
	public ArrayList<String> getATCAfter(LocalDateTime time){
		return getCodesWithSuffixAfter("_ATC", time);
	}
	
	public ArrayList<String> getATCBefore(LocalDateTime time){
		return getCodesWithSuffixBefore("_ATC", time);
	}
	
	public ArrayList<String> getATCBeforeCode(String code){
		return getCodesWithSuffixBeforeCode("_ATC", code);
	}
	
	public ArrayList<String> getATCAfterCode(String code){
		return getCodesWithSuffixAfterCode("_ATC", code);
	}
	
	/**
	 * Suffix methods are help methods that can be used to extract codes with
	 * specific ending, for example to get all IDC-10 codes or ATC-codes
	 * 
	 * @param suffix
	 * @return all codes that ends with suffix
	 */
	
	private ArrayList<String> getCodesWithSuffix(String suffix){
		ArrayList<String> codes = new ArrayList<String>();
		Set<String> keySet = ehrs.keySet();	
		for(String key : keySet){
			if(key.endsWith(suffix)){						
				codes.add(key);				
			}
		}
		return codes;
	}
	

	private ArrayList<String> getCodesWithSuffixAfter(String suffix,LocalDateTime time) {
		ArrayList<String> codes = new ArrayList<String>();
		Set<String> keySet = ehrs.keySet();
		LocalDateTime[] times;
		for (String key : keySet) {
			if (key.endsWith(suffix)) {
				times = ehrs.get(key);
				if (time.isBefore(times[1])) {
					codes.add(key);
				}
			}
		}
		return codes;
	}
	
	private ArrayList<String> getCodesWithSuffixBefore(String suffix,LocalDateTime time) {
		ArrayList<String> codes = new ArrayList<String>();
		Set<String> keySet = ehrs.keySet();
		LocalDateTime[] times;
		for (String key : keySet) {
			if (key.endsWith(suffix)) {
				times = ehrs.get(key);
				if(time.isAfter(times[0])){
					codes.add(key);
				}
			}
		}
		return codes;
	}
	
	private ArrayList<String> getCodesWithSuffixAfterCode(String suffix, String code){		
		if(containsCode(code)){
			LocalDateTime time = ehrs.get(code).clone()[0];
			return getCodesWithSuffixAfter(suffix, time);
		}
		return null;
	}
	
	private ArrayList<String> getCodesWithSuffixBeforeCode(String suffix, String code){		
		if(containsCode(code)){
			LocalDateTime time = ehrs.get(code).clone()[1];
			return getCodesWithSuffixBefore(suffix, time);
		}
		return null;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + AGE;
		result = prime * result + ID;
		result = prime * result + ((ehrs == null) ? 0 : ehrs.hashCode());
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
		if (AGE != other.AGE)
			return false;
		if (ID != other.ID)
			return false;
		if (ehrs == null) {
			if (other.ehrs != null)
				return false;
		} else if (!ehrs.equals(other.ehrs))
			return false;
		return true;
	}
	
	public String toString(){
		return String.valueOf(ID);
	}
	
	
}
