package com.app.core.security.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.app.core.entity.model.UserModel;
import com.app.core.security.service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class DefaultJwtService implements JwtService {
	
	@Value("${application.security.jwt.secret-key}")
	  private String SECRET_KEY;
	  @Value("${application.security.jwt.expiration}")
	  private long jwtExpiration;
	  @Value("${application.security.jwt.refresh-token.expiration}")
	  private long refreshExpiration;
	
	@Override
	public String extractUsernameFromToken(String token) {
		return getClaim(token, Claims::getSubject);
	}
	
	public <T> T getClaim(String token, Function<Claims, T> clamsResolver) {
		final Claims claims = extractAllClaims(token);
		return clamsResolver.apply(claims);
	}
	
	private Claims extractAllClaims(String token) {
		return Jwts
				.parserBuilder()
				.setSigningKey(getSignInKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	private SecretKey getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	@Override
	public String getToken(UserModel userModel) {
		return getToken(new HashMap<>(),userModel, jwtExpiration);
	}
	
	@Override
	public String getRefreshToken(
			UserModel userModel
	) {
	    return getToken(new HashMap<>(), userModel, refreshExpiration);
	}
	
	private String getToken(
			Map<String, Object> extraClains, 
			UserModel userModel,
			long expiration) {		
		return Jwts
				.builder()
				.setClaims(extraClains)
				.setSubject(userModel.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+expiration))
				.signWith(getSignInKey(), SignatureAlgorithm.HS256)
				.compact();
	}	

	@Override
	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = extractUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
	
	private boolean isTokenExpired(String token) {
		return getExpiration(token).before(new Date());
	}
	
	private Date getExpiration(String token) {
		return getClaim(token, Claims::getExpiration);
	}

}
