package me.crimp.claudius.utils;

import com.mojang.realmsclient.gui.ChatFormatting;

import java.util.Random;
import java.util.regex.Pattern;

public class TextUtil {
    public static final String BLACK = String.valueOf(ChatFormatting.BLACK);
    public static final String DARK_BLUE = String.valueOf(ChatFormatting.DARK_BLUE);
    public static final String DARK_GREEN = String.valueOf(ChatFormatting.DARK_GREEN);
    public static final String DARK_AQUA = String.valueOf(ChatFormatting.DARK_AQUA);
    public static final String DARK_RED = String.valueOf(ChatFormatting.DARK_RED);
    public static final String DARK_PURPLE = String.valueOf(ChatFormatting.DARK_PURPLE);
    public static final String GOLD = String.valueOf(ChatFormatting.GOLD);
    public static final String GRAY = String.valueOf(ChatFormatting.GRAY);
    public static final String DARK_GRAY = String.valueOf(ChatFormatting.DARK_GRAY);
    public static final String BLUE = String.valueOf(ChatFormatting.BLUE);
    public static final String GREEN = String.valueOf(ChatFormatting.GREEN);
    public static final String AQUA = String.valueOf(ChatFormatting.AQUA);
    public static final String RED = String.valueOf(ChatFormatting.RED);
    public static final String LIGHT_PURPLE = String.valueOf(ChatFormatting.LIGHT_PURPLE);
    public static final String YELLOW = String.valueOf(ChatFormatting.YELLOW);
    public static final String WHITE = String.valueOf(ChatFormatting.WHITE);
    public static final String OBFUSCATED = String.valueOf(ChatFormatting.OBFUSCATED);
    public static final String BOLD = String.valueOf(ChatFormatting.BOLD);
    public static final String STRIKE = String.valueOf(ChatFormatting.STRIKETHROUGH);
    public static final String UNDERLINE = String.valueOf(ChatFormatting.UNDERLINE);
    public static final String ITALIC = String.valueOf(ChatFormatting.ITALIC);
    public static final String RESET = String.valueOf(ChatFormatting.RESET);
    private static final Random rand = new Random();

    public static String stripColor(String input) {
        if (input != null) {
            return Pattern.compile("(?i)\u00a7[0-9A-FK-OR]").matcher(input).replaceAll("");
        }
        return "";
    }

    public static String coloredString(String string, Color color) {
        String coloredString = string;
        switch (color) {
            case AQUA: {
                coloredString = ChatFormatting.AQUA + coloredString + ChatFormatting.RESET;
                break;
            }
            case WHITE: {
                coloredString = ChatFormatting.WHITE + coloredString + ChatFormatting.RESET;
                break;
            }
            case BLACK: {
                coloredString = ChatFormatting.BLACK + coloredString + ChatFormatting.RESET;
                break;
            }
            case DARK_BLUE: {
                coloredString = ChatFormatting.DARK_BLUE + coloredString + ChatFormatting.RESET;
                break;
            }
            case DARK_GREEN: {
                coloredString = ChatFormatting.DARK_GREEN + coloredString + ChatFormatting.RESET;
                break;
            }
            case DARK_AQUA: {
                coloredString = ChatFormatting.DARK_AQUA + coloredString + ChatFormatting.RESET;
                break;
            }
            case DARK_RED: {
                coloredString = ChatFormatting.DARK_RED + coloredString + ChatFormatting.RESET;
                break;
            }
            case DARK_PURPLE: {
                coloredString = ChatFormatting.DARK_PURPLE + coloredString + ChatFormatting.RESET;
                break;
            }
            case GOLD: {
                coloredString = ChatFormatting.GOLD + coloredString + ChatFormatting.RESET;
                break;
            }
            case DARK_GRAY: {
                coloredString = ChatFormatting.DARK_GRAY + coloredString + ChatFormatting.RESET;
                break;
            }
            case GRAY: {
                coloredString = ChatFormatting.GRAY + coloredString + ChatFormatting.RESET;
                break;
            }
            case BLUE: {
                coloredString = ChatFormatting.BLUE + coloredString + ChatFormatting.RESET;
                break;
            }
            case RED: {
                coloredString = ChatFormatting.RED + coloredString + ChatFormatting.RESET;
                break;
            }
            case GREEN: {
                coloredString = ChatFormatting.GREEN + coloredString + ChatFormatting.RESET;
                break;
            }
            case LIGHT_PURPLE: {
                coloredString = ChatFormatting.LIGHT_PURPLE + coloredString + ChatFormatting.RESET;
                break;
            }
            case YELLOW: {
                coloredString = ChatFormatting.YELLOW + coloredString + ChatFormatting.RESET;
            }
        }
        return coloredString;
    }

    public static String cropMaxLengthMessage(String s, int i) {
        String output = "";
        if (s.length() >= 256 - i) {
            output = s.substring(0, 256 - i);
        }
        return output;
    }

    public enum Color {
        NONE,
        WHITE,
        BLACK,
        DARK_BLUE,
        DARK_GREEN,
        DARK_AQUA,
        DARK_RED,
        DARK_PURPLE,
        GOLD,
        GRAY,
        DARK_GRAY,
        BLUE,
        GREEN,
        AQUA,
        RED,
        LIGHT_PURPLE,
        YELLOW

    }
}

