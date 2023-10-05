package com.app.core.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.app.core.entity.model.UserModel;
import com.app.core.exception.CJNotFoundException;
import com.app.core.repository.UserRepository;
import com.app.core.utils.CustomCodeException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DefaultUserService implements UserService {

	private final UserRepository userRepository;

	@Override
	@Transactional(readOnly = true)
	public UserModel getByName(String name) {
		Assert.notNull(name, "name cannot be null");
		return userRepository.findByFirstname(name).orElseThrow(
				() -> new CJNotFoundException(CustomCodeException.CODE_400, "user not found with name "+name));
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserModel> getAll() {
		return userRepository.findAll();
	}

	@Override
	@Transactional
	public UserModel save(UserModel user) {
		Assert.notNull(user, "user cannot be null");
		return userRepository.save(user);
	}

	@Override
	@Transactional
	public UserModel update(UserModel user, Long id) {

		Assert.notNull(id, "id cannot be null");
		Assert.notNull(user, "user cannot be null");

		UserModel userDb = userRepository.findById(id)
				.orElseThrow(() -> new CJNotFoundException(CustomCodeException.CODE_400, "user not found"));

		userDb.setUsername(user.getUsername());
		userDb.setFirstname(user.getFirstname());
		userDb.setLastname(user.getLastname());
		userDb.setPassword(user.getPassword());
		userDb.setEmail(user.getEmail());		

		return userRepository.save(userDb);
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		Assert.notNull(id, "id cannot be null");
		UserModel userDb = userRepository.findById(id)
				.orElseThrow(() -> new CJNotFoundException(CustomCodeException.CODE_400, "user not found"));
		userRepository.delete(userDb);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<UserModel> finadAll(Pageable pageable) {
		return userRepository.findAll(pageable);
	}

}
