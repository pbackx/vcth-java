package com.peated.valhack.repository;

import com.peated.valhack.model.Team;

import java.util.Optional;

import org.springframework.data.repository.ListCrudRepository;

public interface TeamRepository extends ListCrudRepository<Team, Integer> {
    Optional<Team> findByMappingDataId(String externalId);
    boolean existsByMappingDataId(String externalId);
}
