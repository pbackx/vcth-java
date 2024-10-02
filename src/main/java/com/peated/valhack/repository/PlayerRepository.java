package com.peated.valhack.repository;

import com.peated.valhack.model.Player;

import java.util.Optional;

import org.springframework.data.repository.ListCrudRepository;

public interface PlayerRepository extends ListCrudRepository<Player, Integer> {
    Optional<Player> findByMappingDataId(String externalId);

    boolean existsByMappingDataId(String externalId);
}
