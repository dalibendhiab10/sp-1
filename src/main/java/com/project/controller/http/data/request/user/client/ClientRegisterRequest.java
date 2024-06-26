package com.project.controller.http.data.request.user.client;

public record ClientRegisterRequest(String clientName, String nationality,String phoneNumber, Integer age) {}
