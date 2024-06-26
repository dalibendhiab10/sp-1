package com.project.controller.http.data.request;

import org.springframework.data.domain.Sort;

public record DataPageRequest(Integer page, Integer size, String sortProperty, Sort.Direction sortDirection) {}
