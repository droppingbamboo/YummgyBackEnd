package com.cognixia.jump.model;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "favorites")
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

	@Override
	public int hashCode() {
		return Objects.hash(recipe, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Favorites other = (Favorites) obj;
		return Objects.equals(recipe, other.recipe) && Objects.equals(user, other.user);
	}

	@Override
	public String toString() {
		return "Favorites [favoritesId=" + favoritesId + ", user=" + user + ", recipe=" + recipe + "]";
	}
	
}
