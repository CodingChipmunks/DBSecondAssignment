package model;

import java.util.ArrayList;

public class Album {
	private int id;
	private String name;
	private String year;
	private String user;
	private ArrayList<Artist> artist = new ArrayList<Artist>();
	private ArrayList<Review> review = new ArrayList<Review>();
	private ArrayList<Rating> rating = new ArrayList<Rating>();

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
	public int getId()
	{
		return id;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Review> getReview() {
		return (ArrayList<Review>) review.clone();
	}

	public void setReview(ArrayList<Review> review) {
		this.review = review;
	}

	public void setArtist(ArrayList<Artist> artist)
	{
		this.artist = artist;
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

	public Album(String name, String year, String user, int id) {
		super();
		this.name = name;
		this.year = year;
		this.user = user;
		this.id = id;
	}
	
	public void AddArtist(String name)
	{
		artist.add(new Artist(name));
	}
	
	public float meanRating()
	{
		float mean = 0.0f;
		
		for (int i = 0; i < rating.size(); i++)
			mean += rating.get(i).getScore();
		
		return (mean / rating.size());
	}
	
	public boolean hasArtist(String name)
	{
		boolean result = false;
		
		for (int i = 0; i < artist.size(); i++)
			if (name.equals(artist.get(i).getName()))
				result = true;
		return result;
	}

}
