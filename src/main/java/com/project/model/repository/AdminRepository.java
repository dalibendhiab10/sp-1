package com.project.model.repository;

import com.project.model.dao.AdminDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<AdminDAO,Long> {

	Optional<AdminDAO> findByUserId(Long id);
}
