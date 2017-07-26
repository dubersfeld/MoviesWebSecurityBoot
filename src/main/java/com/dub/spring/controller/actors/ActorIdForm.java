package com.dub.spring.controller.actors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ActorIdForm {

	@Min(value  = 1, message = "validate.min.actorId")
	@NotNull(message = "validate.required.actorId")
	private long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
