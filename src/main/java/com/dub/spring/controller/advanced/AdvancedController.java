package com.dub.spring.controller.advanced;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.dub.spring.services.AdvancedService;
import com.dub.spring.entities.Actor;
import com.dub.spring.entities.Director;
import com.dub.spring.entities.DisplayMovie;
import com.dub.spring.exceptions.ActorNotFoundException;
import com.dub.spring.controller.NameForm;
import com.dub.spring.exceptions.DuplicateEntryException;
import com.dub.spring.controller.movies.TitleDateForm;
import com.dub.spring.exceptions.MovieNotFoundException;
import com.dub.spring.exceptions.DuplicateActorException;


@Controller
public class AdvancedController {
	
	private static final String advancedQueries = "advanced/advancedQueries";
	
	private static final String actorMovies = "advanced/actorMovies";
	private static final String actorMoviesResult = "advanced/actorMoviesResult";
	private static final String actorMoviesNoResult = "advanced/actorMoviesNoResult";
	
	private static final String movieActors = "advanced/movieActors";
	private static final String movieActorsResult = "advanced/movieActorsResult";
	private static final String movieActorsNoResult = "advanced/movieActorsNoResult";
	
	private static final String directorMovies = "advanced/directorMovies";
	private static final String directorMoviesResult = "advanced/directorMoviesResult";
	private static final String directorMoviesNoResult = "advanced/directorMoviesNoResult";
	
	private static final String directorActors = "advanced/directorActors";
	private static final String directorActorsResult = "advanced/directorActorsResult";
	private static final String directorActorsNoResult = "advanced/directorActorsNoResult";
	
	private static final String actorDirectors = "advanced/actorDirectors";
	private static final String actorDirectorsResult = "advanced/actorDirectorsResult";
	private static final String actorDirectorsNoResult = "advanced/actorDirectorsNoResult";
	
	
	private static final String createActorMovie = "advanced/createActorMovie";
	private static final String createActorMovieSuccess = "advanced/createActorMovieSuccess";
	

	private static final String createActorMovieTrans = "advanced/createActorMovieTrans";
	
	private static final String createActorMovieTransSuccess = "advanced/createActorMovieTransSuccess";
	
	private static final String error = "error";
	
	
	@Resource
	private AdvancedService advancedService;
	
		
	@InitBinder({"movieKey", "actorMovieTrans"})	  
	protected void initMovieActorsBinder(WebDataBinder binder) {
	    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");		
	    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	

	@GetMapping("advancedQueries")
	public String advancedQueries() {
		return advancedQueries;
	}
	
	@GetMapping("actorMovies")
	public ModelAndView getActorMovies(ModelMap model) {
		model.addAttribute("actorName", new NameForm());
		return new ModelAndView(actorMovies, model);
	}
	
	@PostMapping("actorMovies")
	public String getActorMovies(
				@Valid @ModelAttribute("actorName") NameForm form,
				BindingResult result, ModelMap model) {
			
		if (result.hasErrors()) {
			return actorMovies;
		}
				
		String firstName = form.getFirstName();
		String lastName = form.getLastName();
		try {
		List<DisplayMovie> movies = 
						advancedService.getMoviesWithActor(firstName, lastName);
			model.put("firstName", firstName); 
			model.put("lastName", lastName); 
		
			if (movies.size() > 0) {					
				model.put("movies", movies);
				return actorMoviesResult;					
			} else {
				return actorMoviesNoResult;	
			}
		} catch (ActorNotFoundException e) {
			result.rejectValue("firstName", "notFound", "actor not found");	
			return actorMovies;
		} catch (RuntimeException e) {	
			return error;
		}
	
	}
	
	@GetMapping(value = "movieActors")
	public ModelAndView getMovieActors(ModelMap model) {
		model.addAttribute("movieKey", new TitleDateForm());
		return new ModelAndView(movieActors, model);
	}
	
	@PostMapping(value = "movieActors")
	public String getMovieActors(
			@Valid @ModelAttribute("movieKey") TitleDateForm form,
			BindingResult result, ModelMap model) {
			
		if (result.hasErrors()) {
			return movieActors;
		} else {
			String title = form.getTitle();
			Date releaseDate = form.getReleaseDate();
			List<Actor> actors = 
					advancedService.getActorsInMovie(title, releaseDate);
			model.put("title", title);
			model.put("releaseDate", releaseDate);
			if (actors.size() > 0) {
				model.put("actors", actors);
				return movieActorsResult;			
			} else {
				return movieActorsNoResult;		
			}
		}
	}
	
	@GetMapping(value = "directorMovies")
	public ModelAndView getDirectorMovies(ModelMap model) {
		model.addAttribute("directorName", new NameForm());
		return new ModelAndView(directorMovies, model);
	}
	
	@PostMapping(value = "directorMovies")
	public String getDirectorMovies(
				@Valid @ModelAttribute("directorName") NameForm form,
				BindingResult result, ModelMap model) {
			
		if (result.hasErrors()) {
			return directorMovies;
		} else {		
			String firstName = form.getFirstName();
			String lastName = form.getLastName();
	
			List<DisplayMovie> movies = 
						advancedService.getMoviesByDirector(firstName, lastName);
			model.put("firstName", firstName); 
			model.put("lastName", lastName); 
			
			if (movies.size() > 0) {	
				model.put("movies", movies);
				return directorMoviesResult;					
			} else {
				return directorMoviesNoResult;	
			}
		}
	}
	
	@GetMapping(value = "directorActors")
	public ModelAndView getDirectorActors(ModelMap model) {
		model.addAttribute("directorName", new NameForm());
		return new ModelAndView(directorActors, model);
	}
	
	@PostMapping(value = "directorActors")
	public String getDirectorActors(
				@Valid @ModelAttribute("directorName") NameForm form,
				BindingResult result, ModelMap model) {
			
		if (result.hasErrors()) {
			return directorActors;
		} else {		
			String firstName = form.getFirstName();
			String lastName = form.getLastName();
			List<Actor> actors = 
						advancedService.getActorsByDirector(firstName, lastName);
			model.put("firstName", firstName); 
			model.put("lastName", lastName); 
			
			if (actors.size() > 0) {					
				model.put("actors", actors);
				return directorActorsResult;					
			} else {
				return directorActorsNoResult;	
			}
		}
	}
	
	@GetMapping(value = "actorDirectors")
	public ModelAndView getActorDirectors(ModelMap model) {
		model.addAttribute("actorName", new NameForm());
		return new ModelAndView(actorDirectors, model);
	}
	
	@PostMapping(value = "actorDirectors")
	public String getActorDirectors(
				@Valid @ModelAttribute("actorName") NameForm form,
				BindingResult result, ModelMap model) {
			
		if (result.hasErrors()) {
			return actorDirectors;
		} else {		
			String firstName = form.getFirstName();
			String lastName = form.getLastName();
			List<Director> directors = 
						advancedService.getDirectorsByActor(firstName, lastName);
			model.put("firstName", firstName); 
			model.put("lastName", lastName); 
			if (directors.size() > 0) {					
				model.put("directors", directors);				
				return actorDirectorsResult;					
			} else {
				return actorDirectorsNoResult;	
			}
		}
	}
	
	@GetMapping(value = "createActorMovie")
	public ModelAndView createActorMovie(ModelMap model) {
		model.addAttribute("actorMovie", new ActorMovieForm());
		return new ModelAndView(createActorMovie, model);
	}
	
	
	@PostMapping(value = "createActorMovie")
	public String createActorMovie(
						@Valid @ModelAttribute("actorMovie") ActorMovieForm actorMovie,
						BindingResult result, ModelMap model) 
	{	
		if (result.hasErrors()) {
			return createActorMovie;
		} else {		
			try {			
				advancedService.createActorFilm(
										actorMovie.getActorId(), 
										actorMovie.getMovieId());				
				return createActorMovieSuccess;
			} catch (DuplicateEntryException e) {			
				result.rejectValue("movieId", "duplicate", "entry already present");
				return createActorMovie;
			} catch (ActorNotFoundException e) {
				result.rejectValue("actorId", "notFound", "actor not found");			
				return createActorMovie;
			} catch (MovieNotFoundException e) {
				result.rejectValue("movieId", "notFound", "film not found");
				return createActorMovie;
			} catch(RuntimeException e) {
				return createActorMovie;
			}
		}		
	}
	
	@GetMapping(value = "createActorMovieTrans")
	public ModelAndView createActorMovieTrans(ModelMap model) {
		model.addAttribute("actorMovieTrans", new ActorMovieTransForm());
		return new ModelAndView(createActorMovieTrans, model);
	}
	
	@PostMapping(value = "createActorMovieTrans")
	public String createActorMovieTrans(
						@Valid @ModelAttribute("actorMovieTrans") ActorMovieTransForm form,
						BindingResult result, ModelMap model) 
	{
		if (result.hasErrors()) {
			return createActorMovieTrans;
		} else {			
			Actor actor = new Actor();
			actor.setFirstName(form.getFirstName());
			actor.setLastName(form.getLastName());
			actor.setBirthDate(form.getBirthDate());	
			try {
				advancedService.createActorFilmSpecial(
											actor, 
											form.getTitle(), 
											form.getReleaseDate());			
				return createActorMovieTransSuccess;
			} catch (com.dub.spring.exceptions.MovieNotFoundException e) {
				result.rejectValue("title", "notFound", "movie not found");						
				return createActorMovieTrans;
			} catch (DuplicateActorException e) {
				result.rejectValue("firstName", "duplicate", "actor already present");						
				return createActorMovieTrans;
			} catch (RuntimeException e) {
				return error;
			}
		}
	}
	
}