package com.miningmark48.oratio.commands.base;

import com.miningmark48.oratio.Oratio;
import com.miningmark48.oratio.commands.CommandPing;
import com.miningmark48.oratio.commands.CommandReload;
import com.miningmark48.oratio.commands.CommandToggleCB;
import com.miningmark48.oratio.util.UtilLogger;

public class InitializeCommands {

    public static void init() {
        Oratio.commands.clear();

        registerCommand("pingcb", new CommandPing());
        registerCommand("reload", new CommandReload());
        registerCommand("togglecb", new CommandToggleCB());

        UtilLogger.INFO.log(String.format("%s commands initialized", Oratio.commands.size()));
    }

    private static void registerCommand(String trigger, ICommand command) {
        Oratio.commands.put(trigger, command);
    }

}
