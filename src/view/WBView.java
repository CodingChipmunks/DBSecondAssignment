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

		// create components in methods
		
	}
	
	public void updateView() {
//		contentPane.updateUI();
	}
}