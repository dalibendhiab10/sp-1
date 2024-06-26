package com.project.controller.http.data.request.user.admin;

import com.project.controller.http.data.request.user.AuthenticationRegisterRequest;

public record AdminAuthenticationRegisterRequest(String email, String password, AdminRegisterRequest userSubtypeRequest) implements AuthenticationRegisterRequest { }
