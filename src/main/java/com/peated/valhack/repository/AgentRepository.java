package com.peated.valhack.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.peated.valhack.model.Agent;

public interface AgentRepository extends CrudRepository<Agent, Integer> {
    boolean existsByGuid(String guid);
    Optional<Agent> findByGuid(String guid);
}
