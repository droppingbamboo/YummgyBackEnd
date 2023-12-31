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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@CrossOrigin
@RequestMapping("/api")
@RestController
@Tag(name = "User-API", description = "The api for managing users.")
public class UserController {
	
	@Autowired
	UserRepository repo;
	
	@Autowired
	PasswordEncoder encoder;
	
	@Autowired
	JwtUtil jwtUtil;
	
	@Operation(summary = "Get all the users in the users table",
			description = "Gets all the users from the users table in the yummgy_db database."
					+ " each user grabbed has an id and username, but does not show user passwords.")
	@ApiResponses(
			@ApiResponse(responseCode="200",
			description="Users have been found")
	)
	@CrossOrigin
	@GetMapping("/users")
	public List<User> getUsers() {
		
		return repo.findAll();
	}
	

	@Operation(summary = "Add a user to the users table",
			description = "Adds a user to the user table in the database based off a username and password."
					+ "The password is encrypted before it is stored to ensure that passwords are secure.")
	@ApiResponses( value = {
			@ApiResponse(responseCode="201",
			description="User has been created"),
			@ApiResponse(responseCode="400",
			description="User data not formatted properly")}
	)
	@CrossOrigin
	@PostMapping("/add/user")
	public ResponseEntity<?> addUser(@Valid @RequestBody User newUser) {
		
		newUser.setUserId(null);
		
		newUser.setYumPassword( encoder.encode( newUser.getYumPassword() ) );
		
		User added = repo.save(newUser); 
		
		return ResponseEntity.status(201).body(added);
	}
	
	@Operation(summary = "Remove the logged in user from the users table",
			description = "Removes a user in the user table in the database based off a user id provided, so long as that id is the user's id"
			+ " and the user is logged in.")
	@ApiResponses( value = {
			@ApiResponse(responseCode="200",
			description="User has been deleted"),
			@ApiResponse(responseCode="403",
			description="User is not yours"),
			@ApiResponse(responseCode="400",
			description="User does not exist")}
	)
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
				return ResponseEntity.status(403).body("user not yours");
			}
		}
		else {
			throw new ResourceNotFoundException("User", id);
		}
			
	}
	
	@Operation(summary = "Get the logged in user's recipes",
			description = "Gets all of the logged in user's recipes and returns it.")
	@ApiResponses( value = {
			@ApiResponse(responseCode="200",
			description="Returned all user recipes"),
			@ApiResponse(responseCode="400",
			description="Logged in user is no longer valid")}
	)
	@CrossOrigin
	@GetMapping("/users/recipes")
	public ResponseEntity<?> getLoggedInUserRecipes(@RequestHeader (name="Authorization") String token) {
		return ResponseEntity.status(200).body(jwtUtil.getLoggedInUser(token).getRecipes());
	}
	
	@Operation(summary = "Get the logged in user's favorites",
			description = "Gets all of the logged in user's favorites and returns it.")
	@ApiResponses( value = {
			@ApiResponse(responseCode="200",
			description="Returned all user favorites"),
			@ApiResponse(responseCode="400",
			description="Logged in user is no longer valid")}
	)
	@CrossOrigin
	@GetMapping("/users/favorites")
	public ResponseEntity<?> getLoggedInUserFavorites(@RequestHeader (name="Authorization") String token) {
		return ResponseEntity.status(200).body(jwtUtil.getLoggedInUser(token).getFavorites());
	}
	
	@Operation(summary = "Get the a user's recipes based off id",
			description = "Gets all of a user's recipes based off id and returns it.")
	@ApiResponses( value = {
			@ApiResponse(responseCode="200",
			description="Returned all user recipes"),
			@ApiResponse(responseCode="400",
			description="User does not exist")}
	)
	@CrossOrigin
	@GetMapping("/users/{userId}/recipes")
	public ResponseEntity<?> getUserRecipes(@PathVariable Integer userId) throws ResourceNotFoundException {
		Optional<User> userOptional = repo.findById(userId);

        if (userOptional.isPresent()) {
            List<Recipe> recipes = userOptional.get().getRecipes();
            return ResponseEntity.status(200).body(recipes);
        } else {
        	throw new ResourceNotFoundException("User", userId);
        }
	}
    
	@Operation(summary = "Get the a user's favorites based off id",
			description = "Gets all of a user's favorites based off id and returns it.")
	@ApiResponses( value = {
			@ApiResponse(responseCode="200",
			description="Returned all user favorites"),
			@ApiResponse(responseCode="400",
			description="User does not exist")}
	)
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
	
	@Operation(summary = "Get the the currently logged in user",
			description = "Grabs the currently logged in user's username and id.")
	@ApiResponses( value = {
			@ApiResponse(responseCode="200",
			description="Returned username and id"),
			@ApiResponse(responseCode="400",
			description="Logged in user no longer valid")}
	)
	@CrossOrigin
	@GetMapping("/users/loggedin")
	public ResponseEntity<?> getLoggedInUser(@RequestHeader (name="Authorization") String token) {
		return ResponseEntity.status(200).body(jwtUtil.getLoggedInUser(token));
	}
}
