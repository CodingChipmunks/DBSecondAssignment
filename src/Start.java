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
					matchHostTheme(frame);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}
	
	/**
	 * Matches application theme to system theme
	 * regardless of system
	 * @param c a component to change theme on
	 */
	private static void matchHostTheme(Component c) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception e) {}
		SwingUtilities.updateComponentTreeUI(c);
	}

}
