package com.miningmark48.oratio.commands;

import com.miningmark48.oratio.commands.base.ICommand;
import com.miningmark48.oratio.util.UtilLogger;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.temporal.ChronoUnit;

public class CommandPing implements ICommand {

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        event.getTextChannel().sendMessage("\uD83C\uDFD3 **Pong! **...").queue(m -> m.editMessage("\uD83C\uDFD3 **Pong! **" + Math.abs(event.getMessage().getTimeCreated().until(m.getTimeCreated(), ChronoUnit.MILLIS)) + "ms \uD83D\uDCF6").queue());
        UtilLogger.DEBUG.log("PING DEBUG");
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {
        return;
    }

    @Override
    public String getName() {
        return "pingcb";
    }

    @Override
    public String getDesc() {
        return "Simple Ping, Pong.";
    }

    @Override
    public String getUsage() {
        return "pingcb";
    }

}
