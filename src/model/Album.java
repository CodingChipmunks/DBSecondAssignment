package model;

import java.util.ArrayList;

public class Album {
	private int id;
	private String name;
	private String year;
	private String user;
	private float rating;
	private Genre genre = new Genre("");
	private ArrayList<Artist> artist = new ArrayList<Artist>();
	private ArrayList<Review> review = new ArrayList<Review>();

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public int getId() {
		return id;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Review> getReview() {
		return (ArrayList<Review>) review.clone();
	}

	public void setReview(ArrayList<Review> review) {
		this.review = review;
	}

	public void setArtist(ArrayList<Artist> artist) {
		this.artist = artist;
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
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

	public Album(String name, String year, String user, int id) {
		super();
		this.name = name;
		this.year = year;
		this.user = user;
		this.id = id;
	}

	public void AddArtist(String name) {
		artist.add(new Artist(name));
	}

	public boolean hasArtist(String name) {
		boolean result = false;

		for (int i = 0; i < artist.size(); i++)
			if (name.equals(artist.get(i).getName()))
				result = true;
		return result;
	}

	@Override
	public String toString() {
		return "Album [id=" + id + ", name=" + name + ", year=" + year
				+ ", genre=" + genre + ", user=" + user + ", artist=" + artist
				+ ", review=" + review + ", rating=" + rating + "]";
	}

	public void setGenre(String string) {
		genre = new Genre(string);
	}

	public void addReview(Review review) {
		this.review.add(review);
	}

}
