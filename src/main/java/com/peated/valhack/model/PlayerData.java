package com.peated.valhack.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PlayerData(
        String id,
        String handle,
        @JsonProperty("first_name") String firstName,
        @JsonProperty("last_name") String lastName,
        String status,
        @JsonProperty("photo_url") String photoUrl,
        @JsonProperty("home_team_id") String homeTeamId,
        @JsonProperty("created_at") String createdAt,
        @JsonProperty("updated_at") String updatedAt
) {
}
