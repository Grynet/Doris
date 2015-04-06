package junit;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import doris.backend.Group;
import doris.backend.Patient;
import doris.backend.Population;

public class PopulationTester {

	@Test
	public void initPopulationTest() {		
		System.gc();
		long startTime = System.nanoTime();		
		Population.init("K:/D642-raw-data-dd.csv");	
		double endTime = (System.nanoTime() - startTime) / 1000000000.0;
		int size = Population.getSize();
		double averageNumATcs = Population.getAverageNumATCs();
		double averageNumICDs = Population.getAverageNumICDs();
		
		System.out
				.printf("Time: %,.2fs |Population size: %d | AverageNumOfDiseases: %,.2f | AverageNumOfDrugs: %,.2f%n%n",
						endTime, size, averageNumATcs, averageNumICDs);
		
	}
}
