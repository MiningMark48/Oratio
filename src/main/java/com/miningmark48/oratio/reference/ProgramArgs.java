package com.miningmark48.oratio.reference;

public enum ProgramArgs {

    DEBUG("debug");

    private final String name;

    ProgramArgs(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
