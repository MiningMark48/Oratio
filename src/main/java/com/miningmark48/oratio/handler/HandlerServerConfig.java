package com.miningmark48.oratio.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.stream.JsonWriter;
import com.miningmark48.oratio.util.UtilLogger;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class HandlerServerConfig {

    //Setup Config
    public static void setupConfig() {
        try {
            File file = new File(HandlerJsonConfig.fileName);

            if (!file.exists()) {

                Writer writer = new OutputStreamWriter(new FileOutputStream(HandlerJsonConfig.fileName), "UTF-8");
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                JsonWriter jw = gson.newJsonWriter(writer);

                jw.beginObject();

                jw.name("servers");
                jw.beginObject();

                jw.endObject();

                jw.endObject();

                writer.close();

                UtilLogger.INFO.log("Server config file created");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Chatbot Blacklist
    public static void toggleCBBlacklistUser(MessageReceivedEvent event, String value) {
        HandlerJsonConfig.toggleOnArray(event, "cb_blacklist", value);
    }

    public static boolean isUserCBBlacklisted(MessageReceivedEvent event, String value) {
        return HandlerJsonConfig.isInArray(event, "cb_blacklist", value);
    }

    public static JsonArray getBlacklistedCBUsers(MessageReceivedEvent event) {
        return HandlerJsonConfig.getArray(event, "cb_blacklist");
    }

//    //Chatbot
//    public static void toggleCB(MessageReceivedEvent event) {
//        JsonConfigHandler.setPropertyBoolean(event, "ar_enabled", !isAREnabled(event));
//    }
//
//    public static boolean isAREnabled(MessageReceivedEvent event) {
//        return JsonConfigHandler.isPropertyBoolean(event, "ar_enabled");
//    }

}
