package com.miningmark48.tidalchatbot;

import com.miningmark48.tidalchatbot.commands.base.ICommand;
import com.miningmark48.tidalchatbot.commands.base.InitializeCommands;
import com.miningmark48.tidalchatbot.messages.InitializeMessages;
import com.miningmark48.tidalchatbot.reference.Reference;
import com.miningmark48.tidalchatbot.util.UtilCommandParser;
import com.miningmark48.tidalchatbot.util.UtilConfig;
import com.miningmark48.tidalchatbot.util.UtilLogger;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.HashMap;

public class TidalChatbot {

    public static JDA jda;
    static final UtilCommandParser parser = new UtilCommandParser();

    public static HashMap<String, ICommand> commands = new HashMap<>();

    public static void main(String[] args) {
        setupAndConnectBot();
    }

    public static void setupAndConnectBot() {
        if (!UtilConfig.getConfigs()) {
            return;
        }

        try {
            jda = new JDABuilder(AccountType.BOT).addEventListeners(new BotListener()).setToken(Reference.botToken).build().awaitReady();
            jda.setAutoReconnect(true);

            UtilLogger.log(UtilLogger.LogType.STATUS, "Bot started!");

        } catch (Exception e) {
            e.printStackTrace();
        }

        InitializeCommands.init();
        InitializeMessages.init();

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

    static void handleMessage(MessageReceivedEvent event){
        InitializeMessages.handleMessage(event);
    }

}
