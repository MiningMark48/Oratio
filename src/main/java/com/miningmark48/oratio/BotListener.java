package com.miningmark48.oratio;

import com.miningmark48.oratio.handler.HandlerMessages;
import com.miningmark48.oratio.handler.HandlerServerConfig;
import com.miningmark48.oratio.reference.Reference;
import com.miningmark48.oratio.util.UtilLogger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class BotListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event){
        if (event.getJDA().getStatus() == JDA.Status.ATTEMPTING_TO_RECONNECT || event.getJDA().getStatus() == JDA.Status.CONNECTING_TO_WEBSOCKET){
            return;
        }

        if(event.getMessage().getContentRaw().startsWith(Reference.botCommandKey) && !event.getMessage().getAuthor().getId().equalsIgnoreCase(event.getJDA().getSelfUser().getId())) {
            Oratio.handleCommand(Oratio.parser.parse(event.getMessage().getContentRaw(), event));
        }

        event.getJDA().getSelfUser();
        if (event.getMember() != null && !event.getMessage().getAuthor().getId().equalsIgnoreCase(event.getJDA().getSelfUser().getId())) {
            if (!event.getMember().getEffectiveName().equalsIgnoreCase(event.getJDA().getSelfUser().getName()) /*&& ServerConfigHandler.isAREnabled(event)*/ ) {
                if (!HandlerServerConfig.isUserCBBlacklisted(event, event.getAuthor().getId())) {
                    HandlerMessages.handleMessage(event);
                }
            }
        }
    }

    @Override
    public void onReady(@NotNull ReadyEvent event){
        UtilLogger.STATUS.log(String.format("Logged in as: %s", event.getJDA().getSelfUser().getName()));
    }

}
