package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import controller.Controller.QueryType;

public class MongoQueryExecuter implements QueryInterpreter {
	private Model model;
	private static String lastQuery;
	private static QueryType lastQueryType;

	private final static String database = "mediacollection";
	private final static String user = "clientapp";
	private final static String pass = "qwerty";

	private MongoCredential cr = null;
	private MongoClient mc = null;
	private DB db = null;
	private DBCollection coll = null;

	// TODO remove
	public void peek() {
		Cursor fetchAll = coll.find();
		// rough peek
		while (fetchAll.hasNext()) {
			System.out.println(fetchAll.next());
		}
	}

	public MongoQueryExecuter(Model model) {
		this.model = model;
		this.open();
	}

	// run this after rating/review/add
	private void rebootDataSet() throws SQLException {
		switch (getLastQueryType()) {
		case ALBUMSEARCH:
			model.setBank(getAlbumsByAny(getLastQuery()).toArray());
			System.out.println("ALBUMSEARCH = " + getLastQuery());
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
	public void disconnect() {
		mc.close();
	}

	@Override
	public void open() {
		try {
			cr = MongoCredential.createMongoCRCredential(user, database,
					pass.toCharArray());
			mc = new MongoClient(new ServerAddress(), Arrays.asList(cr));
			db = mc.getDB("mediacollection");
			coll = db.getCollection("Media");
			mc.setWriteConcern(WriteConcern.JOURNALED);

		} catch (Exception e) {
			System.out.println("catch: " + e.toString());
		}
	}

	private synchronized void setLastQuery(String lastQuery, QueryType queryType) {
		MongoQueryExecuter.lastQuery = lastQuery;
		MongoQueryExecuter.lastQueryType = queryType;
	}

	private synchronized String getLastQuery() {
		return MongoQueryExecuter.lastQuery;
	}

	private synchronized QueryType getLastQueryType() {
		return MongoQueryExecuter.lastQueryType;
	}

	@Override
	public ArrayList<Album> getAlbumsByTitle(String title) throws SQLException {
		ArrayList<Album> result = new ArrayList<Album>();
		DBCollection collection = db.getCollection("Media");

		DBObject query = new BasicDBObject("Title", title);
		Pattern regex = Pattern.compile(title);
		query.put("Title", regex);

		/* --------------- ADDED MEDIATYPE LIMITER ---------------------- */
		DBObject mediatype = new BasicDBObject("Mediatype", "Album");
		BasicDBList and = new BasicDBList();
		and.add(mediatype);
		and.add(query);
		query = new BasicDBObject("$and", and);
		/* -------------- END MEDIATYPE LIMITER ------------------------ */

		Cursor cursor = collection.find(query);
		System.out.println("Searching by title " + title + " ...");
		while (cursor.hasNext()) {
			BasicDBObject dbo = (BasicDBObject) cursor.next();
			Album a = Objectifier.cursorToAlbum(dbo);
			result.add(a);
		}
		return result;
	}

	@Override
	public ArrayList<Album> getAlbumsByGenre(String genre) throws SQLException {
		ArrayList<Album> result = new ArrayList<Album>();
		DBCollection collection = db.getCollection("Media");
		DBObject query = new BasicDBObject("Genre", genre);
		Pattern regex = Pattern.compile(genre);
		query.put("Genre", regex);

		/* --------------- ADDED MEDIATYPE LIMITER ---------------------- */
		DBObject mediatype = new BasicDBObject("Mediatype", "Album");
		BasicDBList and = new BasicDBList();
		and.add(mediatype);
		and.add(query);
		query = new BasicDBObject("$and", and);
		/* -------------- END MEDIATYPE LIMITER ------------------------ */

		Cursor cursor = collection.find(query);
		System.out.println("Searching by genre " + genre + " ...");
		while (cursor.hasNext()) {
			BasicDBObject dbo = (BasicDBObject) cursor.next();
			Album a = Objectifier.cursorToAlbum(dbo);
			result.add(a);
		}
		System.out.println(result);
		return result;
	}

	@Override
	public ArrayList<Album> getAllAlbums(ResultSet rsetAlbum)
			throws SQLException {
		ArrayList<Album> albums = new ArrayList<Album>();

		Cursor cr = coll.find();

		while (cr.hasNext()) {
			cr.next();
		}

		return albums;
	}

	@Override
	public ArrayList<Album> getAlbumsByAny(String query) throws SQLException {
		query = query.replace("%", "");
		System.out.println("Query For Albums LIKE " + query);
		Set<Album> album = new HashSet<Album>();
		setLastQuery(query, QueryType.ALBUMSEARCH);

		// TODO add AlbumByArtist

		album.addAll(getAlbumsByTitle(query));
		album.addAll(getAlbumsByYear(query));
		album.addAll(getAlbumsByUser(query));
		album.addAll(getAlbumsByGenre(query));
		album.addAll(getAlbumsByRating(query));
		album.addAll(getAlbumsByArtist(query));

		model.setBank(album.toArray());
		return new ArrayList<Album>(album);
	}

	@Override
	public ArrayList<Album> getAlbumsByArtist(String artist)
			throws SQLException {
		// TODO: fixit
		ArrayList<Album> result = new ArrayList<Album>();

		DBCollection collection = db.getCollection("Media");
		DBObject query = new BasicDBObject("Creator", artist);
		System.out.println("QUERY" + query);

		Pattern regex = Pattern.compile(artist);
		query.put("Creator", regex);

		// TODO: let db decide how big rating is
		Cursor cursor = collection.find();
		System.out.println("Entering search ...");

		// TODO: more effective searches

		try {
			while (cursor.hasNext()) {

				System.out.println("Searching by artist " + artist + " ...");

				BasicDBObject dbo = (BasicDBObject) cursor.next();

				if (dbo.getString("Mediatype").equals("Album")) {

					// dbo.get("Score");

					// Make Album from result of query
					Album a = Objectifier.cursorToAlbum(dbo);

					// TODO: aid by research
					ArrayList<Artist> tmp = a.getArtist();

					// mm aj lajk de speed of this database
					for (Artist artists : tmp) {
						if (artists.getName().contains(artist)) {
							// put in list, if rating is greater than desired
							result.add(a);
						}
					}
				}
			}
			System.out.println(result);
			return result;
		} finally {
			cursor.close();
		}
	}

	@Override
	public ArrayList<Album> getAlbumsByRating(String rating)
			throws SQLException {
		// TODO: fixit
		ArrayList<Album> result = new ArrayList<Album>();

		DBCollection collection = db.getCollection("Media");
		DBObject query = new BasicDBObject("Rating", new BasicDBObject("Score",
				rating));
		System.out.println("QUERY" + query);

		Pattern regex = Pattern.compile(rating);
		query.put("Rating", regex);

		// TODO: let db decide how big rating is
		Cursor cursor = collection.find();
		System.out.println("Entering search ...");

		// TODO: 2 searches.
		// one to reach rating
		// one for right rating

		try {
			while (cursor.hasNext()) {

				System.out.println("Searching by rating " + rating + " ...");

				BasicDBObject dbo = (BasicDBObject) cursor.next();

				if (dbo.getString("Mediatype").equals("Album")) {
					// dbo.get("Score");

					// Make Album from result of query
					Album a = Objectifier.cursorToAlbum(dbo);

					try {
						// TODO: aid by research
						if (a.getRating() >= (float) Integer.parseInt(rating)) {
							// put in list, if rating is greater than desired
							result.add(a);
						}
					} catch (NumberFormatException e) {
					}
				}
			}
			System.out.println(result);
			return result;
		} finally {
			cursor.close();
		}
	}

	@Override
	public ArrayList<Album> getAlbumsByYear(String year) throws SQLException {
		// TODO ADD TRYCATCH FOR CORRECT EXCEPTION researching API
		ArrayList<Album> result = new ArrayList<Album>();

		DBCollection collection = db.getCollection("Media");
		DBObject query = new BasicDBObject("Year", year);
		Pattern regex = Pattern.compile(year);
		query.put("Year", regex);

		/* --------------- ADDED MEDIATYPE LIMITER ---------------------- */
		DBObject mediatype = new BasicDBObject("Mediatype", "Album");
		BasicDBList and = new BasicDBList();
		and.add(mediatype);
		and.add(query);
		query = new BasicDBObject("$and", and);
		/* -------------- END MEDIATYPE LIMITER ------------------------ */

		Cursor cursor = collection.find(query);
		System.out.println("Searching by year " + year + " ...");
		while (cursor.hasNext()) {
			BasicDBObject dbo = (BasicDBObject) cursor.next();
			// Make Album from result of query
			Album a = Objectifier.cursorToAlbum(dbo);
			// System.out.println(a.toString());

			// put in list
			result.add(a);
		}
		System.out.println("Done searching ...");

		// DEBUG: print them out

		return result;
	}

	@Override
	public ArrayList<Album> getAlbumsByUser(String user) throws SQLException {
		ArrayList<Album> result = new ArrayList<Album>();
		try {
			DBCollection collection = db.getCollection("Media");
			DBObject query = new BasicDBObject("AddedBy", user);
			Pattern regex = Pattern.compile(user);
			query.put("AddedBy", regex);

			/* --------------- ADDED MEDIATYPE LIMITER ---------------------- */
			DBObject mediatype = new BasicDBObject("Mediatype", "Album");
			BasicDBList and = new BasicDBList();
			and.add(mediatype);
			and.add(query);
			query = new BasicDBObject("$and", and);
			/* -------------- END MEDIATYPE LIMITER ------------------------ */

			Cursor cursor = collection.find(query);
			System.out.println("Searching by user " + user + " ...");
			while (cursor.hasNext()) {
				BasicDBObject dbo = (BasicDBObject) cursor.next();
				// Make Album from result of query
				Album a = Objectifier.cursorToAlbum(dbo);
				// System.out.println(a.toString());

				// put in list
				result.add(a);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public ArrayList<Movie> getMoviesByAny(String query) throws SQLException {
		query = query.replace("%", "");
		System.out.println("Query For Albums LIKE " + query);
		Set<Movie> movie = new HashSet<Movie>();
		setLastQuery(query, QueryType.MOVIESEARCH);

		// TODO add AlbumByArtist

		movie.addAll(getMovieByTitle(query));
		movie.addAll(getMovieByYear(query));
		movie.addAll(getMovieByUser(query));
		movie.addAll(getMovieByGenre(query));
		movie.addAll(getMovieByRating(query));
		movie.addAll(getMovieByDirector(query));

		model.setBank(movie.toArray());
		return new ArrayList<Movie>(movie);
	}

	@Override
	public ArrayList<Movie> getMovieByUser(String user) throws SQLException {
		ArrayList<Movie> result = new ArrayList<Movie>();
		try {
			DBCollection collection = db.getCollection("Media");
			DBObject query = new BasicDBObject("AddedBy", user);
			Pattern regex = Pattern.compile(user);
			query.put("AddedBy", regex);

			/* --------------- ADDED MEDIATYPE LIMITER ---------------------- */
			DBObject mediatype = new BasicDBObject("Mediatype", "Movie");
			BasicDBList and = new BasicDBList();
			and.add(mediatype);
			and.add(query);
			query = new BasicDBObject("$and", and);
			/* -------------- END MEDIATYPE LIMITER ------------------------ */

			Cursor cursor = collection.find(query);
			System.out.println("Searching by user " + user + " ...");
			while (cursor.hasNext()) {
				BasicDBObject dbo = (BasicDBObject) cursor.next();
				// Make Album from result of query
				Movie m = Objectifier.cursorToMovie(dbo);
				// System.out.println(a.toString());

				// put in list
				result.add(m);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public ArrayList<Movie> getAllMovies(ResultSet rsetMovie)
			throws SQLException {
		ArrayList<Movie> movies = new ArrayList<Movie>();

		Cursor cr = coll.find();

		while (cr.hasNext()) {
			cr.next();
		}

		return movies;
	}

	@Override
	public ArrayList<Movie> getMovieByYear(String year) throws SQLException {
		// TODO ADD TRYCATCH FOR CORRECT EXCEPTION researching API
		ArrayList<Movie> result = new ArrayList<Movie>();

		DBCollection collection = db.getCollection("Media");
		DBObject query = new BasicDBObject("Year", year);
		Pattern regex = Pattern.compile(year);
		query.put("Year", regex);

		/* --------------- ADDED MEDIATYPE LIMITER ---------------------- */
		DBObject mediatype = new BasicDBObject("Mediatype", "Movie");
		BasicDBList and = new BasicDBList();
		and.add(mediatype);
		and.add(query);
		query = new BasicDBObject("$and", and);
		/* -------------- END MEDIATYPE LIMITER ------------------------ */

		Cursor cursor = collection.find(query);
		System.out.println("Searching by year " + year + " ...");
		while (cursor.hasNext()) {
			BasicDBObject dbo = (BasicDBObject) cursor.next();
			// Make Album from result of query
			Movie m = Objectifier.cursorToMovie(dbo);
			// System.out.println(a.toString());

			// put in list
			result.add(m);
		}
		System.out.println("Done searching ...");

		// DEBUG: print them out

		return result;
	}

	@Override
	public ArrayList<Movie> getMovieByTitle(String title) throws SQLException {
		ArrayList<Movie> result = new ArrayList<Movie>();
		DBCollection collection = db.getCollection("Media");

		DBObject query = new BasicDBObject("Title", title);
		Pattern regex = Pattern.compile(title);
		query.put("Title", regex);

		/* --------------- ADDED MEDIATYPE LIMITER ---------------------- */
		DBObject mediatype = new BasicDBObject("Mediatype", "Movie");
		BasicDBList and = new BasicDBList();
		and.add(mediatype);
		and.add(query);
		query = new BasicDBObject("$and", and);
		/* -------------- END MEDIATYPE LIMITER ------------------------ */

		Cursor cursor = collection.find(query);
		System.out.println("Searching by title " + title + " ...");
		while (cursor.hasNext()) {
			BasicDBObject dbo = (BasicDBObject) cursor.next();
			Movie a = Objectifier.cursorToMovie(dbo);
			result.add(a);
		}
		return result;
	}

	@Override
	public ArrayList<Movie> getMovieByRating(String rating) throws SQLException {
		ArrayList<Movie> result = new ArrayList<Movie>();

		DBCollection collection = db.getCollection("Media");
		DBObject query = new BasicDBObject("Rating", new BasicDBObject("Score",
				rating));
		System.out.println("QUERY" + query);

		Pattern regex = Pattern.compile(rating);
		query.put("Rating", regex);

		// TODO: let db decide how big rating is
		Cursor cursor = collection.find();
		System.out.println("Entering search ...");

		// TODO: 2 searches.
		// one to reach rating
		// one for right rating

		try {
			while (cursor.hasNext()) {
				System.out.println("Searching by rating " + rating + " ...");

				BasicDBObject dbo = (BasicDBObject) cursor.next();

				if (dbo.getString("Mediatype").equals("Movie")) {
					// dbo.get("Score");

					// Make Album from result of query
					Movie a = Objectifier.cursorToMovie(dbo);

					try {
						// TODO: aid by research
						if (a.getRating() >= (float) Integer.parseInt(rating)) {
							// put in list, if rating is greater than desired
							result.add(a);
						}
					} catch (NumberFormatException e) {
					}
				}
			}
			System.out.println(result);
			return result;
		} finally {
			cursor.close();
		}
	}

	@Override
	public ArrayList<Movie> getMovieByDirector(String director)
			throws SQLException {
		ArrayList<Movie> result = new ArrayList<Movie>();

		DBCollection collection = db.getCollection("Media");
		DBObject query = new BasicDBObject("Creator", "...");
		System.out.println("QUERY" + query);

		Pattern regex = Pattern.compile(director);
		query.put("Creator", regex);

		// TODO: let db decide how big rating is
		Cursor cursor = collection.find();
		System.out.println("Entering search ...");

		// TODO: more effective searches

		try {
			while (cursor.hasNext()) {

				System.out.println("Searching by artist " + director + " ...");

				BasicDBObject dbo = (BasicDBObject) cursor.next();
				if (dbo.getString("Mediatype").equals("Movie")) {

					// dbo.get("Score");

					// Make Album from result of query
					Movie d = Objectifier.cursorToMovie(dbo);

					// TODO: aid by research
					ArrayList<Director> tmp = d.getDirector();

					// mm aj lajk de speed of this database
					for (Director directors : tmp) {
						if (directors.getName().contains(director)) {
							// put in list, if rating is greater than desired
							result.add(d);
						}
					}
				}
			}
			System.out.println(result);
			return result;
		} finally {
			cursor.close();
		}
	}

	@Override
	public ArrayList<Movie> getMovieByGenre(String genre) throws SQLException {
		ArrayList<Movie> result = new ArrayList<Movie>();
		DBCollection collection = db.getCollection("Media");
		DBObject query = new BasicDBObject("Genre", genre);
		Pattern regex = Pattern.compile(genre);
		query.put("Genre", regex);

		/* --------------- ADDED MEDIATYPE LIMITER ---------------------- */
		DBObject mediatype = new BasicDBObject("Mediatype", "Movie");
		BasicDBList and = new BasicDBList();
		and.add(mediatype);
		and.add(query);
		query = new BasicDBObject("$and", and);
		/* -------------- END MEDIATYPE LIMITER ------------------------ */

		Cursor cursor = collection.find(query);
		System.out.println("Searching by genre " + genre + " ...");
		while (cursor.hasNext()) {
			BasicDBObject dbo = (BasicDBObject) cursor.next();
			Movie a = Objectifier.cursorToMovie(dbo);
			result.add(a);
		}
		System.out.println(result);
		return result;
	}

	@Override
	public void getReviewsByAny(String queryText) throws SQLException {
		queryText = queryText.replace("%", "");
		ArrayList<Review> reviews = new ArrayList<Review>();
		DBCollection collection = db.getCollection("Media");
		Pattern regex = Pattern.compile(queryText);

		DBObject st1 = new BasicDBObject("Review.Title", regex);
		DBObject st2 = new BasicDBObject("Review.Text", regex);
		DBObject st3 = new BasicDBObject("Review.User", regex);
		DBObject st4 = new BasicDBObject("Mediatype", regex);
		DBObject st5 = new BasicDBObject("Title", regex);
		BasicDBList or = new BasicDBList();
		or.add(st1);
		or.add(st2);
		or.add(st3);
		or.add(st4);
		or.add(st5);
		DBObject query = new BasicDBObject("$or", or);

		Cursor cursor = collection.find(query);
		System.out.println("Searching by Review " + queryText + " ...");
		while (cursor.hasNext()) {
			BasicDBObject dbo = (BasicDBObject) cursor.next();
			reviews.addAll(Objectifier.cursorToReview(dbo, queryText));
		}

		System.out.println("Searching by review...");
		model.setBank(reviews.toArray());
	}

	@Override
	public void reviewMedia(Review review, String pk) throws SQLException {
		// get reviewer

		DBObject findQuery = new BasicDBObject();
		findQuery.put("_id", new ObjectId(pk));

		DBObject listItem = new BasicDBObject("Review", new BasicDBObject(
				"Title", review.getTitle()).append("Text", review.getText())
				.append("User", model.getUser()));
		DBObject updateQuery = new BasicDBObject("$push", listItem);
		coll.update(findQuery, updateQuery, true, false);
		rebootDataSet();

	}

	@Override
	public void rateAlbum(int rating, String pk) throws SQLException {
		// TODO Auto-generated method stub
		DBObject findQuery = new BasicDBObject();
		findQuery.put("_id", new ObjectId(pk));

		DBObject listItem = new BasicDBObject("Rating", new BasicDBObject(
				"Score", rating).append("User", model.getUser()));
		DBObject updateQuery = new BasicDBObject("$push", listItem);
		coll.update(findQuery, updateQuery, true, false);
		// call last.
		rebootDataSet();
	}

	@Override
	public void verifyAccount(String user, String pass) throws SQLException {
		// TODO check that user is longer than 4 chars, do not attempt to
		// verify the account, it cannot be done securely.

		if (user.length() > 3)
			model.setValidAccount(true);
		else
			model.setValidAccount(false);
	}

	/***
	 * @param objects
	 *            private DB db; an array of creators.
	 ***/
	@Override
	public void addMedia(String name, String year, String genre,
			Object[] objects, int duration, int mediaType) throws SQLException {

		BasicDBObject oneDocument = new BasicDBObject("Title", name)
				.append("Genre", genre).append("Year", year)
				.append("Duration", duration);

		oneDocument.put("AddedBy", model.getUser());

		switch (mediaType) {
		case 1:
			oneDocument.put("Mediatype", "Album");
			break;
		case 2:
			oneDocument.put("Mediatype", "Movie");
			break;
		}

		BasicDBList listItem = new BasicDBList();

		for (int i = 0; i < objects.length; i++)
			listItem.add(new BasicDBObject("Name", objects[i].toString()));

		oneDocument.put("Creator", listItem);

		coll.insert(oneDocument);
		rebootDataSet();
	}
}
