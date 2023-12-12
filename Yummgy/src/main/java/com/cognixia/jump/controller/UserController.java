package com.cognixia.jump.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognixia.jump.model.Favorites;
import com.cognixia.jump.model.User;
import com.cognixia.jump.repository.UserRepository;

@CrossOrigin
@RequestMapping("/api")
@RestController
public class UserController {
	
	@Autowired
	UserRepository repo;
	
	@CrossOrigin
	@GetMapping("/users")
	public List<User> getUsers() {
		
		return repo.findAll();
	}
	

	
	@CrossOrigin
	@PostMapping("/add/user")
	public ResponseEntity<?> addUser(@RequestBody User newUser) {
		
		newUser.setUserId(-1);
		
		User added = repo.save(newUser); 
		
		System.out.println("Added: " + added);
		
		return ResponseEntity.status(201).body(added);
	}
	
	@CrossOrigin
	@DeleteMapping("/delete/user/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable int id) {
		
		Optional<User> found = repo.findById(id);
		
		if(found.isPresent()) {
			
			repo.deleteById(id);
			
			return ResponseEntity.status(200).body(found.get());	
		}
		else {
			return ResponseEntity.status(404)
								 .body("User with id = " + id + " not found");
		}
			
	}

    
    @GetMapping("/users/{userId}/favorites")
    public ResponseEntity<?> getUserFavorites(@PathVariable Integer userId) {
        Optional<User> userOptional = repo.findById(userId);

        if (userOptional.isPresent()) {
            List<Favorites> favorites = userOptional.get().getFavorites();
            return ResponseEntity.status(200).body(favorites);
        } else {
            return ResponseEntity.status(404).body("User with id = " + userId + " not found");
        }
    }
	
}
