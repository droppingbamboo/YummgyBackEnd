package com.cognixia.jump.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
	
	public static enum Role {
		ROLE_USER, ROLE_ADMIN
	}
	
	@Schema(description="The id of a user.", example="1", required=true, nullable=false)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;
	
	@Schema(description="The username of a user", example="bomono3", required=true, nullable=false)
	@NotBlank(message = "Username cannot be blank")
	@Column(unique = true, nullable = false)
	private String yumUsername;
	
	@Schema(description="The password of a user, which is stored in an encrypted manner, and never sent in JSON responses.", example="342fdfsdf$T@fegg6$", required=true, nullable=false)
	@NotBlank
	@Column(nullable = false)
	private String yumPassword;
	
	@Schema(description="The user's security role", example="ROLE_USER")
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;
	
	@Schema(description="The user's email", example="bomono8@gmail.com")
	@NotBlank
	private String email;
	
	@Schema(description="The user's account's expiration status", example="false")
	@Column(columnDefinition = "boolean default false")
	private boolean expired;
	
	@Schema(description="The user's account's lock status", example="false")
	@Column(columnDefinition = "boolean default false")
	private boolean locked;
	
	@Schema(description="The user's account's credentials status", example="false")
	@Column(columnDefinition = "boolean default false")
	private boolean credentialsBad;
	
	@Schema(description="The user's account's enabled status", example="false")
	@Column(columnDefinition = "boolean default false")
	private boolean enabled;
	
	@OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Recipe> recipes;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<Favorites> favorites;
	
	public User() {
		
	}
	public User(Integer userId, @NotBlank String yumUsername, @NotBlank String yumPassword, List<Recipe> recipes,
			List<Favorites> favorites, String email) {
		super();
		this.userId = userId;
		this.yumUsername = yumUsername;
		this.yumPassword = yumPassword;
		this.recipes = recipes;
		this.favorites = favorites;
		this.email = email;
	}

	public User(@NotBlank(message = "Username cannot be blank") String yumUsername, @NotBlank String yumPassword,
			Role role, @NotBlank String email) {
		super();
		this.yumUsername = yumUsername;
		this.yumPassword = yumPassword;
		this.role = role;
		this.email = email;
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
	
	public Role getRole() {
		return role;
	}
	
	public void setRole(Role role) {
		this.role = role;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public boolean isExpired() {
		return expired;
	}
	
	public void setExpired(boolean expired) {
		this.expired = expired;
	}
	
	public boolean isLocked() {
		return locked;
	}
	
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	
	public boolean isCredentialsBad() {
		return credentialsBad;
	}
	
	public void setCredentialsBad(boolean credentialsBad) {
		this.credentialsBad = credentialsBad;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public String toJson() {
	    return "{\"userId\" : " + userId
	            + ", \"yumUsername\" : \"" + yumUsername + "\""
	            + ", \"yumPassword\" : \"" + yumPassword + "\""
	            + ", \"email\" : \"" + email + "\""
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
