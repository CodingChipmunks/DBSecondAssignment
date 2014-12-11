package model;

import java.util.ArrayList;

// TODO ignore this file for now, only the fields are used not implemented yet.

public class Book {
	private int id;
	private String genre;
	private String year;
	private String title;
	private Float rating;
	// private int pages
	private String user;
	private ArrayList<Author> author = new ArrayList<Author>();
	private ArrayList<Review> review = new ArrayList<Review>();

	public Book(String title, String year, int id) {
		super();
		this.title = title;
		this.year = year;
		this.id = id;
	}
	
	public void setRating(Float rating)
	{
		this.rating = rating;
	}
	
	public Float getRating()
	{
		return this.rating;
	}
	
	public int getId()
	{
		return this.id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}
	
	
	@Override
	public boolean equals(Object object) {
		Book book = (Book) object;
		if (book.getId() == this.getId())
			return true;
		else
			return false;
	}
	
	@Override
	public int hashCode()
	{
		return this.id;
	}

	public void addAuthor(String name)
	{
		author.add(new Author(name));
	} 
	
	public String getGenre()
	{
		return this.genre;
	}
	
	public void setGenre(String string) {
		this.genre = string;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	public void addReview(Review review) {
		this.review.add(review);
	}

	public String getuser()
	{
		return this.user;
	}
	
	public void setUser(String user) {
		this.user = user;
	}

	public ArrayList<Author> getAuthor() {
		return author;
	}

}
