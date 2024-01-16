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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognixia.jump.exception.NoUserGivenException;
import com.cognixia.jump.exception.ResourceNotFoundException;
import com.cognixia.jump.exception.UrlNotAnImageException;
import com.cognixia.jump.model.Favorites;
import com.cognixia.jump.model.Recipe;
import com.cognixia.jump.model.User;
import com.cognixia.jump.repository.FavoritesRepository;
import com.cognixia.jump.repository.RecipeRepository;
import com.cognixia.jump.repository.UserRepository;
import com.cognixia.jump.util.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;

@CrossOrigin
@RequestMapping("/api")
@RestController
@Tag(name = "Recipe-API", description = "The api for managing recipes and recipe favorites.")
public class RecipeController {
	
	@Autowired
	RecipeRepository repo;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	FavoritesRepository favoritesRepo;
	
	@Autowired
	JwtUtil jwtUtil;
	
	@Operation(summary = "Get all the recipes in the recipe table",
			description = "Gets all the recipes from the recipe table in the yummgy_db database."
					+ " Only meant to be called in the case that a user searched for nothing in the search bar.")
	@ApiResponses(
			@ApiResponse(responseCode="200",
			description="Recipes have been found")
	)
	@CrossOrigin
	@GetMapping("/recipes/search/")
	public List<Recipe> getRecipes() {
		
		return repo.findAll();
	}
	
	@Operation(summary = "Get latest x number of recipes recipes",
			description = "Gets the latest x recipes from the database, with x being the amount provided in the url.")
	@ApiResponses({
			@ApiResponse(responseCode="200",
			description="x number of latest recipes have been returned"),
			@ApiResponse(responseCode="403",
			description="Amount provided is not acceptable"),
		}
	)
	@CrossOrigin
	@GetMapping("/recipes/latest/{amount}")
	public List<Recipe> getLatestRecipesByAmount(@PathVariable int amount) {
		
		return repo.latestRecipesByAmount(amount);
	}
	
	@Operation(summary = "Search for recipes according to the string provided",
			description = "Grab all recipes fitting the search criteria provided.")
	@ApiResponses({
			@ApiResponse(responseCode="200",
			description="Search results returned")
		}
	)
	@CrossOrigin
	@GetMapping("/recipes/search/{search}")
	public List<Recipe> searchRecipeTitles(@PathVariable String search) {
		
		if(search.equals(""))
		{
			return getRecipes();
		}
		
		return repo.findByTitleContaining(search);
	}
	
	@Operation(summary = "Get a single recipe by id",
			description = "Gets a single recipe by the id provided.")
	@ApiResponses({
			@ApiResponse(responseCode="200",
			description="recipe returned"),
			@ApiResponse(responseCode="400",
			description="Id does not correspond to a recipe")
		}
	)
	@CrossOrigin
	@GetMapping("/recipes/{id}")
	public ResponseEntity<?> getSingleRecipe(@PathVariable int id) throws ResourceNotFoundException {
		
		Optional<Recipe> found = repo.findById(id);
		
		if(found.isEmpty()) {
			throw new ResourceNotFoundException("Recipe", id);
		}
		
		return ResponseEntity.status(200).body( found.get() );
	}
	
	@Operation(summary = "Get all of a recipes favorites",
			description = "Gets a single recipes favorites.")
	@ApiResponses({
			@ApiResponse(responseCode="200",
			description="favorites returned"),
			@ApiResponse(responseCode="400",
			description="Id does not correspond to a recipe")
		}
	)
	@CrossOrigin
	@GetMapping("recipes/favorites/{recipeId}")
	public ResponseEntity<?> getFavorites(@PathVariable int recipeId) throws ResourceNotFoundException {
		Optional<Recipe> recipeOptional = repo.findById(recipeId);

        if (recipeOptional.isPresent()) {
            List<Favorites> favorites = recipeOptional.get().getFavorites();
            return ResponseEntity.status(200).body(favorites);
        } else {
        	throw new ResourceNotFoundException("Recipe", recipeId);
        }
	}
	
	@Operation(summary = "Add a recipe",
			description = "Adds in a recipe provided in the body, under the authorship of the user if not specified otherwise.")
	@ApiResponses({
			@ApiResponse(responseCode="201",
			description="Recipe added"),
			@ApiResponse(responseCode="400",
			description="Logged in user invalid, Url is not an image, or provided user for author does not exist.")
		}
	)
	@CrossOrigin
	@PostMapping("/add/recipe")
	public ResponseEntity<?> addRecipe(@RequestHeader (name="Authorization") String token, @Valid @RequestBody Recipe newRecipe) throws NoUserGivenException, ResourceNotFoundException, UrlNotAnImageException {
		
		newRecipe.setRecipeId(null);
		
		newRecipe.setAuthor(jwtUtil.getLoggedInUser(token));
		
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
		else if(!match.find() && ((newRecipe.getFoodImageUrl() != null) && (newRecipe.getFoodImageUrl() != "")))
		{
			throw new UrlNotAnImageException(newRecipe.getFoodImageUrl());
		}
		
		Recipe added = repo.save(newRecipe);
		
		return ResponseEntity.status(201).body(added);
	}
	
	@Operation(summary = "Delete a recipe",
			description = "Deletes a recipe so long as that recipe is under the ownership of the logged in user.")
	@ApiResponses({
			@ApiResponse(responseCode="200",
			description="Recipe deleted"),
			@ApiResponse(responseCode="400",
			description="Id does not correspond to a recipe"),
			@ApiResponse(responseCode="404",
			description="Recipe is not authored by the logged in user.")
		}
	)
	@CrossOrigin
	@DeleteMapping("/delete/recipe/{id}")
	public ResponseEntity<?> deleteRecipe(@RequestHeader (name="Authorization") String token, @PathVariable int id) throws ResourceNotFoundException {
		
		Optional<Recipe> found = repo.findById(id);
		
		if(!jwtUtil.getLoggedInUser(token).getRecipes().contains(found.get()))
		{
			return ResponseEntity.status(404).body("recipe not yours");
		}
		if(found.isPresent()) {
			
			repo.deleteRecipe(id);
			
			return ResponseEntity.status(200).body(found.get());	
		}
		else {
			throw new ResourceNotFoundException("Recipe", id);
		}
			
	}
	
	@Operation(summary = "Delete any recipe",
			description = "Deletes any recipe so long as the user is an admin.")
	@ApiResponses({
			@ApiResponse(responseCode="200",
			description="Recipe deleted"),
			@ApiResponse(responseCode="400",
			description="Id does not correspond to a recipe")
		}
	)
	@CrossOrigin
	@DeleteMapping("/admin/delete/recipe/{id}")
	public ResponseEntity<?> deleteRecipeAdmin(@RequestHeader (name="Authorization") String token, @PathVariable int id) throws ResourceNotFoundException {
		
		Optional<Recipe> found = repo.findById(id);
		
		if(found.isPresent()) {
			
			repo.deleteRecipe(id);
			
			return ResponseEntity.status(200).body(found.get());	
		}
		else {
			throw new ResourceNotFoundException("Recipe", id);
		}
			
	}
	
	@Operation(summary = "Update a recipe",
			description = "Updates a recipe provided in the request body in the database to the recipe provided's"
					+ " information so long as that recipe is under the ownership of the logged in user.")
	@ApiResponses({
			@ApiResponse(responseCode="200",
			description="Recipe deleted"),
			@ApiResponse(responseCode="400",
			description="Id does not correspond to a recipe"),
			@ApiResponse(responseCode="404",
			description="Recipe is not authored by the logged in user.")
		}
	)
	@CrossOrigin
	@PatchMapping("/patch/recipe")
	public ResponseEntity<?> updateRecipe(@RequestHeader (name="Authorization") String token, @Valid @RequestBody Recipe recipe) throws ResourceNotFoundException {
		
		Optional<Recipe> found = repo.findById(recipe.getRecipeId());
		
		if(found.isEmpty()) {
			throw new ResourceNotFoundException("recipe", recipe.getRecipeId());
		}
		if(!jwtUtil.getLoggedInUser(token).getRecipes().contains(found.get()))
		{
			return ResponseEntity.status(404).body("recipe not yours");
		}
		recipe.setFavorites(found.get().getFavorites());
		recipe.setAuthor(found.get().getAuthor());
		
		Recipe updated = repo.save(recipe);
		
		return ResponseEntity.status(200).body(updated);
	}
	
	@Operation(summary = "Update any recipe",
			description = "Updates a recipe provided in the request body in the database to the recipe provided's")
	@ApiResponses({
			@ApiResponse(responseCode="200",
			description="Recipe deleted"),
			@ApiResponse(responseCode="400",
			description="Id does not correspond to a recipe")
		}
	)
	@CrossOrigin
	@PatchMapping("/admin/patch/recipe")
	public ResponseEntity<?> updateRecipeAdmin(@RequestHeader (name="Authorization") String token, @Valid @RequestBody Recipe recipe) throws ResourceNotFoundException {
		
		Optional<Recipe> found = repo.findById(recipe.getRecipeId());
		
		if(found.isEmpty()) {
			throw new ResourceNotFoundException("recipe", recipe.getRecipeId());
		}
		
		recipe.setFavorites(found.get().getFavorites());
		recipe.setAuthor(found.get().getAuthor());
		
		Recipe updated = repo.save(recipe);
		
		return ResponseEntity.status(200).body(updated);
	}
	
	@Operation(summary = "Remove a favorite",
			description = "Removes a favorite to a recipe from the database based off a recipe id, so long as"
					+ " the recipe provided has a corresponding favorite by the logged in user.")
	@ApiResponses({
			@ApiResponse(responseCode="200",
			description="Favorite removed"),
			@ApiResponse(responseCode="400",
			description="Recipe id does not correspond to a favorite by the user")
		}
	)
	@CrossOrigin
	@DeleteMapping("/add/recipe/favorite/{id}")
	public ResponseEntity<?> unfavoriteRecipie(@RequestHeader (name="Authorization") String token, @PathVariable int id) throws ResourceNotFoundException {
		Optional<Recipe> found = repo.findById(id);
		if(found.isPresent()) {
			Favorites fav = new Favorites(null, jwtUtil.getLoggedInUser(token), found.get());
			if(found.get().getFavorites().contains(fav))
			{
				Favorites deleteItem = found.get().getFavorites().get(found.get().getFavorites().indexOf(fav));
				favoritesRepo.deleteFavorite(deleteItem.getFavoritesId());
				return ResponseEntity.status(200).body("Unfavorited");
			}
			else
			{
				return ResponseEntity.status(400).body("You have not favorited this, or not your favorite");
			}
		}
		else {
			throw new ResourceNotFoundException("Recipe", id);
		}
		
	}
	
	@Operation(summary = "Add a favorite",
			description = "Adds a favorite to a recipe from the database based off a recipe id, so long as"
					+ " the recipe provided has a corresponding favorite by the logged in user.")
	@ApiResponses({
			@ApiResponse(responseCode="200",
			description="Favorite added"),
			@ApiResponse(responseCode="400",
			description="Recipe id does not correspond to a favorite by the user")
		}
	)
	@CrossOrigin
	@PostMapping("/add/recipe/favorite/{id}")
	public ResponseEntity<?> favoriteRecipie(@RequestHeader (name="Authorization") String token, @PathVariable int id) throws ResourceNotFoundException {
		Optional<Recipe> found = repo.findById(id);
		if(found.isPresent()) {
			Favorites fav = new Favorites(null, jwtUtil.getLoggedInUser(token), found.get());
			if(found.get().getFavorites().contains(fav))
			{
				Favorites deleteItem = found.get().getFavorites().get(found.get().getFavorites().indexOf(fav));
				favoritesRepo.deleteById(deleteItem.getFavoritesId());
				return ResponseEntity.status(200).body("Unfavorited");
			}
			favoritesRepo.save(fav);
			return ResponseEntity.status(200).body(fav);
		}
		else {
			throw new ResourceNotFoundException("Recipe", id);
		}
		
	}
	
}
