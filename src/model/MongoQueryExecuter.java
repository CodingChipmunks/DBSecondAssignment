package model;

import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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

	public static void main(String args[]) throws UnknownHostException {
		Model model = new Model("User42", "");
		MongoQueryExecuter mqe = new MongoQueryExecuter(model);
	}

	public MongoQueryExecuter(Model model) {
		this.model = model;
		try
		{
		MongoCredential credential = MongoCredential.createMongoCRCredential("clientapp", "admin", "password".toCharArray());
		MongoClient mongoClient = new MongoClient(new ServerAddress(), Arrays.asList(credential));
		
		DB db = mongoClient.getDB( "mydb" );
		Set<String> colls = db.getCollectionNames();
		System.out.println(colls.toString());
		DBCollection coll = db.getCollection("testCollection");
		mongoClient.setWriteConcern(WriteConcern.JOURNALED);
		BasicDBObject variablenamesarethislonginjava = new BasicDBObject("name", "MongoDB").append("name", "mDB")
				.append("type", "databases")
				.append("count", 1)
				.append("info", new BasicDBObject("x", 203).append("y", 565));
		coll.insert(variablenamesarethislonginjava);
		
		Cursor somecursorvariabletopointoutsomethingiwanttoread = coll.find();
		
		while (somecursorvariabletopointoutsomethingiwanttoread.hasNext())
		{
			System.out.println(somecursorvariabletopointoutsomethingiwanttoread.next());
		}
		
		
		
		}
		catch (Exception e)
		{
			System.out.println("catch: " + e.toString());
		}
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Album> getAlbumsByGenre(String genre) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Album> getAllAlbums(ResultSet rsetAlbum)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Album> getAlbumsByAny(String title) throws SQLException {
		// TODO Auto-generated method stub
		return null;
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
		return null;
	}

	@Override
	public ArrayList<Album> getAlbumsByUser(String user) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Movie> getMoviesByAny(String queryText)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
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

	}

	@Override
	public void addMedia(String name, String year, String genre,
			Object[] objects, int duration, int mediaType) throws SQLException {
		// TODO Auto-generated method stub

	}

}
