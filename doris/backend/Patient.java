package doris.backend;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;

public class Patient {
	final private int ID;
	final private HashMap<String, LocalDateTime> ATC_CODES; //LocalDateTime == first report of Code
	final private HashMap<String, LocalDateTime> ICD_CODES; //LocalDateTime == first report of Code
	
	public Patient(int id, HashMap<String, LocalDateTime> codes) throws IllegalArgumentException{
		ATC_CODES = new HashMap<String, LocalDateTime>();
		ICD_CODES = new HashMap<String, LocalDateTime>();
		initCodes(codes);
		ID = id;
	}	
	
	public Patient(Patient other){
		ID = other.getID();
		ATC_CODES = other.getATCCodes();
		ICD_CODES = other.getICDCodes();
	}
	
	private void initCodes(HashMap<String, LocalDateTime> codes) throws IllegalArgumentException{		
		HashMap<String, LocalDateTime> targetMap;			
		
		for(String code : codes.keySet()){	
			LocalDateTime time = codes.get(code);
			if(code.endsWith("_ICD")){
				targetMap = ICD_CODES;				
			}else if(code.endsWith("_ATC")){
				targetMap = ATC_CODES;				
			}else{
				System.out.println("Code: "+code);
				throw new IllegalArgumentException("Unknown code type: "+code);
			}
				
			
			LocalDateTime oldTime = targetMap.get(code);
			if(oldTime == null)
				targetMap.put(code, time);
			else if(time.isBefore(oldTime))
				targetMap.put(code, time);
		}
	}	
	
	public int getID(){
		return ID;
	}
	
	public int getNumATCs(){
		return ATC_CODES.size();
	}
	
	public int getNumICDs(){
		return ICD_CODES.size();
	}
	
	public HashMap<String, LocalDateTime> getATCCodes(){
		return new HashMap<String, LocalDateTime>(ATC_CODES);
	}
	
	public HashMap<String, LocalDateTime> getICDCodes(){
		return new HashMap<String, LocalDateTime>(ICD_CODES);
	}
	
	public HashSet<String> getICDs(){		
		return new HashSet<String>(ICD_CODES.keySet());		
	}
	
	public HashSet<String> getICDsAfterATC(String atcCode){
		LocalDateTime atcTime = ATC_CODES.get(atcCode);
		HashSet<String> icdAfterATC = new HashSet<String>();	
		
		if(atcTime != null){
			for(String icdCode : ICD_CODES.keySet()){
				if(ICD_CODES.get(icdCode).isAfter(atcTime))
					icdAfterATC.add(icdCode);
			}		
		}
		return icdAfterATC;		
	}
	
	public HashSet<String> getATCs(){		
		return new HashSet<String>(ATC_CODES.keySet());		
	}
	
	public HashSet<String> getATCsBeforeICD(String icdCode){
		LocalDateTime icdTime = ICD_CODES.get(icdCode);
		HashSet<String> atcBeforeICD = new HashSet<String>();	
		
		if(icdTime != null){
			for(String atcCode : ATC_CODES.keySet()){
				if(ATC_CODES.get(atcCode).isBefore(icdTime))
					atcBeforeICD.add(atcCode);
			}		
		}
		return atcBeforeICD;		
	}
}
