package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import controller.Controller.QueryType;

/**
 * All db interaction code resides here, could be called directly by view, or by
 * view through intermediary layer of Model. (more about this in QUESTIONS.txt)
 * 
 * This class is a translator between the ER and Object model and vice versa.
 * 
 * 
 */
public final class QueryExecuter implements QueryInterpreter {
	private final static String database = "mediacollection";
	private final static String user = "clientapp";
	private final static String pass = "qwerty";
	private final static String driver = "com.mysql.jdbc.Driver";
	private final static String config = "?noAccessToProcedureBodies=true";
	private final static String host = "jdbc:mysql://localhost:3306/";

	private Connection connection;
	private Model model;
	private static String lastQuery;
	private static QueryType lastQueryType;

	private synchronized void setLastQuery(String lastQuery, QueryType queryType) {
		QueryExecuter.lastQuery = lastQuery;
		QueryExecuter.lastQueryType = queryType;
	}

	private synchronized String getLastQuery() {
		return QueryExecuter.lastQuery;
	}

	private synchronized QueryType getLastQueryType() {
		return QueryExecuter.lastQueryType;
	}

	public QueryExecuter(Model model) throws SQLException {
		this.model = model;

		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(host + database + config,
					user, pass);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void disconnect() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
		}
	}

	@Override
	public ArrayList<Album> getAllAlbums(ResultSet rsetAlbum)
			throws SQLException {
		ArrayList<Album> albums = new ArrayList<Album>();

		try {
			// for every album do...
			while (rsetAlbum.next()) {
				ResultSet rsetGenre;
				ResultSet rsetArtist;
				ResultSet rsetRating;
				ResultSet rsetReview;
				ResultSet rsetUser;
				ResultSet rsetId = null;
				Statement stId = connection.createStatement();
				PreparedStatement stUser = connection
						.prepareStatement("select Name from Account where Id = ?;");
				PreparedStatement stGenre = connection
						.prepareStatement("select Name from Genre where Id = ?;");
				PreparedStatement stArtist = connection
						.prepareStatement("select Name from Contributor inner join Creator where Media_Id = ? and Creator.id = Contributor.Creator_Id;");
				PreparedStatement stRating = connection
						.prepareStatement("select avg(Rating) from Rating where Media_Id = ?;");
				PreparedStatement stReview = connection
						.prepareStatement("select Title, Text, Account_Id from Review where Media_Id = ?;");
				Album album = RowConverter.convertRowToAlbum(rsetAlbum);

				stGenre.setInt(1, rsetAlbum.getInt("Genre_Id"));
				rsetGenre = stGenre.executeQuery();

				rsetGenre.first();
				album.setGenre(rsetGenre.getString("Name"));

				stArtist.setInt(1, album.getId());
				rsetArtist = stArtist.executeQuery();

				while (rsetArtist.next())
					album.AddArtist(rsetArtist.getString("Name"));

				// get the rating.
				stRating.setInt(1, album.getId());
				rsetRating = stRating.executeQuery();

				while (rsetRating.next())
					album.setRating(rsetRating.getFloat(1));

				// finally, get reviews..
				stReview.setInt(1, album.getId());
				rsetReview = stReview.executeQuery();

				while (rsetReview.next()) {
					Review review = RowConverter.convertRowToReview(rsetReview);

					stUser.setInt(1, rsetReview.getInt("Account_Id"));
					rsetUser = stUser.executeQuery();
					rsetUser.first();
					review.setUser(rsetUser.getString("Name"));
					album.addReview(review);
				}

				// ops, need user too..
				stUser.setInt(1, rsetAlbum.getInt("Account_Id"));
				rsetUser = stUser.executeQuery();
				rsetUser.first();
				album.setUser(rsetUser.getString("Name"));

				albums.add(album);

				// ha-ha
				listClose(new Statement[] { stId, stUser, stGenre, stArtist,
						stRating, stReview }, new ResultSet[] { rsetGenre,
						rsetArtist, rsetRating, rsetReview, rsetUser, rsetId });
			}
		} finally {
			closeResultSet(rsetAlbum);
		}

		return albums;
	}

	@Override
	public void addMedia(String name, String year, String genre,
			Object[] objects, int duration, int mediaType) throws SQLException {
		// AddMedia("Foo", "Foo", "Title", "2014", "Genre", Duration,
		CallableStatement stMedia = null;
		CallableStatement stCreator = null;
		try {
			String sql = "{call AddMedia(?, ?, ?, ?, ?, ?, ?, ?)}";

			stMedia = connection.prepareCall(sql);

			stMedia.setString(1, model.getUser());
			stMedia.setString(2, model.getPass());
			stMedia.setString(3, name);
			stMedia.setString(4, year);
			stMedia.setString(5, genre);
			stMedia.setInt(6, duration); // duration not implemented
			stMedia.setInt(7, mediaType);
			stMedia.registerOutParameter(8, java.sql.Types.INTEGER);

			// loop through artist and call AddCreator ...
			stMedia.execute();

			System.out.println("Returned PK = " + stMedia.getInt(8));

			// CALL AddCreator("Foo", "Foo", "CreatorName", 1);

			sql = "{call AddCreator(?, ?, ?, ?)}";
			stCreator = connection.prepareCall(sql);
			for (int i = 0; i < objects.length; i++) {
				stCreator.setString(1, model.getUser());
				stCreator.setString(2, model.getPass());
				stCreator.setString(3, objects[i].toString());
				stCreator.setInt(4, stMedia.getInt(8));
				stCreator.execute();
			}

			rebootDataSet();
		} finally {
			closeStatement(stMedia);
		}
	}

	@Override
	public ArrayList<Album> getAlbumsByAny(String text) throws SQLException {
		Set<Album> album = new HashSet<Album>();

		setLastQuery(text, QueryType.ALBUMSEARCH);
		album.addAll(getAlbumsByUser(text));
		album.addAll(getAlbumsByYear(text));
		album.addAll(getAlbumsByTitle(text));
		album.addAll(getAlbumsByRating(text));
		album.addAll(getAlbumsByArtist(text));
		album.addAll(getAlbumsByGenre(text));

		model.setBank(album.toArray()); // ...

		return new ArrayList<Album>(album);
	}

	@Override
	public ArrayList<Album> getAlbumsByUser(String user) throws SQLException {
		ArrayList<Album> album = new ArrayList<Album>();
		ResultSet rsetAlbum = null;
		PreparedStatement stAlbum = connection
				.prepareStatement("SELECT Media.* FROM Media, Account WHERE Media.MediaType_Id = 1 AND Account_Id = Account.Id  AND Account.Name like ?;");
		try {
			stAlbum.setString(1, user);
			rsetAlbum = stAlbum.executeQuery();

			album = getAllAlbums(rsetAlbum);
		} finally {
			listClose(new Statement[] { stAlbum },
					new ResultSet[] { rsetAlbum });
		}
		return album;
	}

	@Override
	public ArrayList<Album> getAlbumsByYear(String year) throws SQLException {
		ArrayList<Album> album = new ArrayList<Album>();
		ResultSet rsetAlbum = null;
		PreparedStatement stAlbum = connection
				.prepareStatement("SELECT * FROM Media WHERE Media.MediaType_Id = 1 "
						+ "AND Year like ?;");
		try {
			stAlbum.setString(1, year);
			rsetAlbum = stAlbum.executeQuery();

			album = getAllAlbums(rsetAlbum);
		} finally {
			listClose(new Statement[] { stAlbum },
					new ResultSet[] { rsetAlbum });
		}
		return album;
	}

	@Override
	public ArrayList<Album> getAlbumsByRating(String rating)
			throws SQLException {
		ArrayList<Album> album = new ArrayList<Album>();
		ResultSet rsetAlbum = null;
		PreparedStatement stAlbum = connection
				.prepareStatement("SELECT * FROM Media, Rating WHERE Media.MediaType_Id = 1 "
						+ "AND Media.Id = Rating.Media_Id AND  (SELECT avg(Rating) FROM Rating WHERE Media.Id = "
						+ "Rating.Media_Id) >= ?" + " GROUP BY Media.Id;");
		try {
			Float score = Float.parseFloat(rating.replace("%", ""));
			rating = rating.replace("%", "");

			stAlbum.setFloat(1, score);
			rsetAlbum = stAlbum.executeQuery();

			album = getAllAlbums(rsetAlbum);
		} catch (NumberFormatException e) {
			// e.printStackTrace();
		} finally {
			listClose(new Statement[] { stAlbum },
					new ResultSet[] { rsetAlbum });
		}
		return album;
	}

	@Override
	public ArrayList<Album> getAlbumsByGenre(String genre) throws SQLException {
		ArrayList<Album> album = new ArrayList<Album>();
		ResultSet rsetAlbum = null;
		PreparedStatement stAlbum = null;

		try {
			stAlbum = connection
					.prepareStatement("SELECT Media.* FROM Media, Genre WHERE Genre.Name LIKE ?"
							+ " And Media.Genre_Id = Genre.Id AND MediaType_Id = 1;");
			stAlbum.setString(1, genre);
			rsetAlbum = stAlbum.executeQuery();

			if (rsetAlbum.isBeforeFirst()) {
				album = getAllAlbums(rsetAlbum);
			}
		} finally {
			listClose(new Statement[] { stAlbum },
					new ResultSet[] { rsetAlbum });
		}

		return album;
	}

	@Override
	public ArrayList<Album> getAlbumsByTitle(String title) throws SQLException {
		ArrayList<Album> album = new ArrayList<Album>();
		ResultSet rsetAlbum = null;
		PreparedStatement stAlbum = connection
				.prepareStatement("select * from Media where Mediatype_Id = 1 and title like ?;");
		try {
			stAlbum.setString(1, title);
			rsetAlbum = stAlbum.executeQuery();
			album = getAllAlbums(rsetAlbum);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			listClose(new Statement[] { stAlbum },
					new ResultSet[] { rsetAlbum });
		}
		return album;
	}

	@Override
	public ArrayList<Album> getAlbumsByArtist(String artist)
			throws SQLException {
		ArrayList<Album> album = new ArrayList<Album>();
		PreparedStatement stAlbum = connection
				.prepareStatement("SELECT *"
						+ " FROM Media, Contributor, Creator WHERE	Media.Id = Contributor.Media_Id AND "
						+ "Contributor.Creator_Id = Creator.Id AND Media.Mediatype_Id = 1 AND Creator.Name LIKE ? "
						+ "GROUP BY Media.Id;");
		ResultSet rsetAlbum = null;

		try {
			stAlbum.setString(1, artist);
			rsetAlbum = stAlbum.executeQuery();

			album = getAllAlbums(rsetAlbum);
		} finally {
			listClose(new Statement[] { stAlbum },
					new ResultSet[] { rsetAlbum });
		}
		return album;
	}

	private void rebootDataSet() throws SQLException {
		switch (getLastQueryType()) {
		case ALBUMSEARCH:
			model.setBank(getAlbumsByAny(getLastQuery()).toArray());
			System.out.println("ALBUMSEARCH = " + getLastQuery());
			break;
		case BOOKSEARCH:
			model.setBank(getBooksByAny(getLastQuery()).toArray());
			System.out.println("BOOKSEARCH = " + getLastQuery());
			break;
		case MOVIESEARCH:
			model.setBank(getMoviesByAny(getLastQuery()).toArray());
			System.out.println("MOVIESEARCH = " + getLastQuery());
			break;
		default:
			break;
		}
	}

	@Override
	public void rateAlbum(int rating, int media) throws SQLException {
		CallableStatement callableStatement = null;
		try {
			String statement = "{call Rate(?, ?, ?, ?)}";
			callableStatement = connection.prepareCall(statement);

			// hard Data
			callableStatement.setInt(1, media);
			callableStatement.setString(2, model.getUser());
			callableStatement.setString(3, model.getPass());
			callableStatement.setInt(4, rating);

			System.out.println("Executing Rate procedure...");
			callableStatement.executeUpdate();

		} finally {
			closeStatement(callableStatement);
		}
		rebootDataSet();
	}

	/************************************************************************************************************************/

	@Override
	public ArrayList<Movie> getAllMovies(ResultSet rsetMovie)
			throws SQLException {
		ArrayList<Movie> movies = new ArrayList<Movie>();

		try {
			// for every album do...
			while (rsetMovie.next()) {
				ResultSet rsetGenre;
				ResultSet rsetDirector;
				ResultSet rsetRating;
				ResultSet rsetReview;
				ResultSet rsetUser;
				ResultSet rsetId = null;
				Statement stId = connection.createStatement();
				PreparedStatement stUser = connection
						.prepareStatement("select Name from Account where Id = ?;");
				PreparedStatement stGenre = connection
						.prepareStatement("select Name from Genre where Id = ?;");
				PreparedStatement stDirector = connection
						.prepareStatement("select Name from Contributor inner join Creator where Media_Id = ? and Creator.id = Contributor.Creator_Id;");
				PreparedStatement stRating = connection
						.prepareStatement("select avg(Rating) from Rating where Media_Id = ?;");
				PreparedStatement stReview = connection
						.prepareStatement("select Title, Text, Account_Id from Review where Media_Id = ?;");
				Movie movie = RowConverter.convertRowToMovie(rsetMovie);

				stGenre.setInt(1, rsetMovie.getInt("Genre_Id"));
				rsetGenre = stGenre.executeQuery();

				rsetGenre.first();
				movie.setGenre(rsetGenre.getString("Name"));

				stDirector.setInt(1, movie.getId());
				rsetDirector = stDirector.executeQuery();

				while (rsetDirector.next())
					movie.addDirector(rsetDirector.getString("Name"));

				// get the rating.
				stRating.setInt(1, movie.getId());
				rsetRating = stRating.executeQuery();

				while (rsetRating.next())
					movie.setRating(rsetRating.getFloat(1));

				// finally, get reviews..
				stReview.setInt(1, movie.getId());
				rsetReview = stReview.executeQuery();

				while (rsetReview.next()) {
					Review review = RowConverter.convertRowToReview(rsetReview);

					stUser.setInt(1, rsetReview.getInt("Account_Id"));
					rsetUser = stUser.executeQuery();
					rsetUser.first();
					review.setUser(rsetUser.getString("Name"));
					movie.addReview(review);
				}

				// ops, need user too..
				stUser.setInt(1, rsetMovie.getInt("Account_Id"));
				rsetUser = stUser.executeQuery();
				rsetUser.first();
				movie.setUser(rsetUser.getString("Name"));

				movies.add(movie);

				// ha-ha
				listClose(new Statement[] { stId, stUser, stGenre, stDirector,
						stRating, stReview }, new ResultSet[] { rsetGenre,
						rsetDirector, rsetRating, rsetReview, rsetUser, rsetId });
			}
		} finally {
			closeResultSet(rsetMovie);
		}

		return movies;
	}


	@Override
	public ArrayList<Movie> getMovieByUser(String user) throws SQLException {
		ArrayList<Movie> movie = new ArrayList<Movie>();
		ResultSet rsetMovie = null;
		PreparedStatement stMovie = connection.prepareStatement("SELECT Media.* FROM Media, Account WHERE Media.MediaType_Id = 2 "
				+ "AND Account_Id = Account.Id "
				+ " AND Account.Name like ?;");
		try {
			stMovie.setString(1,  user);
			rsetMovie = stMovie.executeQuery();

			movie = getAllMovies(rsetMovie);
		} finally {
			listClose(new Statement[] { stMovie },
					new ResultSet[] { rsetMovie });
		}
		return movie;
	}

	@Override
	public ArrayList<Movie> getMovieByYear(String year) throws SQLException {
		ArrayList<Movie> movie = new ArrayList<Movie>();
		ResultSet rsetMovie = null;
		PreparedStatement stMovie = connection.prepareStatement("SELECT * FROM Media WHERE Media.MediaType_Id = 2 "
				+ "AND Year like ?;");
		try {
			stMovie.setString(1, year);
			rsetMovie = stMovie.executeQuery();
			movie = getAllMovies(rsetMovie);
		} finally {
			listClose(new Statement[] { stMovie },
					new ResultSet[] { rsetMovie });
		}
		return movie;
	}

	@Override
	public ArrayList<Movie> getMovieByTitle(String title) throws SQLException {
		ArrayList<Movie> movie = new ArrayList<Movie>();
		ResultSet rsetMovie = null;
		PreparedStatement stMovie = connection.prepareStatement("select * from Media where Mediatype_Id = 2 and title like ?;");
		try {
			stMovie.setString(1, title);
			rsetMovie = stMovie.executeQuery();
			movie = getAllMovies(rsetMovie);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			listClose(new Statement[] { stMovie },
					new ResultSet[] { rsetMovie });
		}
		return movie;
	}

	@Override
	public ArrayList<Movie> getMovieByRating(String rating) throws SQLException {
		ArrayList<Movie> movie = new ArrayList<Movie>();
		ResultSet rsetMovie = null;
		PreparedStatement stMovie = connection.prepareStatement("SELECT * FROM Media, Rating WHERE Media.MediaType_Id = 2 "
				+ "AND Media.Id = Rating.Media_Id AND  (SELECT avg(Rating) FROM Rating WHERE Media.Id = "
				+ "Rating.Media_Id) >= "
				+ "?"
				+ " GROUP BY Media.Id;");
		try {
			Float score = Float.parseFloat(rating.replace("%", ""));
			rating = rating.replace("%", "");
			stMovie.setFloat(1, score);
			rsetMovie = stMovie.executeQuery();

			movie = getAllMovies(rsetMovie);
		} catch (NumberFormatException e) {
			// e.printStackTrace();
		} finally {
			listClose(new Statement[] { stMovie },
					new ResultSet[] { rsetMovie });
		}
		return movie;
	}

	@Override
	public ArrayList<Movie> getMovieByDirector(String director)
			throws SQLException {
		ArrayList<Movie> album = new ArrayList<Movie>();
		PreparedStatement stMovie = connection.prepareStatement("SELECT *"
				+ " FROM Media, Contributor, Creator WHERE	Media.Id = Contributor.Media_Id AND "
				+ "Contributor.Creator_Id = Creator.Id AND Media.Mediatype_Id = 2 AND Creator.Name LIKE ?"
				+ " GROUP BY Media.Id;");
		ResultSet rsetMovie = null;

		try {
			stMovie.setString(1, director);
			rsetMovie = stMovie.executeQuery();

			album = getAllMovies(rsetMovie);
		} finally {
			listClose(new Statement[] { stMovie },
					new ResultSet[] { rsetMovie });
		}
		return album;
	}

	@Override
	public ArrayList<Movie> getMovieByGenre(String genre) throws SQLException {
		ArrayList<Movie> movie = new ArrayList<Movie>();
		ResultSet rsetMovie = null;
		PreparedStatement stMovie = null;

		try {
			stMovie = connection.prepareStatement("SELECT Media.* FROM Media, Genre WHERE Genre.Name LIKE "
					+ "? And Media.Genre_Id = Genre.Id AND MediaType_Id = 2;");
			stMovie.setString(1, genre);
			rsetMovie = stMovie
					.executeQuery();

			if (rsetMovie.isBeforeFirst()) {
				movie = getAllMovies(rsetMovie);
			}
		} finally {
			listClose(new Statement[] { stMovie },
					new ResultSet[] { rsetMovie });
		}

		return movie;
	}

	@Override
	public ArrayList<Movie> getMoviesByAny(String text) throws SQLException {
		Set<Movie> movie = new HashSet<Movie>();

		setLastQuery(text, QueryType.MOVIESEARCH);
		movie.addAll(getMovieByUser(text));
		movie.addAll(getMovieByYear(text));
		movie.addAll(getMovieByTitle(text));
		movie.addAll(getMovieByRating(text));
		movie.addAll(getMovieByDirector(text));
		movie.addAll(getMovieByGenre(text));

		model.setBank(movie.toArray()); // ...

		return new ArrayList<Movie>(movie);
	}

	/********************************************************************************************************************************/

	@Override
	public ArrayList<Book> getAllBooks(ResultSet rsetBook)
			throws SQLException {
		ArrayList<Book> books = new ArrayList<Book>();

		try {
			// for every album do...
			while (rsetBook.next()) {
				ResultSet rsetGenre;
				ResultSet rsetAuthor;
				ResultSet rsetRating;
				ResultSet rsetReview;
				ResultSet rsetUser;
				ResultSet rsetId = null;
				Statement stId = connection.createStatement();
				PreparedStatement stUser = connection
						.prepareStatement("select Name from Account where Id = ?;");
				PreparedStatement stGenre = connection
						.prepareStatement("select Name from Genre where Id = ?;");
				PreparedStatement stAuthor = connection
						.prepareStatement("select Name from Contributor inner join Creator where Media_Id = ? and Creator.id = Contributor.Creator_Id;");
				PreparedStatement stRating = connection
						.prepareStatement("select avg(Rating) from Rating where Media_Id = ?;");
				PreparedStatement stReview = connection
						.prepareStatement("select Title, Text, Account_Id from Review where Media_Id = ?;");
				Book book = RowConverter.convertRowToBook(rsetBook);

				stGenre.setInt(1, rsetBook.getInt("Genre_Id"));
				rsetGenre = stGenre.executeQuery();

				rsetGenre.first();
				book.setGenre(rsetGenre.getString("Name"));

				stAuthor.setInt(1, book.getId());
				rsetAuthor = stAuthor.executeQuery();

				while (rsetAuthor.next())
					book.addAuthor(rsetAuthor.getString("Name"));

				// get the rating.
				stRating.setInt(1, book.getId());
				rsetRating = stRating.executeQuery();

				while (rsetRating.next())
					book.setRating(rsetRating.getFloat(1));

				// finally, get reviews..
				stReview.setInt(1, book.getId());
				rsetReview = stReview.executeQuery();

				while (rsetReview.next()) {
					Review review = RowConverter.convertRowToReview(rsetReview);

					stUser.setInt(1, rsetReview.getInt("Account_Id"));
					rsetUser = stUser.executeQuery();
					rsetUser.first();
					review.setUser(rsetUser.getString("Name"));
					book.addReview(review);
				}

				// ops, need user too..
				stUser.setInt(1, rsetBook.getInt("Account_Id"));
				rsetUser = stUser.executeQuery();
				rsetUser.first();
				book.setUser(rsetUser.getString("Name"));

				books.add(book);

				// ha-ha 
				listClose(new Statement[] { stId, stUser, stGenre, stAuthor,
						stRating, stReview }, new ResultSet[] { rsetGenre,
						rsetAuthor, rsetRating, rsetReview, rsetUser, rsetId });
			}
		} finally {
			closeResultSet(rsetBook);
		}

		return books;
	}

	@Override
	public ArrayList<Book> getBookByUser(String user) throws SQLException {
		ArrayList<Book> book = new ArrayList<Book>();
		ResultSet rsetBook = null;
		PreparedStatement stBook = connection.prepareStatement("SELECT Media.* FROM Media, Account WHERE Media.MediaType_Id = 3 "
				+ "AND Account_Id = Account.Id "
				+ " AND Account.Name like ?;");
		try {
			stBook.setString(1, user);
			rsetBook = stBook.executeQuery();

			book = getAllBooks(rsetBook);
		} finally {
			listClose(new Statement[] { stBook }, new ResultSet[] { rsetBook });
		}
		return book;
	}

	@Override
	public ArrayList<Book> getBookByYear(String year) throws SQLException {
		ArrayList<Book> book = new ArrayList<Book>();
		ResultSet rsetBook = null;
		PreparedStatement stBook = connection.prepareStatement("SELECT * FROM Media WHERE Media.MediaType_Id = 3 "
				+ "AND Year like ?;");
		try {
			stBook.setString(1, year);
			rsetBook = stBook.executeQuery();

			book = getAllBooks(rsetBook);
		} finally {
			listClose(new Statement[] { stBook }, new ResultSet[] { rsetBook });
		}
		return book;
	}

	@Override
	public ArrayList<Book> getBookByTitle(String title) throws SQLException {
		ArrayList<Book> book = new ArrayList<Book>();
		ResultSet rsetBook = null;
		PreparedStatement stBook = connection.prepareStatement("select * from Media where Mediatype_Id = 3 and title like ?");
		try {
			stBook.setString(1, title);
			rsetBook = stBook.executeQuery();
			book = getAllBooks(rsetBook);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			listClose(new Statement[] { stBook }, new ResultSet[] { rsetBook });
		}
		return book;
	}

	@Override
	public ArrayList<Book> getBookByRating(String rating) throws SQLException {
		ArrayList<Book> book = new ArrayList<Book>();
		ResultSet rsetBook = null;
		PreparedStatement stBook = connection.prepareStatement("SELECT * FROM Media, Rating WHERE Media.MediaType_Id = 3 "
				+ "AND Media.Id = Rating.Media_Id AND  (SELECT avg(Rating) FROM Rating WHERE Media.Id = "
				+ "Rating.Media_Id) >= ?"
				+ " GROUP BY Media.Id;");
		try {
			Float score = Float.parseFloat(rating.replace("%", ""));
			rating = rating.replace("%", "");
			stBook.setFloat(1, score);
			rsetBook = stBook.executeQuery();
			book = getAllBooks(rsetBook);
		} catch (NumberFormatException e) {
			// e.printStackTrace();
		} finally {
			listClose(new Statement[] { stBook }, new ResultSet[] { rsetBook });
		}
		return book;
	}

	@Override
	public ArrayList<Book> getBookByAuthor(String author) throws SQLException {
		ArrayList<Book> book = new ArrayList<Book>();
		PreparedStatement stBook = connection.prepareStatement("SELECT *"
				+ " FROM Media, Contributor, Creator WHERE	Media.Id = Contributor.Media_Id AND "
				+ "Contributor.Creator_Id = Creator.Id AND Media.Mediatype_Id = 3 AND Creator.Name LIKE ?"
				+ " GROUP BY Media.Id;");
		ResultSet rsetBook = null;

		try {
			stBook.setString(1, author);
			rsetBook = stBook.executeQuery();

			book = getAllBooks(rsetBook);
		} finally {
			listClose(new Statement[] { stBook }, new ResultSet[] { rsetBook });
		}
		return book;
	}

	@Override
	public ArrayList<Book> getBookByGenre(String genre) throws SQLException {
		ArrayList<Book> book = new ArrayList<Book>();
		ResultSet rsetBook = null;
		ResultSet rsetGenre = null;
		PreparedStatement stGenre = null;
		PreparedStatement stBook = null;

		try {
			stBook = connection.prepareStatement("SELECT Media.* FROM Media, Genre WHERE Genre.Name LIKE ?"
					+ " And Media.Genre_Id = Genre.Id AND MediaType_Id = 3;");
			stBook.setString(1, genre);
			rsetBook = stBook.executeQuery();

			if (rsetBook.isBeforeFirst()) {
				book = getAllBooks(rsetBook);
			}
		} finally {
			listClose(new Statement[] { stBook, stGenre }, new ResultSet[] {
					rsetBook, rsetGenre });
		}

		return book;
	}

	@Override
	public ArrayList<Book> getBooksByAny(String text) throws SQLException {
		Set<Book> book = new HashSet<Book>();

		setLastQuery(text, QueryType.BOOKSEARCH);
		book.addAll(getBookByUser(text));
		book.addAll(getBookByYear(text));
		book.addAll(getBookByTitle(text));
		book.addAll(getBookByRating(text));
		book.addAll(getBookByAuthor(text));
		book.addAll(getBookByGenre(text));

		model.setBank(book.toArray()); // ...

		return new ArrayList<Book>(book);
	}

	/****************************************************************************************************************************
	 */

	// close statement
	private void closeStatement(Statement s) throws SQLException {
		if (s != null) {
			s.close();
		}
	}

	// close result set
	private void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private void listClose(Statement[] statements, ResultSet[] resultSets) {
		try {
			for (int i = 0; i < statements.length; i++)
				if (null != statements[i])
					statements[i].close();
			for (int i = 0; i < resultSets.length; i++)
				if (null != resultSets[i])
					resultSets[i].close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void verifyAccount(String user, String pass) throws SQLException {
		System.out.println("Verifying account " + user + " - " + pass);

		int accountId;
		CallableStatement stAccount = null;
		try {
			String sql = "{call VerifyAccount(?, ?, ?)}";
			stAccount = connection.prepareCall(sql);

			stAccount.setString(1, model.getUser());
			stAccount.setString(2, model.getPass());
			stAccount.registerOutParameter(3, java.sql.Types.INTEGER);

			stAccount.executeUpdate();
			accountId = stAccount.getInt(3);

		} finally {
			closeStatement(stAccount);
		}

		if (accountId > 0)
			model.setValidAccount(true);
		else
			model.setValidAccount(false);

	}

	@Override
	public void getReviewsByAny(String queryText) throws SQLException {
		// resolve Media_Id & Account_Id to names
		// use base search: title + text
		ArrayList<Review> review = new ArrayList<Review>();
		PreparedStatement stReview = null;
		ResultSet rsetReview = null;

		try {
			stReview = connection.prepareStatement("SELECT mediatype.name, media.title, account.Name, Review.Title, Review.Text FROM "
					+ "review, media, account, mediatype WHERE "
					+ "review.media_id = media.id AND review.Account_Id = account.Id AND mediatype.Id = media.Mediatype_Id "
					+ "AND (media.title LIKE ?" 
					+ "OR review.Title LIKE ? "
					+ "OR review.text LIKE ? "
					+ "OR mediatype.name LIKE ? "
					+ "OR Account.Name LIKE ?);");
			
			stReview.setString(1, queryText);
			stReview.setString(2, queryText);
			stReview.setString(3, queryText);
			stReview.setString(4, queryText);
			stReview.setString(5, queryText);
			rsetReview = stReview.executeQuery();

			while (rsetReview.next()) {
				review.add(RowConverter.convertRowToExtendedReview(rsetReview));
			}

		} finally {
			closeStatement(stReview);
			closeResultSet(rsetReview);
		}

		model.setBank(review.toArray());
	}

	@Override
	public void reviewMedia(Review review, int pk) throws SQLException {
		CallableStatement stReview = null;
		try {
			String sql = "{call MakeReview(?, ?, ?, ?, ?)}";
			stReview = connection.prepareCall(sql);

			stReview.setString(1, model.getUser());
			stReview.setString(2, model.getPass());
			stReview.setString(3, review.getTitle());
			stReview.setString(4, review.getText());
			stReview.setInt(5, pk);

			stReview.executeUpdate();
		} finally {
			closeStatement(stReview);
		}
		rebootDataSet();
	}
}
