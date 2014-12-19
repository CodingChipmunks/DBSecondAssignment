package model;

import java.util.ArrayList;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

public final class Objectifier {
	
	// convert mDB equivalence of Result set to object
	public static Album cursorToAlbum(BasicDBObject dbo) {
		// TODO: Decide which is nicer
		int avgRating = 0;
		
		String mediaId = dbo.getString("_id");
		String mediaTitle = dbo.getString("Title");
		String year = dbo.getString("Year");
		String genre = dbo.getString("Genre");
		String user = dbo.getString("AddedBy");
		//int duration = dbo.getInt("Duration");
		// Duration is not used
		
		// Artists will be many
		//BasicDBObject artist = (BasicDBObject) dbo.get("Creator");
		
		/*String creator = dbo.getString("Creator");
		Artist artist = new Artist(creator);*/
		
		ArrayList<Artist> artists = new ArrayList<Artist>();
		BasicDBList creators = (BasicDBList) dbo.get("Creator");
		
		if (null != creators)
		{
			Object[] arrayOfCreators = creators.toArray();
			
			for (int i = 0; i < arrayOfCreators.length; i++)
			{
				BasicDBObject singleCreator = (BasicDBObject) arrayOfCreators[i];
				
				artists.add(new Artist(singleCreator.getString("Name")));
			}
		}
		
		// get sub document of ratings and users
		BasicDBList ratings = (BasicDBList) dbo.get("Rating");
		// make sure media is rated
		if(null != ratings) {
			// put sub document of ratings in array
			Object[] arrayOfRatings = ratings.toArray();	// nullPtrExc if no rating in fetched obj
			
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
			avgRating = totalScore / arrayOfRatings.length;
		}
		
		// get mediatype
		String media = dbo.getString("Mediatype");
		
		// prepare saving extracted reviews
		ArrayList<Review> reviews = new ArrayList<Review>();
		
		// get reviews
		BasicDBList reviewInDB = (BasicDBList) dbo.get("Review");
		
		if(null != reviewInDB) {
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
		}
		
		
		// generate album
		Album album = new Album(mediaTitle, year, user, mediaId);	// RM id == 0!
		album.setArtist(artists); 
		album.setGenre(genre);
		album.setRating(avgRating);
		album.setReview(reviews);

		
		return album;
	}
	
	// convert mDB equivalence of Result set to object
		public static Movie cursorToMovie(BasicDBObject dbo) {
			// TODO: Decide which is nicer
			int avgRating = 0;
			
			String mediaId = dbo.getString("_id");
			String mediaTitle = dbo.getString("Title");
			String year = dbo.getString("Year");
			String genre = dbo.getString("Genre");
			String user = dbo.getString("AddedBy");
			int duration = Integer.parseInt(dbo.getString("Duration"));
			// Duration is not used
			
			// Artists will be many
			//BasicDBObject artist = (BasicDBObject) dbo.get("Creator");
			
			/*String creator = dbo.getString("Creator");
			Artist artist = new Artist(creator);*/
			
			ArrayList<Director> directors = new ArrayList<Director>();
			BasicDBList creators = (BasicDBList) dbo.get("Creator");
			
			if (null != creators)
			{
				Object[] arrayOfCreators = creators.toArray();
				
				for (int i = 0; i < arrayOfCreators.length; i++)
				{
					BasicDBObject singleCreator = (BasicDBObject) arrayOfCreators[i];
					
					directors.add(new Director(singleCreator.getString("Name")));
				}
			}
			
			// get sub document of ratings and users
			BasicDBList ratings = (BasicDBList) dbo.get("Rating");
			// make sure media is rated
			if(null != ratings) {
				// put sub document of ratings in array
				Object[] arrayOfRatings = ratings.toArray();	// nullPtrExc if no rating in fetched obj
				
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
				avgRating = totalScore / arrayOfRatings.length;
			}
			
			// get mediatype
			String media = dbo.getString("Mediatype");
			
			// prepare saving extracted reviews
			ArrayList<Review> reviews = new ArrayList<Review>();
			
			// get reviews
			BasicDBList reviewInDB = (BasicDBList) dbo.get("Review");
			
			if(null != reviewInDB) {
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
			}
			
			
			// generate album
			Movie movie = new Movie(mediaTitle, genre, year, user, duration, mediaId);	// RM id == 0!
			movie.setDirector(directors); 
			movie.setGenre(genre);
			movie.setRating(avgRating);
			movie.setReview(reviews);

			
			return movie;
		}

	public static ArrayList<Review> cursorToReview(BasicDBObject dbo, String queryText) {
		//Review review = new Review(String title, String text, String user, String media, String mediaTitle);
		ArrayList<Review> reviews = new ArrayList<Review>();
		BasicDBList reviewList = (BasicDBList) dbo.get("Review");

		if(null != reviews && reviewList != null) {
			Object[] arrayOfRatings = reviewList.toArray();	
			
			for (int i = 0; i < arrayOfRatings.length; i++) {
				BasicDBObject singleReview = (BasicDBObject) arrayOfRatings[i];
				
				Review r = new Review(singleReview.getString("Title") , singleReview.getString("Text"), singleReview.getString("User"), 
						dbo.getString("Mediatype"), dbo.getString("Title"));

				if (r.getText().contains(queryText) || 
						r.getTitle().contains(queryText) || 
						r.getUser().contains(queryText) || 
						dbo.getString("Mediatype").contains(queryText) ||
						dbo.getString("Title").contains(queryText))
				reviews.add(r);
			}
		}
		return reviews;
	}
	
//	// convert mDB equivalence of Result set to object
//	public static ArrayList<Album> cursorToAlbum(BasicDBObject dbo) {
//		ArrayList<Album> album = new ArrayList<Album>();
//		
//		
//		return album;
//	}

}
