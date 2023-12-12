package com.cognixia.jump.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
public class Recipe implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer recipeId;
	
	@NotBlank
	@Column(nullable = false)
	private String title;
	
	// Will be in minutes
	
	@Min(0)
	@Column(nullable = false)
	private Integer prepTime;
	
	@NotBlank
	@Column(nullable = false)
	private String ingredients;
	
	@NotBlank
	@Column(nullable = false)
	private String directions;
	
	@NotBlank
	@Column(nullable = true)
	private String foodImageUrl;
	
	@ManyToOne
	@JoinColumn(name = "author", referencedColumnName = "userId")
	private User author;
	
	@OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Favorites> favorites;
	
	public Recipe() {
		
	}

	public Integer getRecipeId() {
		return recipeId;
	}

	public void setRecipeId(Integer recipeId) {
		this.recipeId = recipeId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getPrepTime() {
		return prepTime;
	}

	public void setPrepTime(Integer prepTime) {
		this.prepTime = prepTime;
	}

	public String getIngredients() {
		return ingredients;
	}

	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}

	public String getDirections() {
		return directions;
	}

	public void setDirections(String directions) {
		this.directions = directions;
	}

	public String getFoodImageUrl() {
		return foodImageUrl;
	}

	public void setFoodImageUrl(String foodImageUrl) {
		this.foodImageUrl = foodImageUrl;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public List<Favorites> getFavorites() {
		return favorites;
	}

	public void setFavorites(List<Favorites> favorites) {
		this.favorites = favorites;
	}
	
	
}
