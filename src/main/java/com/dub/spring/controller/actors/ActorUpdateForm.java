package com.dub.spring.controller.actors;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.dub.spring.validation.NotTooOld;

public class ActorUpdateForm {

	private long id;
	private String firstName;
	private String lastName;

	@NotTooOld(message = "{validate.birthDate.remote}")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@NotNull(message = "{validate.birthDate.required}")
	private Date birthDate;


	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
