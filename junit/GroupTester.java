package junit;

import static org.junit.Assert.assertNotEquals;

import java.io.IOException;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import doris.backend.Group;
import doris.backend.Population;


/**
 * This is a JUnit test for the Group class. Comment out the \@Test of the tests
 * you don't want to do.
 * 
 * @author Fredrik Nystad
 *
 */
public class GroupTester {
	
	String filePath = "K:/D642-raw-data-dd.csv";
	String groupCode = "B05BB02_ATC";
	
	@Before
	public void setUp() {			
		try {
			Population.init(filePath);
		} catch (InterruptedException | IOException e) {			
			e.printStackTrace();
		}			
	}

	/**
	 * This test can be used to check how fast subGroups can be retrieved, print
	 * them out and print some fact about the group.
	 */

//	@Test
	public void testGetSubgroups(){
		Group group = Population.getCodeGroup(groupCode);	
		assertNotEquals(group, null);
		
		String groupClassifier = group.getClassifier();
		int size = group.getSize();
		double groupAverageATCs = group.getAverageNumATCs();
		double groupAverageICDs = group.getAverageNumICDs();
		int numSubgroups = group.getNumSubgroups();
		
		System.gc();
		long startTime = System.nanoTime();		
		LinkedList<Group> groups = group.getSubgroups();
		double endTime = (System.nanoTime() - startTime) / 1000000000.0;		
		
		
		double highestCorrelation = Double.MIN_VALUE;
		double lowestCorrelation = Double.MAX_VALUE;
		for(Group g : groups){
			double corr = g.getCorrelationToGroup(group);
			if(corr > highestCorrelation)
				highestCorrelation = corr;
			else if(corr < lowestCorrelation)
				lowestCorrelation = corr;
			System.out.printf("Subgroup: %s | Size: %d%n", g.getClassifier(), g.getSize());
		}	
		
		System.out
		.printf("%n%nGroup: %s%n"
				+ "Group size: %d%n"
				+ "Group average ATCs: %,.2f%n"
				+ "Group average ICDs: %,.2f%n"
				+ "NumSubgroups: %d%n"
				+ "Time to retrive subgroups from group: %,.2fs%n%n",
				
				groupClassifier, size, groupAverageATCs,
				groupAverageICDs, numSubgroups, endTime);
	}
	
	/**
	 * This test can be used to check what the lowest and highest correlation
	 * value is for a subgroup of a group.
	 */
	
	@Test
	public void testCorrelation(){
		Group group = Population.getCodeGroup(groupCode);	
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
		
		Group highestSubgroup = null;
		double highestSubgroupCorrelation =  Double.MIN_VALUE;
		Group lowestSubgroup = null;
		double lowestSubgroupCorrelation =  Double.MAX_VALUE;
		for(Group g : groups){
			double corr = g.getCorrelationToGroup(group);
			if(corr > highestSubgroupCorrelation){
				highestSubgroup = g;
				highestSubgroupCorrelation = corr;
			}else if(corr < lowestSubgroupCorrelation){
				lowestSubgroup = g;
				lowestSubgroupCorrelation = corr;			
			}			
		}
		
		System.out.printf("Highest correlating subgroup: %s%n"
				+ "Size: %d%n"
				+ "Correlation: %,.2f%n", highestSubgroup.getClassifier(), highestSubgroup.getSize(), highestSubgroupCorrelation);
		System.out.printf("%nLowest correlating subgroup: %s%n"
				+ "Size: %d%n"
				+ "Correlation: %,.2f%n", lowestSubgroup.getClassifier(), lowestSubgroup.getSize(), lowestSubgroupCorrelation);
	}

}
