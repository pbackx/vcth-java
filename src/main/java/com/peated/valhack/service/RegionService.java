package com.peated.valhack.service;

import com.peated.valhack.model.Tournament;
import com.peated.valhack.repository.LeagueDataRepository;
import com.peated.valhack.repository.TeamDataRepository;
import org.springframework.stereotype.Service;

@Service
public class RegionService {
    private final TeamDataRepository teamDataRepository;
    private final LeagueDataRepository leagueDataRepository;

    public RegionService(TeamDataRepository teamDataRepository, LeagueDataRepository leagueDataRepository) {
        this.teamDataRepository = teamDataRepository;
        this.leagueDataRepository = leagueDataRepository;
    }

    public String getTeamRegion(Tournament tournament, String teamMappingDataId) {
        var team = teamDataRepository.getTeamDataByMappingDataId(tournament, teamMappingDataId);
        var league = leagueDataRepository.getLeagueDataByMappingDataId(tournament, team.homeLeagueId());
        return league.region();
    }
}
