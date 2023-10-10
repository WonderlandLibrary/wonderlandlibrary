/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package com.wallhacks.losebypass.utils;

import net.minecraft.block.Block;
import net.minecraft.client.multiplayer.ServerData;
import org.lwjgl.input.Keyboard;

public class StringUtil {
    public static String getNameForKey(int bind) {
        if (bind > 0) {
            return Keyboard.getKeyName((int)bind);
        }
        if (bind > -2) return "None";
        switch (bind) {
            case -2: {
                return "RClick";
            }
            case -3: {
                return "LClick";
            }
            case -4: {
                return "MClick";
            }
        }
        return "Mouse" + (-bind - 2);
    }

    public static String BlockToText(Block b) {
        return b.getLocalizedName();
    }

    public static String fmt(double d) {
        if (d == (double)((long)d)) {
            return String.format("%d", (long)d);
        }
        if (d * 10.0 != (double)((long)(d * 10.0))) return String.format("%.2f", d);
        return String.format("%.1f", d);
    }

    public static String fmt(double d, int decimals) {
        return String.format("%." + decimals + "f", d);
    }

    public static long getIntSeed(String seed) {
        try {
            return Long.parseLong(seed);
        }
        catch (Exception exception) {
            int i = 0;
            int index = 1;
            byte[] byArray = seed.getBytes();
            int n = byArray.length;
            int n2 = 0;
            while (n2 < n) {
                Byte c = byArray[n2];
                i += c * index++;
                ++n2;
            }
            return i;
        }
    }

    public static String getServerName(ServerData data) {
        if (data == null) {
            return "Singleplayer";
        }
        String string = data.serverIP;
        return string;
    }

    public static String millisecondToElapsedTime(int milli) {
        String string;
        int i = milli / 1000;
        int j = i / 60;
        if ((i %= 60) < 10) {
            string = j + ":0" + i;
            return string;
        }
        string = j + ":" + i;
        return string;
    }

    public static String SpeedConvertFromShort(double currentSpeed, String shortForm) {
        if (shortForm.equalsIgnoreCase("mps")) {
            return StringUtil.fmt(currentSpeed *= 20.0, 1);
        }
        if (shortForm.equalsIgnoreCase("kmph")) {
            currentSpeed = currentSpeed * 20.0 / 1000.0 * 60.0 * 60.0;
            return StringUtil.fmt(currentSpeed, 1);
        }
        if (!shortForm.equalsIgnoreCase("mpmin")) return StringUtil.fmt(currentSpeed, 1);
        currentSpeed = currentSpeed * 20.0 * 60.0;
        return StringUtil.fmt(currentSpeed, 1);
    }

    public static String SpeedUnitShortToLong(String shortForm) {
        String end = "m/tick";
        if (shortForm.equalsIgnoreCase("mps")) {
            return "m/s";
        }
        if (shortForm.equalsIgnoreCase("kmph")) {
            return "km/h";
        }
        if (!shortForm.equalsIgnoreCase("mpmin")) return end;
        return "m/min";
    }
}

