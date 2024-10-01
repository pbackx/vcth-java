package com.peated.valhack.model;

import java.util.Set;

import org.springframework.data.annotation.Id;

import org.springframework.data.relational.core.mapping.MappedCollection;

public record Team(@Id Integer id, String name, @MappedCollection(idColumn = "TEAM_ID") Set<PlayerRef> players) {

}
