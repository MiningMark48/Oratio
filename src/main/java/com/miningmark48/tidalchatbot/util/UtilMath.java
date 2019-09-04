package com.miningmark48.tidalchatbot.util;

import com.miningmark48.tidalchatbot.util.UtilLogger.LogType;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilMath {

    public static String findMath(String str) {
        Pattern pattern = Pattern.compile("(([0-9]+)([+\\-*/][0-9]+)*)");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    public static String doMath(String equation) throws ScriptException {
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        return engine.eval(equation).toString();
    }

}
