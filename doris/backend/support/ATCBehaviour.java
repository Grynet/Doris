package doris.backend.support;

import java.util.HashSet;

import doris.backend.Patient;

/**
 * This CodeTypeBehaviour implementation will retrieve all the ICD codes that after the specified ATC code. 
 * 
 * @author Fredrik Nystad
 *
 */

public class ATCBehaviour implements CodeTypeBehaviour {

	@Override
	public HashSet<String> getCodes(Patient patient) {
		return patient.getICDs();	
		// return patient.getICDsAfterATC(code);		
	}
}
