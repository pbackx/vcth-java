package com.peated.valhack.model;

public enum Tournament {
    GAME_CHANGERS("game-changers"),
    VCT_CHALLENGERS("vct-challengers"),
    VCT_INTERNATIONAL("vct-international");

    private final String folderName;

    Tournament(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderName() {
        return folderName;
    }
}
