/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.util;

public class StringConversions {
    public static Object castNumber(String newValueText, Object currentValue) {
        if (newValueText.contains(".")) {
            if (newValueText.toLowerCase().contains("f")) {
                return Float.valueOf(Float.parseFloat(newValueText));
            }
            return Double.parseDouble(newValueText);
        }
        if (StringConversions.isNumeric(newValueText)) {
            return Integer.parseInt(newValueText);
        }
        return newValueText;
    }

    public static boolean isNumeric(String text) {
        try {
            Integer.parseInt(text);
            return true;
        }
        catch (NumberFormatException nfe) {
            return false;
        }
    }
}

