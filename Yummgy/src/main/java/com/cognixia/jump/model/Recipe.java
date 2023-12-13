package com.cognixia.jump.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

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
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "recipe")
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
	
	@Column(nullable = true)
	private String foodImageUrl;
	
	@ManyToOne
	@JoinColumn(name = "author", referencedColumnName = "userId")
	private User author;
	
	@OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<Favorites> favorites;
	
	public Recipe() {
		
	}

	public Recipe(@NotBlank String title, @Min(0) Integer prepTime, @NotBlank String ingredients,
			@NotBlank String directions, @NotBlank String foodImageUrl) {
		super();
		this.recipeId = null;
		this.title = title;
		this.prepTime = prepTime;
		this.ingredients = ingredients;
		this.directions = directions;
		this.foodImageUrl = foodImageUrl;
	}

	public Recipe(Integer recipeId, @NotBlank String title, @Min(0) Integer prepTime, @NotBlank String ingredients,
			@NotBlank String directions, @NotBlank String foodImageUrl, User author, List<Favorites> favorites) {
		super();
		this.recipeId = recipeId;
		this.title = title;
		this.prepTime = prepTime;
		this.ingredients = ingredients;
		this.directions = directions;
		this.foodImageUrl = foodImageUrl;
		this.author = author;
		this.favorites = favorites;
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

	@Override
	public int hashCode() {
		return Objects.hash(author, directions, favorites, foodImageUrl, ingredients, prepTime, recipeId, title);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Recipe other = (Recipe) obj;
		return Objects.equals(author, other.author) && Objects.equals(directions, other.directions)
				&& Objects.equals(favorites, other.favorites) && Objects.equals(foodImageUrl, other.foodImageUrl)
				&& Objects.equals(ingredients, other.ingredients) && Objects.equals(prepTime, other.prepTime)
				&& Objects.equals(recipeId, other.recipeId) && Objects.equals(title, other.title);
	}

	@Override
	public String toString() {
		return "Recipe [recipeId=" + recipeId + ", title=" + title + ", prepTime=" + prepTime + ", ingredients="
				+ ingredients + ", directions=" + directions + ", foodImageUrl=" + foodImageUrl
				+ "]";
	}
	
	
	
}
