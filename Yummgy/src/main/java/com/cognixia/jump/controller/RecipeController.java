package com.cognixia.jump.controller;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognixia.jump.exception.NoUserGivenException;
import com.cognixia.jump.exception.ResourceNotFoundException;
import com.cognixia.jump.exception.UrlNotAnImageException;
import com.cognixia.jump.model.Favorites;
import com.cognixia.jump.model.Recipe;
import com.cognixia.jump.model.User;
import com.cognixia.jump.repository.RecipeRepository;
import com.cognixia.jump.repository.UserRepository;

import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;

@CrossOrigin
@RequestMapping("/api")
@RestController
public class RecipeController {
	
	@Autowired
	RecipeRepository repo;
	
	@Autowired
	UserRepository userRepo;
	
	@CrossOrigin
	@GetMapping("/recipes")
	public List<Recipe> getRecipes() {
		
		return repo.findAll();
	}
	
	@CrossOrigin
	@GetMapping("/recipes/latest/{amount}")
	public List<Recipe> getLatestRecipesByAmount(@PathVariable int amount) {
		
		return repo.latestRecipesByAmount(amount);
	}
	
	@CrossOrigin
	@GetMapping("/recipes/search/{search}")
	public List<Recipe> searchRecipeTitles(@PathVariable String search) {
		
		return repo.findByTitleContaining(search);
	}
	
	@CrossOrigin
	@GetMapping("/recipes/{id}")
	public ResponseEntity<?> getSingleRecipe(@PathVariable int id) throws ResourceNotFoundException {
		
		Optional<Recipe> found = repo.findById(id);
		
		if(found.isEmpty()) {
			throw new ResourceNotFoundException("Recipe", id);
		}
		
		return ResponseEntity.status(200).body( found.get() );
	}
	
	@CrossOrigin
	@GetMapping("recipes/favorites/{recipeId}")
	public ResponseEntity<?> getFavorites(@PathVariable int recipeId) throws ResourceNotFoundException {
		Optional<Recipe> recipeOptional = repo.findById(recipeId);

        if (recipeOptional.isPresent()) {
            List<Favorites> favorites = recipeOptional.get().getFavorites();
            return ResponseEntity.status(200).body(favorites);
        } else {
        	throw new ResourceNotFoundException("User", recipeId);
        }
	}
	
	
	
	// Need to use JWT to grab user and create user object to avoid the frontend having to provide user information they don't have.
	@CrossOrigin
	@PostMapping("/add/recipe")
	public ResponseEntity<?> addRecipe(@Valid @RequestBody Recipe newRecipe) throws NoUserGivenException, ResourceNotFoundException, UrlNotAnImageException {
		
		newRecipe.setRecipeId(null);
		
		
		
		if(newRecipe.getAuthor() == null)
		{
			throw new NoUserGivenException("recipe");
		}
		
		Pattern pat = Pattern.compile(".*?(jpeg|png|jpg)");
		Matcher match = pat.matcher(newRecipe.getFoodImageUrl());
		
		
		if(userRepo.findById(newRecipe.getAuthor().getUserId()).isEmpty())
		{
			throw new ResourceNotFoundException("user", newRecipe.getAuthor().getUserId());
		}
		else if(!match.find())
		{
			throw new UrlNotAnImageException(newRecipe.getFoodImageUrl());
		}
		
		Recipe added = repo.save(newRecipe);
		
		return ResponseEntity.status(201).body(added);
	}
	
	@CrossOrigin
	@DeleteMapping("/delete/recipe/{id}")
	public ResponseEntity<?> deleteRecipe(@PathVariable int id) throws ResourceNotFoundException {
		
		Optional<Recipe> found = repo.findById(id);
		
		if(found.isPresent()) {
			
			repo.deleteById(id);
			
			return ResponseEntity.status(200).body(found.get());	
		}
		else {
			throw new ResourceNotFoundException("Recipe", id);
		}
			
	}
	
	@CrossOrigin
	@PatchMapping("/patch/recipe/{id}")
	public ResponseEntity<?> updateRecipe(@PathParam(value="recipeId") int id, @PathParam(value="title") String title,
			 @PathParam(value="prep_time") int prepTime, @PathParam(value="ingredients") String ingredients,
			 @PathParam(value="directions") String directions, @PathParam(value="food_image_url") String url) throws ResourceNotFoundException {
		Optional<Recipe> found = repo.findById(id);
		
		if(found.isEmpty()) {
			throw new ResourceNotFoundException("recipe", id);
		}
		
		Recipe recipeToUpdate = found.get();
		
		recipeToUpdate.setTitle(title);
		recipeToUpdate.setPrepTime(prepTime);
		recipeToUpdate.setIngredients(ingredients);
		recipeToUpdate.setDirections(directions);
		recipeToUpdate.setFoodImageUrl(url);
		Recipe updated = repo.save(recipeToUpdate);
		
		return ResponseEntity.status(200).body(updated);
	}
	
	
}
