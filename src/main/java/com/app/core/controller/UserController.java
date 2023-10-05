package com.app.core.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.core.entity.dto.GetAllUserDto;
import com.app.core.entity.dto.GetUserDto;
import com.app.core.entity.dto.PostUserDto;
import com.app.core.entity.dto.PutUserDto;
import com.app.core.entity.model.UserModel;
import com.app.core.exception.CJNotFoundException;
import com.app.core.service.UserService;
import com.app.core.utils.CustomCodeException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/user")
@Tag(name = "User",description = "User controller")
public class UserController {

	private final UserService userService;
	private final ModelMapper modelMapper;

	@Operation(description = "Get pageable endpoint for user", summary = "This is a summary for user get pageable endpoint")
	@GetMapping(value = "/pageable")
	public ResponseEntity<Page<GetUserDto>> getPageable(Pageable pageable) {
		Page<UserModel> userPage = userService.finadAll(pageable);
		Page<GetUserDto> userPageDto = userPage.map(user -> modelMapper.map(user, GetUserDto.class));
		return ResponseEntity.status(HttpStatus.OK).body(userPageDto);
	}

	@Operation(description = "Get all endpoint for user", summary = "This is a summary for user get all endpoint")
	@GetMapping
	public ResponseEntity<GetAllUserDto> getAll() {
		// Get All user for user services
		List<UserModel> user = userService.getAll();
		// Map user model to DTO objects
		List<GetUserDto> getUserDtos = user.stream().map(userModel -> modelMapper.map(userModel, GetUserDto.class))
				.collect(Collectors.toList());
		// create DTO for all user DTO and size
		GetAllUserDto getAllUserDto = new GetAllUserDto(getUserDtos, getUserDtos.size());
		// return endpoint
		return ResponseEntity.status(HttpStatus.OK).body(getAllUserDto);
	}

	@Operation(description = "Get by name endpoint for user", summary = "This is a summary for user get by name endpoint")
	@GetMapping("/{name}/name")
	public ResponseEntity<GetUserDto> getUserByName(@PathVariable String name) {
		UserModel userDb = userService.getByName(name);
		if(ObjectUtils.isEmpty(userDb))
			throw new CJNotFoundException(CustomCodeException.CODE_400, "user not found with name "+name);
		GetUserDto getUserDto = modelMapper.map(userDb, GetUserDto.class);
		return ResponseEntity.status(HttpStatus.OK).body(getUserDto);
	}

	@Operation(description = "Save  endpoint for user", summary = "This is a summary for user save endpoint")
	@PostMapping
	public ResponseEntity<GetUserDto> saveUser(@Valid @RequestBody PostUserDto dto) {
		UserModel user = modelMapper.map(dto, UserModel.class);
		UserModel userDb = userService.save(user);
		GetUserDto getUserDto = modelMapper.map(userDb, GetUserDto.class);
		return ResponseEntity.status(HttpStatus.CREATED).body(getUserDto);
	}

	@Operation(description = "Update  endpoint for user", summary = "This is a summary for user update endpoint")
	@PutMapping(value = "/{id}")
	public ResponseEntity<GetUserDto> updateUser(@Valid @RequestBody PutUserDto dto,
			@PathVariable(name = "id") Long id) {
		// Map DTO to Model object for service
		UserModel user = modelMapper.map(dto, UserModel.class);
		// Send object to update service
		UserModel userDb = userService.update(user, id);
		// Map Model to DTO object for return endpoint
		GetUserDto getUserDto = modelMapper.map(userDb, GetUserDto.class);
		// Return endpoint
		return ResponseEntity.status(HttpStatus.OK).body(getUserDto);
	}

	@Operation(description = "Delete  endpoint for user", summary = "This is a summary for user delete endpoint")
	@DeleteMapping(value = "{id}")
	public ResponseEntity<GetUserDto> deleteUser(@PathVariable(name = "id") Long id) {
		// Send id to delete service
		userService.deleteById(id);
		// return endpoint
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
