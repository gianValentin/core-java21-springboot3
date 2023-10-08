package com.app.core.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import com.app.core.entity.model.UserModel;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) 
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:postgresql://localhost:5432/db_api_core",
        "spring.datasource.username=postgres",
        "spring.datasource.password=1234"
})
public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TestEntityManager testEntityManager;

	@BeforeEach
	void setUp() {
		persistDemoUsers();
	}
	
	private void persistDemoUsers() {
		UserModel newUser1 = UserModel
				.builder()
				.firstname("giancarlo")
				.username("gianValentin")
				.password("1234")
				.lastname("valentin")
				.email("giancarlo@valentin.com")
				.build();
		
		UserModel newUser2 = UserModel
				.builder()
				.firstname("maria")
				.username("mariaCasas")
				.password("1234")
				.lastname("casas")
				.email("maria@casas.com")
				.build();
		
		testEntityManager.persist(newUser1);
		testEntityManager.persist(newUser2);		
	}
	
	@Test
	@DisplayName("Find user by name case found")
	public void findUserByNameCaseFound() {
		
		Optional<UserModel> userDb = userRepository.findByFirstname("giancarlo");
		
		assertEquals(userDb.get().getFirstname(), "giancarlo");
	}
	
	@Test
	@DisplayName("Find user by name case not found")
	public void findUserByNameCaseNotFound() {
		
		Optional<UserModel> userDb = userRepository.findByFirstname("Juan");
		
		assertTrue(userDb.isEmpty());		
	}
	
	@Test
	@DisplayName("Find all case found all users")
	public void findAllCaseFoundAllUsers() {
		
		List<UserModel> userList = userRepository.findAll();
		
		assertThat(userList).isNotNull();
        assertThat(userList.size()).isEqualTo(2);
	}
	
	@Test
	@DisplayName("Find all case no user is found")
	public void findAllCaseNoUserIsFound() {
		
		userRepository.deleteAll();
		
		List<UserModel> userList = userRepository.findAll();
		
		assertThat(userList).isNotNull();
		assertThat(userList.size()).isEqualTo(0);
	}
	
	@Test
	@DisplayName("Save user case success")
	public void saveUserCaseSuccess() {
		
		UserModel userDb = UserModel
				.builder()				
				.firstname("giancarlo")
				.username("gianValentin")
				.password("1234")
				.lastname("valentin")
				.email("giancarlo@valentin.com")
				.build();
		
		UserModel saveduser = userRepository.save(userDb);
		
		assertThat(saveduser).isNotNull();
		assertThat(saveduser.getId()).isGreaterThan(0);		
	}
	
	@Test
	@DisplayName("Save user case error")
	public void saveUserCaseError() {
		UserModel userDb = null;		
		
		Exception  exception = assertThrows(RuntimeException.class, () -> {
			userRepository.save(userDb);
		}) ;
		
		String expectedMessage = "Entity must not be null";
		String actualMessage = exception.getMessage();
		
		assertTrue(actualMessage.contains(expectedMessage));		
	}
	
	@Test
	@DisplayName("Delete user case success")
	public void DeleteUserCaseSuccess() {
		final String nameTest = "giancarlo";
		
		Long userId = userRepository.findByFirstname(nameTest).get().getId();
		
		 userRepository.deleteById(userId);
		 
		 UserModel user = userRepository.findByFirstname(nameTest).orElse(null);		 		
		
		assertThat(user).isNull();
	}

}
