package model;

public class Rating {
	private float score;
	private String user;

	public float getScore() {
		return score;
	}

	public Rating(String user, float score) {
		super();
		this.score = score;
	}

	public void setScore(float score) {
		this.score = score;
	}
	
	public String getUser()
	{
		return this.user;
	}

}
