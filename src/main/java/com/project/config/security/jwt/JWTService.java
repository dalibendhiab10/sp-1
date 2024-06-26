package com.project.config.security.jwt;

import com.project.controller.http.exception.exception.ControllerException;
import com.project.controller.http.exception.exception.DataNotFoundException;
import com.project.utils.Utils.TimeUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class JWTService {

	private static final String SECRET_ACCESS_KEY = "79bc82e710dbb7c5da99986872abcb3339cc58e76d8895653afe0e5792f945d1";
	private static final String SECRET_REFRESH_KEY = "48bc82ee07c5d39a9aff99872ab986cb86c5653c58e76d882f0151a120a402e2";

	private final RefreshJWTRepository jwtRepository;

	//region Generation
	public String generateAccessToken(UserDetails userDetails) { return generateAccessToken(new HashMap<>(),userDetails); }
	public String generateAccessToken(Map<String, Object> extraClaims, UserDetails userDetails) {
		return Jwts.builder()
				.setClaims(extraClaims)
				.setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + TimeUtils.createLifetimeInMinutes(15)))
				.signWith(getAccessSigningKey(),SignatureAlgorithm.HS256)
				.compact();
	}

	public String generateRefreshToken(UserDetails userDetails) { return generateRefreshToken(new HashMap<>(),userDetails); }
	public String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails) {
		return Jwts.builder()
				.setClaims(extraClaims)
				.setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + TimeUtils.createLifetimeInDays(7)))
				.signWith(getRefreshSigningKey(),SignatureAlgorithm.HS256)
				.compact();
	}
	//endregion

	//region Repository Updates
	public void save(String refreshToken, String accessToken) { jwtRepository.save(new RefreshJWTDAO(refreshToken,accessToken)); }
	public String refreshAccessToken(String accessToken, UserDetails userDetails) throws DataNotFoundException {
		final var refreshToken = jwtRepository.findByAccessToken(accessToken).orElseThrow(() -> new DataNotFoundException(ControllerException.ExceptionSubject.ID));

		if (!isRefreshTokenValid(refreshToken.getValue(), userDetails)) throw new DataNotFoundException(ControllerException.ExceptionSubject.JWT);

		refreshToken.setAccessToken(generateAccessToken(userDetails));

		jwtRepository.save(refreshToken);

		return refreshToken.getAccessToken();
	}
	//endregion

	//region Reading
	public String extractRefreshUsername(String token) { return extractRefreshClaim(token,Claims::getSubject); }
	public String extractAccessUsername(String token) { return extractAccessClaim(token,Claims::getSubject); }

	public boolean isRefreshTokenValid(String token, UserDetails userDetails) {
		final var username = extractRefreshUsername(token);
		return username.equals(userDetails.getUsername()) && !isRefreshTokenExpired(token);
	}
	public boolean isAccessTokenValid(String token, UserDetails userDetails) {
		final var username = extractAccessUsername(token);
		return username.equals(userDetails.getUsername()) && !isAccessTokenExpired(token);
	}
	public boolean isRefreshTokenExpired(String token) { return extractRefreshExpiration(token).before(new Date()); }
	public boolean isAccessTokenExpired(String token) { return extractAccessExpiration(token).before(new Date()); }
	//endregion

	//region Aux Methods
	private Key getRefreshSigningKey() {
		byte[] bytes = Decoders.BASE64.decode(SECRET_REFRESH_KEY);
		return Keys.hmacShaKeyFor(bytes);
	}
	private Key getAccessSigningKey() {
		byte[] bytes = Decoders.BASE64.decode(SECRET_ACCESS_KEY);
		return Keys.hmacShaKeyFor(bytes);
	}

	private Claims extractAllRefreshClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getRefreshSigningKey()).build().parseClaimsJws(token).getBody();
	}
	private Claims extractAllAccessClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getAccessSigningKey()).build().parseClaimsJws(token).getBody();
	}

	private <T> T extractRefreshClaim(String token, Function<Claims,T> claimsResolver) { return claimsResolver.apply(extractAllRefreshClaims(token)); }
	private <T> T extractAccessClaim(String token, Function<Claims,T> claimsResolver) { return claimsResolver.apply(extractAllAccessClaims(token)); }

	private Date extractRefreshExpiration(String token) { return extractRefreshClaim(token,Claims::getExpiration); }
	private Date extractAccessExpiration(String token) { return extractAccessClaim(token,Claims::getExpiration); }

	//endregion
}
