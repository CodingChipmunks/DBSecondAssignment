import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import model.*;
import view.*;

public class Start {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// instantiate model
		// feed model to view when instantiating it
	
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Model model = new Model();
					WBView frame = new WBView(model);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}


}
