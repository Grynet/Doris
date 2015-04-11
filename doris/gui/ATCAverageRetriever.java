package doris.gui;

import doris.backend.Group;

public class ATCAverageRetriever implements AverageRetriever {

	@Override
	public double getAverage(Group group) {
		return group.getAverageNumATCs();
	}

}
