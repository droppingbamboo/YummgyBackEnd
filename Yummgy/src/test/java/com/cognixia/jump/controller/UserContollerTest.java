package com.cognixia.jump.controller;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import com.cognixia.jump.model.Favorites;
import com.cognixia.jump.model.Recipe;
import com.cognixia.jump.model.User;
import com.cognixia.jump.repository.UserRepository;

@WebMvcTest(UserController.class)
public class UserContollerTest {
	private static final String STARTING_URI = "http://localhost:8080/api";
	
	@Autowired
	private MockMvc mvc;
	
    @MockBean
    private UserRepository userRepository;
    
    @Test
    public void testGetUsers() throws Exception {

        String uri = STARTING_URI + "/users";

        List<User> users = new ArrayList<>();

        users.add(new User(1, "JohnDoe", "password", new ArrayList<>(), new ArrayList<>()));
        users.add(new User(2, "JaneSmith", "secure123", new ArrayList<>(), new ArrayList<>()));

        when(userRepository.findAll()).thenReturn(users);

        mvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.length()").value(users.size()))
                .andExpect(jsonPath("$[0].userId").value(users.get(0).getUserId()))
                .andExpect(jsonPath("$[0].yumUsername").value(users.get(0).getYumUsername()))
                .andExpect(jsonPath("$[0].yumPassword").value(users.get(0).getYumPassword()))
                // Add assertions for other fields as needed
                .andExpect(jsonPath("$[1].userId").value(users.get(1).getUserId()))
                .andExpect(jsonPath("$[1].yumUsername").value(users.get(1).getYumUsername()))
                .andExpect(jsonPath("$[1].yumPassword").value(users.get(1).getYumPassword()));

        verify(userRepository, times(1)).findAll();
        verifyNoMoreInteractions(userRepository);
    }
    
    @Test
    public void testAddUser() throws Exception {

        String uri = STARTING_URI + "/add/user";

        User newUser = new User(null, "JohnDoe", "password", new ArrayList<>(), new ArrayList<>());

        when(userRepository.save(Mockito.any(User.class))).thenReturn(newUser);

        mvc.perform(post(uri)
                .content(newUser.toJson())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.userId").value(newUser.getUserId()))
                .andExpect(jsonPath("$.yumUsername").value(newUser.getYumUsername()))
                .andExpect(jsonPath("$.yumPassword").value(newUser.getYumPassword()));

        verify(userRepository, times(1)).save(Mockito.any(User.class));
        verifyNoMoreInteractions(userRepository);
    }
    
    @Test
    public void testDeleteUser() throws Exception {

        String uri = STARTING_URI + "/delete/user/{id}";
        int id = 1;

        User user = new User();
        user.setUserId(id);
        user.setYumUsername("testUser");
        user.setYumPassword("testPassword");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        // Mock the repository deleteById method
        doNothing().when(userRepository).deleteById(id);

        mvc.perform(delete(uri, id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.userId").value(id))
                .andExpect(jsonPath("$.yumUsername").value("testUser"))
                .andExpect(jsonPath("$.yumPassword").value("testPassword"));

        verify(userRepository, times(1)).findById(id);
        verify(userRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(userRepository);
    }
    

    
    
}
