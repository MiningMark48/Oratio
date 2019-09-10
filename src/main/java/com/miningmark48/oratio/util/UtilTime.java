package com.miningmark48.oratio.util;

import java.util.Calendar;
import java.util.Date;

public class UtilTime {

    public static String getTimeHMS() {
        Date date = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return String.format("%s:%s:%s", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
    }

    public static String getTimeHMAPTZ() {
        Date date = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return String.format("%s:%s %s %s", calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), calendar.get(Calendar.AM_PM) == Calendar.PM ? "PM" : "AM", calendar.getTimeZone().getDisplayName().replaceAll("\\B.|\\P{L}", ""));
    }

}
