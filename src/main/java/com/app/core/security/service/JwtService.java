package com.app.core.security.service;

import org.springframework.security.core.userdetails.UserDetails;

import com.app.core.entity.model.UserModel;

public interface JwtService {

	public String extractUsernameFromToken(String jwt);
	public String getToken(final UserModel userModel);
	public String getRefreshToken(final UserModel userModel);
	public boolean isTokenValid(String token, UserDetails userDetails);
}
