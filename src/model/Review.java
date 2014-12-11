package model;

public class Review {
	private String media;
	private String mediaTitle;
	public String getMediaTitle() {
		return mediaTitle;
	}

	public void setMediaTitle(String mediaTitle) {
		this.mediaTitle = mediaTitle;
	}

	private String title;
	private String text;
	private String user;

	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public String getText() {
		return text;
	}
	
	public String getMedia()
	{
		return this.media;
	}
	
	public void setMedia(String media)
	{
		this.media = media;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getUser() {
		return user;
	}

	public Review(String title, String text, String user, String media, String mediaTitle) {
		super();
		this.media = media;
		this.title = title;
		this.mediaTitle = mediaTitle;
		this.text = text;
		this.user = user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
	public String toString()
	{
		return title + " " + "Written by " + user + " " + text;
	}

}
