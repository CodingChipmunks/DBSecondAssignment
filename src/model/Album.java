package model;

import java.util.ArrayList;

public class Album {
	private String name;
	private String year;
	private ArrayList<Review> review;
	private ArrayList<Rating> rating;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public Album(String name, String year) {
		super();
		this.name = name;
		this.year = year;
	}

}
