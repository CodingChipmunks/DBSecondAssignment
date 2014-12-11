package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	public Model model;

	public static void main(String args[]) {
		try {
			QueryExecuter qx = new QueryExecuter(new Model());
			qx.getAlbumsByAny("Rosenrot");
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

				rsetArtist = stArtist
						.executeQuery("select Name from Contributor inner join Creator where Media_Id = "
								+ album.getId()
								+ " and Creator.id = Contributor.Creator_Id;");

				while (rsetArtist.next())
					album.AddArtist(rsetArtist.getString("Name"));

				// get the rating.
				rsetRating = stRating
						.executeQuery("select avg(Rating) from Rating where Media_Id = "
								+ album.getId() + ";");

				while (rsetRating.next())
					album.setRating(rsetRating.getFloat(1));

				// finally, get reviews..
				rsetReview = stReview
						.executeQuery("select Title, Text, Account_Id from Review where Media_Id = "
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

		} finally {
			closeResultSet(rsetAlbum);
		}

		return albums;
	}

	public void getGenre() throws SQLException {
		try {

		} finally {

		}

	}

	public void getMovie() throws SQLException {
		List<Movie> movies = new ArrayList<Movie>();
		Statement statement;
		ResultSet resultSet;
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
		Statement statement = null;
		ResultSet resultSet;
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
		Statement statement = null;
		ResultSet resultSet;
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
	public ArrayList<Album> getAlbumsByAny(String text) throws SQLException {
		Set<Album> album = new HashSet<Album>();

		  album.addAll(searchByAlbumTitle(text));
		  album.addAll(getAlbumsByRating(text));
		  album.addAll(getAlbumsByArtist(text));
		  album.addAll(searchByGenre(text));

		return new ArrayList<Album>(album);
	}

	@Override
	public ArrayList<Album> getAlbumsByRating(String rating)
			throws SQLException {
		ArrayList<Album> album = new ArrayList<Album>();
		ResultSet rsetAlbum = null;
		Statement stAlbum = connection.createStatement();
		try {
			float ratingF = Float.parseFloat(rating.replace("%", ""));
			rating = rating.replace("%", "");
			rsetAlbum = stAlbum
					.executeQuery("SELECT * FROM Media, Rating WHERE Media.MediaType_Id = 1 "
							+ "AND Media.Id = Rating.Media_Id AND  (SELECT avg(Rating) FROM Rating WHERE Media.Id = "
							+ "Rating.Media_Id) >= "
							+ rating
							+ " GROUP BY Media.Id;");

			album = getAllAlbums(rsetAlbum);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
		} finally {
			listClose(new Statement[] { stAlbum },
					new ResultSet[] { rsetAlbum });
		}
		return album;
	}

	@Override
	public ArrayList<Album> searchByGenre(String genre) throws SQLException {
		ArrayList<Album> album = new ArrayList<Album>();
		ResultSet rsetAlbum = null;
		ResultSet rsetGenre = null;
		Statement stGenre = null;
		Statement stAlbum = null;

		try {
			stGenre = connection.createStatement();
			rsetGenre = stGenre
					.executeQuery("select Id from Genre where Name like '"
							+ genre + "';");

			if (rsetGenre.isBeforeFirst()) {
				rsetGenre.next();
				stAlbum = connection.createStatement();
				rsetAlbum = stAlbum
						.executeQuery("select * from Media where MediaType_Id = 1 and Genre_Id='"
								+ rsetGenre.getInt(1) + "';");

				album = getAllAlbums(rsetAlbum);
			}
		} finally {
			listClose(new Statement[] { stAlbum, stGenre }, new ResultSet[] {
					rsetAlbum, rsetGenre });
		}

		return album;
	}

	@Override
	public ArrayList<Album> searchByAlbumTitle(String title)
			throws SQLException {
		ArrayList<Album> album = new ArrayList<Album>();
		ResultSet rsetAlbum = null;
		Statement stAlbum = connection.createStatement();
		try {
			rsetAlbum = stAlbum
					.executeQuery("select * from Media where Mediatype_Id = 1 and title like '"
							+ title + "'");
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
		Statement stAlbum = connection.createStatement();
		ResultSet rsetAlbum = null;

		try {
			rsetAlbum = stAlbum
					.executeQuery("SELECT *"
							+ " FROM Media, Contributor, Creator WHERE	Media.Id = Contributor.Media_Id AND "
							+ "Contributor.Creator_Id = Creator.Id AND Media.Mediatype_Id = 1 AND Creator.Name LIKE '"
							+ artist + "' GROUP BY Media.Id;");

			album = getAllAlbums(rsetAlbum);
		} finally {
			listClose(new Statement[] { stAlbum },
					new ResultSet[] { rsetAlbum });
		}
		return album;
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
}
