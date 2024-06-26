package com.project.model.service;

import com.project.controller.http.data.request.user.admin.AdminRegisterRequest;
import com.project.controller.http.exception.exception.ControllerException.ExceptionSubject;
import com.project.controller.http.exception.exception.DataAlreadyRegisteredException;
import com.project.controller.http.exception.exception.DataNotFoundException;
import com.project.model.dao.AdminDAO;
import com.project.model.dao.UserDAO;
import com.project.model.repository.AdminRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminService {

	private final AdminRepository repository;

	public AdminService(AdminRepository repository) {
		this.repository = repository;
	}

	//region CRUD

	public AdminDAO get(long id) throws DataNotFoundException {
		return repository.findByUserId(id).orElseThrow(() -> new DataNotFoundException(ExceptionSubject.ID));
	}
	public AdminDAO save(UserDAO userDAO, AdminRegisterRequest request) {
		if (repository.existsById(userDAO.getId())) throw new DataAlreadyRegisteredException(ExceptionSubject.ID);

		var dao = new AdminDAO(userDAO,request.adminName(), request.clearanceLevel());

		repository.save(dao);

		return dao;
	}
	//endregion
}
