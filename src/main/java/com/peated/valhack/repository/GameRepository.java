package com.peated.valhack.repository;

import com.peated.valhack.model.Game;

import java.util.Optional;

import org.springframework.data.repository.ListCrudRepository;

public interface GameRepository extends ListCrudRepository<Game, Integer> {
    Optional<Game> findByPlatformGameId(String externalId);
    boolean existsByPlatformGameId(String externalId);
}
