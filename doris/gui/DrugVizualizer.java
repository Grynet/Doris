package doris.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardXYZToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYBubbleRenderer;
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
@SuppressWarnings("serial")
public class DrugVizualizer extends JFrame {
	private ChartPanel chart;
	private JComboBox<String> xAxisDropDown;
	
	private String xAxisSelected;
	private JTextField codeInputField;
	private JTextField pathInputField;
	private JButton pathButton;

	DrugVizualizer() {
		super("Doris 1.0");		

		/********************************************************************************************
		 * Setup JFrame
		 */

		// North Panel
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new BorderLayout());
		JPanel upperNorthPanel = new JPanel();
		northPanel.add(upperNorthPanel, BorderLayout.NORTH);
		JPanel lowerNorthPanel = new JPanel();
		northPanel.add(lowerNorthPanel, BorderLayout.SOUTH);
		JLabel codeLabel = new JLabel("Code: ");
		codeInputField = new JTextField(10);
		codeInputField.setEditable(false);
		JButton codeButton = new JButton("Group by code");
		codeButton.setEnabled(false);
		/*
		 * ActionListener for codeButton
		 */
		codeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateChart(
						codeInputField.getText(),
						xAxisSelected = (String) xAxisDropDown
								.getSelectedItem(), "Correlation");
			}
		});

		JLabel pathLabel = new JLabel("Path: ");
		pathInputField = new JTextField(20);
		JButton browseButton = new JButton("Browse file");
		/*
		 * ActionListener for browseButton
		 */
		browseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.showDialog(null, "Choose file");
				File selectedFile = fc.getSelectedFile();
				if (selectedFile != null) {
					pathInputField.setText(selectedFile.getAbsolutePath());
				}
			}
		});
		pathButton = new JButton("Load file-path");
		/*
		 * ActionListener for pathButton
		 */
		pathButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (pathInputField.getText().equals("")) {
					JOptionPane.showMessageDialog(null,
							"You must input a file path");
				} else {
					try {						
						Population.init(pathInputField.getText().replaceAll(
								"\\\\", "/"));						
						// set all components to editable
						codeInputField.setEditable(true);
						xAxisDropDown.setEnabled(true);
						codeButton.setEnabled(true);
					} catch (FileNotFoundException fnf) {
						JOptionPane.showMessageDialog(null,
								"Could not find the file");
					}catch (IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(null,
								iae.getMessage());
					}catch (OutOfMemoryError o) {
						JOptionPane.showMessageDialog(null,
								"Out of memory");
					} catch (InterruptedException ie) {
						JOptionPane.showMessageDialog(null,
								"Instantiation interrupted unexpectedly");
					} catch (IOException e) {
						JOptionPane.showMessageDialog(null,
								"IOException while instantiating");
					}/*
					 * catch (ArrayIndexOutOfBoundsException e){
					 * JOptionPane.showMessageDialog(null,
					 * "Array index out of bounds"); }
					 */
				}
			}

		});
		upperNorthPanel.add(pathLabel);
		upperNorthPanel.add(pathInputField);
		upperNorthPanel.add(browseButton);
		upperNorthPanel.add(pathButton);
		lowerNorthPanel.add(codeLabel);
		lowerNorthPanel.add(codeInputField);
		lowerNorthPanel.add(codeButton);

		// X-Axis ComboBox
		String[] xToSortBy = { "Average number of drugs per patient",
				"Average number of diseases per patient" };
		xAxisDropDown = new JComboBox<String>(xToSortBy);
		xAxisDropDown.setEnabled(false);
		/*
		 * ActionListener for xAxisDropDown
		 */
		xAxisDropDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				xAxisSelected = (String) xAxisDropDown.getSelectedItem();
				updateChart(codeInputField.getText(), xAxisSelected,
						"Correlation");
			}
		});
		createBubbleChart("No code entered", "", "Correlation", new DefaultXYZDataset());
		add(northPanel, BorderLayout.NORTH);
		// Center
		add(chart);
		// South
		JPanel southPanel = new JPanel();
		add(southPanel, BorderLayout.SOUTH);
		southPanel.add(new JLabel("X-axis"));
		southPanel.add(xAxisDropDown);
		setVisible(true);		
		pack();		
		setLocationRelativeTo(null);
		
		setResizable(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

	}

	/**
	 *
	 * Invoked every time something new will be displayed. For example the
	 * selection of ways of grouping, the choice of the x-axis, the choice of
	 * the y-axis. "code" is the entered code as groupings shall be made after.
	 * 
	 * @param code
	 * @param xAxis
	 * @param yAxis
	 */
	private void updateChart(String code, String xAxis, String yAxis) {
		if (code.equals("")
				|| (!(code.endsWith("_ATC")) && !(code.endsWith("_ICD")))) {
			JOptionPane.showMessageDialog(null, "You must input a code");
		} else {
			try {
				Group mainGroup = Population.getCodeGroup(code);
				LinkedList<Group> groupList = mainGroup.getSubgroups();
				AverageRetriever retriever = null;
							
				switch(xAxis){
				case "Average number of drugs per patient":
					retriever = new ATCAverageRetriever();
					break;
				case "Average number of diseases per patient":
					retriever = new ICDAverageRetriever();
					break;				
				}				
				
				double zRatio = 0;				
				HashMap<String, GroupContainer> container = new HashMap<>();
				
				for(Group group : groupList){					
					double groupAverage = retriever.getAverage(group);
					int groupSize = group.getSize();
					double correlation = group.getCorrelationToGroup(mainGroup);
					String classifier = group.getClassifier();
					
					zRatio = (correlation > zRatio) ? correlation : zRatio;
					container.put(classifier, new GroupContainer(groupAverage, groupSize, correlation));					
				}
				
				DefaultXYZDataset dataset = addSeries(zRatio, container);		
				createBubbleChart(code, xAxis, yAxis, dataset);
				chart.setDisplayToolTips(true);
				chart.validate();
				chart.repaint();
		
			} catch (NullPointerException e) {
				JOptionPane.showMessageDialog(null, "Nobody with that code.");
			}
		}
	}

	private DefaultXYZDataset addSeries(double zRatio,HashMap<String, GroupContainer> container) {
		DefaultXYZDataset dataset = new DefaultXYZDataset();
		dataset.setNotify(false);			
		for(String code : container.keySet()){			
			GroupContainer con = container.get(code);			
			double[] xArray = new double[] { con.groupAverage };
			double[] yArray = new double[] { con.correlation };
			double[] zArray = new double[] { con.groupSize%zRatio/6 };
			double[][] serie = new double[][] { xArray, yArray, zArray };
			dataset.addSeries(code, serie);		
			
		}		
		dataset.setNotify(true);	
		return dataset;
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
				xAxisLabel, yAxisLabel, dataset, PlotOrientation.VERTICAL,
				false, true, rootPaneCheckingEnabled);
		XYBubbleRenderer bubbleRenderer = new XYBubbleRenderer(XYBubbleRenderer.SCALE_ON_RANGE_AXIS);		
		bubbleRenderer.setBaseToolTipGenerator(new StandardXYZToolTipGenerator());
		bubbleChart.getXYPlot().setRenderer(bubbleRenderer);		
	
		if (!codeInputField.getText().equals("")) {
			remove(chart);
			chart = new ChartPanel(bubbleChart);
			add(chart);
			validate();
			repaint();
		} else
			chart = new ChartPanel(bubbleChart);
		chart.setMaximumDrawWidth(1920);
		chart.setMaximumDrawHeight(1200);

	}	


	public static void main(String[] args) {
		new DrugVizualizer();

	}

}
