package model;

public class Author {
	private String name;
	
	public String getName()
	{
		return this.name;
	}
	
	public Author(String name)
	{
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
