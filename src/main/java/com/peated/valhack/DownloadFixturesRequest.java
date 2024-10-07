package com.peated.valhack;

import com.peated.valhack.model.Tournament;

public class DownloadFixturesRequest {
    private String tournament;

    public Tournament getTournament() {
        return Tournament.valueOf(tournament);
    }

    public void setTournament(String tournament) {
        this.tournament = tournament;
    }
}
