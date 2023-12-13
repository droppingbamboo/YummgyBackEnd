package com.cognixia.jump.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cognixia.jump.model.Favorites;

@Repository
public interface FavoritesRepository extends JpaRepository<Favorites, Integer> {
	@Transactional
	@Modifying
	@Query(value = "delete from favorites where favorites.favorites_id = ?1", nativeQuery = true)
	public int deleteFavorite(int id);
}
