package com.project.config.security.jwt;

import com.project.controller.http.exception.exception.DataNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
@AllArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter {



	public static final String AUTHORIZATION_HEADER = "Authorization",
			ACCESS_TOKEN = "jwt-access-token";

	private final JWTService jwtService;
	private final UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
			throws IOException, ServletException {

		Cookie accessTokenCookie = null;
		String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

		if (authorizationHeader == null || authorizationHeader.isBlank()) {
			try {
				for (var cookie : request.getCookies()) {
					if (cookie.getName().equals(ACCESS_TOKEN) && cookie.isHttpOnly()) {
						accessTokenCookie = cookie;
						break;
					}
				}
			}
			catch (Exception ignored) {}
		};

		if (accessTokenCookie == null && (authorizationHeader == null || authorizationHeader.isBlank())) {
			filterChain.doFilter(request,response);
			return;
		}

		String accessToken, accessUsername;

		accessToken = (authorizationHeader == null || authorizationHeader.isBlank()) ?  accessTokenCookie.getValue() : authorizationHeader;

		try {
			accessUsername = jwtService.extractAccessUsername(accessToken);
		}
		catch (JwtException e) {
			filterChain.doFilter(request,response);
			return;
		}

		if (accessUsername != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(accessUsername);

			if (jwtService.isAccessTokenExpired(accessToken)) {
				try {
					accessToken = jwtService.refreshAccessToken(accessToken, userDetails);
				}
				catch (DataNotFoundException ignored) {
					filterChain.doFilter(request,response);
					return;
				}
			}

			if (jwtService.isAccessTokenValid(accessToken,userDetails)) {
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}

		filterChain.doFilter(request,response);
	}
}
