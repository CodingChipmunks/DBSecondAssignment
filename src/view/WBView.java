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
	private String[] searchOptions = {"Album", "Movie", "E-Book"};
	
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
		btnPanel.add(rateBtn);
		
		JButton reviewBtn = new JButton("Review Selected");
		btnPanel.add(reviewBtn);
		
		JButton addBtn = new JButton("Add Media");
		btnPanel.add(addBtn);
		
		JPanel searchPanel = new JPanel();
		contentPane.add(searchPanel, BorderLayout.NORTH);
		searchPanel.setLayout(new BorderLayout(0, 0));
		
		textField = new JTextField();
		searchPanel.add(textField, BorderLayout.NORTH);
		textField.setColumns(10);
		
		JButton searchBtn = new JButton("Search");
		searchPanel.add(searchBtn, BorderLayout.EAST);
		
		JComboBox comboBox = new JComboBox(searchOptions);
		comboBox.setActionCommand("comboBox");
		comboBox.setPrototypeDisplayValue("as long as this");
		comboBox.setSelectedIndex(0);
		comboBox.addActionListener(controller);
		searchPanel.add(comboBox, BorderLayout.WEST);
		

		// create components in methods
		
	}
	
	public void updateView() {
		contentPane.updateUI();
	}
}
