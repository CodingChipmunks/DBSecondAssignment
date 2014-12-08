package model;

public class Review {
 private String text;
 private User user; //? or text.
public String getText() {
	return text;
}
public void setText(String text) {
	this.text = text;
}
public User getUser() {
	return user;
}
public Review(String text, User user) {
	super();
	this.text = text;
	this.user = user;
}
public void setUser(User user) {
	this.user = user;
}
 
 
}
