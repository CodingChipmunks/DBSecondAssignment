package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface QueryInterpreter {
	
	//public Connection connect(String db, String uname, String pwd) throws Exception;	// is it better to handle by constructor?
	
	public void disconnect();
	public List<Album> searchByAlbumTitle(String title) throws SQLException;
	public List<Artist> searchByArtist(String artist) throws SQLException;
	public List<Album> searchByGenre(String genre) throws SQLException;	//enum? / JComboBoxItem! == Object[] 
	public List<Album> searchByRating(int rating);	//object for ratings??
	public void insertAlbum(Album album);
	public void rateAlbum(int rating);
	List<Album> getAllAlbums(ResultSet rsetAlbum) throws SQLException;
	List<Album> getAlbumsByAny(String title) throws SQLException;
	
	// moved to helperclass rowConverter
	// private methods can't be in interface
//	Album convertRowToAlbum();
//	
//	Artist convertRowToArtist();
}
