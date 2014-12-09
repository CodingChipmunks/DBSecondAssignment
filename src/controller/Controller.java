package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.ChangeListener;

import model.*;
import view.*;

/**
 * Handles listeners, could be moved to View
 * 
 * @author softish
 *
 */
public class Controller implements ActionListener {

	private WBView wbview;
	private View view;
	private Model model;
	
	public Controller(Model m, View v) {
		this.model = m;
		this.view = v;
	}
	
	public Controller(Model m, WBView wbv) {
		this.model = m;
		this.wbview = wbv;
	}
	
	
	//TODO make own classes for different types of listeners
	// inner or in separate files? 
//	private class SearchListener implements ActionListener {
//
//		@Override
//		public void actionPerformed(ActionEvent e) {
//		}
//	}
	
	


	// Softish votes for this:
	// or one actionPerformed method that checks source of e with if (e.getActionCommand() == searchField)
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		
		
		//end with //view.updateView();
	}


	
}
