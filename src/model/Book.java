package model;

import java.util.ArrayList;

// TODO ignore this file for now, only the fields are used not implemented yet.

public class Book {
	private String name;
	private ArrayList<String> author;
	private String year;

	public Book(String name, ArrayList<String> author, String year) {
		super();
		this.name = name;
		this.author = author;
		this.year = year;
	}

	public String getTitle() {
		return name;
	}

	public void setTitle(String name) {
		this.name = name;
	}

	public ArrayList<String> getAuthor() {
		return author;
	}

	public void setAuthor(ArrayList<String> author) {
		this.author = author;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

}
