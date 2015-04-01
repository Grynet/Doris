package main;

import java.util.Set;

/**
 * Strategy Pattern implementation used to retrieve IDC codes from patient.
 * 
 * @author Fredrik Nystad
 *
 */

public class IDCRetriever implements CodeRetriever {

	@Override
	public Set<String> retriveCodes(Patient patient) {
		return patient.getIDCs();
	}
	

}
