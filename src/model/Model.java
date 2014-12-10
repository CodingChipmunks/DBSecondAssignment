package model;

import java.sql.SQLException;
import java.util.ArrayList;

import controller.*;
import view.*;

/**
 * Keeps track of components in the model
 * 
 * @author softish
 *
 */
 public class Model {
	private ArrayList<Album> album; // TODO are these required?
	private ArrayList<Movie> movie;
	private ArrayList<Review> review;
	private Object[] bank = null; // contains the result of last query.

	// TODO: add QueryExecuter
	public Model()
	{
		//QueryExecuter queryExecuter = new QueryExecuter("", "", "");	// dbInterpreter, dba(dataBaseAcess)
		
	}
	
	public void setBank(Object[] bank)
	{
		this.bank = bank;
	}
	
	@SuppressWarnings("unchecked")
	public Object[] getBank()
	{
		return bank;
	}
	
	public void clear() {
		album.clear();
		movie.clear();
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Album> getAlbum() {
		return (ArrayList<Album>) album.clone();
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Review> getReview()
	{
		return ((ArrayList<Review>) review.clone());
	}
	
	
	@SuppressWarnings("unchecked")
	public ArrayList<Movie> getMovie() {
		return (ArrayList<Movie>) movie.clone();
	}

	public int getMovieCount()
	{
		return movie.size();
	}
	
	public int getAlbumCount()
	{
		return album.size();
	}
	
	public Movie getMovie(int index) {
		return movie.get(index);
	}

	public Album getAlbum(int index) {
		return album.get(index);
	}
}
