package com.peated.valhack.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;

import java.util.Set;

public record Team(
        @Id Integer id,
        String name,
        String mappingDataId,
        String region,
        @MappedCollection(idColumn = "TEAM_ID") Set<PlayerRef> players
) {

}
