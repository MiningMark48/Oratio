package com.miningmark48.tidalchatbot.util;

public class UtilString {

    public static String capitalize(String str) {
        return str.length() > 0 ? str.substring(0, 1).toUpperCase() + str.substring(1) : str;
    }

}
