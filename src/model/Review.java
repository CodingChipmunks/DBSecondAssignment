package model;

public class Review {
	private String text;
	private String user;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getUser() {
		return user;
	}

	public Review(String text, String user) {
		super();
		this.text = text;
		this.user = user;
	}

	public void setUser(String user) {
		this.user = user;
	}

}
