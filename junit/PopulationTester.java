package junit;

import java.io.IOException;

import org.junit.Test;

import doris.backend.Population;

public class PopulationTester {
	// "C:/Development/Workspace/Eclipse/Foobar/test.csv"
	// "K:/D642-raw-data-dd.csv"
	String filePath = "C:/Development/Workspace/Eclipse/Foobar/million.csv"; //Choose the correct path to the file

	@Test
	public void initPopulationTest() {		
		System.gc();
		long startTime = System.nanoTime();		
		try {
			Population.init(filePath);
		} catch (InterruptedException | IOException e) {			
			e.printStackTrace();
		}	
		double endTime = (System.nanoTime() - startTime) / 1000000000.0;
		int size = Population.getSize();
		
		System.out.printf("Time: %,.2fs |Population size: %d %n%n",endTime, size);		
	}
}
