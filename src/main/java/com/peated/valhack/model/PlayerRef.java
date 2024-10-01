package com.peated.valhack.model;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("PLAYER_TEAM")
public record PlayerRef(
    @Column("PLAYER_ID") Integer playerId
) {

}
