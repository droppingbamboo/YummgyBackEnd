package com.cognixia.jump.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.aspectj.lang.annotation.Before;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import com.cognixia.jump.filter.JwtRequestFilter;
import com.cognixia.jump.model.Favorites;
import com.cognixia.jump.model.Recipe;
import com.cognixia.jump.model.User;
import com.cognixia.jump.model.User.Role;
import com.cognixia.jump.repository.UserRepository;
import com.cognixia.jump.service.MyUserDetailsService;
import com.cognixia.jump.util.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UserController.class)
public class UserContollerTest {
	private static final String STARTING_URI = "http://localhost:8080/api";

	@Autowired
	private MockMvc mvc;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private PasswordEncoder passwordEncoder;

	@MockBean
	private JwtUtil jwtUtil;

	@MockBean
	private MyUserDetailsService userDetailsService;

	@MockBean
	ConfirmationTokenController tokenController;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@BeforeEach
	public void setup(TestInfo testInfo) {
		// Print the name of the test being run
		System.out.println("-------------RUNNING TEST------------- : " + testInfo.getTestMethod().get().getName());
		// Init MockMvc Object and build
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	@WithMockUser(username = "testUser", password = "testPassword")
	public void testGetUsers() throws Exception {
	    String uri = STARTING_URI + "/users";
	    // Mock data
	    List<User> users = new ArrayList<>();
	    users.add(new User("JohnDoe", "password", User.Role.ROLE_USER, "john@example.com"));

	    // Mock UserRepository response
	    when(userRepository.findAll()).thenReturn(users);

	    // Perform the GET request
	    mvc.perform(get(uri).with(user("testUser"))).andExpect(status().isOk())
	            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
	            .andExpect(jsonPath("$.length()").value(users.size()))
	            .andExpect(jsonPath("$[0].userId").value(users.get(0).getUserId()))
	            .andExpect(jsonPath("$[0].yumUsername").value(users.get(0).getYumUsername()));

	    // Verify interactions with userRepository
	    verify(userRepository, times(1)).findAll();
	    verifyNoMoreInteractions(userRepository);
	}

	// SEARCH USERS TESTS
	@Test
	@WithMockUser(username = "testUser", password = "testPassword")
	public void testGetSearchUsers() throws Exception {
	    String uri = STARTING_URI + "/users/search/";
	    // Mock data
	    List<User> users = new ArrayList<>();
	    users.add(new User("JohnDoe", "password", User.Role.ROLE_USER, "john@example.com"));

	    // Mock UserRepository response
	    when(userRepository.findAll()).thenReturn(users);

	    // Perform the GET request
	    mvc.perform(get(uri).with(user("testUser"))).andExpect(status().isOk())
	            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
	            .andExpect(jsonPath("$.length()").value(users.size()))
	            .andExpect(jsonPath("$[0].userId").value(users.get(0).getUserId()))
	            .andExpect(jsonPath("$[0].yumUsername").value(users.get(0).getYumUsername()));

	    // Verify interactions with userRepository
	    verify(userRepository, times(1)).findAll();
	    verifyNoMoreInteractions(userRepository);
	}

	@Test
	@WithMockUser(username = "testUser", password = "testPassword")
	public void testSearchUsers() throws Exception {
	    String uri = STARTING_URI + "/users/search/test";
	    // Mock data
	    List<User> users = new ArrayList<>();
	    users.add(new User("JohnDoe", "password", User.Role.ROLE_USER, "john@example.com"));

	    // Mock UserRepository response
	    when(userRepository.findByYumUsernameContaining(anyString())).thenReturn(users);

	    // Perform the GET request with search parameter
	    mvc.perform(get(uri).with(user("testUser"))).andExpect(status().isOk())
	            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
	            .andExpect(jsonPath("$.length()").value(users.size()))
	            .andExpect(jsonPath("$[0].userId").value(users.get(0).getUserId()))
	            .andExpect(jsonPath("$[0].yumUsername").value(users.get(0).getYumUsername()));

	    // Verify interactions with userRepository
	    verify(userRepository, times(1)).findByYumUsernameContaining("test");
	    verifyNoMoreInteractions(userRepository);
	}

	//
	// //GET USER FAVORITES & RECIPES
	@Test
	@WithMockUser(username = "testUser", password = "testPassword")
	public void testGetUserFavorites() throws Exception {
	    String uri = STARTING_URI + "/users/1/favorites";
	    // Mock data

	    User testUser = new User("JohnDoe", "password", User.Role.ROLE_USER, "john@example.com");
	    Recipe recipe1 = new Recipe(1, "Recipe 1", 30, "Ingredients 1", "Directions 1", null, testUser,
	            new ArrayList<>());
	    Recipe recipe2 = new Recipe(2, "Recipe 2", 45, "Ingredients 2", "Directions 2", null, testUser,
	            new ArrayList<>());
	    Favorites favorite1 = new Favorites(1, testUser, recipe1);
	    Favorites favorite2 = new Favorites(2, testUser, recipe2);
	    testUser.setFavorites(List.of(favorite1, favorite2));

	    // Mock UserRepository response
	    when(userRepository.findById(anyInt())).thenReturn(Optional.of(testUser));

	    // Perform the GET request
	    MvcResult result = mvc.perform(get(uri).header("Authorization", "Bearer test-token"))
	            .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
	            .andReturn();

	    // Parse JSON manually
	    String jsonResponse = result.getResponse().getContentAsString();
	    JSONArray jsonArray = new JSONArray(jsonResponse);

	    // Your assertions on jsonArray
	    assertThat(jsonArray.length()).isEqualTo(2);

	    JSONObject favorites1 = jsonArray.getJSONObject(0);
	    assertThat(favorites1.getInt("favoritesId")).isEqualTo(1);

	    JSONObject favorites2 = jsonArray.getJSONObject(1);
	    assertThat(favorites2.getInt("favoritesId")).isEqualTo(2);

	    // Verify interactions with UserRepository
	    verify(userRepository, times(1)).findById(1);
	    verifyNoMoreInteractions(userRepository);
	}

	@Test
	@WithMockUser(username = "testUser", password = "testPassword", roles = "USER")
	public void testGetUserRecipes() throws Exception {
	    String uri = STARTING_URI + "/users/1/recipes";
	    // Mock data
	    User testUser = new User("JohnDoe", "password", User.Role.ROLE_USER, "john@example.com");
	    Recipe recipe1 = new Recipe(1, "Recipe 1", 30, "Ingredients 1", "Directions 1", null, testUser,
	            new ArrayList<>());
	    Recipe recipe2 = new Recipe(2, "Recipe 2", 45, "Ingredients 2", "Directions 2", null, testUser,
	            new ArrayList<>());
	    testUser.setRecipes(List.of(recipe1, recipe2));

	    // Mock UserRepository response
	    when(userRepository.findById(anyInt())).thenReturn(Optional.of(testUser));

	    // Perform the GET request
	    MvcResult result = mvc.perform(get(uri).header("Authorization", "Bearer test-token"))
	            .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
	            .andReturn();

	    // Parse JSON manually
	    String jsonResponse = result.getResponse().getContentAsString();
	    JSONArray jsonArray = new JSONArray(jsonResponse);

	    // Your assertions on jsonArray
	    assertThat(jsonArray.length()).isEqualTo(2);

	    JSONObject recipe1Json = jsonArray.getJSONObject(0);
	    assertThat(recipe1Json.getInt("recipeId")).isEqualTo(1);

	    JSONObject recipe2Json = jsonArray.getJSONObject(1);
	    assertThat(recipe2Json.getInt("recipeId")).isEqualTo(2);

	    // Verify interactions with UserRepository
	    verify(userRepository, times(1)).findById(1);
	    verifyNoMoreInteractions(userRepository);
	}


	
	// //ADD USER ADMIN
    @Test
    public void testAddUserAdmin() throws Exception {
    	String uri = STARTING_URI + "/admin/add/user";
        // Mock data
        User newUser = new User();
        newUser.setYumUsername("admin");
        newUser.setYumPassword("adminPassword");
        newUser.setEmail("admin@example.com");
        newUser.setUserId(null);
        newUser.setRole(Role.ROLE_ADMIN);
        newUser.setEnabled(true);
        
        String json = asJsonString(newUser);
        System.out.println(json);
        // Mock UserRepository response
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // Perform the POST request
        mvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newUser.toJson()))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.yumUsername").value("admin"))
                .andExpect(jsonPath("$.email").value("admin@example.com"));

        // Verify interactions with UserRepository
        verify(userRepository, times(1)).save(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }


	// USER ADD ADMIN IMPROPER FORMAT TEST
	@Test
	@WithMockUser(username = "testUser", roles = "ADMIN")
	public void testAddAdminUserImproperFormat() throws Exception {
		String uri = STARTING_URI + "/admin/add/user";
		// Mock data with missing required fields
		User newUser = new User();

		// Perform the POST request
		mvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON).content(asJsonString(newUser))) // Use
																														// your
																														// asJsonString
																														// method
				.andExpect(status().isBadRequest()); // Expecting a 400 Bad Request status

		// Verify no interactions with UserRepository
		verifyNoMoreInteractions(userRepository);
	}

	// DELETE USER TEST
	@Test
	@WithMockUser(username = "testUser", password = "testPassword", roles = "USER")
	public void testDeleteUser() throws Exception {
		String uri = STARTING_URI + "/delete/user/1";
		// Mock data
		User testUser = new User("JohnDoe", "password", User.Role.ROLE_USER, "john@example.com");

		// Mock UserRepository response
		when(userRepository.findById(anyInt())).thenReturn(Optional.of(testUser));
		when(jwtUtil.getLoggedInUser(anyString())).thenReturn(testUser);

		// Perform the DELETE request
		mvc.perform(delete(uri).header("Authorization", "test-token")).andExpect(status().isOk());

		// Verify interactions with UserRepository
		verify(userRepository, times(1)).findById(1);
		verify(userRepository, times(1)).deleteById(1);
		verify(jwtUtil, times(1)).getLoggedInUser("test-token");
		verifyNoMoreInteractions(userRepository, jwtUtil);
	}



	// LOGGED IN TESTS
	@Test
	@WithMockUser(username = "testUser", password = "testPassword")
	public void testGetLoggedInUser() throws Exception {
		String uri = STARTING_URI + "/users/loggedin";
		// Mock data
		User loggedInUser = new User("JohnDoe", "password", User.Role.ROLE_USER, "john@example.com");

		// Mock JwtUtil behavior
		when(jwtUtil.getLoggedInUser("test-token")).thenReturn(loggedInUser);

		// Perform the GET request
		MvcResult result = mvc.perform(get(uri).header("Authorization", "test-token"))
				.andExpect(status().isOk()).andReturn();

		// Get the response content
		String jsonResponse = result.getResponse().getContentAsString();

		// Apply JSON path assertions or other relevant assertions

		// Verify interactions with JwtUtil
		verify(jwtUtil, times(1)).getLoggedInUser("test-token");
		verifyNoMoreInteractions(jwtUtil);
	}

	@Test
	@WithMockUser(username = "testUser", password = "testPassword")
	public void testGetLoggedInUserFavorites() throws Exception {
		String uri = STARTING_URI + "/users/favorites";
		// Mock data
		User loggedInUser = new User("JohnDoe", "password", User.Role.ROLE_USER, "john@example.com");
		Recipe recipe1 = new Recipe(1, "Recipe 1", 30, "Ingredients 1", "Directions 1", null, loggedInUser,
				new ArrayList<>());
		Recipe recipe2 = new Recipe(2, "Recipe 2", 45, "Ingredients 2", "Directions 2", null, loggedInUser,
				new ArrayList<>());
		Favorites favorite1 = new Favorites(1, loggedInUser, recipe1);
		Favorites favorite2 = new Favorites(2, loggedInUser, recipe2);
		loggedInUser.setFavorites(List.of(favorite1, favorite2));

		// Mock JwtUtil behavior
		when(jwtUtil.getLoggedInUser("test-token")).thenReturn(loggedInUser);

		// Perform the GET request
		MvcResult result = mvc.perform(get(uri).header("Authorization", "test-token"))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		// Parse JSON manually
		String jsonResponse = result.getResponse().getContentAsString();
		JSONArray jsonArray = new JSONArray(jsonResponse);

		// Your assertions on jsonArray
		assertThat(jsonArray.length()).isEqualTo(2);

		JSONObject favorites1 = jsonArray.getJSONObject(0);
		assertThat(favorites1.getInt("favoritesId")).isEqualTo(1);

		JSONObject favorites2 = jsonArray.getJSONObject(1);
		assertThat(favorites2.getInt("favoritesId")).isEqualTo(2);

		// Verify interactions with JwtUtil
		verify(jwtUtil, times(1)).getLoggedInUser("test-token");
		verifyNoMoreInteractions(jwtUtil);
	}

	@Test
	@WithMockUser(username = "testUser", password = "testPassword")
	public void testGetLoggedInUserRecipes() throws Exception {
		String uri = STARTING_URI + "/users/recipes";
		// Mock data
		User loggedInUser = new User("JohnDoe", "password", User.Role.ROLE_USER, "john@example.com");
		Recipe recipe1 = new Recipe(1, "Recipe 1", 30, "Ingredients 1", "Directions 1", null, loggedInUser,
				new ArrayList<>());
		Recipe recipe2 = new Recipe(2, "Recipe 2", 45, "Ingredients 2", "Directions 2", null, loggedInUser,
				new ArrayList<>());
		loggedInUser.setRecipes(List.of(recipe1, recipe2));

		// Mock JwtUtil behavior
		when(jwtUtil.getLoggedInUser("test-token")).thenReturn(loggedInUser);

		// Perform the GET request
		MvcResult result = mvc.perform(get(uri).header("Authorization", "test-token"))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		// Parse JSON manually
		String jsonResponse = result.getResponse().getContentAsString();
		JSONArray jsonArray = new JSONArray(jsonResponse);

		// Your assertions on jsonArray
		assertThat(jsonArray.length()).isEqualTo(2);

		JSONObject recipe1Json = jsonArray.getJSONObject(0);
		assertThat(recipe1Json.getInt("recipeId")).isEqualTo(1);

		JSONObject recipe2Json = jsonArray.getJSONObject(1);
		assertThat(recipe2Json.getInt("recipeId")).isEqualTo(2);

		// Verify interactions with JwtUtil
		verify(jwtUtil, times(1)).getLoggedInUser("test-token");
		verifyNoMoreInteractions(jwtUtil);
	}

	// //TOGGLE TESTS
	@Test
	@WithMockUser(username = "testUser", password = "testPassword", roles = "ADMIN")
	public void testToggleEnabled() throws Exception {
		String uri = STARTING_URI + "/admin/user/security/enabled/1";
		// Mock data
		User testUser = new User("JohnDoe", "password", User.Role.ROLE_USER, "john@example.com");
		testUser.setRole(Role.ROLE_ADMIN);
		testUser.setEnabled(true); // Set initial enabled status

		// Mock UserRepository response
		when(userRepository.findById(anyInt())).thenReturn(Optional.of(testUser));
		when(userRepository.save(any(User.class))).thenReturn(testUser);

		// Perform the PATCH request
		MvcResult result = mvc
				.perform(patch(uri).header("Authorization", "Bearer test-token"))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		// Parse JSON manually
		String jsonResponse = result.getResponse().getContentAsString();

		// Your assertions on jsonResponse
		assertThat(jsonResponse).isEqualTo("false"); // Assuming toggling sets to false in this case

		// Verify interactions with UserRepository
		verify(userRepository, times(1)).findById(1);
		verify(userRepository, times(1)).save(any(User.class));
		verifyNoMoreInteractions(userRepository);
	}

	@Test
	@WithMockUser(username = "testUser", password = "testPassword", roles = "ADMIN")
	public void testToggleLocked() throws Exception {
		String uri = STARTING_URI + "/admin/user/security/locked/1";
		// Mock data
		User testUser = new User("JohnDoe", "password", User.Role.ROLE_USER, "john@example.com");
		testUser.setRole(Role.ROLE_ADMIN);
		testUser.setLocked(false); // Set initial locked status

		// Mock UserRepository response
		when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
		when(userRepository.save(any(User.class))).thenReturn(testUser);

		// Perform the PATCH request
		MvcResult result = mvc
				.perform(patch(uri).header("Authorization", "Bearer test-token"))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		// Parse JSON manually
		String jsonResponse = result.getResponse().getContentAsString();

		// Your assertions on jsonResponse
		assertThat(jsonResponse).isEqualTo("true"); // Assuming toggling sets to false in this case

		// Verify interactions with UserRepository
		verify(userRepository, times(1)).findById(1);
		verify(userRepository, times(1)).save(any(User.class));
		verifyNoMoreInteractions(userRepository);
	}

	@Test
	@WithMockUser(username = "testUser", password = "testPassword", roles = "ADMIN")
	public void testToggleExpired() throws Exception {
		String uri = STARTING_URI + "/admin/user/security/expired/1";
		// Mock data
		User testUser = new User("JohnDoe", "password", User.Role.ROLE_USER, "john@example.com");
		testUser.setRole(Role.ROLE_ADMIN);
		testUser.setExpired(false); // Set initial locked status

		// Mock UserRepository response
		when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
		when(userRepository.save(any(User.class))).thenReturn(testUser);

		// Perform the PATCH request
		MvcResult result = mvc
				.perform(patch(uri).header("Authorization", "Bearer test-token"))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		// Parse JSON manually
		String jsonResponse = result.getResponse().getContentAsString();

		// Your assertions on jsonResponse
		assertThat(jsonResponse).isEqualTo("true"); // Assuming toggling sets to false in this case

		// Verify interactions with UserRepository
		verify(userRepository, times(1)).findById(1);
		verify(userRepository, times(1)).save(any(User.class));
		verifyNoMoreInteractions(userRepository);
	}

	@Test
	@WithMockUser(username = "testUser", password = "testPassword", roles = "ADMIN")
	public void testToggleCredentials() throws Exception {
		String uri = STARTING_URI + "/admin/user/security/credentials/1";
		// Mock data
		User testUser = new User("JohnDoe", "password", User.Role.ROLE_USER, "john@example.com");
		testUser.setRole(Role.ROLE_ADMIN);
		testUser.setCredentialsBad(false); // Set initial locked status

		// Mock UserRepository response
		when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
		when(userRepository.save(any(User.class))).thenReturn(testUser);

		// Perform the PATCH request
		MvcResult result = mvc
				.perform(patch(uri).header("Authorization", "Bearer test-token"))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		// Parse JSON manually
		String jsonResponse = result.getResponse().getContentAsString();

		// Your assertions on jsonResponse
		assertThat(jsonResponse).isEqualTo("true"); // Assuming toggling sets to false in this case

		// Verify interactions with UserRepository
		verify(userRepository, times(1)).findById(1);
		verify(userRepository, times(1)).save(any(User.class));
		verifyNoMoreInteractions(userRepository);
	}

	// converts any object to a JSON string
	public static String asJsonString(final Object obj) {

		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}

	}

}
