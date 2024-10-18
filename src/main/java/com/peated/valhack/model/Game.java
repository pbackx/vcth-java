package com.peated.valhack.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

public record Game(
        @Id Integer id,
        String platformGameId,
        @Column("WINNING_TEAM_ID") Integer winningTeamId,
        @Column("LOSING_TEAM_ID") Integer losingTeamId,
        Integer tournamentId,
        Integer gameYear
) {
}
