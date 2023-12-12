package com.cognixia.jump.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cognixia.jump.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	public List<User> findAll();
	
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.favorites WHERE u.userId = :userId")
    Optional<User> findByIdWithFavorites(@Param("userId") Integer userId);
}
