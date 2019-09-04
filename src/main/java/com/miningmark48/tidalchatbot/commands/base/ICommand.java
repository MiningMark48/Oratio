package com.miningmark48.tidalchatbot.commands.base;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface ICommand {

    //Info
    String getName();
    String getDesc();
    String getUsage();
    default boolean isMusic() { return false; }

    boolean called(String[] args, MessageReceivedEvent event);
    void action(String[] args, MessageReceivedEvent event);
    void executed(boolean success, MessageReceivedEvent event);

}
