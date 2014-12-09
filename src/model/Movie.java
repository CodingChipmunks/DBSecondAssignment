package model;

import java.util.ArrayList;

public class Movie {
	private int id;
	private String genre;
	private String director;
	private String year;
	private String title;
	private ArrayList<Review> review;
	private ArrayList<Rating> rating;

	public int getId() {
		return id;
	}

	public ArrayList<Review> getReview() {
		return review;
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

	public Movie(String genre, String director, String year, String title, int id) {
		super();
		this.genre = genre;
		this.director = director;
		this.year = year;
		this.title = title;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
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
