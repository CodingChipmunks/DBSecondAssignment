package model;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

public final class Objectifier {
	
	// convert mDB equivalence of Result set to object
	public static Album cursorToAlbum(BasicDBObject dbo) {

		String mediaTitle = dbo.getString("Title");
		String year = dbo.getString("Year");
		String genre = dbo.getString("Genre");
		String user = dbo.getString("AddedBy");
		// Duration is not used
		
		// Artists will be many
		//BasicDBObject artist = (BasicDBObject) dbo.get("Creator");
		String creator = dbo.getString("Creator");
		Artist artist = new Artist(creator);
		
		ArrayList<Artist> artists = new ArrayList<Artist>();
		artists.add(artist);
		
		// get sub document of ratings and users
		BasicDBList ratings = (BasicDBList) dbo.get("Rating");
		// put sub document of ratings in array
		Object[] arrayOfRatings = ratings.toArray();
		
		System.out.println("iterating");
		// collect ratings to calc. avg.
		int totalScore = 0;
		// pick out every rating separately
		for (int i = 0; i < arrayOfRatings.length; i++) {
			BasicDBObject eachRating = (BasicDBObject) arrayOfRatings[i];
			String score = eachRating.getString("Score");
			totalScore += Integer.parseInt(score);
		}
		
		// calculate avg. and assign it later to the Album.
		int avgRating = totalScore / arrayOfRatings.length;
		
		
		// get mediatype
		String media = dbo.getString("Mediatype");
		
		// prepare saving extracted reviews
		ArrayList<Review> reviews = new ArrayList<Review>();
		
		// get reviews
		BasicDBList reviewInDB = (BasicDBList) dbo.get("Review");
		// make array of sub documents, each review
		Object[] arrayOfReveiws = reviewInDB.toArray();
		
		for (int i = 0; i < arrayOfReveiws.length; i++) {
			BasicDBObject eachReview = (BasicDBObject) arrayOfReveiws[i];
			String reviewTitle = eachReview.getString("Title");
			String reviewText = eachReview.getString("Text");
			String reviewAuthor = eachReview.getString("User");
			
			// create obj. repr. of each review
			Review review = new Review(reviewTitle, reviewText, reviewAuthor, media, mediaTitle);
			reviews.add(review);
		}
		
		
		
		// generate album
		Album album = new Album(mediaTitle, year, user, 0);	// RM id == 0!
		album.setArtist(artists);
		album.setGenre(genre);
		album.setRating(avgRating);
		album.setReview(reviews);

		
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
