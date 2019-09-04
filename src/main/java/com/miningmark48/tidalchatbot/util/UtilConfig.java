package com.miningmark48.tidalchatbot.util;

import com.google.gson.*;
import com.google.gson.stream.JsonWriter;
import com.miningmark48.tidalchatbot.reference.Reference;
import com.miningmark48.tidalchatbot.util.UtilLogger.LogType;

import java.io.*;

public class UtilConfig {

    public static boolean getConfigs() {
        UtilLogger.log(LogType.STATUS, "Getting configs...");

        try {
            String configFile = "config.json";
            File file = new File(configFile);

            if (!file.exists()) {
                Writer writer = new OutputStreamWriter(new FileOutputStream(configFile), "UTF-8");
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                JsonWriter jw = gson.newJsonWriter(writer);

                jw.beginObject();

                jw.name("bot");
                jw.beginObject();
                jw.name("botname").value("Bot Name");
                jw.name("token").value("bot.token");
                jw.name("key").value("bot.key");
                jw.name("client id").value("bot.id");
                jw.endObject();

                jw.endObject();

                writer.close();

                UtilLogger.log(LogType.STATUS, "Config file was created and must be filled in, stopping bot.");
                return false;
            } else {

                try {

                    JsonParser jp = new JsonParser();
                    InputStream inputStream = new FileInputStream(configFile);
                    JsonElement root = jp.parse(new InputStreamReader(inputStream));
                    JsonObject jsonObject = root.getAsJsonObject();

                    if (jsonObject != null) {
                        JsonObject jsonObjectBot = jsonObject.getAsJsonObject("bot");

                        Reference.botName = jsonObjectBot.get("botname").getAsString();
                        Reference.botToken = jsonObjectBot.get("token").getAsString();
                        Reference.botCommandKey = jsonObjectBot.get("key").getAsString();
                        Reference.botClientID = jsonObjectBot.get("client id").getAsString();

                    } else {
                        throw new NullPointerException();
                    }

                } catch (NullPointerException e) {
                    UtilLogger.log(UtilLogger.LogType.FATAL, "Configs were unable to be loaded, stopping bot.");
                    e.printStackTrace();
                    return false;
                }

                UtilLogger.log(UtilLogger.LogType.STATUS, "Configs were loaded, continuing.");
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
