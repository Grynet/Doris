package doris.gui;

import doris.backend.Group;

public class ICDAverageRetriever implements AverageRetriever {

	@Override
	public double getAverage(Group group) {
		return group.getAverageNumICDs();
	}

}
