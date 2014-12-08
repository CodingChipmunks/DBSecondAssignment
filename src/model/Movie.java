package model;

public class Movie {
 private String genre;
 private String director;
 private String year;
 private String title;
public String getGenre() {
	return genre;
}
public void setGenre(String genre) {
	this.genre = genre;
}
public Movie(String genre, String director, String year, String title) {
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
