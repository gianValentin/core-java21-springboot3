package com.app.core.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.app.core.config.ModelMapperConfig;
import com.app.core.config.ValidationConfig;
import com.app.core.entity.model.UserModel;
import com.app.core.security.JwtAuthenticationFilter;
import com.app.core.service.UserService;
import com.app.core.utils.CustomCodeException;

@WebMvcTest(
		controllers = UserController.class,
		excludeFilters = @ComponentScan.Filter(
				type = FilterType.ASSIGNABLE_TYPE,
				classes = JwtAuthenticationFilter.class))
@Import({ ValidationConfig.class, ModelMapperConfig.class})
public class UserControllerTest {

	private final String path = "/api/v1/user";

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	final Long USER_DEMO_ONE_ID = 1L;
	final String USER_DEMO_ONE_NAME = "giancarlo";
	final String USER_DEMO_ONE_USERNAME = "gianValentin";
	final String USER_DEMO_ONE_PASSWORD = "1234";
	final String USER_DEMO_ONE_LASTNAME = "valentin";
	final String USER_DEMO_ONE_EMAIL = "giancarlo@valentin.com";

	final Long USER_DEMO_TWO_ID = 2L;
	final String USER_DEMO_TWO_NAME = "maria";
	final String USER_DEMO_TWO_USERNAME = "mariaCasas";
	final String USER_DEMO_TWO_PASSWORD = "1234";
	final String USER_DEMO_TWO_LASTNAME = "casas";
	final String USER_DEMO_TWO_EMAIL = "maria@casas.com";

	final Long USER_NON_EXISTING_ID = 3L;
	final String USER_NON_EXISTING_NAME = "jesus";
	final String USER_NON_EXISTING_USERNAME = "jesusTorres";
	final String USER_NON_EXISTING_PASSWORD = "1234";
	final String USER_NON_EXISTING_LASTNAME = "torres";
	final String USER_NON_EXISTING_EMAIL = "jesus@torres.com";

	@BeforeEach
	void serUp() {		
		createDemoPersist();
	}
	
	@Test
	@WithMockUser
	@DisplayName("Find by name ignore case found")
	public void findByNameIgnoreCaseFound() throws Exception {		
		mockMvc.perform(
				get(path.concat("/{name}/name"), USER_DEMO_ONE_NAME)						
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.firstname").value("giancarlo"));
	}

	
	@Test
	@WithMockUser
	@DisplayName("Find by name ignore case not found")
	public void findByNameIgnoreCaseNotFound() throws Exception {
		mockMvc.perform(
				get(path.concat("/{name}/name"), USER_NON_EXISTING_NAME)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(CustomCodeException.CODE_400));
	}

	private void createDemoPersist() {
		UserModel newUserOne = UserModel
				.builder()
				.id(USER_DEMO_ONE_ID)
				.firstname(USER_DEMO_ONE_NAME)
				.username(USER_DEMO_ONE_USERNAME)
				.password(USER_DEMO_ONE_PASSWORD)
				.lastname(USER_DEMO_ONE_LASTNAME)
				.email(USER_DEMO_ONE_EMAIL)
				.build();

		UserModel newUserTwo = UserModel
				.builder()
				.id(USER_DEMO_TWO_ID)
				.firstname(USER_DEMO_TWO_NAME)
				.username(USER_DEMO_TWO_USERNAME)
				.password(USER_DEMO_TWO_PASSWORD)
				.lastname(USER_DEMO_TWO_LASTNAME)
				.email(USER_DEMO_TWO_EMAIL)
				.build();

		// get by name
		Mockito.when(userService.getByName(USER_DEMO_ONE_NAME)).thenReturn(newUserOne);

		// get all
		Mockito.when(userService.getAll()).thenReturn(List.of(newUserOne, newUserTwo));			
	}
}
