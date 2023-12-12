package com.cognixia.jump.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cognixia.jump.model.Recipe;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
	
	public List<Recipe> findAll();
	
	@Query(value = "select * from recipe order by recipe_id desc limit ?1", nativeQuery = true)
	public List<Recipe> latestRecipesByAmount(int numberGrabbed);
	
	public List<Recipe> findByTitleContaining(String search);
}
