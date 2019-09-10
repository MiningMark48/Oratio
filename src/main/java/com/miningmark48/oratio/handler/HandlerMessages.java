package com.miningmark48.oratio.handler;

import com.google.gson.*;
import com.miningmark48.oratio.reference.Actions;
import com.miningmark48.oratio.reference.JsonNames;
import com.miningmark48.oratio.reference.Reference;
import com.miningmark48.oratio.util.UtilLogger;
import com.miningmark48.oratio.util.UtilMath;
import com.miningmark48.oratio.util.UtilString;
import com.miningmark48.oratio.util.UtilTime;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.script.ScriptException;
import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HandlerMessages {

    private static final String baseDirectory = Reference.messageDir;

    private static HashMap<Integer, ArrayList<String>> triggers = new HashMap<>();
    private static HashMap<Integer, ArrayList<String>> responses = new HashMap<>();
    private static HashMap<Integer, ArrayList<String>> reactions = new HashMap<>();
    private static HashMap<Integer, String> types = new HashMap<>();

    public static void init() {
        triggers.clear();
        responses.clear();
        types.clear();

        File folder = new File(baseDirectory);
        if (folder.listFiles() == null) {
            UtilLogger.WARN.log("No message files detected!");
            return;
        }
        ArrayList<File> filesList = new ArrayList<>(Arrays.asList(Objects.requireNonNull(folder.listFiles())));

        int index = 0;
        for (File file : filesList) {
            if (file.isFile() && file.exists() && file.getName().endsWith(".json")) {
                try {
                    JsonParser jp = new JsonParser();
                    InputStream inputStream = new FileInputStream(file);
                    JsonElement root = jp.parse(new InputStreamReader(inputStream));

                    JsonArray messages = root.getAsJsonObject().getAsJsonArray(JsonNames.MESSAGES.getName());

                    for (JsonElement q : messages) {
                        index++;

                        JsonObject obj = q.getAsJsonObject();
                        ArrayList<String> jsonTriggers = new ArrayList<>();
                        obj.getAsJsonArray(JsonNames.TRIGGERS.getName()).forEach(trig -> jsonTriggers.add(trig.getAsString().toLowerCase()));

                        ArrayList<String> jsonResponses = new ArrayList<>();
                        if (obj.getAsJsonArray(JsonNames.RESPONSES.getName()) != null)
                            obj.getAsJsonArray(JsonNames.RESPONSES.getName()).forEach(resp -> jsonResponses.add(resp.getAsString()));

                        ArrayList<String> jsonReactions = new ArrayList<>();
                        if (obj.getAsJsonArray(JsonNames.REACTIONS.getName()) != null)
                            obj.getAsJsonArray(JsonNames.REACTIONS.getName()).forEach(reac -> jsonReactions.add(reac.getAsString()));

                        types.put(index, obj.get(JsonNames.TYPE.getName()) != null ? obj.get(JsonNames.TYPE.getName()).getAsString().toLowerCase() : JsonNames.OP_AND.getName());

                        triggers.put(index, jsonTriggers);
                        if (!jsonResponses.isEmpty()) responses.put(index, jsonResponses);
                        if (!jsonReactions.isEmpty()) reactions.put(index, jsonReactions);

                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (JsonParseException e) {
                    UtilLogger.WARN.log(String.format("INVALID JSON - %s\n%s", file.getName(), e.getMessage()));
                }
            }
        }

        UtilLogger.INFO.log(String.format("%s messages initialized.", triggers.size()));
    }

    @SuppressWarnings("Duplicates")
    public static void handleMessage(MessageReceivedEvent event) {
        String msg = event.getMessage().getContentRaw().toLowerCase();
        Random rand = new Random();

        for (Map.Entry<Integer, ArrayList<String>> entry : triggers.entrySet()) {
            ArrayList<String> trigArray = entry.getValue();
            int andIndex = 0;
            for (int j = 0; j < trigArray.size(); j++) {
                if (msg.contains(trigArray.get(j))) {
                    String type = types.get(entry.getKey()).toLowerCase();
                    if (type.equalsIgnoreCase(JsonNames.OP_AND.getName())) {
                        andIndex++;
                        if (andIndex == trigArray.size()) {
                            if (responses.containsKey(entry.getKey())) sendResponse(event, entry.getKey(), rand);
                            if (reactions.containsKey(entry.getKey())) addReaction(event, entry.getKey(), rand);
                            return;
                        }
                    } else if (type.equalsIgnoreCase(JsonNames.OP_OR.getName())) {
                        if (responses.containsKey(entry.getKey())) sendResponse(event, entry.getKey(), rand);
                        if (reactions.containsKey(entry.getKey())) addReaction(event, entry.getKey(), rand);
                        return;
                    }
                }
            }
        }
    }

    private static void sendResponse(MessageReceivedEvent event, int index, Random rand) {
        ArrayList<String> respArray = responses.get(index);
        String response = responses.get(index).get(rand.nextInt(respArray.size()));
        response = handleActions(event, response);
        event.getTextChannel().sendMessage(UtilString.capitalize(response)).queue();
    }

    private static void addReaction(MessageReceivedEvent event, int index, Random rand) {
        ArrayList<String> reacArray = reactions.get(index);
        String reaction = reactions.get(index).get(rand.nextInt(reacArray.size()));
        event.getMessage().addReaction(reaction).queue();
    }

    private static String handleActions(MessageReceivedEvent event, String response) {
        String msg = event.getMessage().getContentRaw();
        Pattern pattern = Pattern.compile("\\{(.*?)}");
        Matcher matcher = pattern.matcher(response);

        ArrayList<String> matches = new ArrayList<>();

        while (matcher.find()) {
            matches.add(matcher.group(1));
        }

        String replacement = response;
        Random rand = new Random();
        for (String match : matches) {
            if (match.contains(Actions.MATH.getName())) {
                replacement = mathResponse(msg, replacement);
            }
            replacement = coinResponse(msg, replacement, rand);
            replacement = randNumResponse(msg, replacement, rand);
            replacement = randListResponse(msg, replacement, rand);
            replacement = randResponse(msg, replacement, rand);
            replacement = timeResponse(msg, replacement);
            replacement = keyResponse(msg, replacement);
        }
        return replacement;
    }

    private static String coinResponse(String msg, String response, Random rand) {
        return response.replace(Actions.COIN.getAction(), rand.nextInt(1) == 0 ? "heads" : "tails");
    }

    private static String randNumResponse(String msg, String response, Random rand) {
        Pattern regex = Pattern.compile("\\{(rand:)(.*[0-9])}");
        Matcher matcher = regex.matcher(response);
        while (matcher.find()){
            if (matcher.group().length() != 0){
                try {
                    response = response.replaceAll(regex.pattern(), String.valueOf(rand.nextInt(Integer.parseInt(matcher.group(2))) + 1));
                } catch (NumberFormatException e){
                    response = "[ERROR: NFE]";
                }
            }
        }
        return response;
    }

    private static String randListResponse(String msg, String response, Random rand) {
        Pattern regex = Pattern.compile("\\{rand:\\?[a-zA-Z0-9<>\\-+_,#'.\\s]+\\?}");
        Matcher matcher = regex.matcher(response);
        while (matcher.find()) {
            Pattern pFile = Pattern.compile("\\?([a-zA-Z0-9\\-+_,#'.\\s]+)\\?");
            Matcher mFile = pFile.matcher(matcher.group());

            while (mFile.find()) {
                try {
                    File file = new File(String.format("%s\\lists\\%s", baseDirectory, mFile.group(1)));
                    if (file.exists()) {
                        List<String> contents = Files.readAllLines(file.toPath());
                        response = response.replaceFirst(regex.pattern(), contents.get(rand.nextInt(contents.size())));
                    }
                } catch (IOException e) {
                    UtilLogger.FATAL.log("ERROR READING FILE\n" + e.getMessage());
                }
            }
        }
        return response;
    }

    private static String randResponse(String msg, String response, Random rand) {
        Pattern regex = Pattern.compile("\\{rand:[a-zA-Z0-9<>\\-+_,#'\\s]+}");
        Matcher matcher = regex.matcher(response);
        while (matcher.find()) {
            Pattern list = Pattern.compile("<([a-zA-Z0-9\\-+_,#'\\s]+)>");
            Matcher mList = list.matcher(matcher.group());

            ArrayList<String> options = new ArrayList<>();
            while (mList.find()) {
                options.add(mList.group(1));
            }

            if (matcher.group().length() != 0){
                response = response.replaceFirst(regex.pattern(), options.get(rand.nextInt(options.size())));
            }
        }
        return response;
    }

    private static String mathResponse(String msg, String response) {
        try {
            String math = msg.replaceAll("([A-Za-z?$#@!{},;:'\"`~|])", ""); //Removes characters that don't work
            return response.replace(Actions.MATH.getAction(), UtilMath.doMath(math));
        } catch (ScriptException e) {
            return "[INVALID MATH]";
        }
    }

    private static String timeResponse(String msg, String response) {
        return response.replace(Actions.TIME.getAction(), UtilTime.getTimeHMAPTZ());
    }

    private static String keyResponse(String msg, String response) {
        return response.replace(Actions.KEY.getAction(), Reference.botCommandKey);
    }

}
