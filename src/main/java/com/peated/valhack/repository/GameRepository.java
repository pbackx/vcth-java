package com.peated.valhack.repository;

import com.peated.valhack.model.Game;
import org.springframework.data.repository.ListCrudRepository;

public interface GameRepository extends ListCrudRepository<Game, Integer> {

}
