package com.miningmark48.tidalchatbot.commands.base;

import com.miningmark48.tidalchatbot.TidalChatbot;
import com.miningmark48.tidalchatbot.commands.CommandReload;
import com.miningmark48.tidalchatbot.commands.CommandToggleCB;
import com.miningmark48.tidalchatbot.util.UtilLogger;

public class InitializeCommands {

    public static void init() {
        TidalChatbot.commands.clear();

        registerCommand("reload", new CommandReload());
        registerCommand("togglecb", new CommandToggleCB());

        UtilLogger.log(UtilLogger.LogType.INFO, TidalChatbot.commands.size() + " commands initialized.");
    }

    private static void registerCommand(String trigger, ICommand command) {
        TidalChatbot.commands.put(trigger, command);
    }

}
