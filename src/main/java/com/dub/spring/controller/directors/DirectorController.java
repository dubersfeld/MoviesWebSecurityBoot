package com.dub.spring.controller.directors;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.dub.spring.entities.Director;
import com.dub.spring.exceptions.DirectorNotFoundException;
import com.dub.spring.exceptions.DuplicateDirectorException;
import com.dub.spring.services.DirectorService;
import com.dub.spring.controller.directors.DirectorIdForm;
import com.dub.spring.controller.directors.DirectorUpdateForm;
import com.dub.spring.controller.NameForm;

@Controller
public class DirectorController {
	 
	private static final String directorQueries = "directors/directorQueries";
	private static final String numberOfDirectors = "directors/numberOfDirectors";
	private static final String getDirector = "directors/getDirector";
	private static final String getDirectorResult = "directors/getDirectorResult";
	private static final String getDirectorByName = "directors/getDirectorByName";
	
	private static final String getDirectorNoResult = "directors/getDirectorNoResult";
	
	private static final String allDirectors = "directors/allDirectors";
	
	private static final String createDirector = "directors/createDirector";
	private static final String createDirectorSuccess = "directors/createDirectorSuccess";
	
	private static final String updateDirector1 = "directors/updateDirector1";
	private static final String updateDirector2 = "directors/updateDirector2";
	private static final String updateDirectorSuccess = "directors/updateDirectorSuccess";
	
	
	private static final String deleteDirector = "directors/deleteDirector";
	private static final String deleteDirectorSuccess = "directors/deleteDirectorSuccess";
	
	private static final String error = "error";
	
	private static final String accessDenied = "accessDenied";
	
	
	
	@Autowired
	private DirectorService directorService;
	  
	@GetMapping("directorQueries")
		public String directorQueries() {
		
			return directorQueries;	
	}
	
	@GetMapping("numberOfDirectors")
	public String numberOfDirectors(Map<String, Object> model) {
	
		Long number = directorService.numberOfDirectors();
		model.put("number", number);
		return numberOfDirectors;
	}// numberOfDirectors
	
	
	@GetMapping("getDirector")
	public String directorIdForm(ModelMap model) {
		
		model.addAttribute("directorIdForm", new DirectorIdForm());
		return getDirector;
	}// getDirector
	
	@PostMapping("getDirector")
	public String directorIdSubmit(@Valid DirectorIdForm directorIdForm,
			BindingResult result,
			ModelMap model) {

		if (result.hasErrors()) {
            return getDirector;
        }
		
		try  {
			Director actor = directorService.getDirector(directorIdForm.getId());
			
			model.put("actor", actor);
			
			return getDirectorResult;
		} catch (DirectorNotFoundException e) {
			result.rejectValue("id", "notFound", "actor not found");	
			return getDirector;
		} catch (RuntimeException e) {
			return error;
		}
		
	}
	
	@GetMapping("getDirectorByName")
	public String directorForm(ModelMap model) {
		
		model.addAttribute("directorName", new NameForm());
		return getDirectorByName;
	}// getDirector
	
	@PostMapping("getDirectorByName")
	public String directorSubmit(@Valid @ModelAttribute("directorName") NameForm form,
			BindingResult bindingResult,
			ModelMap model) {

		
		if (bindingResult.hasErrors()) {
            return getDirectorByName;
        }
			
		try {
			Director director = directorService.getDirector(form.getFirstName(), 
				form.getLastName());
		
			model.put("director", director);
		
			return getDirectorResult;
		} catch (DirectorNotFoundException e) {
			return getDirectorNoResult;
		} catch (RuntimeException e) {
			return error;
		}
	}// getDirector
	
	@GetMapping("allDirectors")
    public ModelAndView allDirectors() {
       
		List<Director> directors = directorService.getAllDirectors();

        Map<String, Object> params = new HashMap<>();
        params.put("directors", directors);

        return new ModelAndView(allDirectors, params);
    }
	
	@GetMapping(value = "createDirector")
	public ModelAndView createDirector(ModelMap model) {
		model.addAttribute("directorForm", new DirectorForm());
		
		return new ModelAndView(createDirector, model);
	}// createDirector

	@PostMapping(value = "createDirector")
	public String createDirector(
			@Valid @ModelAttribute("directorForm") DirectorForm form,
			BindingResult result, ModelMap model) {
		
		if (result.hasErrors()) {
			return createDirector;
		} else {
			Director director = new Director();
			director.setFirstName(form.getFirstName());
			director.setLastName(form.getLastName());
			director.setBirthDate(form.getBirthDate());
			try {
				directorService.createDirector(director);	
				model.addAttribute("director", director);
				return createDirectorSuccess;
			} catch (DuplicateDirectorException e) {
				
				result.rejectValue("firstName", "duplicate", "name already present");				
				return createDirector;
			} catch (AccessDeniedException e) {
				return accessDenied;
			} catch (RuntimeException e) {
				return error;
			}
		}// if		
	}// createDirector
	
	
	@GetMapping(value = "updateDirector")
	public ModelAndView updateDirector(ModelMap model) {
		
		model.addAttribute("getDirector", new DirectorIdForm());
		return new ModelAndView(updateDirector1, model);
	}// updateDirector
			
	@PostMapping(value = "updateDirector1")
	public String updateDirector(
				@Valid @ModelAttribute("getDirector") DirectorIdForm form,
				BindingResult result, ModelMap model) {		
		if (result.hasErrors()) {
	
			return updateDirector1;
		} else {
			try {
				long directorId = form.getId();
				Director director = directorService.getDirector(directorId);
				model.addAttribute("director", director);
				return updateDirector2;
			} catch (DirectorNotFoundException e) {
				result.rejectValue("id", "notFound", "director not found");							
				return updateDirector1;
			} catch (RuntimeException e) {
				return error;
			}// try
		}// if				
	}// updateDirector
	
	@PostMapping(value = "updateDirector2")
	public String updateDirector2(
						@Valid @ModelAttribute("director") DirectorUpdateForm form,
						BindingResult result, ModelMap model) {		
		if (result.hasErrors()) {
			return updateDirector2;			
		} else {
			try {
				Director director = new Director();
				director.setFirstName(form.getFirstName());
				director.setLastName(form.getLastName());
				director.setBirthDate(form.getBirthDate());
				director.setId(form.getId());
				directorService.updateDirector(director);			
				return updateDirectorSuccess;
			} catch (AccessDeniedException e) {
				return accessDenied;
			} catch (RuntimeException e) {
				return error;
			}				
		}// if		
	}// updateDirector
	
	
	@GetMapping(value = "deleteDirector")
	public ModelAndView deleteDirector(ModelMap model) {

		model.addAttribute("getDirector", new DirectorIdForm());
		return new ModelAndView(deleteDirector, model);
	}// deleteDirector
	
	@PostMapping(value = "deleteDirector")
	public String deleteDirector(
			@Valid @ModelAttribute("getDirector") DirectorIdForm form,
			BindingResult result, ModelMap model) {	
		if (result.hasErrors()) {
			return deleteDirector;
		} else {
			try {
				long directorId = form.getId();
				Director director = directorService.getDirector(directorId);
				model.addAttribute("director", director);
				directorService.deleteDirector(directorId);		
				return deleteDirectorSuccess;
			} catch (DirectorNotFoundException e) {
				result.rejectValue("id", "notFound", "director not found");				
				return deleteDirector;
			} catch (AccessDeniedException e) {
				return accessDenied;
			} catch (RuntimeException e) {
				return error;
			}// try								
		}
	}// deleteDirector

}
