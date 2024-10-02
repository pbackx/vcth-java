package com.peated.valhack.model;

import java.util.Map;

public record MappingData(
    String platformGameId, 
    String esportsGameId, 
    String tournamentId, 
    Map<Integer, String> teamMapping, 
    Map<Integer, String> participantMapping) {

}
