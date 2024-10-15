package com.peated.valhack.model;

public enum Role {
    // Ideally this should be loaded from the database... I know...
    Duelist(1),
    Initiator(2),
    Controller(3),
    Sentinel(4);

    private final int id;

    Role(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}