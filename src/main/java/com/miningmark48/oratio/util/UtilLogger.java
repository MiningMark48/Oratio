package com.miningmark48.oratio.util;

import com.miningmark48.oratio.Oratio;

public enum UtilLogger {

    INFO("\u001B[34m"),
    DEBUG("\u001B[36m"),
    WARN("\u001B[33m"),
    FATAL("\u001B[31m"),
    STATUS("\u001B[32m");

    private String color = getReset();

    UtilLogger() {

    }

    UtilLogger(String color) {
        this.color = color;
    }

    private String getColor() {
        return color;
    }

    private String getReset() {
        return "\u001B[0m";
    }

    public void log(String message) {
        if (this == DEBUG && !Oratio.isDebugMode()) return;
        System.out.printf("%s[%s] [%s] %s%s\n", getColor(), UtilTime.getTimeHMS(), this.name().toUpperCase(), message, getReset());
    }

}
