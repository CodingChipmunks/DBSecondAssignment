package model;

import java.util.List;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
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

	public static void main(String args[]) throws UnknownHostException,
			SQLException {
		
		Model model = new Model("User42", "");
		MongoQueryExecuter mqe = new MongoQueryExecuter(model);
		
		mqe.getAlbumsByTitle("Rosenrot");
		mqe.getAlbumsByGenre("Lennst");
		mqe.getAlbumsByRating("3");

		//mqe.peek();
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
			cr = MongoCredential.createMongoCRCredential(user,
					database, pass.toCharArray());
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
			//ArrayList<Album> a = getAlbumsByYear("2009");
			//System.out.println("Found some: " + a.toString());
			//ArrayList<Album> u = getAlbumsByUser("Foo");
			//System.out.println("Found some usr's: " + u.toString());

		} catch (Exception e) {
			System.out.println("catch: " + e.toString());
		}
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
		// called after creation; after queries are executed.
	}

	@Override
	public void open() {
		// called after creation; before queries are executed.
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

		// TODO set last query to title
		// TODO set last query type to Album = 1
		// TODO Create a set of album = no copies can be added (override
		// hashCode & Compare % eqia�s)

		// TODO call every getXByX methods and add result to set.
		// TODO call model.setBank(arraylist<movie>.toArray())

		album.addAll(getAlbumsByTitle(query));
		album.addAll(getAlbumsByYear(query));
		album.addAll(getAlbumsByUser(query));
		album.addAll(getAlbumsByGenre(query));

		model.setBank(album.toArray());
		return new ArrayList<Album>(album);
	}

	@Override
	public ArrayList<Album> getAlbumsByArtist(String artist)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Album> getAlbumsByRating(String rating) throws SQLException {
		// TODO: fixit
		ArrayList<Album> result = new ArrayList<Album>();
		
		DBCollection collection =  db.getCollection("Media");
		DBObject query = new BasicDBObject("Rating", new BasicDBObject("Score", rating));
		System.out.println("QUERY"  + query);
		
		Pattern regex = Pattern.compile(rating);
		query.put("Rating", regex);
		
		// TODO: let db decide how big rating is
		Cursor cursor = collection.find();
		System.out.println("Entering search ...");
		
		// TODO: 2 searches.
		// one to reach rating
		// one for right rating
		
		try {	
			while(cursor.hasNext()) {
				
				System.out.println("Searching by rating " + rating + " ...");
				
				BasicDBObject dbo = (BasicDBObject) cursor.next();
				
				// dbo.get("Score");
				
				// Make Album from result of query
				Album a = Objectifier.cursorToAlbum(dbo);
				
				// TODO: aid by research
				if(a.getRating() >= (float) Integer.parseInt(rating)) {
					// put in list, if rating is greater than desired
					result.add(a);
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
		
		// call last.
		rebootDataSet();
	}

	@Override
	public void rateAlbum(int rating, int media) throws SQLException {
		// TODO Auto-generated method stub
		
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

		List<BasicDBObject> creators = new ArrayList<BasicDBObject>();
		for (int i = 0; i < objects.length; i++) {
			creators.add(new BasicDBObject("Name", objects[i].toString()));
		}
		oneDocument.put("Creator", creators);

		coll.insert(oneDocument);
		rebootDataSet();
	}
}
