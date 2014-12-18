package model;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

public final class Objectifier {
	
	private Genre genre = new Genre("");
	private String year;
	private String name;
	private String user;
	//private int length (in seconds)
	private float rating;
	private ArrayList<Artist> artist = new ArrayList<Artist>();
	private ArrayList<Review> review = new ArrayList<Review>();
	
	// convert mDB equivalence of Result set to object
	public static Album cursorToAlbum(BasicDBObject dbo) {

		String mediaTitle = dbo.getString("Title");
		String year = dbo.getString("Year");
		String genre = dbo.getString("Genre");
		String user = dbo.getString("User");
		// Duration is not used
		
		// Artists will be many
		//BasicDBObject artist = (BasicDBObject) dbo.get("Creator");
		String creator = dbo.getString("Creator");
		Artist artist = new Artist(creator);
		
		ArrayList<Artist> artists = new ArrayList<Artist>();
		artists.add(artist);
		
		// get list of ratings and users
		BasicDBList ratings = (BasicDBList) dbo.get("Rating");
		// pick out only rating
		ratings.get(1);	// index of document within Rating (not what is searched for)
		System.out.println(ratings.get(1));
		
//		BasicDBObject aRating = (BasicDBObject) ratings.get("Rating");
		// merge into one rating
		// TODO: move to method
		// calculate avg.! 
//		Object[] array = ratings.toArray();
//		String aRating = ratings.get("Rating");
//		ratings.
		

		
		int allRatings = 0;
//		for (Object obj : array) {
//			String rating = obj.toString();
//			try {
//				allRatings += Integer.parseInt(rating);
//			} catch (Exception e) {
//				// TODO: handle exception
//				e.printStackTrace();
//			}
//			
//		}
//		int avgRating = rating / array.length;
		
		// get reviews
		// TODO: loop
//		BasicDBObject r = (BasicDBObject) dbo.get("Review");
//		String reviewTitle = r.getString("Title");
//		String reviewText = r.getString("Text");
//		String reviewAuthor = r.getString("User");
//		String media = dbo.getString("Mediatype");
//		
//		Review review = new Review(reviewTitle, reviewText, reviewAuthor, media, mediaTitle);
//		
//		ArrayList<Review> reviews = new ArrayList<Review>();
//		reviews.add(review);
		
		// generate album
		Album album = new Album(mediaTitle, year, user, 0);	// RM id == 0!
		album.setArtist(artists);
		album.setGenre(genre);
		//album.setRating(avgRating);
//		album.setReview(reviews);
		
		//public Album(String name, String year, String user, int id) {

		
		return album;
	}
	
//	// convert mDB equivalence of Result set to object
//	public static ArrayList<Album> cursorToAlbum(BasicDBObject dbo) {
//		ArrayList<Album> album = new ArrayList<Album>();
//		
//		
//		return album;
//	}

}
