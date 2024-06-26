package com.project.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class Client implements EntityDTO {

	private final long id;
	private final String email, clientName, nationality, phoneNumber;
	private final int stayDuration;

	@Override public boolean equals(Object obj) {
		if (obj instanceof Client client) return this.id == client.id;
		else return false;
	}
}
