package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.SwingUtilities;
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

	// executes a query in a thread, when the query is done an event is
	// added to gui thread, which will load all available data from the data
	// bank in model.
	public void executeQuery(QueryType queryType) {
		new Thread() {
			String errormsg = "";
			public void run() {
				System.out.println("query running!");
				try {
					QueryExecuter qx = new QueryExecuter(model);

					// determine type of query. EEEK!?!?!?
					switch (queryType) {
					case BOOKSEARCH:
						qx.getAllAlbums();
						break;
					case MOVIESEARCH:
						qx.getMovie();
						break;
					case ALBUMSEARCH:
						qx.getAllAlbums();
						break;
					// TODO add rating/review
					}

					System.out.println("query complete!");
				} catch (SQLException e) {
					errormsg = e.getMessage();
				}
				System.out.println("View Waiting for update..");
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						if (!errormsg.equals(""))
							wbview.showError(errormsg);
						wbview.feedTable(model.getBank());
						System.out.println("View was updated!");
					}
				});
			}
		}.start();
	}

	public void setButtonSearch(JButton button) {
		button.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				QueryType qt = null;

				switch (wbview.getSearchPanelComponent().getSelectedIndex()) {
				case 0:
					qt = QueryType.ALBUMSEARCH;
				case 1:
					qt = QueryType.BOOKSEARCH;
				case 2:
					qt = QueryType.MOVIESEARCH;
				}
				executeQuery(qt);
			}
		});
	}
	
	public void setButtonReview(JButton button) {
		button.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				//TODO create review query..
			}
		});
	}
	
	public void setButtonRate(JButton button) {
		button.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				// TODO create rate query..
			}
		});
	}
	
	public void setButtonAdd(JButton button) {
		button.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				// TODO create add query
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//
	}

	// TODO add query types
	public enum QueryType {
		BOOKSEARCH, ALBUMSEARCH, MOVIESEARCH
	};
}
