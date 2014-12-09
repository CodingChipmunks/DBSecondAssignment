package model;

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
	private ArrayList<Album> album; // clear these on some queries.
	private ArrayList<Movie> movie;

	// TODO: add QueryExecuter
	
	public void clear() {
		album.clear();
		movie.clear();
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Album> getAlbum() {
		return (ArrayList<Album>) album.clone();
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
