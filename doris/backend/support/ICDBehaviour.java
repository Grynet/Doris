package doris.backend.support;

import java.util.HashSet;

import doris.backend.Patient;

/**
 * This CodeTypeBehaviour implementation will retrieve all the ATC codes that before the specified ICD code. 
 * 
 * @author Fredrik Nystad
 *
 */
public class ICDBehaviour implements CodeTypeBehaviour{

	@Override
	public HashSet<String> getCodes(Patient patient, String code) {
		return patient.getATCs();
		// return patient.getATCsBeforeICD(code);		
	}
	
	

}
