package com.cognixia.jump.controller;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import com.cognixia.jump.model.Favorites;
import com.cognixia.jump.model.Recipe;
import com.cognixia.jump.model.User;
import com.cognixia.jump.repository.UserRepository;
import com.cognixia.jump.service.MyUserDetailsService;
import com.cognixia.jump.util.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.cognixia.jump.repository.FavoritesRepository;
import com.cognixia.jump.repository.RecipeRepository;
import com.cognixia.jump.repository.UserRepository;
import com.cognixia.jump.model.Recipe;

@WebMvcTest(RecipeController.class)
public class RecipeControllerTest {
	
	private static final String STARTING_URI = "http://localhost:8080/api";


    @Autowired
    private MockMvc mvc;

    @MockBean
    private RecipeRepository recipeRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private FavoritesRepository favoritesRepository;

    @MockBean
    private JwtUtil jwtUtil;
	    
    @MockBean
    private MyUserDetailsService userDetailsService;
    
    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup(TestInfo testInfo)
    {
        // Print the name of the test being run
        System.out.println("-------------RUNNING TEST--------- : " + testInfo.getTestMethod().get().getName());
    	//Init MockMvc Object and build
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    
    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    public void testGetSingleRecipe() throws Exception {
        String uri = STARTING_URI + "/recipes/{id}";
        int recipeId = 1; // Set the desired recipe ID for testing

        // Mock data
        Recipe recipe = new Recipe(recipeId, "Spaghetti Bolognese", 30, "Ground beef, tomatoes, pasta", "Cook the beef, add tomatoes, mix with pasta", "image_url_1", new User(), new ArrayList<>());

        // Mock RecipeRepository response
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));

        // Perform the GET request
        mvc.perform(get(uri, recipeId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.recipeId").value(recipe.getRecipeId()))
                .andExpect(jsonPath("$.title").value(recipe.getTitle()))
                .andExpect(jsonPath("$.prepTime").value(recipe.getPrepTime()))
                .andExpect(jsonPath("$.ingredients").value(recipe.getIngredients()))
                .andExpect(jsonPath("$.directions").value(recipe.getDirections()))
                .andExpect(jsonPath("$.foodImageUrl").value(recipe.getFoodImageUrl()));

        // Verify interactions with RecipeRepository
        verify(recipeRepository, times(1)).findById(recipeId);
        verifyNoMoreInteractions(recipeRepository);
    }
    
    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    public void testGetFavorites() throws Exception {
        String uri = STARTING_URI + "/recipes/favorites/{recipeId}";
        int recipeId = 1; // Set the desired recipe ID for testing

        // Mock data
        Recipe recipe = new Recipe(recipeId, "Spaghetti Bolognese", 30, "Ground beef, tomatoes, pasta", "Cook the beef, add tomatoes, mix with pasta", "image_url_1", new User(), new ArrayList<>());
        Favorites favorite1 = new Favorites(1, new User(), recipe);
        Favorites favorite2 = new Favorites(2, new User(), recipe);
        recipe.setFavorites(Arrays.asList(favorite1, favorite2));

        // Mock RecipeRepository response
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));

        // Perform the GET request
        mvc.perform(get(uri, recipeId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.length()").value(recipe.getFavorites().size()))
                .andExpect(jsonPath("$[0].favoritesId").value(favorite1.getFavoritesId()))
                .andExpect(jsonPath("$[1].favoritesId").value(favorite2.getFavoritesId()));

        // Verify interactions with RecipeRepository
        verify(recipeRepository, times(1)).findById(recipeId);
        verifyNoMoreInteractions(recipeRepository);
    }
    
    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    public void testSearchRecipes() throws Exception {
    	// Mock data
        List<Recipe> recipes = Arrays.asList(
                new Recipe(1, "Recipe 1", 30, "Ingredients 1", "Directions 1", null, new User(), new ArrayList<>()),
                new Recipe(2, "Recipe 2", 45, "Ingredients 2", "Directions 2", null, new User(), new ArrayList<>())
        );
        
        List<Recipe> recipes2 = Arrays.asList(
                new Recipe(2, "Recipe 2", 45, "Ingredients 2", "Directions 2", null, new User(), new ArrayList<>())
        );
        
        Sort sortAscId = Sort.by(Sort.Direction.ASC, "title");
        Sort sortDescId = Sort.by(Sort.Direction.DESC, "prepTime");
        when(recipeRepository.findByTitleContaining(anyString(), eq(sortAscId), eq(Limit.of(2)))).thenReturn(recipes);
        when(recipeRepository.findByTitleContaining(anyString(), eq(sortDescId), eq(Limit.of(1)))).thenReturn(recipes2);
        
        mvc.perform(get("/api/recipes/search/{ordering}/{ascending}/{amount}/{search}", "title", "ASC", "2","Recipe"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.length()").value(recipes.size()))
        .andExpect(jsonPath("$[0].recipeId").value(recipes.get(0).getRecipeId()))
        .andExpect(jsonPath("$[1].recipeId").value(recipes.get(1).getRecipeId()));
        
        mvc.perform(get("/api/recipes/search/{ordering}/{ascending}/{amount}/{search}", "prepTime", "DESC", "1","Recipe"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.length()").value(recipes2.size()))
        .andExpect(jsonPath("$[0].recipeId").value(recipes2.get(0).getRecipeId()));
        
        mvc.perform(get("/api/recipes/search/{ordering}/{ascending}/{amount}/", "prepTime", "DESC", "1"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.length()").value(recipes2.size()))
        .andExpect(jsonPath("$[0].recipeId").value(recipes2.get(0).getRecipeId()));
        
        verify(recipeRepository, times(1)).findByTitleContaining("Recipe", sortAscId, Limit.of(2));
        verify(recipeRepository, times(1)).findByTitleContaining("Recipe", sortDescId, Limit.of(1));
        verify(recipeRepository, times(1)).findByTitleContaining("", sortDescId, Limit.of(1));
        verifyNoMoreInteractions(recipeRepository);
    }
    
    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    public void testGetFavoritesUsers() throws Exception {
        // Mock data
        Recipe recipe = new Recipe(1, "Spaghetti Bolognese", 30, "Ground beef, tomatoes, pasta", "Cook the beef, add tomatoes, mix with pasta", "image_url_1", new User(), new ArrayList<>());
        Favorites favorite1 = new Favorites(1, new User(), recipe);
        Favorites favorite2 = new Favorites(2, new User(), recipe);
        recipe.setFavorites(Arrays.asList(favorite1, favorite2));

        // Mock RecipeRepository response
        when(recipeRepository.findById(anyInt())).thenReturn(Optional.of(recipe));

        // Perform the GET request
        MvcResult result = mvc.perform(get("/api/recipes/favorites/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        // Parse JSON manually
        String jsonResponse = result.getResponse().getContentAsString();
        JSONArray jsonArray = new JSONArray(jsonResponse);
        
     // Your assertions on jsonArray
        assertThat(jsonArray.length()).isEqualTo(recipe.getFavorites().size());

        JSONObject user1 = jsonArray.getJSONObject(0);
        assertThat(user1.opt("userId")).isEqualTo(favorite1.getUser().getUserId());

        JSONObject user2 = jsonArray.getJSONObject(1);
        assertThat(user2.opt("userId")).isEqualTo(favorite2.getUser().getUserId());



        // Verify interactions with RecipeRepository
        verify(recipeRepository, times(1)).findById(1);
        verifyNoMoreInteractions(recipeRepository);
    }


    
//    @Test
//    @WithMockUser(username = "testUser", roles = "USER")
//    public void testAddRecipe() throws Exception {
//        String uri = STARTING_URI + "/add/recipe";
//        
//        // Mock data
//        User testUser = new User(1, "testUser", "testPassword", new ArrayList<>(), new ArrayList<>());
//        Recipe newRecipe = new Recipe(null, "New Recipe", 45, "Ingredients", "Directions", "image_url", testUser, new ArrayList<>());
//
//        // Mock UserRepository response
//        when(userRepository.findById(anyInt())).thenReturn(Optional.of(testUser));
//
//        // Mock JwtUtil response
//        when(jwtUtil.getLoggedInUser(anyString())).thenReturn(testUser);
//
//        // Perform the POST request
//        mvc.perform(post(uri)
//                .header("Authorization", "Bearer test-token")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(newRecipe)))
//                .andExpect(status().isCreated())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(jsonPath("$.recipeId").exists());
//
//        // Verify interactions with repositories and JwtUtil
//        verify(userRepository, times(1)).findById(anyInt());
//        verify(jwtUtil, times(1)).getLoggedInUser(anyString());
//        verify(recipeRepository, times(1)).save(any(Recipe.class));
//        verifyNoMoreInteractions(userRepository, jwtUtil, recipeRepository);
//    }
//    
//    private static String asJsonString(final Object obj) throws JsonProcessingException {
//        return new ObjectMapper().writeValueAsString(obj);
//    }
//	    @Test
//	    public void testLoadUserByUsername() {
//	        // Mock data
//	        User testUser = new User(null, "testUser", "testPassword", null, null);
//	        when(userRepository.findByYumUsername("testUser")).thenReturn(Optional.of(testUser));
//
//	        // Perform the test
//	        UserDetails userDetails = userDetailsService.loadUserByUsername("testUser");
//
//	        // Assertions
//	        assertNotNull(userDetails);
//	        assertEquals("testUser", userDetails.getUsername());
//	        assertEquals("testPassword", userDetails.getPassword());
//	        assertEquals(1, userDetails.getAuthorities().size());
//	        assertEquals("ROLE_USER", userDetails.getAuthorities().iterator().next().getAuthority());
//
//	        // Verify interactions with UserRepository
//	        verify(userRepository, times(1)).findByYumUsername("testUser");
//	        verifyNoMoreInteractions(userRepository);
//	    }
//	    
//	    @Test
//	    public void testGetLatestRecipesByAmount() throws Exception {
//
//	        int amount = 3;
//	        String uri = STARTING_URI + "/recipes/latest/" + amount;
//
//	        List<Recipe> latestRecipes = new ArrayList<>();
//
//	        latestRecipes.add(new Recipe(1, "Spaghetti Bolognese", 30, "Ground beef, tomatoes, pasta", "Cook the beef, add tomatoes, mix with pasta", "image_url_1", new User(), new ArrayList<>()));
//	        latestRecipes.add(new Recipe(2, "Chicken Alfredo", 25, "Chicken, Alfredo sauce, pasta", "Cook the chicken, add Alfredo sauce, mix with pasta", "image_url_2", new User(), new ArrayList<>()));
//	        latestRecipes.add(new Recipe(3, "Vegetarian Pizza", 20, "Dough, tomato sauce, cheese, vegetables", "Prepare dough, add sauce and toppings, bake", "image_url_3", new User(), new ArrayList<>()));
//
//	        when(recipeRepository.latestRecipesByAmount(amount)).thenReturn(latestRecipes);
//
//	        mvc.perform(get(uri))
//	                .andDo(print())
//	                .andExpect(status().isOk())
//	                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//	                .andExpect(jsonPath("$.length()").value(latestRecipes.size()))
//	                .andExpect(jsonPath("$[0].recipeId").value(latestRecipes.get(0).getRecipeId()))
//	                .andExpect(jsonPath("$[0].title").value(latestRecipes.get(0).getTitle()))
//	                .andExpect(jsonPath("$[0].prepTime").value(latestRecipes.get(0).getPrepTime()))
//	                .andExpect(jsonPath("$[0].ingredients").value(latestRecipes.get(0).getIngredients()))
//	                .andExpect(jsonPath("$[0].directions").value(latestRecipes.get(0).getDirections()))
//	                .andExpect(jsonPath("$[0].foodImageUrl").value(latestRecipes.get(0).getFoodImageUrl()))
//	                .andExpect(jsonPath("$[1].recipeId").value(latestRecipes.get(1).getRecipeId()))
//	                .andExpect(jsonPath("$[1].title").value(latestRecipes.get(1).getTitle()))
//	                .andExpect(jsonPath("$[1].prepTime").value(latestRecipes.get(1).getPrepTime()))
//	                .andExpect(jsonPath("$[1].ingredients").value(latestRecipes.get(1).getIngredients()))
//	                .andExpect(jsonPath("$[1].directions").value(latestRecipes.get(1).getDirections()))
//	                .andExpect(jsonPath("$[1].foodImageUrl").value(latestRecipes.get(1).getFoodImageUrl()))
//	                .andExpect(jsonPath("$[2].recipeId").value(latestRecipes.get(2).getRecipeId()))
//	                .andExpect(jsonPath("$[2].title").value(latestRecipes.get(2).getTitle()))
//	                .andExpect(jsonPath("$[2].prepTime").value(latestRecipes.get(2).getPrepTime()))
//	                .andExpect(jsonPath("$[2].ingredients").value(latestRecipes.get(2).getIngredients()))
//	                .andExpect(jsonPath("$[2].directions").value(latestRecipes.get(2).getDirections()))
//	                .andExpect(jsonPath("$[2].foodImageUrl").value(latestRecipes.get(2).getFoodImageUrl()));
//
//	        verify(recipeRepository, times(1)).latestRecipesByAmount(amount);
//	        verifyNoMoreInteractions(recipeRepository);
//	    }
//	    
//	    @Test
//	    public void testSearchRecipeTitles() throws Exception {
//
//	        String searchQuery = "Chicken";
//	        String uri = STARTING_URI + "/recipes/search/" + searchQuery;
//
//	        List<Recipe> matchingRecipes = new ArrayList<>();
//
//	        matchingRecipes.add(new Recipe(1, "Chicken Alfredo", 25, "Chicken, Alfredo sauce, pasta", "Cook the chicken, add Alfredo sauce, mix with pasta", "image_url_1", new User(), new ArrayList<>()));
//	        matchingRecipes.add(new Recipe(2, "Grilled Chicken Salad", 20, "Chicken, mixed greens, tomatoes, dressing", "Grill the chicken, toss with greens, add tomatoes and dressing", "image_url_2", new User(), new ArrayList<>()));
//
//	        when(recipeRepository.findByTitleContaining(searchQuery)).thenReturn(matchingRecipes);
//
//	        mvc.perform(get(uri))
//	                .andDo(print())
//	                .andExpect(status().isOk())
//	                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//	                .andExpect(jsonPath("$.length()").value(matchingRecipes.size()))
//	                .andExpect(jsonPath("$[0].recipeId").value(matchingRecipes.get(0).getRecipeId()))
//	                .andExpect(jsonPath("$[0].title").value(matchingRecipes.get(0).getTitle()))
//	                .andExpect(jsonPath("$[0].prepTime").value(matchingRecipes.get(0).getPrepTime()))
//	                .andExpect(jsonPath("$[0].ingredients").value(matchingRecipes.get(0).getIngredients()))
//	                .andExpect(jsonPath("$[0].directions").value(matchingRecipes.get(0).getDirections()))
//	                .andExpect(jsonPath("$[0].foodImageUrl").value(matchingRecipes.get(0).getFoodImageUrl()))
//	                .andExpect(jsonPath("$[1].recipeId").value(matchingRecipes.get(1).getRecipeId()))
//	                .andExpect(jsonPath("$[1].title").value(matchingRecipes.get(1).getTitle()))
//	                .andExpect(jsonPath("$[1].prepTime").value(matchingRecipes.get(1).getPrepTime()))
//	                .andExpect(jsonPath("$[1].ingredients").value(matchingRecipes.get(1).getIngredients()))
//	                .andExpect(jsonPath("$[1].directions").value(matchingRecipes.get(1).getDirections()))
//	                .andExpect(jsonPath("$[1].foodImageUrl").value(matchingRecipes.get(1).getFoodImageUrl()));
//
//	        verify(recipeRepository, times(1)).findByTitleContaining(searchQuery);
//	        verifyNoMoreInteractions(recipeRepository);
//	    }
//	    
//	    @Test
//	    public void testGetSingleRecipe() throws Exception {
//
//	        int recipeId = 1;
//	        String uri = STARTING_URI + "/recipes/" + recipeId;
//
//	        Recipe existingRecipe = new Recipe(recipeId, "Spaghetti Bolognese", 30, "Ground beef, tomatoes, onion, garlic, pasta", "Cook beef with onion and garlic, add tomatoes, simmer, serve over pasta", "image_url", new User(), new ArrayList<>());
//
//	        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(existingRecipe));
//
//	        mvc.perform(get(uri))
//	                .andDo(print())
//	                .andExpect(status().isOk())
//	                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//	                .andExpect(jsonPath("$.recipeId").value(existingRecipe.getRecipeId()))
//	                .andExpect(jsonPath("$.title").value(existingRecipe.getTitle()))
//	                .andExpect(jsonPath("$.prepTime").value(existingRecipe.getPrepTime()))
//	                .andExpect(jsonPath("$.ingredients").value(existingRecipe.getIngredients()))
//	                .andExpect(jsonPath("$.directions").value(existingRecipe.getDirections()))
//	                .andExpect(jsonPath("$.foodImageUrl").value(existingRecipe.getFoodImageUrl()));
//
//	        verify(recipeRepository, times(1)).findById(recipeId);
//	        verifyNoMoreInteractions(recipeRepository);
//	    }
//	    
//	    @Test
//	    public void testGetFavorites() throws Exception {
//	        int recipeId = 1;
//	        String uri = STARTING_URI + "/recipes/favorites/" + recipeId;
//
//	        Recipe recipe = new Recipe();
//	        recipe.setRecipeId(recipeId);
//	        recipe.setTitle("Test Recipe");
//	        recipe.setPrepTime(30);
//	        recipe.setIngredients("Test ingredients");
//	        recipe.setDirections("Test directions");
//	        recipe.setFoodImageUrl("https://example.com/test-image.jpg");
//
//	        List<Favorites> favorites = Arrays.asList(
//	                new Favorites(1, new User(1, "JohnDoe", "password", new ArrayList<>(), new ArrayList<>()), recipe),
//	                new Favorites(2, new User(2, "JaneSmith", "secure123", new ArrayList<>(), new ArrayList<>()), recipe)
//	        );
//
//	        recipe.setFavorites(favorites);
//
//	        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));
//
//	        mvc.perform(get(uri))
//	                .andDo(print())
//	                .andExpect(status().isOk())
//	                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//	                .andExpect(jsonPath("$.length()").value(favorites.size()))
//	                .andExpect(jsonPath("$[0].favoritesId").value(favorites.get(0).getFavoritesId()))
//	                .andExpect(jsonPath("$[0].user.userId").value(favorites.get(0).getUser().getUserId()))
//	                .andExpect(jsonPath("$[0].user.yumUsername").value(favorites.get(0).getUser().getYumUsername()))
//	                .andExpect(jsonPath("$[0].recipe.recipeId").value(favorites.get(0).getRecipe().getRecipeId()))
//	                .andExpect(jsonPath("$[0].recipe.title").value(favorites.get(0).getRecipe().getTitle()))
//	                .andExpect(jsonPath("$[1].favoritesId").value(favorites.get(1).getFavoritesId()))
//	                .andExpect(jsonPath("$[1].user.userId").value(favorites.get(1).getUser().getUserId()))
//	                .andExpect(jsonPath("$[1].user.yumUsername").value(favorites.get(1).getUser().getYumUsername()))
//	                .andExpect(jsonPath("$[1].recipe.recipeId").value(favorites.get(1).getRecipe().getRecipeId()))
//	                .andExpect(jsonPath("$[1].recipe.title").value(favorites.get(1).getRecipe().getTitle()));
//
//	        verify(recipeRepository, times(1)).findById(recipeId);
//	        verifyNoMoreInteractions(recipeRepository);
//	    }

}
