package com.peated.valhack.repository;

import com.peated.valhack.model.Player;
import org.springframework.data.repository.ListCrudRepository;

public interface PlayerRepository extends ListCrudRepository<Player, Integer> {

}
