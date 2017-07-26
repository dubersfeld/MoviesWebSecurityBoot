package com.dub.spring.controller.actors;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.dub.spring.entities.Actor;
import com.dub.spring.entities.ActorWithPhoto;
import com.dub.spring.exceptions.ActorNotFoundException;
import com.dub.spring.exceptions.DuplicateActorException;
import com.dub.spring.exceptions.PhotoNotFoundException;
import com.dub.spring.services.ActorService;
import com.dub.spring.controller.NameForm;

@Controller
public class ActorController {
	
	private static final String actorQueries = "actors/actorQueries";
	private static final String numberOfActors = "actors/numberOfActors";
	private static final String getActor = "actors/getActor";
	private static final String getActorResult = "actors/getActorResult";
	private static final String getActorByName = "actors/getActorByName";
	
	private static final String getActorWithPhotoByName = "actors/getActorWithPhotoByName";
	
	private static final String getActorNoResult = "actors/getActorNoResult";
	
	private static final String getActorWithPhotoResult = "actors/getActorWithPhotoResult";
	
	private static final String allActors = "actors/allActors";
	
	private static final String createActor = "actors/createActor";
	private static final String createActorSuccess = "actors/createActorSuccess";
	
	private static final String listActorsWithPhoto = "actors/listActorsWithPhoto";
	
	private static final String getAllPhotosByActor = "actors/getAllPhotosByActor";
	
	private static final String getAllPhotosByActorResult = "actors/getAllPhotosByActorResult";
	
	private static final String updateActor1 = "actors/updateActor1";
	private static final String updateActor2 = "actors/updateActor2";
	private static final String updateActorSuccess = "actors/updateActorSuccess";
	
	
	private static final String deleteActor = "actors/deleteActor";
	private static final String deleteActorSuccess = "actors/deleteActorSuccess";
	
	private static final String deleteActorPhotoSuccess = "actors/deleteActorPhotoSuccess";
	
	
	private static final String createActorPhotoMultipart = "actors/createActorPhotoMultipart";
	private static final String createActorPhotoSuccess = "actors/createActorPhotoSuccess";
	
	private static final String deleteActorPhoto = "actors/deleteActorPhoto";
	
	private static final String error = "error";
	
	private static final String accessDenied = "accessDenied";
	
	
	@Autowired
	private ActorService actorService;
	  
	@GetMapping(value = "actorQueries")
		public String actorQueries() {
			return actorQueries;	
	}
	
	@GetMapping("numberOfActors")
	public String numberOfActors(Map<String, Object> model) {	
		Long number = actorService.numberOfActors();
		model.put("number", number);
		return numberOfActors;
	}// numberOfActors
	
	
	@GetMapping("getActor")
	public String actorIdForm(ModelMap model) {
		model.addAttribute("actorIdForm", new ActorIdForm());
		return getActor;
	}// getActor
	
	@PostMapping("getActor")
	public String actorIdSubmit(@Valid ActorIdForm actorIdForm,
			BindingResult result,
			ModelMap model) {	
		if (result.hasErrors()) {
            return getActor;
        }
		
		try  {
			Actor actor = actorService.getActor(actorIdForm.getId());
			
			model.put("actor", actor);
			
			return getActorResult;
		} catch (ActorNotFoundException e) {
			result.rejectValue("id", "notFound", "actor not found");	
			return getActor;
		} catch (RuntimeException e) {
			return error;
		}
	}
	
	@GetMapping("getActorByName")
	public String actorForm(ModelMap model) {
		model.addAttribute("actorName", new NameForm());
		return getActorByName;
	}
	
	@PostMapping("getActorByName")
	public String actorSubmit(@Valid @ModelAttribute("actorName") NameForm form,
			BindingResult bindingResult,
			ModelMap model) {		
		if (bindingResult.hasErrors()) {
            return getActorByName;
        }
				
		try {
			Actor actor = actorService.getActor(form.getFirstName(), 
				form.getLastName());
			
			model.put("actor", actor);
		
			return getActorResult;
		} catch (ActorNotFoundException e) {
			return getActorNoResult;
		} catch (RuntimeException e) {
			return error;
		}
	}// getActor
	
	@GetMapping("allActors")
    public ModelAndView allActors() {
       
		List<Actor> actors = actorService.getAllActors();

        Map<String, Object> params = new HashMap<>();
        params.put("actors", actors);

        return new ModelAndView(allActors, params);
    }
	
	@GetMapping(value = "createActor")
	public ModelAndView createActor(ModelMap model) {
		model.addAttribute("actorForm", new ActorForm());
		return new ModelAndView(createActor, model);
	}// createActor

	@PostMapping(value = "createActor")
	public String createActor(
			@Valid @ModelAttribute("actorForm") ActorForm form,
			BindingResult result, ModelMap model) {
		
		if (result.hasErrors()) {
			return createActor;
		} else {
			Actor actor = new Actor();
			actor.setFirstName(form.getFirstName());
			actor.setLastName(form.getLastName());
			actor.setBirthDate(form.getBirthDate());
			try {
				actorService.createActor(actor);	
				model.addAttribute("actor", actor);
				return createActorSuccess;
			} catch (DuplicateActorException e) {
				result.rejectValue("firstName", "duplicate", "name already present");				
				return createActor;
			} catch (AccessDeniedException e) {
				return accessDenied;
			} catch (RuntimeException e) {
				System.out.println("shit happaens " + e);
				return error;
			}
		}// if		
	}// createActor
	
	
	@GetMapping(value = "updateActor")
	public ModelAndView updateActor(ModelMap model) {
		model.addAttribute("getActor", new ActorIdForm());
		return new ModelAndView(updateActor1, model);
	}// updateActor
			
	@PostMapping(value = "updateActor1")
	public String updateActor(
				@Valid @ModelAttribute("getActor") ActorIdForm form,
				BindingResult result, ModelMap model) {		
		if (result.hasErrors()) {
			return updateActor1;
		} else {
			try {
				long actorId = form.getId();
				Actor actor = actorService.getActor(actorId);
				model.addAttribute("actor", actor);
				return updateActor2;
			} catch (ActorNotFoundException e) {
				result.rejectValue("id", "notFound", "actor not found");							
				return updateActor1;
			} catch (RuntimeException e) {
				return error;
			}// try
		}// if				
	}// updateActor
	
	@PostMapping(value = "updateActor2")
	public String updateActor2(
						@Valid @ModelAttribute("actor") ActorUpdateForm form,
						BindingResult result, ModelMap model) {		
		if (result.hasErrors()) {
			return updateActor2;			
		} else {
			try {
				Actor actor = new Actor();
				actor.setFirstName(form.getFirstName());
				actor.setLastName(form.getLastName());
				actor.setBirthDate(form.getBirthDate());
				actor.setId(form.getId());
				actorService.updateActor(actor);			
				return updateActorSuccess;
			} catch (AccessDeniedException e) {
				return accessDenied;
			} catch (RuntimeException e) {
				return error;
			}				
		}// if		
	}// updateActor
	
	
	@GetMapping(value = "deleteActor")
	public ModelAndView deleteActor(ModelMap model) {
		model.addAttribute("getActor", new ActorIdForm());
		return new ModelAndView(deleteActor, model);
	}// deleteActor
	
	@PostMapping(value = "deleteActor")
	public String deleteActor(
			@Valid @ModelAttribute("getActor") ActorIdForm form,
			BindingResult result, ModelMap model) {	
		if (result.hasErrors()) {
			return deleteActor;
		} else {
			try {
				long actorId = form.getId();
				Actor actor = actorService.getActor(actorId);
				model.addAttribute("actor", actor);
				actorService.deleteActor(actorId);		
				return deleteActorSuccess;
			} catch (ActorNotFoundException e) {
				result.rejectValue("id", "notFound", "actor not found");				
				return deleteActor;
			} catch (AccessDeniedException e) {
				return accessDenied;
			} catch (RuntimeException e) {
				return error;
			}// try								
		}
	}// deleteActor
	
	@GetMapping("createActorPhotoMulti")
	public ModelAndView createActorPhotoMulti(ModelMap model) {
		model.addAttribute("actorPhotoMulti", new ActorPhotoMultiForm());
		return new ModelAndView(createActorPhotoMultipart, model);
	}// getActor

	@PostMapping("createActorPhotoMulti")      	 
	public String uploadActorPhoto(
            @Valid @ModelAttribute("actorPhotoMulti") ActorPhotoMultiForm form, 
            BindingResult result) {	 
			
		if (result.hasErrors()) {
			return createActorPhotoMultipart;
		}
		// Get name of uploaded file.
		MultipartFile uploadedFileRef = null;
		String fileName;
		long actorId= form.getActorId();
	 
		uploadedFileRef = form.getUploadedFile();
		fileName = form.getUploadedFile().getOriginalFilename();
	 
		String path = "/home/dominique/Pictures/tmp/" + fileName; 
       	 
		File outputFile = new File(path);
	 
		InputStream is = null;     
		OutputStream os = null;
	
		// This buffer will store the data read from 'uploadedFileRef'
		byte[] buffer = new byte[1000];
		int bytesRead = -1;
		int totalBytes = 0;
    
		// actual photo upload
		
		try {
			is = uploadedFileRef.getInputStream();
			os = new FileOutputStream(outputFile);
		
			while ((bytesRead = is.read(buffer)) != -1) {
				os.write(buffer);
				totalBytes += bytesRead;
			}
	
			if (totalBytes == 0 || totalBytes != uploadedFileRef.getSize()) {
				return error;
			}
		} catch (FileNotFoundException e) {
			result.rejectValue("uploadedFile", "notFound", "file not found");
			return createActorPhotoMultipart;	
		} catch (AccessDeniedException e) {
			return accessDenied;
		} catch (Exception e) {
			e.printStackTrace();	
		} finally{             
			try {
				is.close();
				os.close();
			} catch (NullPointerException e) {
				return createActorPhotoMultipart;	
			} catch (IOException e) {
				e.printStackTrace();
			}
		}// try
    	    	   	      
		// Now persist actor photo to the database  
		   
		CreateActorPhoto createActorPhoto = new CreateActorPhoto();	        
		createActorPhoto.setActorId(actorId);	        
		createActorPhoto.setImageFile(path);
        
		try {
			if (totalBytes > 65535) {
				// photo size too large for a BLOB					 
				result.rejectValue("uploadedFile", "size", "file exceeds 65535 bytes");
				return createActorPhotoMultipart;	
			}
			actorService.createActorPhoto(createActorPhoto);
		} catch (ActorNotFoundException e) {
			result.rejectValue("actorId", "notFound", "actor not found");							
			return createActorPhotoMultipart;	
		} catch (IOException e) {
			return error;
		} catch (RuntimeException e) {
			return error;
		} finally {
			// always delete temporary file			
			try {
				Files.deleteIfExists(outputFile.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}	  
		}
		return createActorPhotoSuccess; 
	}// uploadActorPhoto

	
	@GetMapping(value = "listAllActorsWithPhotos")
	public String listAllActorsWithPhotos(Map<String, Object> model) {				
		List<Actor> list;
		List<ActorWithPhoto> actors = new ArrayList<>();		
		try {
			list = actorService.getAllActors();		
			for (Actor actor : list) {
				ActorWithPhoto actw = new ActorWithPhoto(actor);
				try {
					long photoId = actorService.getPhotoId(actor);
					actw.setPhotoId(photoId);
				} catch(PhotoNotFoundException e) {
					actw.setPhotoId(0);
				}									
				actors.add(actw);
			}// for
			model.put("actors", actors);	
			return listActorsWithPhoto;
		} catch (RuntimeException e) {
			return error;
		}
        
	}// listAllActorsWithPhoto 
	
	@GetMapping("getAllPhotosByActor")
	public ModelAndView getAllPhotosByActor(ModelMap model) {
		model.addAttribute("actorName", new NameForm());
		return new ModelAndView(getAllPhotosByActor, model);
	}
		
	@PostMapping("getAllPhotosByActor")
	public String getAllPhotosByActor(
				@Valid @ModelAttribute("actorName") NameForm form,
				BindingResult result, ModelMap model) {			
		if (result.hasErrors()) {
			return getAllPhotosByActor;
		} else {				
			try {
				Actor actor = actorService.getActor(
												form.getFirstName(),
												form.getLastName());
				List<Long> photoIds = 
							actorService.getAllPhotoIds(actor);
				model.addAttribute("photoIds", photoIds);
				model.addAttribute("firstName", actor.getFirstName());
				model.addAttribute("lastName", actor.getLastName());
				return getAllPhotosByActorResult;
			} catch (ActorNotFoundException e) {				
				model.addAttribute("backPage", "getAllPhotosByActor");
				return getActorNoResult;
			} catch (RuntimeException e) {
				e.printStackTrace();
				return error;
			}// try
		}
	}
	

	@GetMapping("getActorWithPhotoByName")
	public ModelAndView getActorWithPhotoByName(ModelMap model) {
		model.addAttribute("actorName", new NameForm());
		return new ModelAndView(getActorWithPhotoByName, model);
	}
	
	@PostMapping("getActorWithPhotoByName")
	public String getActorWithPhotoByName(
				@Valid @ModelAttribute("actorName") NameForm form,
				BindingResult result, ModelMap model) {
			
		if (result.hasErrors()) {
			return getActorWithPhotoByName;
		} else {
			try {								
				Actor actor = actorService.getActor(
												form.getFirstName(), 
												form.getLastName());
				ActorWithPhoto actw = new ActorWithPhoto(actor);
				try {
					long photoId = actorService.getPhotoId(actor);
					actw.setPhotoId(photoId);	
				} catch (PhotoNotFoundException e) {
					actw.setPhotoId(0);	
				} 											
				model.addAttribute("actor", actw);	
				return getActorWithPhotoResult;
			} catch (ActorNotFoundException e) {
				model.addAttribute("backPage", "getActorWithPhotoByName");
				return getActorNoResult;
			} catch (RuntimeException e) {
				e.printStackTrace();
				return error;
			}
		}// if
	}// getActorWithPhotoByName

	
	@GetMapping("deleteActorPhoto")
	public ModelAndView deleteActorPhoto(ModelMap model) {
		model.addAttribute("getActorPhoto", new PhotoIdForm());
		return new ModelAndView(deleteActorPhoto, model);
	}// deleteActorPhoto
	
	@PostMapping("deleteActorPhoto")
	public String deleteActorPhoto(
			@Valid @ModelAttribute("getActorPhoto") PhotoIdForm form,
			BindingResult result, ModelMap model) {	
		if (result.hasErrors()) {
			return deleteActorPhoto;
		}
		try {
			long photoId = form.getId();
			actorService.deletePhoto(photoId);
			return deleteActorPhotoSuccess;
		} catch (PhotoNotFoundException e) {
			result.rejectValue("id", "notFound", "photo not found");				
			return deleteActorPhoto;
		} catch (AccessDeniedException e) {
			return accessDenied;
		} catch (RuntimeException e) {	
			return error;
		}
	}// deleteActorPhoto

	
	
	@GetMapping("doGetActorPhoto/{photoId}")
	public @ResponseBody byte[] doGetActorPhoto(@PathVariable("photoId") long photoId)  {
		try {
			byte[] imageBytes = actorService.getPhotoData(photoId);
			return imageBytes;
		} catch (PhotoNotFoundException e) {
			return null;
		}// try
	}
	

}
