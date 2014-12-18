package model;

import java.util.List;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

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
	private final static String host = "mongodb://";
	private final static String connection = "localhost:27017/";

	private MongoCredential cr = null;
	private MongoClient mc = null;
	private DB db = null;
	private DBCollection coll = null;

	// URI styled
	// host + user + ":" + pass + "@" + connection + database

	public static void main(String args[]) throws UnknownHostException,
			SQLException {
		Model model = new Model("User42", "");
		MongoQueryExecuter mqe = new MongoQueryExecuter(model);
		mqe.addMedia("Lenny Hits", "1998", "Lennstyle", new Object[] {
				"Lenny P", "Lenny K" }, 360, 1);

		mqe.addMedia("Rosenrot", "2005", "Rammstyle",
				new Object[] { "Rammstein" }, 360, 1);

		mqe.peek();
	}

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
		try {
			cr = MongoCredential.createMongoCRCredential("clientapp",
					"mediacollection", "qwerty".toCharArray());
			mc = new MongoClient(new ServerAddress(), Arrays.asList(cr));

			db = mc.getDB("mediacollection");
			// Set<String> colls = db.getCollectionNames();
			// System.out.println(colls.toString());
			coll = db.getCollection("Media");

			mc.setWriteConcern(WriteConcern.JOURNALED);

			/*
			 * BasicDBObject oneAlbum = new BasicDBObject("Title", "Imagine")
			 * .append("Creator", "John Lennon") .append("Genre",
			 * "Lennstyle").append("Year", "2006") .append("Duration", "120");
			 * 
			 * List<BasicDBObject> reviews = new ArrayList<BasicDBObject>();
			 * reviews.add(new BasicDBObject("User", "Bar").append("Its good",
			 * "Excellent!").append("Review", "I liked this album."));
			 * oneAlbum.put("Review", reviews);
			 * 
			 * List<BasicDBObject> rating = new ArrayList<BasicDBObject>();
			 * rating.add(new BasicDBObject("User", "Foo").append("Rating",
			 * "5")); rating.add(new BasicDBObject("User",
			 * "Bar").append("Rating", "5")); oneAlbum.put("Rating", rating);
			 * 
			 * oneAlbum.put("AddedBy", "Bar"); oneAlbum.put("Mediatype",
			 * "Album");
			 * 
			 * coll.insert(oneAlbum);
			 */

			// Cursor fetchAll = coll.find();

			// rough peek
			// while (fetchAll.hasNext()) {
			// System.out.println(fetchAll.next());
			// }

			// specified peek

			
			// tst search
			ArrayList<Album> a = getAlbumsByYear("2009");
			System.out.println("Found some: " + a.toString());
			
		} catch (Exception e) {
			System.out.println("catch: " + e.toString());
		}
	}

	@Override
	public void disconnect() {

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

		return null;
	}

	@Override
	public ArrayList<Album> getAlbumsByGenre(String genre) throws SQLException {

		return null;
	}

	@Override
	public ArrayList<Album> getAllAlbums(ResultSet rsetAlbum)
			throws SQLException {

		return null;
	}

	@Override
	public ArrayList<Album> getAlbumsByAny(String title) throws SQLException {
		// TODO set last query to title
		// TODO set last query type to Album = 1
		// TODO Create a set of album = no copies can be added (override
		// hashCode & Compare % eqia�s)

		// TODO call every getXByX methods and add result to set.
		// TODO call model.setBank(arraylist<movie>.toArray())

		return null; // return set.toArrayList ? :o
	}

	@Override
	public ArrayList<Album> getAlbumsByArtist(String artist)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Album> getAlbumsByRating(String rating)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Album> getAlbumsByYear(String year) throws SQLException {
		// TODO Auto-generated method stub
		ArrayList<Album> result = new ArrayList<Album>();
		
		DBCollection collection =  db.getCollection("Media");
		DBObject query = new BasicDBObject("Year", year);
		Cursor cursor = collection.find(query);
		System.out.println("Entering search ...");
		while(cursor.hasNext()) {
			System.out.println("Searching by year " + year + " ...");
			BasicDBObject dbo = (BasicDBObject) cursor.next();
			// Make Album from result of query
			Album a = Objectifier.cursorToAlbum(dbo);
			//System.out.println(a.toString());
			
			// put in list
			result.add(a);
		}
		System.out.println("Done searching ...");
		
		// DEBUG: print them out
		
		
		return result;
	}

	@Override
	public ArrayList<Album> getAlbumsByUser(String user) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Movie> getMoviesByAny(String queryText)
			throws SQLException {
		// TODO set last query to title
		// TODO set last query type to Movie = 2
		// TODO Create a set of album = no copies can be added (override
		// hashCode & Compare % eqia�s)

		// TODO call every getXByX methods and add result to set.
		// TODO call model.setBank(arraylist<movie>.toArray())

		return null; // return set.toArrayList ? :o
	}

	@Override
	public ArrayList<Movie> getMovieByUser(String user) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Movie> getAllMovies(ResultSet rsetMovie)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Movie> getMovieByYear(String year) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Movie> getMovieByTitle(String title) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Movie> getMovieByRating(String rating) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Movie> getMovieByDirector(String director)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Movie> getMovieByGenre(String genre) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void getReviewsByAny(String queryText) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void reviewMedia(Review review, int pk) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void rateAlbum(int rating, int media) throws SQLException {
		// TODO Auto-generated method stub

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
	private DB db;
	 *            an array of creators.
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

		List<BasicDBObject> creators = new ArrayList<BasicDBObject>();
		for (int i = 0; i < objects.length; i++) {
			creators.add(new BasicDBObject("Name", objects[i].toString()));
		}
		oneDocument.put("Creator", creators);

		coll.insert(oneDocument);
	}

	@Override
	public void open() {
		// TODO Auto-generated method stub

	}
}
