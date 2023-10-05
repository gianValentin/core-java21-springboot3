package com.app.core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.core.entity.model.UserModel;

public interface UserRepository extends JpaRepository<UserModel, Long>{
	Optional<UserModel> findByFirstname(final String name);
}
