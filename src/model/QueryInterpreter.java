package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public interface QueryInterpreter {
	void disconnect();	
	
	// TODO use a single type for albums, movie, book while reading from the database
	// the relations are the same, just modify RowConverter to convert to appropriate object.
	ArrayList<Album> getAlbumsByTitle(String title) throws SQLException;
	ArrayList<Album> getAlbumsByGenre(String genre) throws SQLException;
	ArrayList<Album> getAllAlbums(ResultSet rsetAlbum) throws SQLException;
	ArrayList<Album> getAlbumsByAny(String title) throws SQLException;
	ArrayList<Album> getAlbumsByArtist(String artist) throws SQLException;
	ArrayList<Album> getAlbumsByRating(String rating) throws SQLException;
	ArrayList<Album> getAlbumsByYear(String year) throws SQLException;
	ArrayList<Album> getAlbumsByUser(String user) throws SQLException;
	
	ArrayList<Movie> getMoviesByAny(String queryText) throws SQLException;
	ArrayList<Movie> getMovieByUser(String user) throws SQLException;
	ArrayList<Movie> getAllMovies(ResultSet rsetMovie) throws SQLException;
	ArrayList<Movie> getMovieByYear(String year) throws SQLException;
	ArrayList<Movie> getMovieByTitle(String title) throws SQLException;
	ArrayList<Movie> getMovieByRating(String rating) throws SQLException;
	ArrayList<Movie> getMovieByDirector(String director) throws SQLException;
	ArrayList<Movie> getMovieByGenre(String genre) throws SQLException;

	ArrayList<Book> getBooksByAny(String queryText) throws SQLException;
	ArrayList<Book> getAllBooks(ResultSet rsetBook) throws SQLException;
	ArrayList<Book> getBookByUser(String user) throws SQLException;
	ArrayList<Book> getBookByYear(String year) throws SQLException;
	ArrayList<Book> getBookByTitle(String title) throws SQLException;
	ArrayList<Book> getBookByRating(String rating) throws SQLException;
	ArrayList<Book> getBookByAuthor(String artist) throws SQLException;
	ArrayList<Book> getBookByGenre(String genre) throws SQLException;

	void getReviewsByAny(String queryText) throws SQLException;
	
	void rateAlbum(int rating, int media) throws SQLException;
	void verifyAccount(String user, String pass) throws SQLException;
	void addMedia(String name, String year, String genre, Object[] objects,
			int duration, int mediaType) throws SQLException;
}
