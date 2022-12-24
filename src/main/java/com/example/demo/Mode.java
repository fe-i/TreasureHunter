package com.example.demo;

public class Mode {
    public final static String EASY_MODE = "easy";
    public final static String NORMAL_MODE = "normal";
    public final static String HARD_MODE = "hard";
    public final static String DEV_MODE = "developer";

    private static String currentMode = NORMAL_MODE;

    public static void setCurrentMode(String currentMode) {
        Mode.currentMode = currentMode;
    }

    public static String getCurrentMode() {
        return currentMode;
    }

    public static int getStartingGold() {
        int startingGold = 0;
        switch (currentMode) {
            case EASY_MODE -> startingGold = 50;
            case NORMAL_MODE -> startingGold = 20;
            case HARD_MODE -> startingGold = 10;
            case DEV_MODE -> startingGold = 100;
            default -> startingGold = 0;
        }
        return startingGold;
    }

    public static double getMarkdown() {
        double markdown;
        switch (currentMode) {
            case EASY_MODE -> markdown = 0.2;
            case NORMAL_MODE -> markdown = 0.4;
            case HARD_MODE -> markdown = 0.6;
            default -> markdown = 0.0;
        }
        return markdown;
    }

    public static double getToughness() {
        double toughness;
        switch (currentMode) {
            case EASY_MODE -> toughness = 0.2;
            case NORMAL_MODE -> toughness = 0.5;
            case HARD_MODE -> toughness = 0.75;
            default -> toughness = 0.0;
        }
        return toughness;
    }

    public static boolean isDev() {
        return currentMode.equals(DEV_MODE);
    }

    public static boolean isHard() {
        return currentMode.equals(HARD_MODE);
    }

    public static boolean isNormal() {
        return currentMode.equals(NORMAL_MODE);
    }

    public static boolean isEasy() {
        return currentMode.equals(EASY_MODE);
    }
}
