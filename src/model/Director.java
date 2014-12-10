package model;

public class Director {
	private String name;
	
	public String getName()
	{
		return this.name;
	}
	
	public Director(String name)
	{
		this.name = name;
	}

	@Override
	public String toString() {
		return "Director [name=" + name + "]";
	}

}
