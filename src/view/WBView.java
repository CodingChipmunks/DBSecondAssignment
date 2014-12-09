package view;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import controller.Controller;

import model.Model;
import model.QueryExecuter;
import model.QueryInterpreter;

/**
 * WindowBuilder class, compatible with git? Better than Start?
 * 
 */
public class WBView extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private JTextField textField;
	private JComboBox comboBox;
	private JPanel searchPanel;
	private String[] searchOptions = { "Album", "Movie", "E-Book" };

	// declare all GUI components here

	/**
	 * Create the frame.
	 */
	public WBView(Model m) {
		Controller controller = new Controller(m, this);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		table = new JTable();
		contentPane.add(table, BorderLayout.CENTER);

		JPanel btnPanel = new JPanel();
		contentPane.add(btnPanel, BorderLayout.SOUTH);

		JButton rateBtn = new JButton("Rate Selected");
		rateBtn.setActionCommand("rateBtn");
		rateBtn.addActionListener(controller);
		btnPanel.add(rateBtn);

		JButton reviewBtn = new JButton("Review Selected");
		reviewBtn.setActionCommand("reviewBtn");
		reviewBtn.addActionListener(controller);
		btnPanel.add(reviewBtn);

		JButton addBtn = new JButton("Add Media");
		addBtn.setActionCommand("addBtn");
		addBtn.addActionListener(controller);
		btnPanel.add(addBtn);

		searchPanel = new JPanel();
		contentPane.add(searchPanel, BorderLayout.NORTH);
		searchPanel.setLayout(new BorderLayout(0, 0));

		textField = new JTextField();
		searchPanel.add(textField, BorderLayout.NORTH);
		textField.setColumns(10);

		JButton searchBtn = new JButton("Search");
		searchBtn.setActionCommand("searchBtn");
		searchBtn.addActionListener(controller);
		searchPanel.add(searchBtn, BorderLayout.EAST);

		JComboBox comboBox = new JComboBox(searchOptions);
		comboBox.setActionCommand("comboBox");
		comboBox.setPrototypeDisplayValue("as long as this");
		comboBox.setSelectedIndex(0);
		comboBox.addActionListener(controller);
		searchPanel.add(comboBox, BorderLayout.WEST);

		// create components in methods

	}

	// PArt of the dangerous solution
	public JComboBox getSearchPanelComponent() {
		return (JComboBox) searchPanel.getComponent(2);
	}

	// unusable ref.
	// public JComboBox getComboBox() {
	// return comboBox;
	// }

	// unusable ref.
	// public int getSelectedItem() {
	// return comboBox.getSelectedIndex();
	// }

	public void feedTable() {
		
	}
	
	public void invokeReviewMediaDialog() {

	}

	public void invokeRateMediaDialog() {

	}

	public void invokeAddMediaDialog() {

	}

	public String getSearchFieldText() {
		return textField.getText();
	}

	public void updateView() {
		contentPane.updateUI();
	}
}
