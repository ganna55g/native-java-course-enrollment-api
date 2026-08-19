package com.coursemanagement.http.json;

import java.math.BigDecimal;

public class JsonParser {

    public static boolean hasField(String json, String fieldName) {

        return json.contains("\"" + fieldName + "\"");
    }

    public static String readString(String json, String fieldName) {

        String key = "\"" + fieldName + "\"";
        int keyIndex = json.indexOf(key);

        if (keyIndex == -1) {
            throw new IllegalArgumentException(
                    "Missing field: " + fieldName
            );
        }

        int start = json.indexOf("\"", keyIndex + key.length());

        start = json.indexOf("\"", start + 1);

        int end = json.indexOf("\"", start + 1);

        if (end == -1) {
            throw new IllegalArgumentException(
                    "Invalid JSON value for: " + fieldName
            );
        }

        return json.substring(start + 1, end);
    }

    public static int readInteger(String json, String fieldName) {

        String value = readValue(json, fieldName);

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Invalid integer for: " + fieldName
            );
        }
    }

    public static BigDecimal readDecimal(String json, String fieldName) {

        String value = readValue(json, fieldName);

        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Invalid decimal for: " + fieldName
            );
        }
    }

    public static <T extends Enum<T>> T readEnum(
            String json,
            String fieldName,
            Class<T> enumClass) {

        String value = readString(json, fieldName);

        try {
            return Enum.valueOf(enumClass, value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Invalid enum for: " + fieldName
            );
        }
    }

    private static String readValue(String json, String fieldName) {

        String key = "\"" + fieldName + "\"";
        int keyIndex = json.indexOf(key);

        if (keyIndex == -1) {
            throw new IllegalArgumentException(
                    "Missing field: " + fieldName
            );
        }

        int colonIndex = json.indexOf(":", keyIndex);

        int start = colonIndex + 1;

        while (Character.isWhitespace(json.charAt(start))) {
            start++;
        }

        int end = start;

        while (end < json.length()
                && json.charAt(end) != ','
                && json.charAt(end) != '}') {

            end++;
        }

        return json.substring(start, end).trim();
    }
}