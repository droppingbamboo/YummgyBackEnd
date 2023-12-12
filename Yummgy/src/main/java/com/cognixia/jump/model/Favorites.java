package com.cognixia.jump.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Favorites implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer favoritesId;
	
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "userId")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "recipe_id", referencedColumnName = "recipeId")
	private Recipe recipe;
	
	public Favorites() {
		
	}

	public Favorites(Integer id, User user, Recipe recipe) {
		super();
		this.favoritesId = id;
		this.user = user;
		this.recipe = recipe;
	}

	public Integer getFavoritesId() {
		return favoritesId;
	}

	public void setFavoritesId(Integer favorites_id) {
		this.favoritesId = favorites_id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Recipe getRecipe() {
		return recipe;
	}

	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}
	
	
	
}
