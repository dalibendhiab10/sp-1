package com.project.model.repository;

import com.project.model.dao.ClientDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<ClientDAO,Long> {

	Optional<ClientDAO> findByUserId(Long id);
}
