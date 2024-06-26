package com.project.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class Admin implements EntityDTO {

	private final long id;
	private final String email, adminName;
	private final int clearanceLevel;

	@Override public boolean equals(Object obj) {
		if (obj instanceof Admin admin) return this.id == admin.id;
		else return false;
	}
}
