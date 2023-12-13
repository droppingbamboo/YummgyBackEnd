package com.cognixia.jump.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognixia.jump.exception.ResourceNotFoundException;
import com.cognixia.jump.model.Favorites;
import com.cognixia.jump.model.Recipe;
import com.cognixia.jump.model.User;
import com.cognixia.jump.repository.UserRepository;
import com.cognixia.jump.util.JwtUtil;

import jakarta.validation.Valid;

@CrossOrigin
@RequestMapping("/api")
@RestController
public class UserController {
	
	@Autowired
	UserRepository repo;
	
	@Autowired
	PasswordEncoder encoder;
	
	@Autowired
	JwtUtil jwtUtil;
	
	@CrossOrigin
	@GetMapping("/users")
	public List<User> getUsers() {
		
		return repo.findAll();
	}
	

	
	@CrossOrigin
	@PostMapping("/add/user")
	public ResponseEntity<?> addUser(@Valid @RequestBody User newUser) {
		
		newUser.setUserId(null);
		
		newUser.setYumPassword( encoder.encode( newUser.getYumPassword() ) );
		
		User added = repo.save(newUser); 
		
		System.out.println("Added: " + added);
		
		return ResponseEntity.status(201).body(added);
	}
	
	@CrossOrigin
	@DeleteMapping("/delete/user/{id}")
	public ResponseEntity<?> deleteUser(@RequestHeader (name="Authorization") String token, @PathVariable int id) throws ResourceNotFoundException {
		
		Optional<User> found = repo.findById(id);
		
		
		if(found.isPresent()) {
			
			if(found.get().equals(jwtUtil.getLoggedInUser(token)))
			{
				repo.deleteById(id);
				
				return ResponseEntity.status(200).body(found.get());	
			}
			else
			{
				return ResponseEntity.status(404).body("user not yours");
			}
		}
		else {
			throw new ResourceNotFoundException("User", id);
		}
			
	}
	
	@CrossOrigin
	@GetMapping("/users/recipes")
	public ResponseEntity<?> getLoggedInUserRecipes(@RequestHeader (name="Authorization") String token) {
		return ResponseEntity.status(200).body(jwtUtil.getLoggedInUser(token).getRecipes());
	}
	
	@CrossOrigin
	@GetMapping("/users/favorites")
	public ResponseEntity<?> getLoggedInUserFavorites(@RequestHeader (name="Authorization") String token) {
		return ResponseEntity.status(200).body(jwtUtil.getLoggedInUser(token).getFavorites());
	}
	
	@CrossOrigin
	@GetMapping("/users/{userId}/recipes")
	public ResponseEntity<?> getUserRecipes(@PathVariable int id) throws ResourceNotFoundException {
		Optional<User> userOptional = repo.findById(id);

        if (userOptional.isPresent()) {
            List<Recipe> recipes = userOptional.get().getRecipes();
            return ResponseEntity.status(200).body(recipes);
        } else {
        	throw new ResourceNotFoundException("User", id);
        }
	}
    
	@CrossOrigin
    @GetMapping("/users/{userId}/favorites")
    public ResponseEntity<?> getUserFavorites(@PathVariable Integer userId) throws ResourceNotFoundException {
        Optional<User> userOptional = repo.findById(userId);

        if (userOptional.isPresent()) {
            List<Favorites> favorites = userOptional.get().getFavorites();
            return ResponseEntity.status(200).body(favorites);
        } else {
        	throw new ResourceNotFoundException("User", userId);
        }
    }
	
	@CrossOrigin
	@GetMapping("/users/loggedin")
	public ResponseEntity<?> getLoggedInUser(@RequestHeader (name="Authorization") String token) {
		return ResponseEntity.status(200).body(jwtUtil.getLoggedInUser(token));
	}
}
