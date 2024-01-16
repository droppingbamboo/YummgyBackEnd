package com.cognixia.jump.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cognixia.jump.model.Recipe;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
	
	public List<Recipe> findAll();
	
	@Query(value = "select * from recipe order by recipe_id desc limit ?1", nativeQuery = true)
	public List<Recipe> latestRecipesByAmount(int numberGrabbed);
	
	public List<Recipe> findByTitleContaining(String search);
	
	@Query(value = "select * from recipe where title like %?1% order by prep_time asc", nativeQuery = true)
	public List<Recipe> findByTitleSortByPrep(String search);
	
	@Transactional
	@Modifying
	@Query(value = "delete from recipe where recipe.recipe_id = ?1", nativeQuery = true)
	public int deleteRecipe(int id);
}
