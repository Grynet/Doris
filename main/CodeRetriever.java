package main;

import java.util.Set;

/**
 * Strategy Pattern interface used to define what type of codes that should be retrieved. 
 * 
 * @author Fredrik Nystad
 *
 */

public interface CodeRetriever {
	public Set<String> retriveCodes(Patient patient);

}
