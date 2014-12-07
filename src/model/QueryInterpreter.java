package model;

import java.sql.Connection;
import java.util.List;

public interface QueryInterpreter {
	
	//public Connection connect(String db, String uname, String pwd) throws Exception;	// is it better to handle by constructor?
	
	public void disconnect();
	
	public List<Album> getAllAlbums();
	
	public List<Album> searchByAlbumTitle(String title);
	
	public List<Album> searchByArtist(String artist);
	
	public List<Album> searchByGenre(String genre);	//enum? / JComboBoxItem! == Object[] 
	
	public List<Album> searchByRating(int rating);	//object for ratings??
	
	public void insertAlbum(Album album);
	
	public void rateAlbum(int rating);
	
	// moved to helperclass rowConverter
	// private methods can't be in interface
//	Album convertRowToAlbum();
//	
//	Artist convertRowToArtist();
}