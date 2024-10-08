package com.peated.valhack.model;

public enum Tournament {
    GAME_CHANGERS("game-changers", 1),
    VCT_CHALLENGERS("vct-challengers", 2),
    VCT_INTERNATIONAL("vct-international", 3);

    private final String folderName;
    private final Integer id;

    Tournament(String folderName, Integer id) {
        this.folderName = folderName;
        this.id = id;
    }

    public String getFolderName() {
        return folderName;
    }

    public Integer getId() {
        return id;
    }

    public static Tournament fromId(Integer id) {
        for (Tournament tournament : values()) {
            if (tournament.getId().equals(id)) {
                return tournament;
            }
        }
        return null;
    }
}
