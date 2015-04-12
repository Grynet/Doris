package doris.gui;

/**
 * Container class that stores Group values.
 * 
 * @author Fredrik Nystad
 *
 */

public class GroupContainer {
	
	public final double groupAverage;
	public final double groupSize;
	public final double correlation;	

	public GroupContainer(double groupAverage, int groupSize, double correlation) {
		this.groupAverage = groupAverage;
		this.groupSize = groupSize;
		this.correlation = correlation;
	}

}
