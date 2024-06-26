package com.project.config.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationContext {

	public Authentication current() { return SecurityContextHolder.getContext().getAuthentication(); }
}
