package model;

import java.util.ArrayList;

public class Movie {
	private String id;
	private String genre;
	private String year;
	private String title;
	private float rating;
	private int duration;
	private String user;
	private ArrayList<Director> director = new ArrayList<Director>();
	private ArrayList<Review> review = new ArrayList<Review>();

	public String getId() {
		return id;
	}
	
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	@SuppressWarnings("unchecked") 
	public ArrayList<Review> getReview() {
		return (ArrayList<Review>) review.clone();
	}

	public void setReview(ArrayList<Review> review) {
		this.review = review;
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float f) {
		this.rating = f;
	}

	public String getGenre() {
		return genre;
	}
	
	public void setDuration(int duration)
	{
		this.duration = duration;
	}
	
	public int getDuration()
	{
		return this.duration;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public Movie(String title, String genre, String year, String user, int duration, String id) {
		this.genre = genre;
		this.year = year;
		this.user = user;
		this.title = title;
		this.duration = duration;
		this.id = id;
	}

	public ArrayList<Director> getDirector() {
		return director;
	}

	public void setDirector(ArrayList<Director> director) {
		this.director = director;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void addReview(Review review) {
		this.review.add(review);
	}

	public void addDirector(String string) {
		director.add(new Director(string));
		
	}
	
	@Override
	public boolean equals(Object object) {
		Movie movie = (Movie) object;
		if (movie.getId().equals(this.getId()))
			return true;
		else
			return false;
	}
	
	@Override
	public int hashCode()
	{
		return this.id.hashCode();
	}

}
