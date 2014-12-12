package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/*
 * Helper class for row conversion
 * it performs the actual translation
 * from the relational to the object model
 */
public final class RowConverter {
	public static Review convertRowToReview(ResultSet reviewRow)
			throws SQLException {
		return  new Review(reviewRow.getString("title"), reviewRow.getString("text"), "", "", "");
	}
	
	public static Review convertRowToExtendedReview(ResultSet reviewRow) throws SQLException
	{
//		typename, itemtitle, username, revtitle, revtext
		//Review(String title, String text, String user, String media, String mediaTitle)
		return new Review(reviewRow.getString(4), reviewRow.getString(5), reviewRow.getString(3), reviewRow.getString(1), reviewRow.getString(2));	
	}
 
	// convert resultset to list of Artist objects
	public static ArrayList<Artist> convertRowToArtist(ResultSet artistRow)
			throws SQLException {
		ArrayList<Artist> artist = new ArrayList<Artist>();

		while (artistRow.next()) {
			artist.add(new Artist(artistRow.getString("name")));
		}

		return artist;
	}

	public static Album convertRowToAlbum(ResultSet bigAlbumRow)
			throws SQLException {

		Album album = new Album(bigAlbumRow.getString("title"),
				bigAlbumRow.getString("year"), "", bigAlbumRow.getInt("id"));

		return album;
	}

	// String title, String genre, String director, String year, int id
	// convert resultset to Movie object
	public static Movie convertRowToMovie(ResultSet movieRow)
			throws SQLException {

		Movie movie = new Movie(movieRow.getString("title"), "",
				movieRow.getString("year"), "", movieRow.getInt("duration"), movieRow.getInt("id"));

		return movie;
	}

	public static Book convertRowToBook(ResultSet bookRow) throws SQLException {
		Book book = new Book(bookRow.getString("title"), bookRow.getString("year"), bookRow.getInt("id"));

		return book;
	}
}
