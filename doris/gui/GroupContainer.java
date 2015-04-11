package doris.gui;

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
