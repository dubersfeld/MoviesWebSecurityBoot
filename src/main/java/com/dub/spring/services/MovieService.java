package com.dub.spring.services;

import java.util.Date;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import com.dub.spring.entities.DisplayMovie;
import com.dub.spring.entities.Movie;


@PreAuthorize("hasAuthority('VIEW')")
public interface MovieService {

	List<DisplayMovie> getAllMovies();
	long numberOfMovies();
	
	Movie getMovie(long id);
	
	DisplayMovie getMovie(String title, Date releaseDate);
		
	List<DisplayMovie> getMovie(String title);
	
	@PreAuthorize("hasAuthority('DELETE')")
	void deleteMovie(long id);
	
	@PreAuthorize("hasAuthority('CREATE')")
	void createMovie(Movie movie);	
	
	@PreAuthorize("hasAuthority('UPDATE')")
	void updateMovie(Movie movie);	

}
