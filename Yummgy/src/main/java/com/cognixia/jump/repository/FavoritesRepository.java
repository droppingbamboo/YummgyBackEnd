package com.cognixia.jump.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cognixia.jump.model.Favorites;

@Repository
public interface FavoritesRepository extends JpaRepository<Favorites, Integer> {
	
}
