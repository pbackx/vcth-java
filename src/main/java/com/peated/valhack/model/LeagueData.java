package com.peated.valhack.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LeagueData(
        @JsonProperty("league_id") String leagueId,
        String region,
        @JsonProperty("dark_logo_url") String darkLogoUrl,
        @JsonProperty("light_logo_url") String lightLogoUrl,
        String name,
        String slug
) {
}
