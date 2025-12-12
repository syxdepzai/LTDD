package com.example.bt9.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {
    
    private static final SimpleDateFormat ISO_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
    
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat(
            "HH:mm", Locale.getDefault());
    
    private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat(
            "dd/MM/yyyy HH:mm", Locale.getDefault());

    static {
        ISO_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public static String formatTimestamp(String isoTimestamp) {
        try {
            Date date = ISO_FORMAT.parse(isoTimestamp);
            if (date != null) {
                return TIME_FORMAT.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String formatFullTimestamp(String isoTimestamp) {
        try {
            Date date = ISO_FORMAT.parse(isoTimestamp);
            if (date != null) {
                return DATE_TIME_FORMAT.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getCurrentISOTimestamp() {
        return ISO_FORMAT.format(new Date());
    }
}

