package com.miningmark48.tidalchatbot.handler;

import com.google.gson.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.*;

@SuppressWarnings("Duplicates")
public class HandlerJsonConfig {

    public static String fileName = "server_configs.json";
    private static BufferedWriter bufferedWriter = null;

    //Json Handling
    public static JsonObject getJson(MessageReceivedEvent event) {
        String guildID = event.getGuild().getId();

        File file = new File(fileName);

        if (!file.exists()) {
            HandlerServerConfig.setupConfig();
            return null;
        }

        try {
            JsonObject jsonObj = parseJSONFile(fileName);
            assert jsonObj != null;
            JsonObject servs = jsonObj.getAsJsonObject("servers");
            if (servs.getAsJsonObject(guildID) == null) {
                return null;
            }
            return servs.getAsJsonObject(guildID);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    static void setupGuild(JsonObject serversObject, String id, JsonObject jsonObject, String name) {
        jsonObject.addProperty("__comment_GuildName", name);
        serversObject.add(id, jsonObject);
    }

    static void toggleOnArray(MessageReceivedEvent event, String arrayName, String value) {
        String guildID = event.getGuild().getId();

        File file = new File(fileName);

        if (!file.exists()) {
            HandlerServerConfig.setupConfig();
        }

        try {
            JsonObject jsonObj = parseJSONFile(fileName);

            Writer writer = new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8");
            bufferedWriter = new BufferedWriter(writer);

            assert jsonObj != null;
            JsonObject servs = jsonObj.getAsJsonObject("servers");
            if (servs.getAsJsonObject(guildID) == null) {
                JsonObject tempObj = new JsonObject();
                tempObj.add(arrayName, new JsonArray());
//                servs.add(guildID, tempObj);
                setupGuild(servs, guildID, tempObj, event.getGuild().getName());
            }

            JsonObject newObj = servs.getAsJsonObject(guildID);
            if (newObj.getAsJsonArray(arrayName) == null) {
                JsonArray tempArray = new JsonArray();
                newObj.add(arrayName, tempArray);
            }

            JsonArray newJson = newObj.getAsJsonArray(arrayName);
            if (!newJson.contains(new JsonPrimitive(value))) {
                newJson.add(value);
            } else {
                newJson.remove(new JsonPrimitive(value));
            }

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonFinal = gson.toJson(jsonObj);
            bufferedWriter.write(jsonFinal);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedWriter != null){
                    bufferedWriter.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    static void setPropertyBoolean(MessageReceivedEvent event, String key, boolean value) {
        String guildID = event.getGuild().getId();

        File file = new File(fileName);

        if (!file.exists()) {
            HandlerServerConfig.setupConfig();
        }

        try {
            JsonObject jsonObj = parseJSONFile(fileName);

            Writer writer = new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8");
            bufferedWriter = new BufferedWriter(writer);

            assert jsonObj != null;
            JsonObject servs = jsonObj.getAsJsonObject("servers");
            if (servs.getAsJsonObject(guildID) == null) {
                JsonObject tempObj = new JsonObject();
                tempObj.add(key, new JsonPrimitive(value));
//                servs.add(guildID, tempObj);
                setupGuild(servs, guildID, tempObj, event.getGuild().getName());
            }

            JsonObject newObj = servs.getAsJsonObject(guildID);
            newObj.addProperty(key, value);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonFinal = gson.toJson(jsonObj);
            bufferedWriter.write(jsonFinal);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedWriter != null){
                    bufferedWriter.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    static JsonArray getArray(MessageReceivedEvent event, String arrayName) {
        String guildID = event.getGuild().getId();

        File file = new File(fileName);

        if (!file.exists()) {
            HandlerServerConfig.setupConfig();
            return null;
        }

        try {
            JsonObject jsonObj = parseJSONFile(fileName);
            assert jsonObj != null;
            JsonObject servs = jsonObj.getAsJsonObject("servers");
            if (servs.getAsJsonObject(guildID) == null) {
                return null;
            }
            JsonObject newObj = servs.getAsJsonObject(guildID);
            if (newObj.getAsJsonArray(arrayName) == null) {
                return null;
            }
            return newObj.getAsJsonArray(arrayName);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    static boolean isInArray(MessageReceivedEvent event, String arrayName, String value) {
        String guildID = event.getGuild().getId();

        File file = new File(fileName);

        if (!file.exists()) {
            HandlerServerConfig.setupConfig();
            return false;
        }

        try {
            JsonObject jsonObj = parseJSONFile(fileName);
            assert jsonObj != null;
            JsonObject servs = jsonObj.getAsJsonObject("servers");
            if (servs.getAsJsonObject(guildID) == null) {
                return false;
            }
            JsonObject newObj = servs.getAsJsonObject(guildID);
            JsonArray newJson = newObj.getAsJsonArray(arrayName);

            if (newJson == null) {
                return false;
            }

            if (newJson.contains(new JsonPrimitive(value))) {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;

    }

    static boolean isPropertyBoolean(MessageReceivedEvent event, String key) {
        String guildID = event.getGuild().getId();
        return isPropertyBoolean(guildID, key);
    }

    static boolean isPropertyBoolean(String guildID, String key) {
        File file = new File(fileName);

        if (!file.exists()) {
            HandlerServerConfig.setupConfig();
            return false;
        }

        try {
            JsonObject jsonObj = parseJSONFile(fileName);
            assert jsonObj != null;
            JsonObject servs = jsonObj.getAsJsonObject("servers");
            if (servs.getAsJsonObject(guildID) == null) return false;
            JsonObject newObj = servs.getAsJsonObject(guildID);
            if (newObj.get(key).isJsonNull()) return false;
            return newObj.get(key).getAsBoolean();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;

    }

    private static JsonObject parseJSONFile(String fileName) {
        try {

            JsonParser jp = new JsonParser();
            InputStream inputStream = new FileInputStream(fileName);
            JsonElement root = jp.parse(new InputStreamReader(inputStream));

            return root.getAsJsonObject();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
