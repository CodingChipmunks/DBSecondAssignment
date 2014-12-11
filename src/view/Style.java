package view;

import javax.swing.*;

/**
 * Makes window
 * 
 * @author softish
 *
 */
@SuppressWarnings("serial")
public class Style extends JPanel {
	public static void setUITheme(JFrame frame) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}
		SwingUtilities.updateComponentTreeUI(frame);
	}
}
