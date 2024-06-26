package com.project.config.security.jwt;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @AllArgsConstructor @NoArgsConstructor
@Entity @Table(name = "refresh_tokens")
public class RefreshJWTDAO {

	@Id @Column(name = "value") private String value;
	@Column(name = "access_token") private String accessToken;

	public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
}
