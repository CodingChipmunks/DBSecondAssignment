package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MongoQueryExecuter implements QueryInterpreter {

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		
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
