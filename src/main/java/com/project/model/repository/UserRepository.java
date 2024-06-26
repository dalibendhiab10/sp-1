package com.project.model.repository;

import com.project.model.dao.UserDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserDAO,Long> {
	Optional<UserDAO> findByEmail(String email);
}
