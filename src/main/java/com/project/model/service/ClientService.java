package com.project.model.service;

import com.project.controller.http.data.request.DataPageRequest;
import com.project.controller.http.data.request.user.client.ClientEditRequest;
import com.project.controller.http.data.request.user.client.ClientRegisterRequest;
import com.project.controller.http.exception.exception.ControllerException.ExceptionSubject;
import com.project.controller.http.exception.exception.DataAlreadyRegisteredException;
import com.project.controller.http.exception.exception.DataNotFoundException;
import com.project.model.dao.ClientDAO;
import com.project.model.dao.UserDAO;
import com.project.model.repository.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service @AllArgsConstructor
public class ClientService {

	private final ClientRepository repository;

	//region Getters
	public ClientDAO get(long id) {
		return repository.findByUserId(id).orElseThrow(() -> new DataNotFoundException(ExceptionSubject.ID));
	}
	public Page<ClientDAO> getAll(DataPageRequest request) {
		final int page = request.page() == null ? 0 : request.page();
		final int size = request.size() == null ? 10 : request.size();

		Sort sort = Sort.by(request.sortDirection() != null ? request.sortDirection() : Sort.Direction.ASC,
				request.sortProperty() != null ? request.sortProperty() : "clientName");

		Pageable pageable = PageRequest.of(page, size, sort);

		return repository.findAll(pageable);
	}
	//endregion

	//region Basic CRUD
	public ClientDAO save(UserDAO userDAO, ClientRegisterRequest request) {
		if (repository.existsById(userDAO.getId())) throw new DataAlreadyRegisteredException(ExceptionSubject.ID);

		var dao = new ClientDAO(userDAO, request.clientName(), request.nationality(), request.phoneNumber(), request.age());

		repository.save(dao);

		return dao;
	}
	public ClientDAO edit(Long id, ClientEditRequest request) {
		var dao = repository.findById(id).orElseThrow(() -> new DataNotFoundException(ExceptionSubject.USERNAME));
		if (request.clientName() != null) dao.setClientName(request.clientName());
		if (request.phoneNumber() != null) dao.setPhoneNumber(request.phoneNumber());
		if (request.age() != null) dao.setAge(request.age());
		return dao;
	}
	public void delete(Long id) {
		if (!repository.existsById(id)) throw new DataNotFoundException(ExceptionSubject.ID);
		repository.deleteById(id);
	}
	//endregion
}
