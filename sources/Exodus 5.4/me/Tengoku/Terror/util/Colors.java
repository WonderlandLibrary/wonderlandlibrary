/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.util;

import java.awt.Color;
import net.minecraft.client.Minecraft;

public class Colors {
    private static int[] colorCode = new int[32];

    public static Color getFromColorCode(char c) {
        int n = "0123456789abcdefklmnor".indexOf(c);
        int n2 = colorCode[n];
        float f = (float)(n2 >> 16) / 255.0f;
        float f2 = (float)(n2 >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(n2 & 0xFF) / 255.0f;
        return new Color(f, f2, f3);
    }

    public static int getColor(Color color) {
        return Colors.getColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public static int getColor(int n, int n2, int n3) {
        return Colors.getColor(n, n2, n3, 255);
    }

    static {
        int n = 0;
        while (n < 32) {
            int n2 = (n >> 3 & 1) * 85;
            int n3 = (n >> 2 & 1) * 170 + n2;
            int n4 = (n >> 1 & 1) * 170 + n2;
            int n5 = (n >> 0 & 1) * 170 + n2;
            if (n == 6) {
                n3 += 85;
            }
            Minecraft.getMinecraft();
            if (Minecraft.gameSettings.anaglyph) {
                int n6 = (n3 * 30 + n4 * 59 + n5 * 11) / 100;
                int n7 = (n3 * 30 + n4 * 70) / 100;
                int n8 = (n3 * 30 + n5 * 70) / 100;
                n3 = n6;
                n4 = n7;
                n5 = n8;
            }
            if (n >= 16) {
                n3 /= 4;
                n4 /= 4;
                n5 /= 4;
            }
            Colors.colorCode[n] = (n3 & 0xFF) << 16 | (n4 & 0xFF) << 8 | n5 & 0xFF;
            ++n;
        }
    }

    public static int getColor(int n, int n2, int n3, int n4) {
        int n5 = n4 << 24;
        n5 |= n << 16;
        n5 |= n2 << 8;
        return n5 |= n3;
    }

    public static int getColor(int n) {
        return Colors.getColor(n, n, n, 255);
    }

    public static int getColor(int n, int n2) {
        return Colors.getColor(n, n, n, n2);
    }
}

