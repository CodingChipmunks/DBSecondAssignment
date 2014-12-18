import java.awt.EventQueue;

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
					new WBView(model, new MongoQueryExecuter(model));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

}
