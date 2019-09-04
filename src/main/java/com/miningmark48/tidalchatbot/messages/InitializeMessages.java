package com.miningmark48.tidalchatbot.messages;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.miningmark48.tidalchatbot.reference.Actions;
import com.miningmark48.tidalchatbot.reference.JsonNames;
import com.miningmark48.tidalchatbot.util.UtilLogger;
import com.miningmark48.tidalchatbot.util.UtilLogger.LogType;
import com.miningmark48.tidalchatbot.util.UtilMath;
import com.miningmark48.tidalchatbot.util.UtilString;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.script.ScriptException;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class InitializeMessages {

    private static String baseDirectory = "messages";

    private static ArrayList<ArrayList<String>> triggers = new ArrayList<>();
    private static ArrayList<ArrayList<String>> responses = new ArrayList<>();
    private static ArrayList<String> types = new ArrayList<>();

    public static void init() {
        triggers.clear();
        responses.clear();
        types.clear();

        File folder = new File(baseDirectory);
        ArrayList<File> filesList = new ArrayList<>(Arrays.asList(Objects.requireNonNull(folder.listFiles())));

        filesList.forEach(file -> {
            if (!file.isFile() || !file.exists()) return;
            try {

                JsonParser jp = new JsonParser();
                InputStream inputStream = new FileInputStream(file);
                JsonElement root = jp.parse(new InputStreamReader(inputStream));

                JsonArray messages = root.getAsJsonObject().getAsJsonArray(JsonNames.MESSAGES.getName());

                messages.forEach(q -> {
                    JsonObject obj = q.getAsJsonObject();
                    ArrayList<String> jsonTriggers = new ArrayList<>();
                    obj.getAsJsonArray(JsonNames.TRIGGERS.getName()).forEach(trig -> jsonTriggers.add(trig.getAsString().toLowerCase()));

                    ArrayList<String> jsonResponses = new ArrayList<>();
                    obj.getAsJsonArray(JsonNames.RESPONSES.getName()).forEach(resp -> jsonResponses.add(resp.getAsString()));

                    types.add(obj.get(JsonNames.TYPE.getName()) != null ? obj.get(JsonNames.TYPE.getName()).getAsString().toLowerCase() : JsonNames.OP_AND.getName());

                    triggers.add(jsonTriggers);
                    responses.add(jsonResponses);
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

        UtilLogger.log(LogType.INFO, String.format("%s messages initialized.", triggers.size()));

    }

    public static void handleMessage(MessageReceivedEvent event) {
//        UtilLogger.log(LogType.DEBUG, triggers.toString());

        String msg = event.getMessage().getContentRaw().toLowerCase();

        for (int i = 0; i < triggers.size(); i++) {
            ArrayList<String> trigArray = triggers.get(i);
            int andIndex = 0;
            for (int j = 0; j < trigArray.size(); j++) {
                if (msg.contains(trigArray.get(j))) {
                    String type = types.get(i).toLowerCase();
                    if (type.equalsIgnoreCase(JsonNames.OP_AND.getName())) {
                        andIndex++;
//                        UtilLogger.log(LogType.DEBUG, andIndex + " " + trigArray.size());
                        if (andIndex == trigArray.size()) {
                            sendResponse(event, i, j);
                            return;
                        }
                    } else if (type.equalsIgnoreCase(JsonNames.OP_OR.getName())) {
                        sendResponse(event, i, j);
                        return;
                    }
                }
            }
        }

    }

    private static void sendResponse(MessageReceivedEvent event, int index, int index2) {
        ArrayList<String> respArray = responses.get(index);
        Random rand = new Random();
        String response = responses.get(index).get(rand.nextInt(respArray.size()));
        response = handleActions(event, response);
        event.getTextChannel().sendMessage(UtilString.capitalize(response)).queue();
    }

    private static String handleActions(MessageReceivedEvent event, String response) {
        String msg = event.getMessage().getContentRaw();
        if (response.contains(String.format("{%s}", Actions.MATH.getName()))) {
            try {
                return mathResponse(msg, response);
            } catch (ScriptException e) {
                event.getTextChannel().sendMessage("Error! Invalid math.").queue();
                return "";
            }
        }
        return response;
    }

    private static String mathResponse(String msg, String response) throws ScriptException {
        String math = msg.replaceAll("([A-Za-z?$#@!{},;:'\"`~|])", ""); //Removes characters that don't work
        return response.replace(String.format("{%s}", Actions.MATH.getName()), UtilMath.doMath(math));
    }

}
