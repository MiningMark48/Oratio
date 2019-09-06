package com.miningmark48.tidalchatbot.commands;

import com.miningmark48.tidalchatbot.commands.base.ICommand;
import com.miningmark48.tidalchatbot.handler.HandlerMessages;
import com.miningmark48.tidalchatbot.reference.Reference;
import com.miningmark48.tidalchatbot.util.UtilLogger;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandReload implements ICommand {

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if (event.getAuthor().getId().equals(Reference.botOwner)) {
            event.getTextChannel().sendMessage("Reloading...").queue(m -> {
                HandlerMessages.init();
                m.editMessage("Reloaded!").queue();
                UtilLogger.INFO.log(String.format("Reload! (by %s)", event.getAuthor().getAsTag()));
            });
        } else {
            event.getTextChannel().sendMessage(String.format("Sorry %s, you do not have permission to use that command.", event.getAuthor().getAsMention())).queue();
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDesc() {
        return "Reloads the messages.";
    }

    @Override
    public String getUsage() {
        return "reload";
    }

//    @Override
//    public EnumRestrictions getPermissionRequired() {
//        return EnumRestrictions.REGULAR;
//    }

}
