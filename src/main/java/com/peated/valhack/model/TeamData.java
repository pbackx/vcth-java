package com.peated.valhack.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TeamData(
        String id,
        String acronym,
        @JsonProperty("home_league_id") String homeLeagueId,
        @JsonProperty("dark_logo_url") String darkLogoUrl,
        @JsonProperty("light_logo_url") String lightLogoUrl,
        String slug,
        String name
) {
}
