package com.app.core.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.app.core.entity.model.UserModel;

public interface UserService {
	UserModel getByName(final String name);
	List<UserModel> getAll();
	UserModel save(final UserModel user);
	UserModel update(final UserModel user, final Long id);
	void deleteById(final Long id);
	Page<UserModel> finadAll(Pageable pageable);
}
