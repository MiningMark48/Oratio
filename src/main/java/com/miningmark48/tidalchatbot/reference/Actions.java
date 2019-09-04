package com.miningmark48.tidalchatbot.reference;

public enum Actions {

    MATH("math");

    private final String name;

    Actions(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
