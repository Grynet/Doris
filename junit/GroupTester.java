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
	
	String filePath = "F:/test.csv"; //Choose the correct path to the file
	String groupCode = "N02AA01_ATC";
	
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

	@Test
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
			System.out.printf("Subgroup: %s | Size: %d | Correlation: %,.2f%n", g.getClassifier(), g.getSize(), corr);
		}	
		
		System.out
		.printf("%n%nGroup: %s%n"
				+ "Group size: %d%n"
				+ "Group average ATCs: %,.2f%n"
				+ "Group average ICDs: %,.2f%n"
				+ "NumSubgroups: %d%n"
				+ "Lowest Correlation subgroup: %,.2f%n"
				+ "Highest Correlation subgroup: %,.2f%n"
				+ "Time to retrive subgroups from group: %,.2fs%n%n",
				
				groupClassifier, size, groupAverageATCs,
				groupAverageICDs, numSubgroups, lowestCorrelation, highestCorrelation, endTime);
	}
	
	/**
	 * This test can be used to check what the lowest and highest correlation
	 * value is for a subgroup of a group.
	 */
	
//	@Test
	public void testCorrelation(){
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
		
		System.out
				.printf("Group: %s%nGroup size: %d%n"
						+ "Group average ATCs: %,.2f%n"
						+ "Group average ICDs: %,.2f%n"
						+ "NumSubgroups: %d%n"
						+ "Time to retrieve: %,.2fs%n%n",
						groupClassifier, size, groupAverageATCs,
						groupAverageICDs, numSubgroups, endTime);
		
		Group highestSubgroup = null;
		double highestSubgroupCorrelation =  Double.MIN_VALUE;
		Group lowestSubgroup = null;
		double lowestSubgroupCorrelation =  Double.MAX_VALUE;
		for(Group g : groups){
			double corr = g.getCorrelationToGroup(group);
			if(corr > highestSubgroupCorrelation){
				highestSubgroup = g;
				highestSubgroupCorrelation = corr;
			}if(corr < lowestSubgroupCorrelation){
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
	
	/**
	 * Prints out information about group and the highesy/lowest ICD/ATC average,
	 */
	
//	@Test
	public void testAverage(){
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
		
		System.out
				.printf("Group: %s%nGroup size: %d%n"
						+ "Group average ATCs: %,.2f%n"
						+ "Group average ICDs: %,.2f%n"
						+ "NumSubgroups: %d%n"
						+ "Time to retrieve: %,.2fs%n%n",
						groupClassifier, size, groupAverageATCs,
						groupAverageICDs, numSubgroups, endTime);
		

		double highestICD =  Double.MIN_VALUE;		
		double lowestICD =  Double.MAX_VALUE;
		double highestATC =  Double.MIN_VALUE;		
		double lowestATC =  Double.MAX_VALUE;
		for(Group g : groups){			
			double icdAverage = g.getAverageNumICDs();
			double atcAverage = g.getAverageNumATCs();
			if(icdAverage > highestICD){				
				highestICD = icdAverage;
			}if(icdAverage < lowestICD){				
				lowestICD = icdAverage;			
			}
			
			if(atcAverage > highestATC){				
				highestATC = atcAverage;
			}if(atcAverage < lowestATC){				
				lowestATC = atcAverage;			
			}			
		}
			
		System.out.printf("Highest ICD average: subgroup: %,.2f%n", highestICD);				
		System.out.printf("Lowest ICD average: subgroup: %,.2f%n", lowestICD);	
		System.out.printf("Highest ATC average: subgroup: %,.2f%n", highestATC);				
		System.out.printf("Lowest ATC average: subgroup: %,.2f%n", lowestATC);	
	}

}
