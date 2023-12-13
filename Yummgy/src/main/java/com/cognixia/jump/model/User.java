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
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
public class User implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;
	
	@NotBlank
	@Column(unique = true, nullable = false)
	private String yumUsername;
	
	@NotBlank
	@Column(nullable = false)
	private String yumPassword;
	
	@OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Recipe> recipes;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Favorites> favorites;
	
	public User() {
		
	}
	public User(Integer userId, @NotBlank String yumUsername, @NotBlank String yumPassword, List<Recipe> recipes,
			List<Favorites> favorites) {
		super();
		this.userId = userId;
		this.yumUsername = yumUsername;
		this.yumPassword = yumPassword;
		this.recipes = recipes;
		this.favorites = favorites;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public String getYumUsername() {
		return yumUsername;
	}
	
	public void setYumUsername(String yumUsername) {
		this.yumUsername = yumUsername;
	}
	
	@JsonProperty(access = Access.WRITE_ONLY)
	public String getYumPassword() {
		return yumPassword;
	}

	public void setYumPassword(String yumPassword) {
		this.yumPassword = yumPassword;
	}

	public List<Recipe> getRecipes() {
		return recipes;
	}

	public void setRecipes(List<Recipe> recipes) {
		this.recipes = recipes;
	}

	public List<Favorites> getFavorites() {
		return favorites;
	}

	public void setFavorites(List<Favorites> favorites) {
		this.favorites = favorites;
	}
	
	public String toJson() {
	    return "{\"userId\" : " + userId
	            + ", \"yumUsername\" : \"" + yumUsername + "\""
	            + ", \"yumPassword\" : \"" + yumPassword + "\""
	            // Add other fields as needed...
	            + "}";
	}
	@Override
	public String toString() {
		return "User [userId=" + userId + ", yumUsername=" + yumUsername + ", yumPassword=" + yumPassword + "]";
	}
	@Override
	public int hashCode() {
		return Objects.hash(yumUsername);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(yumUsername, other.yumUsername);
	}
	
	
	
}
