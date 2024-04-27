/*
 * Decompiled with CFR 0.152.
 */
package liying.utils;

import java.awt.Color;

public class SuperLib {
    public static int reAlpha(int n, float f) {
        Color color = new Color(n);
        float f2 = 0.003921569f * (float)color.getRed();
        float f3 = 0.003921569f * (float)color.getGreen();
        float f4 = 0.003921569f * (float)color.getBlue();
        return new Color(f2, f3, f4, f).getRGB();
    }

    public static String removeColorCode(String string) {
        String string2 = string;
        if (string.contains("\u00a7")) {
            for (int i = 0; i < string2.length(); ++i) {
                if (!Character.toString(string2.charAt(i)).equals("\u00a7")) continue;
                try {
                    String string3 = string2.substring(0, i);
                    String string4 = string2.substring(Math.min(i + 2, string2.length()));
                    string2 = string3 + string4;
                    continue;
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
        }
        return string2;
    }
}

