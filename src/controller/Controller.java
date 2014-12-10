package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.event.ChangeListener;

import model.*;
import view.*;

/**
 * Handles listeners
 * 
 * @author softish
 * 
 */
public class Controller implements ActionListener {

	private WBView wbview;
	private View view;
	private Model model;
	private QueryInterpreter dbInterpreter;
	private int selectedItemInCombobox;

	public Controller(Model m, View v) {
		this.model = m;
		this.view = v;
	}

	public Controller(Model m, WBView wbv) {
		this.model = m;
		this.wbview = wbv;
	}

	@Override // TODO set OnMouseListener for every object separately?
	public void actionPerformed(ActionEvent e) {

		// Works great, but unusable
		if (e.getActionCommand() == "comboBox") {
			System.out.println("Checking mediaType...");

			JComboBox cb = (JComboBox) e.getSource();
			String item = (String) cb.getSelectedItem();
			System.out.println("Item " + item);

		}
		
		if (e.getActionCommand().equals("searchBtn")) {
			System.out.println("Searching...");
			
			wbview.feedTable(); // resultset

			// Dangerous solution but works!
			JComboBox cb = wbview.getSearchPanelComponent();
			String item = (String) cb.getSelectedItem();
			System.out.println("Item " + item);

			// selectedItemInCombobox = wbview.getSelectedItem();
			// System.out.println("Selected mediaType..." +
			// selectedItemInCombobox);
		}
		
		if (e.getActionCommand().equals("rateBtn")) {
			System.out.println("Rating dialog...");
			wbview.invokeRateMediaDialog();
		}
		
		if (e.getActionCommand().equals("reviewBtn")) {
			System.out.println("Review dialog...");
			wbview.invokeReviewMediaDialog();
		}
		
		if (e.getActionCommand().equals("addBtn")) {
			System.out.println("Adding dialog...");
			wbview.invokeAddMediaDialog();
		}
		// TODO create a Thread that runs QueryExecuter, the thread will wait for
		// QueryExecuter to finish and then call view.update();

		// end with //view.updateView();
	}

}
