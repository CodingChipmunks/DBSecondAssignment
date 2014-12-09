package model;

import java.util.ArrayList;

public class Movie {
	private int id;
	private String genre;
	private String year;
	private String title;
	private String user;
	private ArrayList<Director> director;
	private ArrayList<Review> review;
	private ArrayList<Rating> rating;

	public int getId() {
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

	public ArrayList<Rating> getRating() {
		return rating;
	}

	public void setRating(ArrayList<Rating> rating) {
		this.rating = rating;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public Movie(String title, String genre, String year, String user, int id) {
		super();
		this.genre = genre;
		this.year = year;
		this.user = user;
		this.title = title;
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

}
