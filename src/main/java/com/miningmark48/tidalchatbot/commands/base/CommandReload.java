package com.miningmark48.tidalchatbot.commands.base;

import com.miningmark48.tidalchatbot.messages.InitializeMessages;
import com.miningmark48.tidalchatbot.reference.Reference;
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
                InitializeMessages.init();
                m.editMessage("Reloaded!").queue();
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
