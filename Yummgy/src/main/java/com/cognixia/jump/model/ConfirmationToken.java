package com.cognixia.jump.model;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "confirmation_token")
public class ConfirmationToken {
	
	@Schema(description="The id of a confirmation token", example="1")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Schema(description="The confirmation token", example="7d0026b4-b3ba-4d42-9df7-67fd6fc29ead", nullable=false)
	@Column(nullable = false)
    private String token;
	
	@Schema(description="The time at which a confirmation token was created", example="2024-01-30 13:16:30.419914", nullable=false)
    @Column(nullable = false)
    private LocalDateTime createdAt;
	
	@Schema(description="The time at which a confirmation token expires (15 minutes from creation)", example="2024-01-30 13:31:30.419914", nullable=false)
    @Column(nullable = false)
    private LocalDateTime expiresAt;
	
	@Schema(description="The time at which a confirmation token was confirmed", example="2024-01-30 13:16:37.361652", nullable=false)
    private LocalDateTime confirmedAt;
    
    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "user_id"
    )
    private User user;
    
    public ConfirmationToken(String token, LocalDateTime createdAt, LocalDateTime expiresAt, User user) {
    		this.token = token;
    		this.createdAt = createdAt;
    		this.expiresAt = expiresAt;
    		this.user = user;
    }

	public ConfirmationToken() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(LocalDateTime expiresAt) {
		this.expiresAt = expiresAt;
	}

	public LocalDateTime getConfirmedAt() {
		return confirmedAt;
	}

	public void setConfirmedAt(LocalDateTime confirmedAt) {
		this.confirmedAt = confirmedAt;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
