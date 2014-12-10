package model;

public class Artist {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Artist(String name) {
		super();
		this.name = name;
	}

	public String toString()
	{
		return this.name;
	}
	
}
