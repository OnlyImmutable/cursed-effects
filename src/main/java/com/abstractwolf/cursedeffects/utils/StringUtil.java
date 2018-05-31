package com.abstractwolf.cursedeffects.utils;

public class StringUtil {

    public static String setUppercaseEachStart(String input) {
        StringBuilder builder = new StringBuilder();

        if (input.contains(" ")) {
            for (String part : input.split(" ")) {
                builder.append(part.substring(0, 1).toUpperCase() + part.substring(1, part.length()).toLowerCase()).append(" ");
            }
        } else {
            builder.append(input.substring(0, 1).toUpperCase() + input.substring(1, input.length()).toLowerCase()).append(" ");
        }

        return builder.toString().trim();
    }
}
