package com.dub.spring.services;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dub.spring.entities.Actor;
import com.dub.spring.entities.Director;
import com.dub.spring.entities.DisplayMovie;
import com.dub.spring.entities.Movie;

import com.dub.spring.repositories.ActorRepository;
import com.dub.spring.repositories.AdvancedDAO;


@Service
public class AdvancedServiceImpl implements AdvancedService {

	//private static final Logger log = LogManager.getLogger();
	
	@Autowired 
	private AdvancedDAO advancedDAO;
	
	@Autowired 
	private ActorRepository actorRepository;
		

	@Override
	public List<DisplayMovie> getMoviesWithActor(String firstName, String lastName) {
		List<Movie> movies = 
				advancedDAO.getMoviesByActorName(firstName, lastName);
		List<DisplayMovie> dmovies = new ArrayList<>();
				
		for (Movie movie : movies) {		
			Director director = movie.getDirector();
			String name = director.getFirstName() + " " + director.getLastName();
			DisplayMovie movieDisplay = new DisplayMovie(movie);
			movieDisplay.setDirectorName(name);
			dmovies.add(movieDisplay);			
		}
						
		return dmovies;
	}

	@Override
	public List<Actor> getActorsInMovie(String title, Date releaseDate) {
		return advancedDAO.getActorsByMovie(title, releaseDate);
	}

	@Override
	public List<DisplayMovie> getMoviesByDirector(String firstName,
			String lastName) {
		List<Movie> movies = advancedDAO.getMoviesByDirectorName(firstName, lastName);
		List<DisplayMovie> dmovies = new ArrayList<>();
		
	
		for (Movie movie : movies) {		
			String name = firstName + " " + lastName;
			DisplayMovie movieDisplay = new DisplayMovie(movie);
			movieDisplay.setDirectorName(name);
			dmovies.add(movieDisplay);	
		}
						
		return dmovies;
	}

	@Override
	public List<Actor> getActorsByDirector(String firstName, String lastName) {
		return advancedDAO.getActorsByDirectorName(firstName, lastName);
	}

	@Override
	public List<Director> getDirectorsByActor(String firstName, String lastName) {
		return advancedDAO.getDirectorsByActorName(firstName, lastName);
	}

	@Override
	public void createActorFilm(long actorId, long movieId) {		
		advancedDAO.createActorFilm(actorId, movieId);
	}

	@Override
	@Transactional
	public void createActorFilmSpecial(
								Actor actor, String title,
								Date releaseDate) 
	{	
		actorRepository.save(actor);
		//log.info("transaction step 1 completed");
		
		advancedDAO.createActorFilm(actor, title, releaseDate);
		
	}

}
