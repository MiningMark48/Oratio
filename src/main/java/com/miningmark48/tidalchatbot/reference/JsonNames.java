package com.miningmark48.tidalchatbot.reference;

public enum JsonNames {

    MESSAGES("messages"),
    RESPONSES("responses"),
    TRIGGERS("triggers"),
    TYPE("type"),
    OP_AND("and"),
    OP_OR("or");

    private final String name;

    JsonNames(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
