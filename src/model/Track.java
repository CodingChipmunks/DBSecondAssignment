package model;

public class Track {
private String title;
private String year;
private Artist artist;
private Rating rating;

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
public Track(String title, String year, Artist artist, Rating rating) {
	super();
	this.title = title;
	this.year = year;
	this.artist = artist;
	this.rating = rating;
}
public Artist getArtist() {
	return artist;
}
public void setArtist(Artist artist) {
	this.artist = artist;
}
public Rating getRating() {
	return rating;
}
public void setRating(Rating rating) {
	this.rating = rating;
}
}
