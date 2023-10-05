package com.app.core.security.service.impl;

import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.core.security.dto.AuthDto;
import com.app.core.security.dto.LoginDto;
import com.app.core.security.dto.RegisterDto;
import com.app.core.security.entity.SecurityUser;
import com.app.core.security.entity.Token;
import com.app.core.security.entity.TokenType;
import com.app.core.security.repository.SecurityUserRepository;
import com.app.core.security.repository.TokenRepository;
import com.app.core.security.service.AuthService;
import com.app.core.security.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultAuthService implements AuthService {
	
	private final SecurityUserRepository securityUserRepository;
	private final TokenRepository tokenRepository;
	private final JwtService jwtService;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final ModelMapper modelMapper;

	@Override
	@Transactional
	public AuthDto login(LoginDto dto) {
		
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
		
		SecurityUser user = securityUserRepository.findByUsername(dto.getUsername()).orElseThrow();
		String token = jwtService.getToken(user);
		String refreshtoken = jwtService.getRefreshToken(user);
		
		revokeAllUserTokens(user);
		saveUserToken(user, token);
		
		return AuthDto.builder()
				.accessToken(token)
				.refreshToken(refreshtoken)
				.build();
	}

	@Override
	@Transactional
	public AuthDto register(RegisterDto dto) {		
		
		dto.setPassword(passwordEncoder.encode(dto.getPassword()));
		SecurityUser user = modelMapper.map(dto, SecurityUser.class);
		
		SecurityUser userSaved = securityUserRepository.save(user);
		String token = jwtService.getToken(user);
		String refreshtoken = jwtService.getRefreshToken(user);
				
		saveUserToken(userSaved, token);
		
		return AuthDto.builder()
				.accessToken(token)
				.refreshToken(refreshtoken)
				.build();
	}
	
	private void saveUserToken(SecurityUser user, String jwtToken) {
	    Token token = Token.builder()
	        .user(user)
	        .token(jwtToken)
	        .tokenType(TokenType.BEARER)
	        .expired(false)
	        .revoked(false)
	        .build();
	    tokenRepository.save(token);
	  }
	
	private void revokeAllUserTokens(SecurityUser user) {
	    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
	    if (validUserTokens.isEmpty()) {
	    	return;	
	    }
	    
	    validUserTokens.forEach(token -> {
	      token.setExpired(true);
	      token.setRevoked(true);
	    });
	    tokenRepository.saveAll(validUserTokens);
	  }

	@Override
	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
	    final String refreshToken;
	    final String username;
	    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
	      return;
	    }
	    refreshToken = authHeader.substring(7);
	    username = jwtService.extractUsernameFromToken(refreshToken);
	    if (username != null) {
	      SecurityUser user = securityUserRepository.findByUsername(username)
	              .orElseThrow();
	      if (jwtService.isTokenValid(refreshToken, user)) {
	        var accessToken = jwtService.getToken(user);
	        revokeAllUserTokens(user);
	        saveUserToken(user, accessToken);
	        AuthDto authResponse = AuthDto.builder()
	                .accessToken(accessToken)
	                .refreshToken(refreshToken)
	                .build();
	        response.setContentType("application/json");
	        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
	      }
	    }
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isUsernameValid(String username) {		
		return !securityUserRepository.existsSecurityUserByUsername(username);
	}

}
