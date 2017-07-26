package com.dub.spring.controller.movies;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;


import com.dub.spring.entities.DisplayMovie;
import com.dub.spring.entities.Movie;
import com.dub.spring.exceptions.DirectorNotFoundException;
import com.dub.spring.exceptions.DuplicateMovieException;
import com.dub.spring.exceptions.MovieNotFoundException;
import com.dub.spring.services.MovieService;


@Controller
public class MovieController {
	

	private static final String movieQueries = "movies/movieQueries";
	
	private static final String listMovies = "movies/listMovies";
	
	private static final String getMovie = "movies/getMovie";
	
	private static final String getSingleMovie = "movies/getSingleMovie";
	
	private static final String getMovieResult = "movies/getMovieResult";
	private static final String getMovieNoResult = "movies/getMovieNoResult";
	
	private static final String getSingleMovieResult = "movies/getSingleMovieResult";
	
	private static final String numberOfMovies = "movies/numberOfMovies";
	private static final String updateMovie1 = "movies/updateMovie1";
	private static final String updateMovie2 = "movies/updateMovie2";
	
	private static final String updateMovieSuccess = "movies/updateMovieSuccess";
	
	private static final String createMovie = "movies/createMovie";
	private static final String createMovieSuccess = "movies/createMovieSuccess";
	

	private static final String deleteMovie = "movies/deleteMovie";
	private static final String deleteMovieSuccess = "movies/deleteMovieSuccess";

	
	private static final String error = "error";
	
	private static final String accessDenied = "accessDenied";
	
	
	@Resource
	private MovieService movieService;
	
	
	@InitBinder({"movie", "movieKey"})	  
	protected void initBinder(WebDataBinder binder) {
	    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");		
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	
		
	@GetMapping("movieQueries")
	public String movieQueries() {
		return movieQueries;
	}
	
	@GetMapping("listAllMovies")
	public String listAllMoviesWithDirName(Map<String, Object> model) 
	{		
		List<DisplayMovie> list = movieService.getAllMovies();	
        model.put("movies", list);
        return listMovies;
	}
		
	@GetMapping("numberOfMovies")
	public String numberOfMovies(Map<String, Object> model) 
	{		
		Long number = movieService.numberOfMovies();
		model.put("number", number);
		return numberOfMovies;
	}
	
	@GetMapping("getMovie")
	public ModelAndView getMovie(ModelMap model) {
		model.addAttribute("getMovie", new TitleForm());
		return new ModelAndView(getMovie, model);
	}
	
	@PostMapping("getMovie")
	public String getMovie(
			@Valid @ModelAttribute("getMovie") TitleForm form,
			BindingResult result, ModelMap model) {
		
		if (result.hasErrors()) {
			return getMovie;
		} else {
			List<DisplayMovie> list = 
						movieService.getMovie(form.getTitle());
			if (list.size() > 0) {
				model.put("movies", list);
				return getMovieResult;
			} else { 				
				model.addAttribute("backPage", "getMovie");
				return getMovieNoResult;			
			}
		}
	}
	
	@GetMapping("getSingleMovie")
	public ModelAndView getSingleMovie(ModelMap model) {
		model.addAttribute("movieKey", new TitleDateForm());		
		return new ModelAndView(getSingleMovie, model);
	}
	
	@PostMapping("getSingleMovie")
	public String getSingleMovie(
			@Valid @ModelAttribute("movieKey") TitleDateForm form,
			BindingResult result, ModelMap model) 
	{	
		if (result.hasErrors()) {		
			return getSingleMovie;
		} else {	
			try {
				DisplayMovie displayMovie = movieService.getMovie(
								form.getTitle(), form.getReleaseDate());																
				model.put("movie", displayMovie);
				
				return getSingleMovieResult;		
			} catch (MovieNotFoundException e) {
				model.addAttribute("backPage", "getSingleMovie");
				return getMovieNoResult;	
			} catch (RuntimeException e) {
				return error;
			}	
		}
	}
	
	@GetMapping("createMovie")
	public ModelAndView createMovie(ModelMap model) {
		model.addAttribute("movie", new MovieForm());		
		return new ModelAndView(createMovie, model);
	}

	@PostMapping("createMovie")
	public String createMovie(
						@Valid @ModelAttribute("movie") MovieForm form,
						BindingResult result, ModelMap model) 
	{
		if (result.hasErrors()) {	
			return createMovie;
		} else {
			try {
				Movie movie = new Movie();
				movie.setDirectorId(form.getDirectorId());
				movie.setReleaseDate(form.getReleaseDate());
				movie.setRunningTime(form.getRunningTime());
				movie.setTitle(form.getTitle());
				movieService.createMovie(movie);
				model.addAttribute("movie", movie);
				return createMovieSuccess;		
			} catch (DuplicateMovieException e) {
				result.rejectValue("title", "duplicate", 
						"film already present");
				return createMovie;
			} catch (DirectorNotFoundException e) {
				result.rejectValue("directorId", "notFound", 
						"director not found");
				return createMovie;
			} catch (AccessDeniedException e) {
				return accessDenied;
			} catch (RuntimeException e) {
				return error;
			}
						
		}
	}
	
	@GetMapping("deleteMovie")
	public ModelAndView deleteMovie(ModelMap model) {
		model.addAttribute("movieId", new MovieIdForm());
		return new ModelAndView(deleteMovie, model);
	}
	
	@PostMapping("deleteMovie")
	public String deleteMovie(
						@Valid @ModelAttribute("movieId") MovieIdForm form,
						BindingResult result, ModelMap model) 
	{	
		if (result.hasErrors()) {
			return deleteMovie;
		} else {
			try {
				long id = form.getId();			
				movieService.deleteMovie(id);			
				return deleteMovieSuccess;
			} catch (MovieNotFoundException e) {
				result.rejectValue("id", "notFound", "movie not found");				
				return deleteMovie;
			} catch (AccessDeniedException e) {
				return accessDenied;
			} catch (RuntimeException e) {
				return error;
			}		
		}
	}
	
	
	@GetMapping("updateMovie")
	public ModelAndView updateMovie(ModelMap model) {
		model.addAttribute("movieId", new MovieIdForm());
		return new ModelAndView(updateMovie1, model);
	}
	
	@PostMapping("updateMovie1")
	public String updateMovie(
			@Valid @ModelAttribute("movieId") MovieIdForm form,
			BindingResult result, ModelMap model) {
		
		if (result.hasErrors()) {
			return updateMovie1;
		} else {
			try { 
				Movie movie = movieService.getMovie(form.getId());
				model.addAttribute("movie", movie);									
				return updateMovie2;
			} catch (MovieNotFoundException e) {
				result.rejectValue("id", "notFound", "movie not found");									
				return updateMovie1;
			} catch (RuntimeException e) {
				return error;
			}			
		}		 
	}
	
	@PostMapping("updateMovie2")
	public String updateMovie2(
			@Valid @ModelAttribute("movie") UpdateMovieForm form,
			BindingResult result, ModelMap model) {
		
		if (result.hasErrors()) {	
			return updateMovie2;
		} else {
			try {
				Movie movie = new Movie();
				movie.setDirectorId(form.getDirectorId());
				movie.setRunningTime(form.getRunningTime());
				movie.setId(form.getId());
				movie.setReleaseDate(form.getReleaseDate());
				movie.setTitle(form.getTitle());
				movieService.updateMovie(movie);			
				return updateMovieSuccess;
			} catch (DirectorNotFoundException e) {
				result.rejectValue("directorId", "notFound", 
								"director not found");				
				return updateMovie2;	
			} catch (AccessDeniedException e) {
				return accessDenied;
			} catch (RuntimeException e) {
				return error;
			}
		} 
	}	
}