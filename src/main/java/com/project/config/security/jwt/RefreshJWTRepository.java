package com.project.config.security.jwt;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshJWTRepository  extends JpaRepository<RefreshJWTDAO,Long> {

	Optional<RefreshJWTDAO> findByAccessToken(String accessToken);
}
