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
		final String user;
		final String pass;
		
		if (args.length != 2) {
			user = "";
			pass = "";
		}
		else
		{
			user = args[0];
			pass = args[1];
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Model model = new Model(user, pass);
					WBView frame = new WBView(model);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

}
