package com.dub.spring.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.dub.spring.controller.actors.CreateActorPhoto;
import com.dub.spring.entities.Actor;
import com.dub.spring.entities.ActorPhoto;
//import com.dub.entities.ActorPhoto;
import com.dub.spring.exceptions.ActorNotFoundException;
import com.dub.spring.exceptions.DuplicateActorException;
import com.dub.spring.exceptions.PhotoNotFoundException;
import com.dub.spring.repositories.ActorPhotoRepository;
//import com.dub.spring.repositories.ActorPhotoRepository;
import com.dub.spring.repositories.ActorRepository;
import com.dub.spring.services.ActorService;


@Service
public class ActorServiceImpl implements ActorService {
	
	@Autowired
	private ActorRepository actorRepository;
	
	@Autowired
	private ActorPhotoRepository photoRepository;
	
	
	@Override
	public List<Actor> getAllActors() {
		
		return (List<Actor>)actorRepository.findAll();
	}

	@Override
	public void deleteActor(long id) {
		try {
			actorRepository.delete(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ActorNotFoundException();
		}
	}

	@Override
	@Transactional
	public void createActor(Actor actor) {
		try {
			actorRepository.save(actor);
		} catch (Exception e) {
			String ex = ExceptionUtils.getRootCauseMessage(e);
			if (ex.contains("actor_unique")) {
				throw new DuplicateActorException();
			} else {
				System.out.println("Exception caught " + e);
				throw e;
			}
		}
	}

	@Override
	public void updateActor(Actor actor) {
		if (actorRepository.exists(actor.getId())) {
			actorRepository.save(actor);
		} else {
			throw new ActorNotFoundException();
		}
	}

	@Override
	public long numberOfActors() {
		return actorRepository.count();
	}

	@Override
	public Actor getActor(long id) {
		if (actorRepository.exists(id)) {
			return actorRepository.findOne(id);		
		} else {
			throw new ActorNotFoundException();
		}
	}

	@Override
	public Actor getActor(String firstName, String lastName) {
		List<Actor> list = actorRepository.findByFirstNameAndLastName(firstName, lastName);
		if (!list.isEmpty()) {
			return list.get(0);
		} else {
			throw new ActorNotFoundException();
		}
	}

	
	@Override
	@Transactional
	public void createActorPhoto(CreateActorPhoto crPhoto) 
	throws FileNotFoundException, IOException {
		
		File blobIn = new File(crPhoto.getImageFile());
		InputStream blobIs = new FileInputStream(blobIn);
		ActorPhoto photo = new ActorPhoto();
		
		try {
			photo.setActorId(crPhoto.getActorId());
			byte[] data = new byte[blobIs.available()];
			blobIs.read(data);
			photo.setImageData(data);
			this.photoRepository.save(photo);
		} catch (Exception e) {
			String ex = ExceptionUtils.getRootCauseMessage(e);
			if (ex.contains("FOREIGN KEY")) {
				throw new ActorNotFoundException();
			} else {
				throw e;
			}
		} finally {
			blobIs.close();
		}
	}
	
	
	@Override
	public ActorPhoto getPhoto(long id) {	
		if (photoRepository.exists(id)) {
			return photoRepository.findOne(id);	
		} else {
			throw new PhotoNotFoundException();
		}
	}

	@Override
	public long getPhotoId(Actor actor) {
		
		List<Long> list = getAllPhotoIds(actor);
		
		
		if (list.size() > 0) {
			return list.get(0);
		} else {
			
			throw new PhotoNotFoundException();
		}
	}

	@Override
	public List<Long> getAllPhotoIds(Actor actor) {
		
		List<ActorPhoto> photos 
							= photoRepository.findByActorId(actor.getId());
		
		
		List<Long> photoIds = new ArrayList<Long>();
		
		for (ActorPhoto photo : photos) {
			photoIds.add(photo.getId());
		}
		return photoIds;
	}

	@Override
	public void deletePhoto(long id) {
		try {
			photoRepository.delete(id);
		} catch (EmptyResultDataAccessException e) {
			throw new PhotoNotFoundException();
		}
	}

	@Override
	public byte[] getPhotoData(long id) {
		if (photoRepository.exists(id)) {
			byte[] data = photoRepository.findOne(id).getImageData();
			if (data != null) {
				return data;
			} else {
				throw new PhotoNotFoundException();
			}
		} else {
			throw new PhotoNotFoundException();
		}
	}
	

}// class
