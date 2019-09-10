package com.miningmark48.oratio.commands;

import com.miningmark48.oratio.commands.base.ICommand;
import com.miningmark48.oratio.handler.HandlerServerConfig;
import com.miningmark48.oratio.reference.Reference;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandToggleCB implements ICommand {

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        HandlerServerConfig.toggleCBBlacklistUser(event, event.getAuthor().getId());
        event.getTextChannel().sendMessage(event.getAuthor().getAsMention() + ", **" + (HandlerServerConfig.isUserCBBlacklisted(event, event.getAuthor().getId()) ? "added" : "removed") + "** you " + (HandlerServerConfig.isUserCBBlacklisted(event, event.getAuthor().getId()) ? "to" : "from") + " the chatbot blacklist.").queue();
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String getName() {
        return "arblacklist";
    }

    @Override
    public String getDesc() {
        return "Opt-in to be blacklisted from " + Reference.botName + "'s Chatbot";
    }

    @Override
    public String getUsage() {
        return "togglecb";
    }

}
