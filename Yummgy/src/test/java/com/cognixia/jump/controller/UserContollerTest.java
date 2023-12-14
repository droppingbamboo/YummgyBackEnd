package com.cognixia.jump.controller;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.server.csrf.CsrfToken;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import com.cognixia.jump.model.Favorites;
import com.cognixia.jump.model.Recipe;
import com.cognixia.jump.model.User;
import com.cognixia.jump.repository.UserRepository;
import com.cognixia.jump.service.MyUserDetails;
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

    @Test
    @WithMockUser(username = "testUser", password = "testPassword")
    public void testGetUsers() throws Exception {
        // Mock data
        List<User> users = new ArrayList<>();
        users.add(new User(1, "JohnDoe", "password", new ArrayList<>(), new ArrayList<>()));

        // Mock UserRepository response
        when(userRepository.findAll()).thenReturn(users);

        // Perform the GET request
        mvc.perform(get("/api/users").with(user("testUser")))
                .andExpect(status().isOk())
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
    public void testGetUserFavorites() throws Exception {
        // Mock data
        User testUser = new User(1, "testUser", "testPassword", new ArrayList<>(), new ArrayList<>());
        Recipe recipe1 = new Recipe(1, "Recipe 1", 30, "Ingredients 1", "Directions 1", null, testUser, new ArrayList<>());
        Recipe recipe2 = new Recipe(2, "Recipe 2", 45, "Ingredients 2", "Directions 2", null, testUser, new ArrayList<>());
        Favorites favorite1 = new Favorites(1, testUser, recipe1);
        Favorites favorite2 = new Favorites(2, testUser, recipe2);
        testUser.setFavorites(List.of(favorite1, favorite2));

        // Mock UserRepository response
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(testUser));

        // Perform the GET request
        MvcResult result = mvc.perform(get("/api/users/1/favorites").header("Authorization", "Bearer test-token"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
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
        // Mock data
        User testUser = new User(1, "testUser", "testPassword", new ArrayList<>(), new ArrayList<>());
        Recipe recipe1 = new Recipe(1, "Recipe 1", 30, "Ingredients 1", "Directions 1", null, testUser, new ArrayList<>());
        Recipe recipe2 = new Recipe(2, "Recipe 2", 45, "Ingredients 2", "Directions 2", null, testUser, new ArrayList<>());
        testUser.setRecipes(List.of(recipe1, recipe2));

        // Mock UserRepository response
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(testUser));

        // Perform the GET request
        MvcResult result = mvc.perform(get("/api/users/1/recipes").header("Authorization", "Bearer test-token"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
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
    
//    @Test
//    @WithMockUser(username = "testUser", roles = "USER")
//    public void testAddUser() throws Exception {
//        // Mock data
//        User newUser = new User();
//        newUser.setUserId(null);  // Setting userId to null to simulate a new user
//        newUser.setYumUsername("John Doo");
//        newUser.setYumPassword("pass123");
//
//        // Mock UserRepository response
//        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
//            User savedUser = invocation.getArgument(0);
//            savedUser.setUserId(11);  // Assigning a userId to simulate the saved user
//            return savedUser;
//        });
//
//        // Perform the POST request
//        mvc.perform(post("/api/add/user")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(newUser)))  // Use your asJsonString method
//                .andExpect(status().isCreated())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(jsonPath("$.userId").value(11))  // Check for the assigned userId
//                .andExpect(jsonPath("$.yumUsername").value("John Doo"))
//                .andExpect(jsonPath("$.yumPassword").doesNotExist());  // Ensure yumPassword is not returned
//
//        // Verify interactions with UserRepository
//        verify(userRepository, times(1)).save(any(User.class));
//        verifyNoMoreInteractions(userRepository);
//    }
//    
//    @Test
//    @WithMockUser(username = "testUser", password = "testPassword", roles = "USER")
//    public void testDeleteUser() throws Exception {
//        // Mock data
//        User testUser = new User(1, "testUser", "testPassword", new ArrayList<>(), new ArrayList<>());
//
//        // Mock UserRepository response
//        when(userRepository.findById(anyInt())).thenReturn(Optional.of(testUser));
//        when(jwtUtil.getLoggedInUser(anyString())).thenReturn(testUser);
//
//        // Perform the DELETE request
//        mvc.perform(delete("/api/delete/user/1").header("Authorization", "Bearer test-token"))
//                .andExpect(status().isOk());
//
//        // Verify interactions with UserRepository
//        verify(userRepository, times(1)).findById(1);
//        verify(userRepository, times(1)).deleteById(1);
//        verify(jwtUtil, times(1)).getLoggedInUser("test-token");
//        verifyNoMoreInteractions(userRepository, jwtUtil);
//    }
//    
//    @Test
//    @WithMockUser(username = "testUser", roles = "USER")
//    public void testGetLoggedInUserFavorites() throws Exception {
//        // Mock data
//        User testUser = new User();
//        testUser.setUserId(1);  // Set a valid user ID
//        testUser.setYumUsername("testUser");
//        testUser.setYumPassword("password");
//        
//        // Mock the JwtUtil behavior
//        when(jwtUtil.getLoggedInUser(anyString())).thenReturn(testUser);
//
//        // Mock the repository response for favorites (an empty list)
//        when(userRepository.findById(anyInt())).thenReturn(Optional.of(testUser));
//
//        // Perform the request
//        mvc.perform(get("/api/users/favorites").header("Authorization", "Bearer token123"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))  // Ensure content type is set
//                .andExpect(content().json("[]"));  // Verify that the response body is an empty list (no favorites)
//        
//        // Verify interactions
//        verify(jwtUtil, times(1)).getLoggedInUser(anyString());
//        verifyNoMoreInteractions(jwtUtil);
//        verify(userRepository, times(1)).findById(anyInt());
//        verifyNoMoreInteractions(userRepository);
//    }
//    @Test
//    @WithMockUser(username = "testUser", password = "testPassword", roles = "USER")
//    public void testGetLoggedInUserRecipes() throws Exception {
//        // Mock data
//        User testUser = new User(1, "testUser", "testPassword", new ArrayList<>(), new ArrayList<>());
//        Recipe recipe1 = new Recipe(1, "Recipe 1", 30, "Ingredients 1", "Directions 1", null, testUser, new ArrayList<>());
//        Recipe recipe2 = new Recipe(2, "Recipe 2", 45, "Ingredients 2", "Directions 2", null, testUser, new ArrayList<>());
//        testUser.setRecipes(List.of(recipe1, recipe2));
//
//        // Mock JwtUtil response
//        when(jwtUtil.getLoggedInUser("test-token")).thenReturn(testUser);
//
//        // Perform the GET request
//        MvcResult result = mvc.perform(get("/api/users/recipes").header("Authorization", "Bearer test-token"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andReturn();
//
//        // Parse JSON manually
//        String jsonResponse = result.getResponse().getContentAsString();
//        JSONArray jsonArray = new JSONArray(jsonResponse);
//
//        // Your assertions on jsonArray
//        assertThat(jsonArray.length()).isEqualTo(2);
//
//        JSONObject recipe1Json = jsonArray.getJSONObject(0);
//        assertThat(recipe1Json.getInt("recipeId")).isEqualTo(1);
//
//        JSONObject recipe2Json = jsonArray.getJSONObject(1);
//        assertThat(recipe2Json.getInt("recipeId")).isEqualTo(2);
//
//        // Verify interactions with JwtUtil
//        verify(jwtUtil, times(1)).getLoggedInUser("test-token");
//        verifyNoMoreInteractions(jwtUtil);
//    }

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
