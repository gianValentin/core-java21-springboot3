package com.app.core.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.core.security.entity.SecurityUser;

public interface SecurityUserRepository  extends JpaRepository<SecurityUser, Long>{	
	Optional<SecurityUser> findByUsername(final String name);
	boolean existsSecurityUserByUsername(final String username);
}
