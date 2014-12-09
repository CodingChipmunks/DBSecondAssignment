package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/*
 * Helper class for row conversion
 */
public class RowConverter {
	public ArrayList<Review> convertRowToReview(ResultSet reviewRow)
			throws SQLException {
		ArrayList<Review> review = new ArrayList<Review>();

		while (reviewRow.next()) {
			review.add(new Review(reviewRow.getString("text"), reviewRow
					.getString("user")));
		}

		return review;
	}

	public ArrayList<Rating> convertRowToRating(ResultSet ratingRow)
			throws SQLException {
		ArrayList<Rating> rating = new ArrayList<Rating>();

		while (ratingRow.next()) {
			rating.add(new Rating(ratingRow.getString("user"), ratingRow
					.getInt("rating")));
		}

		return rating;
	}

	public ArrayList<Director> convertRowToDirector(ResultSet directorRow)
			throws SQLException {
		ArrayList<Director> director = new ArrayList<Director>();

		while (directorRow.next()) {
			director.add(new Director(directorRow.getString("name")));
		}

		return director;
	}

	public ArrayList<Artist> convertRowToArtist(ResultSet artistRow)
			throws SQLException {
		ArrayList<Artist> artist = new ArrayList<Artist>();

		while (artistRow.next()) {
			artist.add(new Artist(artistRow.getString("name")));
		}

		return artist;
	}

	public Album convertRowToAlbum(ResultSet albumRow, ResultSet artistRow,
			ResultSet reviewRow, ResultSet ratingRow) throws SQLException {

		Album album = new Album(albumRow.getString("name"),
				albumRow.getString("year"), albumRow.getString("user"), albumRow.getInt("id"));

		album.setReview(convertRowToReview(reviewRow));
		album.setRating(convertRowToRating(ratingRow));
		album.setArtist(convertRowToArtist(artistRow));
		return album;
	}

	// String title, String genre, String director, String year, int id
	public Movie convertRowToMovie(ResultSet movieRow, ResultSet directorRow,
			ResultSet reviewRow, ResultSet ratingRow) throws SQLException {

		Movie movie = new Movie(movieRow.getString("title"),
				movieRow.getString("genre"), movieRow.getString("year"), movieRow.getString("user"),
				movieRow.getInt("id"));

		movie.setRating(convertRowToRating(ratingRow));
		movie.setReview(convertRowToReview(reviewRow));
		movie.setDirector(convertRowToDirector(directorRow));
		return movie;
	}
}
