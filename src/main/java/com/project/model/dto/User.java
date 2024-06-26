package com.project.model.dto;

import com.project.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class User implements EntityDTO {

	private final long id;
	private final String email, password;
	private final UserRole role;

	@Override public boolean equals(Object obj) {
		if (obj instanceof User client) return this.id == client.id;
		else return false;
	}
}
