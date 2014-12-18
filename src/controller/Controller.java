package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import model.*;
import view.*;

/**
 * Handles listeners.
 * 
 * @author Lazar Klincov, Robin Duda.
 */
public class Controller implements ActionListener {

	private WBView wbview;
	private Model model;
	private Album album = null;
	private Movie movie = null;
	private Book book = null;
	private Review review = null;
	private int rating;
	private int media;
	private QueryInterpreter qx = null;

	public Controller(Model m, WBView wbv, QueryInterpreter queryExecuter) {
		this.model = m;
		this.wbview = wbv;
		this.qx = queryExecuter;
		executeQuery(QueryType.ALBUMSEARCH, "%");
	}

	/** executes a query in a thread, when the query is done an event is
	 	added to gui thread, which will load all available data from the data
	 	bank in model.
	 	
	 	@param queryType type of query to be executed.
	 	@param queryText text parameters to query. */
	public void executeQuery(final QueryType queryType, final String queryText) {
		new Thread() {
			String errormsg = "";

			public void run() {
				qx.open();
				try {
					wbview.setColumnFilter(new String[] { "review" });
					switch (queryType) {
					case MEDIAADD:
						switch (queryType()) {
						case ALBUMSEARCH:
							qx.addMedia(album.getName(), album.getYear(), album
									.getGenre(), album.getArtist().toArray(),
									0, 1);
							break;
						case MOVIESEARCH:
							qx.addMedia(movie.getTitle(), movie.getYear(),
									movie.getGenre(), movie.getDirector()
											.toArray(), movie.getDuration(), 2);
							break;
						case BOOKSEARCH:
							qx.addMedia(book.getTitle(), book.getYear(),
									book.getGenre(),
									book.getAuthor().toArray(), 0, 3);
							break;
						default:
							break;
						}
						break;
					case MOVIESEARCH:
						qx.getMoviesByAny(queryText);
						break;
					case ALBUMSEARCH:
						qx.getAlbumsByAny(queryText);
						break;
					case REVIEWSEARCH:
						qx.getReviewsByAny(queryText);
						break;
					case RATE:
						qx.rateAlbum(rating, media);
						break;
					case REVIEW:
						qx.reviewMedia(review, queryText);
						break;
					case LOGIN:
						qx.verifyAccount(model.getUser(), model.getPass());
						break;
					default:
						break;
					}
				} catch (SQLException e) {
					e.printStackTrace();
					errormsg = e.getMessage();
				} finally {
					qx.disconnect();
				}
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						if (!errormsg.equals(""))
							wbview.showError(errormsg);

						if (queryType != QueryType.LOGIN)
							wbview.feedTable(model.getBank());
						if (queryType == QueryType.LOGIN) {
							if (model.getValidAccount()) {
								wbview.setVisible(true);
								wbview.getLoginDialog().setVisible(false);
							} else {
								wbview.getLoginDialog().loginFailed();
								wbview.showError("Invalid Login!\r\nPlease contact database admin\r\nto create a new account.");
							}
						}
					}
				});
			}
		}.start();
	}

	/*** @return type of media seleted in AddDialog ***/
	private QueryType addType() {
		QueryType qt = null;

		switch (wbview.getMediaDialog().getMediaIndex()) {
		case 0:
			qt = QueryType.ALBUMSEARCH;
			break;
		case 1:
			qt = QueryType.MOVIESEARCH;
			break;
		case 2:
			qt = QueryType.BOOKSEARCH;
			break;
		}

		return qt;
	}

	/*** @return type of media selected in main search-form. ***/
	private QueryType queryType() {
		QueryType qt = null;

		switch (wbview.getMediaIndex()) {
		case 0:
			qt = QueryType.ALBUMSEARCH;
			break;
		case 1:
			qt = QueryType.MOVIESEARCH;
			break;
		//case 2:
		//	qt = QueryType.BOOKSEARCH;
		//	break;
		case 2:
			qt = QueryType.REVIEWSEARCH;
			break;
		}

		return qt;
	}

	/*** listen to changes in the search-box, updates search. ***/
	public void setQuerySource(JTextField textField) {
		textField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				executeQuery(queryType(), "%" + wbview.getMediaQuery() + "%");
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				executeQuery(queryType(), "%" + wbview.getMediaQuery() + "%");
			}
		});
	}

	/*** the search button, using this will return only exact matches. ***/
	public void setButtonSearch(JButton button) {
		button.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				executeQuery(queryType(), wbview.getMediaQuery());
			}
		});
	}

	/*** Event triggered when a revied is added from AddReviewDialog ***/
	public void setButtonAddReview(JButton button) {
		button.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				ReviewMediaDialog reviewDialog = wbview.getReviewDialog();
				review = new Review(reviewDialog.getReviewTitle(), reviewDialog.getReviewBody(), "", "", "");
				executeQuery(QueryType.REVIEW, "" + reviewDialog.getPK());
				wbview.getReviewDialog().setVisible(false);
			}
		});
	}

	/*** Triggered before showind the AddReviewDialog, ensures that not more or less than a single
	 * row is selected.
	 * @param button triggers the event.
	 */
	public void setButtonReview(JButton button) {
		button.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				try {
					
					if (wbview.getSelectedRowCount() == 1) {
						wbview.getReviewDialog().setPK(wbview.getSelectedId());
						wbview.invokeReviewMediaDialog();
					} else {
						throw new Exception("multiselect");
					}
				} catch (Exception e) {
					e.printStackTrace();
					if (e.getMessage().equals("multiselect")) {
						wbview.showError("You have to select ONE media item!");
					} else {
						wbview.showError("You have to select a media item!");
					}
				}
			}
		});

	}

	/*** Button used to show the rating dialog, ensures that valid amount of rows are selected ***/
	public void setButtonRate(JButton button) {
		button.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				try {
					System.out.println("Rate Button for Media_Id: "
							+ wbview.getSelectedId());

					if (wbview.getSelectedRowCount() == 1) {
						wbview.invokeRateMediaDialog();
					} else {
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

	/*** Convert a list of values into an object.
	 * 
	 * @param key determines the name of the class attribute.
	 * @param value determines the value of the class attribute.
	 */
	private void dataToAlbum(String key, String value) {
		if (key.toString().equals("name"))
			album.setName(value);
		if (key.toString().equals("genre"))
			album.setGenre(value);
		if (key.toString().equals("year"))
			album.setYear(value);
		if (key.toString().equals("artist")) {
			String[] artists = value.split(", ");

			for (int i = 0; i < artists.length; i++)
				album.AddArtist(artists[i]);
		}
	}


	/*** Convert a list of values into an object.
	 * 
	 * @param key determines the name of the class attribute.
	 * @param value determines the value of the class attribute.
	 */
	private void dataToMovie(String key, String value) {
		if (key.toString().equals("title"))
			movie.setTitle(value);
		if (key.toString().equals("genre"))
			movie.setGenre(value);
		if (key.toString().equals("year"))
			movie.setYear(value);
		if (key.toString().equals("director")) {
			String[] director = value.split(", ");

			for (int i = 0; i < director.length; i++)
				movie.addDirector(director[i]);
		}
	}


	/*** Convert a list of values into an object.
	 * 
	 * @param key determines the name of the class attribute.
	 * @param value determines the value of the class attribute.
	 */
	private void dataToBook(String key, String value) {
		if (key.toString().equals("title"))
			book.setTitle(value);
		if (key.toString().equals("genre"))
			book.setGenre(value);
		if (key.toString().equals("year"))
			book.setYear(value);
		if (key.toString().equals("author")) {
			String[] author = value.split(", ");

			for (int i = 0; i < author.length; i++)
				book.addAuthor(author[i]);
		}
	}

	/*** Creates an object from given parameter values in dialog, type is determined
	 * by search type. ***/
	public void setSubmit(JButton button, final AddMediaDialog dialog) {
		button.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				@SuppressWarnings("rawtypes")
				Hashtable table = dialog.getValues();
				dialog.setVisible(false);

				album = new Album("", "", "", "");
				movie = new Movie("", "", "", "", 0, 0);
				book = new Book("", "", 0);

				@SuppressWarnings("rawtypes")
				Enumeration e = table.keys();
				while (e.hasMoreElements()) {
					String key = (String) e.nextElement();
					System.out.println(key + " : " + table.get(key));

					switch (addType()) {
					case ALBUMSEARCH:
						dataToAlbum(key, table.get(key).toString());
						break;
					case MOVIESEARCH:
						dataToMovie(key, table.get(key).toString());
						break;
					case BOOKSEARCH:
						dataToBook(key, table.get(key).toString());
						break;
					default:
						break;
					}
				}
				executeQuery(QueryType.MEDIAADD, "");
			}
		});
	}

	/*** Submits a media rating ***/
	public void setSubmitRate(JButton button, final RateMediaDialog dialog) {
		button.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				rating = dialog.getValues();
				media = dialog.getSelectedId();
				dialog.setVisible(false);
				executeQuery(QueryType.RATE, "");
			}
		});
	}

	/*** Show add media dialog ***/
	public void setButtonAdd(JButton button) {
		button.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				if (queryType() != QueryType.REVIEWSEARCH)
					wbview.invokeAddMediaDialog(wbview.getMediaIndex());
				else
					wbview.invokeAddMediaDialog(0);
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	}

	public enum QueryType {
		BOOKSEARCH, ALBUMSEARCH, MOVIESEARCH, REVIEWSEARCH, MEDIAADD, RATE, LOGIN, REVIEW
	}

	/*** check a users password and set model password ***/
	public void setLogin(JButton login, final LoginDialog loginDialog) {
		login.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				model.setPass(loginDialog.getPass());
				model.setUser(loginDialog.getUser());
				executeQuery(QueryType.LOGIN, "");
			}
		});
	}

	/*** called from wbview on ComboChanged. ***/
	public void comboMediaChanged() {
		executeQuery(queryType(), "%" + wbview.getMediaQuery() + "%");
	}
}
