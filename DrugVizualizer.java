import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYZDataset;

public class DrugVizualizer extends JFrame {
	private ChartPanel chart;
	private DefaultXYZDataset dataset;
	private String groupingSelected;
	private String xAxisSelected;
	private String yAxisSelected;
	private JTextField codeInputField;

	DrugVizualizer() {
		super("Doris 1.0");
		dataset = createDataset();
		addSerie(dataset, 1.0, 4.0, 2.0, "Ett");
		addSerie(dataset, 2.0, 5.0, 3.0, "Två");
		addSerie(dataset, 15.0, 2.0, 1.0, "Tre");

		/********************************************************************************************
		 * Setup JFrame
		 */

		// North Panel

		JPanel northPanel = new JPanel();
		JLabel groupByLabel = new JLabel("Group by: ");

		JLabel CodeLabel = new JLabel("Code: ");
		codeInputField = new JTextField(10);
		northPanel.add(CodeLabel);
		northPanel.add(codeInputField);
		northPanel.add(groupByLabel);
		// GroupByComboBox
		String[] groupToSortBy = { "Drug", "Disease", "Age" };
		JComboBox<String> groupByDropDown = new JComboBox<String>(
				groupToSortBy);
		groupByDropDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				groupingSelected = (String) groupByDropDown
						.getSelectedItem();
				updateChart(codeInputField.getText(), groupingSelected, xAxisSelected, yAxisSelected);
			}
		});
		// X-Axis ComboBox
		String[] xToSortBy = { "Drug", "Disease", "Age" };
		JComboBox<String> xAxisDropDown = new JComboBox<String>(
				xToSortBy);
		xAxisDropDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				xAxisSelected = (String) xAxisDropDown
						.getSelectedItem();
				updateChart(codeInputField.getText(), groupingSelected, xAxisSelected, yAxisSelected);
			}
		});
		// Y-Axis ComboBox
		String[] yToSortBy = { "Correlation" };
		JComboBox<String> yAxisDropDown = new JComboBox<String>(
				yToSortBy);
		yAxisDropDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				yAxisSelected = (String) yAxisDropDown
						.getSelectedItem();
				updateChart(codeInputField.getText(), groupingSelected, xAxisSelected, yAxisSelected);
			}
		});

		northPanel.add(groupByDropDown);
		add(northPanel, BorderLayout.NORTH);
		// Center
		add(chart, BorderLayout.CENTER);
		// West
		add(xAxisDropDown, BorderLayout.WEST);
		// South
		add(yAxisDropDown, BorderLayout.SOUTH);
		setVisible(true);
		setExtendedState(Frame.MAXIMIZED_BOTH);
		pack();
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

	}

	/**
	 *
	 * Anropas varje gång någonting nytt skall visas. T.ex. vid val av sätt att
	 * gruppera, val av x-axel, val av y-axel. groupBy är en String och är för
	 * tillfället antingen "Drug" eller "Disease". code är den inskrivna koden
	 * som grupperingarna skall göras efter.
	 * 
	 * @param code
	 * @param groupBy
	 * @param xAxis
	 * @param yAxis
	 */
	private void updateChart(String code, String groupBy, String xAxis, String yAxis) {
		ArrayList<Group> groupList = getListOfGroups(code, groupBy);
		switch (xAxis) {
			case "Average number of drugs per patient":
				for (Group group : groupList) {
					addSerie(dataset, group.getDrugAverage, group.getCorrelation,
						group.getSize, group.getName);
				}
				break;
			case "Average number of diseases per patient":
				for (Group group : groupList) {
					addSerie(dataset, group.getDiseaseAverage,
						group.getCorrelation, group.getSize, group.getName);
				}
				break;
			case "Average age":
				for (Group group : groupList) {
					addSerie(dataset, group.getAgeAverage, group.getCorrelation,
						group.getSize, group.getName);
				}
				break;
			default:
				break;
		}
		createBubbleChart(groupBy, xAxis, yAxis, dataset);
	}

	/**
	 * Hjälpmetod för "updateChart".
	 * 
	 * @param chartHeadline
	 * @param xAxisLabel
	 * @param yAxisLabel
	 * @param dataset
	 */
	private void createBubbleChart(String chartHeadline, String xAxisLabel,
			String yAxisLabel, XYZDataset dataset) {
		JFreeChart bubbleChart = ChartFactory.createBubbleChart(chartHeadline,
				xAxisLabel, yAxisLabel, dataset);
		chart = new ChartPanel(bubbleChart);
	}

	private DefaultXYZDataset createDataset() {
		DefaultXYZDataset dataset = new DefaultXYZDataset();
		return dataset;

	}

	/**
	 * Hjälpmetod för "updateChart".
	 * 
	 * @param ds
	 * @param x
	 * @param y
	 * @param z
	 * @param nameOfSerie
	 */
	private void addSerie(DefaultXYZDataset ds, double x, double y, double z,
			String nameOfSerie) {
		double[] xArray = new double[] { x };
		double[] yArray = new double[] { y };
		double[] zArray = new double[] { z };
		double[][] serie = new double[][] { xArray, yArray, zArray };
		ds.addSeries(nameOfSerie, serie);
	}

	public static void main(String[] args) {
		new DrugVizualizer();
	}

}
