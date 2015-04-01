package main;

import java.util.Set;

/**
 * Strategy Pattern implementation used to retrieve ATC codes from patient.
 * 
 * @author Fredrik Nystad
 *
 */

public class ATCRetriever implements CodeRetriever {

	@Override
	public Set<String> retriveCodes(Patient patient) {
		return patient.getATCs();
	}

}
