package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import model.*;
import view.*;

/**
 * Handles listeners
 * 
 * 
 */
public class Controller implements ActionListener {

	private WBView wbview;
	private View view;
	private Model model;
	private QueryInterpreter dbInterpreter;
	private int selectedItemInCombobox;
	private Album album = null;
	private int rating;

	/*
	 * public Controller(Model m, View v) { this.model = m; this.view = v; }
	 */

	public Controller(Model m, WBView wbv) {
		this.model = m;
		this.wbview = wbv;
		executeQuery(QueryType.ALBUMSEARCH, "%");
	}

	// executes a query in a thread, when the query is done an event is
	// added to gui thread, which will load all available data from the data
	// bank in model.
	public void executeQuery(final QueryType queryType, final String queryText) {
		new Thread() {
			String errormsg = "";

			public void run() {
				QueryExecuter qx = null;
				try {
					qx = new QueryExecuter(model);
					wbview.setColumnFilter(new String[] { "review" });
					// determine type of query. EEEK!?!?!?
					switch (queryType) {
					case MEDIAADD:
						qx.addMedia(album);
						break;
					case BOOKSEARCH:
						qx.getAlbumsByAny(queryText);
						break;
					case MOVIESEARCH:
						qx.getAlbumsByAny(queryText);
						break;
					case ALBUMSEARCH:
						qx.getAlbumsByAny(queryText);
						break;
					case RATE:
						qx.rateAlbum(rating);
					// TODO add rating/review
					}
				} catch (SQLException e) {
					errormsg = e.getMessage();
				} finally {
					qx.disconnect();
				}
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						if (!errormsg.equals(""))
							wbview.showError(errormsg);
						wbview.feedTable(model.getBank());
					}
				});
			}
		}.start();
	}

	private QueryType queryType() {
		QueryType qt = null;

		switch (wbview.getMediaIndex()) {
		case 0:
			qt = QueryType.ALBUMSEARCH;
		case 1:
			qt = QueryType.BOOKSEARCH;
		case 2:
			qt = QueryType.MOVIESEARCH;
		}

		return qt;
	}

	// add listener
	public void setQuerySource(JTextField textField) {
		textField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				System.out.println("Query=" + wbview.getMediaQuery());
				executeQuery(queryType(), "%" + wbview.getMediaQuery() + "%");
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				System.out.println("Query=" + wbview.getMediaQuery());
				executeQuery(queryType(), "%" + wbview.getMediaQuery() + "%");
			}
		});
	}

	public void setButtonSearch(JButton button) {
		button.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				executeQuery(queryType(), wbview.getMediaQuery());
			}
		});
	}

	public void setButtonReview(JButton button) {
		button.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				// TODO create review query..
				System.out.println("Review Button");
				wbview.invokeReviewMediaDialog();
			}
		});
	}

	public void setButtonRate(JButton button) {
		button.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				try {
					System.out.println("Rate Button for Media_Id: "
							+ wbview.getSelectedId());

					if (wbview.getSelectedRowCount() == 1) {
						wbview.invokeRateMediaDialog(wbview.getSelectedId());
						
						// while?
						
					} else {
						// wbview.showError("You have to select ONE media item!");
						throw new Exception("multiselect");
					}

				} catch (Exception e) {
					if (e.getMessage().equals("multiselect")) {
						wbview.showError("You have to select ONE media item!");
					} else {
						wbview.showError("You have to select a media item!");
					}
				}
			}
		});
	}

	// button add submit in addMediaDialog
	public void setSubmit(JButton button, final AddMediaDialog dialog) {
		button.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				Hashtable table = dialog.getValues();
				dialog.setVisible(false);
				album = new Album("", "", "", 0);

				Enumeration e = table.keys();
				while (e.hasMoreElements()) {
					String key = (String) e.nextElement();
					System.out.println(key + " : " + table.get(key));

					if (key.toString().equals("name"))
						album.setName(table.get(key).toString());
					if (key.toString().equals("genre"))
						album.setGenre(table.get(key).toString());
					if (key.toString().equals("year"))
						album.setYear(table.get(key).toString());
					if (key.toString().equals("artist")) {
						// TODO change split-pattern, ", " is common!
						String[] artists = table.get(key).toString()
								.split(", ");

						for (int i = 0; i < artists.length; i++)
							album.AddArtist(artists[i]);
					}
				}
				executeQuery(QueryType.MEDIAADD, "");
			}
		});
	}
	
	public void setSubmitRate(JButton button, final RateMediaDialog dialog) {
		button.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				
				rating = dialog.getValues();
				
				dialog.setVisible(false);
				
				// set rating
				executeQuery(QueryType.RATE, "");
				// display update
				executeQuery(QueryType.ALBUMSEARCH, "%");	// 2 fast?
			}
		});
	}

	public void setButtonAdd(JButton button) {
		button.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				// TODO create add query
				System.out.println("Add Button");
				wbview.invokeAddMediaDialog();
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//
	}

	// TODO add query types
	public enum QueryType {
		BOOKSEARCH, ALBUMSEARCH, MOVIESEARCH, MEDIAADD, RATE
	};
}
