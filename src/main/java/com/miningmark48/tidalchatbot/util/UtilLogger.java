package com.miningmark48.tidalchatbot.util;

import com.miningmark48.tidalchatbot.TidalChatbot;

public enum UtilLogger {

    INFO,
    DEBUG,
    WARN,
    FATAL,
    STATUS;

    UtilLogger() {

    }

    public void log(String message) {
        if (this == DEBUG && !TidalChatbot.isDebugMode()) return;
        System.out.printf("[%s] [%s] %s\n", UtilTime.getTimeHMS(), this.name().toUpperCase(), message);
    }

}
