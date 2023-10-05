package com.app.core.security.service;

import java.io.IOException;

import com.app.core.security.dto.AuthDto;
import com.app.core.security.dto.LoginDto;
import com.app.core.security.dto.RegisterDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

	AuthDto login(LoginDto dto);

	AuthDto register(RegisterDto dto);
	
	void refreshToken(final HttpServletRequest request, final HttpServletResponse response) throws IOException;
	
	boolean isUsernameValid(String username);
}
