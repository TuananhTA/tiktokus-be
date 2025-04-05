package com.tiktokus.tiktokus.util;

public class StringFormatter {
    public static String formatString(String str) {
        str = str.replaceAll("\\s+", " ").trim();
        if (str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}

