package com.dub.spring.services;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import com.dub.spring.entities.Director;

@PreAuthorize("hasAuthority('VIEW')")
public interface DirectorService {

	List<Director> getAllDirectors();
	
	Director getDirector(long id);	
	Director getDirector(String firstName, String lastName);
	
	@PreAuthorize("hasAuthority('DELETE')")
	void deleteDirector(long id);
	
	@PreAuthorize("hasAuthority('CREATE')")
	void createDirector(Director director);
	
	@PreAuthorize("hasAuthority('UPDATE')")
	void updateDirector(Director director);
	
	long numberOfDirectors();
}

