package com.example.collegepal.util;

public class TimeFormat {
    public static String TimeSeparator(String time) {
        int position = time.length() / 2;
        char newChar = ':';

        StringBuilder stringBuilder = new StringBuilder(time);
        stringBuilder.insert(position, newChar);

        return stringBuilder.toString();
    }
}
