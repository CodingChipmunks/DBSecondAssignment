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
	private ResultSet resultSet;
	
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
		
		disconnect();
	}


	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Album> getAllAlbums() throws SQLException {
		List<Album> albums = new ArrayList<Album>();
		
		// make statement and result set
		Statement s;
		ResultSet r;

		
		// TODO define sql query
		try {
			// use statement and result set to fetch info
			s = connection.createStatement();
			r = s.executeQuery("select * from Album");
			// loop through result set
			while(r.next()){
				// convert row to Album and add to list: preferably with helper class
			}
				
			
			//return list
			
		}
		finally {
			// close statement and result set
		}
		
		
		return null;
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

	@Override
	public List<Album> searchByArtist(String artist) {
		// TODO Auto-generated method stub
		return null;
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

	



}
