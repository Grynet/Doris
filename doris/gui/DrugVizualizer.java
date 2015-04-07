package doris.gui;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

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

import doris.backend.Group;
import doris.backend.Population;
/**
 * GUI for Doris 1.0
 * 
 * @author Xceed
 *
 */
public class DrugVizualizer extends JFrame {
	private ChartPanel chart;
	private DefaultXYZDataset dataset;
	private String groupingSelected;
	private String xAxisSelected;
	private String yAxisSelected;
	private JTextField codeInputField;
	private JTextField pathInputField;
	private JButton pathButton;

	DrugVizualizer() {
		super("Doris 1.0");
		dataset = createDataset();

		/********************************************************************************************
		 * Setup JFrame
		 */

		// North Panel

		JPanel northPanel = new JPanel();
		JLabel groupByLabel = new JLabel("Group by: ");

		JLabel codeLabel = new JLabel("Code: ");
		codeInputField = new JTextField(10);
		
		JLabel pathLabel = new JLabel("Path: ");
		pathInputField = new JTextField(10);
		pathButton = new JButton("PATH!!!");
		pathButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//System.out.println();
				Population.init(pathInputField.getText());
				//set all to editable
			}
			
		});
		northPanel.add(pathLabel);
		northPanel.add(pathInputField);
		northPanel.add(pathButton);
		northPanel.add(codeLabel);
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
		createBubbleChart("No code entered", "No code entered", "No code entered", dataset);
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
	 * Invoked every time something new will be displayed. For example the
	 * selection of ways of grouping, the choice of the x-axis, the choice of
	 * the y-axis. "groupBy" is a String, and is currently either "Drug" or
	 * "Disease". "code" is the entered code as groupings shall be made after.
	 * 
	 * @param code
	 * @param groupBy
	 * @param xAxis
	 * @param yAxis
	 */
	private void updateChart(String code, String groupBy, String xAxis, String yAxis) {
		System.out.println(code);
		System.out.println(Population.getSize());
		Population.getCodeGroupSize(code);
		
		Group mainGroup = Population.getCodeGroup(code);
		System.out.println(mainGroup.getClassifier());
		LinkedList<Group> groupList = mainGroup.getSubgroups();
		switch (xAxis) {
			case "Average number of drugs per patient":
				for (Group group : groupList) {
					addSerie(dataset, group.getAverageNumATCs(), group.getCorrelationToGroup(mainGroup),
						group.getSize(), group.getClassifier());
				}
				break;
			case "Average number of diseases per patient":
				for (Group group : groupList) {
					addSerie(dataset, group.getAverageNumICDs(),
						group.getCorrelationToGroup(mainGroup), group.getSize(), group.getClassifier());
				}
				break;
			default:
				break;
		}
		createBubbleChart(code, xAxis, yAxis, dataset);
	}

	/**
	 * Support Method for "updateChart".
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
	 * Support Method for "updateChart".
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
