package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
	private final static String host = "jdbc:mysql://localhost:3306/";

	public Connection connection;
	public Statement statement;
	public PreparedStatement preparedStatement;
	public ResultSet resultSet;
	public Model model;

	public static void main(String args[]) {
		try {
			QueryExecuter qx = new QueryExecuter(new Model());
			qx.getAllAlbums("Rammstein");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public QueryExecuter(Model model) throws SQLException {
		this.model = model;

		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(host + database, user,
					pass);
			statement = connection.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void disconnect() {
		try {
			if (connection != null) {
				connection.close();
				System.out.println("Connection closed.");
			}
		} catch (SQLException e) {
		}
	}

	@Override
	public List<Album> getAllAlbums(String search) throws SQLException {
		List<Album> albums = new ArrayList<Album>();
		ResultSet rsetAlbum = null;

		try {
			rsetAlbum = statement
					.executeQuery("select * from Media where Mediatype_Id = 1 and title like '"
							+ search + "'"); // 1

			// for every album do...
			while (rsetAlbum.next()) {
				ResultSet rsetGenre;
				ResultSet rsetArtist;
				ResultSet rsetRating;
				ResultSet rsetReview;
				ResultSet rsetUser;
				ResultSet rsetId = null;
				Statement stId = connection.createStatement();
				Statement stUser = connection.createStatement();
				Statement stGenre = connection.createStatement();
				Statement stArtist = connection.createStatement();
				Statement stRating = connection.createStatement();
				Statement stReview = connection.createStatement();
				Album album = RowConverter.convertRowToAlbum(rsetAlbum);

				// get genre.
				rsetGenre = stGenre
						.executeQuery("select Name from Genre where Id = "
								+ rsetAlbum.getInt("Genre_Id"));
				rsetGenre.first();
				album.setGenre(rsetGenre.getString("Name"));

				// get artists.
				System.out.println("Id is: " + album.getId());

				rsetArtist = stArtist
						.executeQuery("select Name from Contributor inner join Creator where Media_Id = "
								+ album.getId()
								+ " and Creator.id = Contributor.Creator_Id;");

				while (rsetArtist.next())
					album.AddArtist(rsetArtist.getString("Name"));

				// get the rating.
				rsetRating = stRating
						.executeQuery("select avg(Rating) from rating where Media_Id = "
								+ album.getId() + ";");

				while (rsetRating.next())
					album.setRating(rsetRating.getFloat(1));

				// finally, get reviews..
				rsetReview = stReview
						.executeQuery("select Title, Text, Account_Id from review where Media_Id = "
								+ album.getId() + ";");

				while (rsetReview.next()) {
					Review review = RowConverter.convertRowToReview(rsetReview);

					rsetUser = stUser
							.executeQuery("select Name from Account where Id = "
									+ rsetReview.getInt("Account_Id") + ";");
					rsetUser.first();
					review.setUser(rsetUser.getString("Name"));
					album.addReview(review);
				}

				// ops, need user too..
				rsetUser = stUser
						.executeQuery("select Name from Account where Id = "
								+ rsetAlbum.getInt("Account_Id"));
				rsetUser.first();
				album.setUser(rsetUser.getString("Name"));

				albums.add(album);

				// ha-ha
				listClose(new Statement[] { stId, stUser, stGenre, stArtist,
						stRating, stReview }, new ResultSet[] { rsetGenre,
						rsetArtist, rsetRating, rsetReview, rsetUser, rsetId });
			}

			model.setBank(albums.toArray());

			for (Album a : albums) {
				System.out.println(a.toString());
			}

		} finally {
			closeStatement(statement);
			closeResultSet(rsetAlbum);
		}

		return null;
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

	// more research...
	public List<Album> getAllAlbums2() throws SQLException {
		List<Album> albums = new ArrayList<Album>();

		// make statement and result set
		Statement s = null;

		Statement sAlbum = null;
		Statement sArtist = null;
		Statement sReview = null;
		Statement sRating = null;

		ResultSet rAlbum = null;
		ResultSet rArtist = null;
		ResultSet rReview = null;
		ResultSet rRating = null;

		try {
			// use statement and result set to fetch info
			s = connection.createStatement();

			sAlbum = connection.createStatement();
			sArtist = connection.createStatement();
			sReview = connection.createStatement();
			sRating = connection.createStatement();

			// get info
			rAlbum = sAlbum.executeQuery("select * " + "from Media "
					+ "where Mediatype_Id = 1");

			rArtist = sArtist.executeQuery("select Creator.Name "
					+ "from Contributor, Creator, Media "
					+ "where Contributor.Creator_Id = Media.Id "
					+ "and Media.Mediatype_Id = 1");

			rReview = sReview.executeQuery("select * " + "from Review, Media "
					+ "where Review.Media_Id = Media.Id");

			rRating = sRating.executeQuery("select * " + "from Rating, Media "
					+ "where Rating.Media_Id = Media.Id");

			// loop through result set
			while (rAlbum.next()) {
				albums.add(RowConverter.convertRowToAlbum(rAlbum, rArtist,
						rReview, rRating));
			}

			// System.out.println("Albums");
			// return list

			for (Album a : albums) {
				System.out.println(a.toString());
			}

		} finally {
			// closeStatementAndResultSet(s, r);
		}

		return null;
	}

	public void getGenre() throws SQLException {
		try {

		} finally {

		}

	}

	public void getMovie() throws SQLException {
		List<Movie> movies = new ArrayList<Movie>();
		try {
			// use statement and result set to fetch info
			statement = connection.createStatement();
			resultSet = statement
					.executeQuery("select * from Media where Mediatype_Id = 2");

			// loop through result set
			while (resultSet.next()) {
				System.out.println(resultSet.getString("Name"));
				// movies = rc.convertRowToMovie(resultSet);
			}

			// System.out.println("movies");
			for (Movie m : movies) {
				System.out.println(m.toString());
			}
		} finally {

		}
	}

	public List<Director> getDirectors() throws SQLException {
		List<Director> directors = new ArrayList<Director>();

		try {
			// use statement and result set to fetch info
			statement = connection.createStatement();
			resultSet = statement.executeQuery("...coming soon...");

			// loop through result set
			while (resultSet.next()) {
				System.out.println(resultSet.getString("Name"));
				directors = RowConverter.convertRowToDirector(resultSet);
			}

			System.out.println("Directors");
			for (Director d : directors) {
				System.out.println(d.toString());
			}

			return directors;

		} finally {
			closeStatement(statement);
		}
	}

	public List<Artist> getArtists() throws SQLException {
		List<Artist> artists = new ArrayList<Artist>();

		try {
			// use statement and result set to fetch info
			statement = connection.createStatement();
			// MMm such good when directors in dat table...
			resultSet = statement
					.executeQuery("select Name from Creator, Contributor where Id = Creator_Id");

			// loop through result set
			while (resultSet.next()) {
				System.out.println(resultSet.getString("Name"));
				artists = RowConverter.convertRowToArtist(resultSet);
			}

			System.out.println("Artists");
			for (Artist a : artists) {
				System.out.println(a.toString());
			}

			return artists;

		} finally {
			closeStatement(statement);
		}
	}

	@Override
	public List<Album> searchByAlbumTitle(String title) {
		List<Album> resultingAlbum = new ArrayList<Album>();

		// make statement and result set

		// TODO define sql query
		try {

			// statement with WHERE clause, prepare it correctly using
			// PArameters

			// loop through result set adding objects to list

			// return list
		} finally {
			// close statement and result set
		}
		return null;
	}

	@Override
	public List<Artist> searchByArtist(String artist) throws SQLException {
		List<Artist> resultingArtists = new ArrayList<Artist>();

		try {
			artist = artist.trim();
			artist += "%";
			// first make sure there is no directors!

			// Search for designated artist
			preparedStatement = connection
					.prepareStatement("select Name from Creator where Name like ?");

			preparedStatement.setString(1, artist);
			resultSet = preparedStatement.executeQuery();

			// loop through result set
			while (resultSet.next()) {
				// resultingArtists = rc.convertRowToArtist(resultSet);
				resultingArtists.addAll(RowConverter
						.convertRowToArtist(resultSet));
			}

			if (resultingArtists.size() > 0) {
				System.out.println("Found Artists: ");
				// NOTHING! but it should work...
				// Char encodin issue?
				for (Artist a : resultingArtists) {
					System.out.println(a.toString());
				}
			}
			return resultingArtists;

		} finally {
			// closeStatementAndResultSet();
		}
	}

	@Override
	public List<Album> searchByGenre(String genre) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Album> searchByRating(int rating) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insertAlbum(Album album) {
		// CALLING METHOD: show dialog

		// CALLING METHOD: supply result as album

		// make statement

		// try to execute that statement to db
	}

	@Override
	public void rateAlbum(int rating) {
		// TODO Auto-generated method stub

	}

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
}
