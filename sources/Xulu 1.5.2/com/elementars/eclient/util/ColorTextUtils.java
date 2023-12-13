// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.util;

import java.util.ArrayList;

public class ColorTextUtils
{
    public static ArrayList<String> colors;
    
    public static void initColors() {
        (ColorTextUtils.colors = new ArrayList<String>()).add("White");
        ColorTextUtils.colors.add("Black");
        ColorTextUtils.colors.add("Blue");
        ColorTextUtils.colors.add("Green");
        ColorTextUtils.colors.add("Cyan");
        ColorTextUtils.colors.add("Red");
        ColorTextUtils.colors.add("Purple");
        ColorTextUtils.colors.add("Gold");
        ColorTextUtils.colors.add("LightGray");
        ColorTextUtils.colors.add("Gray");
        ColorTextUtils.colors.add("Lavender");
        ColorTextUtils.colors.add("LightGreen");
        ColorTextUtils.colors.add("LightBlue");
        ColorTextUtils.colors.add("LightRed");
        ColorTextUtils.colors.add("Pink");
        ColorTextUtils.colors.add("Yellow");
    }
    
    public static String getColor(final String value) {
        String prefix;
        if (value.equalsIgnoreCase("White")) {
            prefix = "&f";
        }
        else if (value.equalsIgnoreCase("Red")) {
            prefix = "&4";
        }
        else if (value.equalsIgnoreCase("Blue")) {
            prefix = "&1";
        }
        else if (value.equalsIgnoreCase("Cyan")) {
            prefix = "&3";
        }
        else if (value.equalsIgnoreCase("Pink")) {
            prefix = "&d";
        }
        else if (value.equalsIgnoreCase("Black")) {
            prefix = "&0";
        }
        else if (value.equalsIgnoreCase("Green")) {
            prefix = "&2";
        }
        else if (value.equalsIgnoreCase("Purple")) {
            prefix = "&5";
        }
        else if (value.equalsIgnoreCase("Yellow")) {
            prefix = "&e";
        }
        else if (value.equalsIgnoreCase("LightRed")) {
            prefix = "&c";
        }
        else if (value.equalsIgnoreCase("LightBlue")) {
            prefix = "&b";
        }
        else if (value.equalsIgnoreCase("LightGreen")) {
            prefix = "&a";
        }
        else if (value.equalsIgnoreCase("Gold")) {
            prefix = "&6";
        }
        else if (value.equalsIgnoreCase("Gray")) {
            prefix = "&8";
        }
        else if (value.equalsIgnoreCase("Lavender")) {
            prefix = "&9";
        }
        else if (value.equalsIgnoreCase("LightGray")) {
            prefix = "&7";
        }
        else {
            prefix = "&r";
        }
        return prefix;
    }
}
