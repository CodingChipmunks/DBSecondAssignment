package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	
	public Controller(Model m, View v) {
		this.model = m;
		this.view = v;
	}
	
	public Controller(Model m, WBView wbv) {
		this.model = m;
		this.wbview = wbv;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

		
		
		
		//end with //view.updateView();
	}


	
}
