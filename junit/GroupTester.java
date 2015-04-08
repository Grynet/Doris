package junit;

import static org.junit.Assert.assertNotEquals;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import doris.backend.Group;
import doris.backend.Population;

public class GroupTester {
	
	@Before
	public void setUp() throws Exception {	
		Population.init("C:/Users/Tobias/Dropbox/FoT/Kandidatuppsats/Deluppgifter/Deliverable 5/test.csv");
	}

	
	@Test
	public void testGetClass(){
		Group group = Population.getCodeGroup("N02AA01_ATC");	
		assertNotEquals(group, null);
		
		String groupClassifier = group.getClassifier();
		int size = group.getSize();
		double groupAverageATCs = group.getAverageNumATCs();
		double groupAverageICDs = group.getAverageNumICDs();
		int numSubgroups = group.getNumSubgroups();
		LinkedList<Group> groups = group.getSubgroups();
		
		System.out
				.printf("Group: %s%nGroup size: %d%nGroup average ATCs: %,.2f%nGroup average ICDs: %,.2f%nNumSubgroups: %d%n%n",
						groupClassifier, size, groupAverageATCs,
						groupAverageICDs, numSubgroups);
		
		for(Group g : groups){
			System.out.printf("Subgroup: %s | Size: %d%n", g.getClassifier(), g.getSize());
		}	
		
	}

}
