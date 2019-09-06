package com.miningmark48.tidalchatbot;

import com.miningmark48.tidalchatbot.commands.base.ICommand;
import com.miningmark48.tidalchatbot.commands.base.InitializeCommands;
import com.miningmark48.tidalchatbot.handler.HandlerMessages;
import com.miningmark48.tidalchatbot.handler.HandlerWatchService;
import com.miningmark48.tidalchatbot.reference.ProgramArgs;
import com.miningmark48.tidalchatbot.reference.Reference;
import com.miningmark48.tidalchatbot.util.UtilCommandParser;
import com.miningmark48.tidalchatbot.util.UtilConfig;
import com.miningmark48.tidalchatbot.util.UtilLogger;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.ChannelType;

import java.util.HashMap;

public class TidalChatbot {

    public static JDA jda;
    static final UtilCommandParser parser = new UtilCommandParser();

    public static HashMap<String, ICommand> commands = new HashMap<>();

    private static boolean isDebugMode = false;

    public static void main(String[] args) {
        for (String arg : args) {
            if (ProgramArgs.valueOf(arg.toUpperCase()).equals(ProgramArgs.DEBUG)) {
                isDebugMode = true;
            }
        }

        setupAndConnectBot();
    }

    public static void setupAndConnectBot() {

        if (!UtilConfig.getConfigs()) {
            return;
        }

        try {
            jda = new JDABuilder(AccountType.BOT).addEventListeners(new BotListener()).setToken(Reference.botToken).build().awaitReady();
            jda.setAutoReconnect(true);

            UtilLogger.STATUS.log("Bot Started!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        InitializeCommands.init();
        HandlerMessages.init();
        HandlerWatchService.init();
    }

    static void handleCommand(UtilCommandParser.CommandContainer cmd) {
        if (commands.containsKey(cmd.invoke)) {
            boolean safe = commands.get(cmd.invoke).called(cmd.args, cmd.event);
            if (safe){
                if (cmd.event.getChannelType().equals(ChannelType.PRIVATE)) {
                    return;
                }
                commands.get(cmd.invoke).action(cmd.args, cmd.event);
            } else {
                commands.get(cmd.invoke).executed(safe, cmd.event);
            }
        }
    }

    public static boolean isDebugMode() {
        return isDebugMode;
    }
}
