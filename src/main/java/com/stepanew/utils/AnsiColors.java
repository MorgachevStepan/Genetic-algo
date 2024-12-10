package com.stepanew.utils;

public class AnsiColors {

    public static final String RESET = "\u001B[0m";

    public static final String RED = "\u001B[31m";

    public static final String GREEN = "\u001B[32m";

    public static String color(String message, String color) {
        return color + message + RESET;
    }

}
