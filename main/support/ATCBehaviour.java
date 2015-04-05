package main.support;

import java.util.HashSet;

import main.Patient;

/**
 * This CodeTypeBehaviour implementation will retrieve all the ICD codes that appear before the specified ATC code. 
 * 
 * @author Fredrik Nystad
 *
 */

public class ATCBehaviour implements CodeTypeBehaviour {

	@Override
	public HashSet<String> getCodes(Patient patient, String code) {
		return patient.getICDsBeforeATC(code);		
	}

}
