package view;

import javax.swing.*;

import model.Model;

/**
 * Makes window
 * 
 * @author softish
 *
 */
public class View extends JPanel {
	public static void setUITheme(JFrame frame) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}
		SwingUtilities.updateComponentTreeUI(frame);
	}
}
