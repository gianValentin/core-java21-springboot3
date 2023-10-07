package com.app.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

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
		
		Mockito.when(userRepository.save(any(UserModel.class))).thenReturn(newUser1);
		Mockito.when(userRepository.findByFirstname("giancarlo")).thenReturn(Optional.of(newUser1));
		Mockito.when(userRepository.findAll()).thenReturn(List.of(newUser1, newUser2));
	}
	
	@Test
	@DisplayName("Should save the user object to database")
	public void saveUserCaseSuccess() {
		//action
		UserModel newUser = UserModel
				.builder()
				.id(1L)
				.firstname("giancarlo")
				.username("gianValentin")
				.password("1234")
				.lastname("valentin")
				.email("giancarlo@valentin.com")
				.build();		
		UserModel userDb = userService.save(newUser);
		//assert
		assertNotNull(userDb);
		assertThat(userDb.getFirstname()).isEqualTo("giancarlo");
	}
	
	@Test
	@DisplayName("Find By Name Ignore Case Should Found")
	public void findByNameIgnoreCaseShouldFound() {
		//action
		String name = "giancarlo";
		UserModel user = userService.getByName(name);
		//assert
		assertEquals(user.getFirstname(), name);
	}
	
	@Test
	@DisplayName("Find all case found all users")
	public void finadAllCaseFoundAllUsers() {
		//action
		List<UserModel> userList = userService.getAll();
		//assert
		assertThat(userList).isNotNull();
        assertThat(userList.size()).isEqualTo(2);
	}
	
	@Configuration
	  @Import(DefaultUserService.class)
	  static class TestConfig {
	    @Bean
	    UserRepository userRepository() {
	      return mock(UserRepository.class);
	    }
	  }
}
