package com.app.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.app.core.entity.model.UserModel;
import com.app.core.repository.UserRepository;

@SpringBootTest
public class UserServiceTest {

	@Autowired
	private UserService userService;
	@MockBean
	private UserRepository userRepository;
	
	@BeforeEach
	void setUp() {
		createNewUser();
	}
	
	private void createNewUser() {
		UserModel newUser1 = UserModel
				.builder()
				.id(1L)
				.firstname("giancarlo")
				.username("gianValentin")
				.password("1234")
				.lastname("valentin")
				.email("giancarlo@valentin.com")
				.build();
		
		UserModel newUser2 = UserModel
				.builder()
				.id(2L)
				.firstname("maria")
				.username("mariaCasas")
				.password("1234")
				.lastname("casas")
				.email("maria@casas.com")
				.build();
		
		Mockito.when(userRepository.findByFirstname("giancarlo")).thenReturn(Optional.of(newUser1));
		Mockito.when(userRepository.findAll()).thenReturn(List.of(newUser1, newUser2));
	}
	
	@Test
	@DisplayName("Find By Name Ignore Case Should Found")
	public void findByNameIgnoreCaseShouldFound() {
		String name = "giancarlo";
		UserModel user = userService.getByName(name);
		assertEquals(user.getFirstname(), name);
	}
	
	@Test
	@DisplayName("Find all case found all users")
	public void finadAllCaseFoundAllUsers() {
		List<UserModel> userList = userService.getAll();
		assertThat(userList).isNotNull();
        assertThat(userList.size()).isEqualTo(2);
	}
	
}
