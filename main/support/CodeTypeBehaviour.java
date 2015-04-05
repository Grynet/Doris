package main.support;

import java.util.HashSet;

import main.Patient;

/**
 * This is a support class for the Group class and it's used to determine what will be contained in its subgroups.  
 * 
 * @author Fredrik Nystad
 *
 */

public interface CodeTypeBehaviour {
	
	public HashSet<String> getCodes(Patient patient, String code);
}
