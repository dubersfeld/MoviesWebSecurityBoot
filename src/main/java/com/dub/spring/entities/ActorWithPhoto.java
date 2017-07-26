package com.dub.spring.entities;



public class ActorWithPhoto extends Actor {   

	/**
	 * Not an entity
	 */
	private static final long serialVersionUID = 1L;
	private long photoId;
	
	public ActorWithPhoto() {
	}
	
	public ActorWithPhoto(Actor source) {
		this.birthDate = source.birthDate;
		this.firstName = source.firstName;
		this.lastName = source.lastName;
		this.id = source.id; 
	}

	public long getPhotoId() {
		return photoId;
	}

	public void setPhotoId(long photoId) {
		this.photoId = photoId;
	}

	public String toString()  {
		return firstName + " " + lastName + " " + this.photoId;
	}
}