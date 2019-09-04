package com.miningmark48.tidalchatbot;

import com.miningmark48.tidalchatbot.reference.Reference;
import com.miningmark48.tidalchatbot.util.UtilLogger;
import com.miningmark48.tidalchatbot.util.UtilLogger.LogType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
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
            TidalChatbot.handleCommand(TidalChatbot.parser.parse(event.getMessage().getContentRaw(), event));
        }

        event.getJDA().getSelfUser();
        if (event.getMember() != null && !event.getMessage().getAuthor().getId().equalsIgnoreCase(event.getJDA().getSelfUser().getId())) {
            if (!event.getMember().getEffectiveName().equalsIgnoreCase(event.getJDA().getSelfUser().getName()) /*&& ServerConfigHandler.isAREnabled(event)*/ ) {
//                if (!ServerConfigHandler.isUserARBlacklisted(event, event.getAuthor().getId())) {
                    TidalChatbot.handleMessage(event);
//                }
            }
        }

    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event){

        TextChannel tc = event.getGuild().getTextChannels().stream().filter(textChannel -> textChannel.getName().equalsIgnoreCase("welcome")).findFirst().orElse(null);

        if(tc != null) {
            tc.sendMessage("Welcome **" + event.getMember().getEffectiveName() + "** to **" + event.getGuild().getName() + "**!").queue();
        }

    }

    @Override
    public void onReady(@NotNull ReadyEvent event){
        //event.getJDA().getAccountManager().update();
        UtilLogger.log(LogType.STATUS, "Logged in as: " + event.getJDA().getSelfUser().getName());
    }

}
