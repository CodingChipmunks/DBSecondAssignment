package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface QueryInterpreter {
	
	//public Connection connect(String db, String uname, String pwd) throws Exception;	// is it better to handle by constructor?
	
	public void disconnect();
	public List<Album> searchByAlbumTitle(String title) throws SQLException;
	public List<Album> searchByGenre(String genre) throws SQLException;	//enum? / JComboBoxItem! == Object[] 
	public void insertAlbum(Album album);
	public void rateAlbum(int rating);
	ArrayList<Album> getAllAlbums(ResultSet rsetAlbum) throws SQLException;
	ArrayList<Album> getAlbumsByAny(String title) throws SQLException;
	ArrayList<Album> getAlbumsByArtist(String artist) throws SQLException;
	ArrayList<Album> getAlbumsByRating(String rating) throws SQLException;
	
	// moved to helperclass rowConverter
	// private methods can't be in interface
//	Album convertRowToAlbum();
//	
//	Artist convertRowToArtist();
}
