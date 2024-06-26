package com.project.controller.http.data.request.user.client;

import com.project.controller.http.data.request.user.AuthenticationRegisterRequest;

public record ClientAuthenticationRegisterRequest(String email, String password, ClientRegisterRequest userSubtypeRequest) implements AuthenticationRegisterRequest { }
