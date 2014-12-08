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
	
	private Connection connection;
	
	public QueryExecuter(String db, String uname, String pwd) throws SQLException {
		
		// connect to db 
		connection = DriverManager.getConnection(db, uname, pwd);
	}


	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Album> getAllAlbums() {
		List<Album> albums = new ArrayList<Album>();
		
		// make statement and result set
		
		// TODO define sql query
		try {
			// use statement and result set to fetch info
			
			// loop through result set
			
			// convert row to Album and add to list: preferably with helper class
			
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
