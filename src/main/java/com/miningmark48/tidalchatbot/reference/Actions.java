package com.miningmark48.tidalchatbot.reference;

public enum Actions {

    COIN("coin"),
    KEY("key"),
    MATH("math"),
    TIME("time");

    private final String name;

    Actions(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String getAction() {
        return String.format("{%s}", getName());
    }

}
