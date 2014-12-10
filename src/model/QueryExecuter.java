package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * All db interaction code resides here, 
 * could be called directly by view, 
 * or by view through intermediary layer of
 * Model. (more about this in QUESTIONS.txt)
 * 
 * This class is a translator between the ER and Object model
 * and vice versa.
 * 
 * @author softish, 
 *
 */
public class QueryExecuter implements QueryInterpreter {
	private final static String database = "mediacollection";
	private final static String user = "clientapp";
	private final static String pass= "qwerty";
	private final static String driver = "com.mysql.jdbc.Driver";
	private final static String host = "jdbc:mysql://localhost:3306/";
	
	private Connection connection;
	private Statement statement;
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;
	
	private RowConverter rc;
	
	public static void main(String args[])
	{
		try { 
			QueryExecuter lols = new QueryExecuter();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public QueryExecuter() throws SQLException {
		// enabling conversion form relational to object model
		rc = new RowConverter();
		
		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(host + database, user, pass);
			
			Statement s = connection.createStatement();
			ResultSet r = s.executeQuery("select Name from Account");
			
			while(r.next()){
				System.out.println(r.getString("Name"));
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		try {
			getAllAlbums();
			//getDirectors();
			//searchByAlbumTitle("R");
			searchByArtist("R");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		disconnect();
	}


	@Override
	public void disconnect() {
    	try {
    		if(connection != null) {
    			connection.close();
    			System.out.println("Connection closed.");
    		}
    	} 
    	catch(SQLException e) {}
	}

	@Override
	public List<Album> getAllAlbums() throws SQLException {
		List<Album> albums = new ArrayList<Album>();

		// make statement and result set
		Statement s = null;
		ResultSet r = null;

		// Result 
		
		// definition of sql query
		try {
			// use statement and result set to fetch info
			s = connection.createStatement();
			// get basic album info
			// Artist name will be missing
			r = s.executeQuery("select * from Media where Mediatype_Id = 1");	// 1 == Album
			
			// get Genre of album
			r = s.executeQuery("select Name from Genre where Id = 3");
			// get name of Media
			//r = s.executeQuery("select Name from Mediatype where Id = 1");
			
			// Account info?
			
			//rc.convertRowToAlbum(mediaRow, creatorRow, reviewRow, ratingRow)
			getArtists();
			
			// loop through result set
			while(r.next()){
				System.out.println(r.getString("Name"));
				// convert row to Album and add to list: preferably with helper class
				//rc.convertRowToAlbum(albumRow, artistRow, reviewRow, ratingRow)
			}
				
//			System.out.println("Albums");
			//return list
			
		}
		finally {
			closeStatementAndResultSet(s,r);
		}
		
		
		return null;
	}

	public void getGenre() throws SQLException {
		try {

		}
		finally {

		}
		
	}

	public void getMovie() throws SQLException {
		List<Movie> movies = new ArrayList<Movie>();
		try {
			// use statement and result set to fetch info
			statement = connection.createStatement();
			resultSet = statement.executeQuery("select * from Media where Mediatype_Id = 2");
			
			
			// loop through result set
			while(resultSet.next()){
				System.out.println(resultSet.getString("Name"));
				//movies = rc.convertRowToMovie(resultSet);
			}
			
//			System.out.println("movies");
			for (Movie m : movies) {
				System.out.println(m.toString());
			}
		}
		finally {

		}
	}

	public List<Director> getDirectors() throws SQLException {
		List<Director> directors = new ArrayList<Director>();

		try {
			// use statement and result set to fetch info
			statement = connection.createStatement();
			resultSet = statement.executeQuery("...coming soon...");
			
			
			// loop through result set
			while(resultSet.next()){
				System.out.println(resultSet.getString("Name"));
				directors = rc.convertRowToDirector(resultSet);
			}
			
			System.out.println("Directors");
			for (Director d : directors) {
				System.out.println(d.toString());
			}
			
			return directors;
			
		}
		finally {
			closeStatementAndResultSet();
		}
	}
	
	public List<Artist> getArtists() throws SQLException {
		List<Artist> artists = new ArrayList<Artist>();

		try {
			// use statement and result set to fetch info
			statement = connection.createStatement();
			// MMm such good when directors in dat table...
			resultSet = statement.executeQuery("select Name from Creator, Contributor where Id = Creator_Id");
			
			
			// loop through result set
			while(resultSet.next()){
				System.out.println(resultSet.getString("Name"));
				artists = rc.convertRowToArtist(resultSet);
			}
			
			System.out.println("Artists");
			for (Artist a : artists) {
				System.out.println(a.toString());
			}
			
			return artists;
			
		}
		finally {
			closeStatementAndResultSet();
		}
	}

	@Override
	public List<Album> searchByAlbumTitle(String title) {
		List<Album> resultingAlbum = new ArrayList<Album>();
		
		// make statement and result set
		
		// TODO define sql query
		try {
			
			// statement with WHERE clause, prepare it correctly using PArameters
			
			// loop through result set adding objects to list 
			
			// return list
		}
		finally {
			// close statement and result set
		}
		return null;
	}

	//searchByCreator???
	@Override
	public List<Artist> searchByArtist(String artist) throws SQLException {
		List<Artist> resultingArtists = new ArrayList<Artist>();
		
		try {	
			artist += "%";
			preparedStatement = connection.prepareStatement("select Name from Creator where Name like ?");
			
			preparedStatement.setString(1, artist);
			resultSet = preparedStatement.executeQuery();

			// loop through result set
			while(resultSet.next()){
				resultingArtists = rc.convertRowToArtist(resultSet);
			}
			
			System.out.println("Found Artists: ");
			// NOTHING! but it should work...
			for (Artist a : resultingArtists) {
				System.out.println(a.toString());
			}
			
			return resultingArtists;
			
		}
		finally {
//			closeStatementAndResultSet();
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

	// moved to helperclass rowConverter
//	@Override
//	public Album convertRowToAlbum() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Artist convertRowToArtist() {
//		// TODO Auto-generated method stub
//		return null;
//	}

	
	private void closeStatementAndResultSet() throws SQLException {
		// close statement and result set
		if (statement != null) { 
    		statement.close(); 
    	}
		if(resultSet != null) {
			resultSet.close();
		}
	}

	private void closeStatementAndResultSet(Statement s, ResultSet r) throws SQLException {
		// close statement and result set
		if (s != null) { 
    		s.close(); 
    	}
		if(r != null) {
			r.close();
		}
	}


}
