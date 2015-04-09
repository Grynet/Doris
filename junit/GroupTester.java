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
		Population.init("K:/D642-raw-data-dd.csv");
	}

	
//	@Test
//	public void testGetClass(){
//		Group group = Population.getCodeGroup("N02AA01_ATC");	
//		assertNotEquals(group, null);
//		
//		String groupClassifier = group.getClassifier();
//		int size = group.getSize();
//		double groupAverageATCs = group.getAverageNumATCs();
//		double groupAverageICDs = group.getAverageNumICDs();
//		int numSubgroups = group.getNumSubgroups();
//		LinkedList<Group> groups = group.getSubgroups();
//		
//		System.out
//				.printf("Group: %s%nGroup size: %d%nGroup average ATCs: %,.2f%nGroup average ICDs: %,.2f%nNumSubgroups: %d%n%n",
//						groupClassifier, size, groupAverageATCs,
//						groupAverageICDs, numSubgroups);
//		
//		for(Group g : groups){
//			System.out.printf("Subgroup: %s | Size: %d%n", g.getClassifier(), g.getSize());
//		}		
//	}
	
	/**
	 * This test can be used to check what the lowest or highest correlation value is for a subgroup of a group
	 */
	
	@Test
	public void testCorrelation(){
		Group group = Population.getCodeGroup("B05BB02_ATC");	
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
		
		Group subgroup = null;
		double subgroupCorrelation = 0;
		for(Group g : groups){
			if(g.getCorrelationToGroup(group) > subgroupCorrelation){ // if > retrive HighestCorrelation else if < retrieve lowest correlation
				subgroup = g;
				subgroupCorrelation = g.getCorrelationToGroup(group);
			}			
		}
		
		System.out.printf("Chosen subgroups: %s | Size: %d | Correlation: %,.2f%n", subgroup.getClassifier(), subgroup.getSize(), subgroupCorrelation);
	}

}
