/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.utils.render;

import java.awt.Color;
import java.util.regex.Pattern;
import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI;
import net.ccbluex.liquidbounce.utils.render.ApacheMath;
import org.lwjgl.opengl.GL11;

public class ColorManager {
    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)\u00a7[0-9A-FK-ORX]");

    public static Color getColorAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public static Color rainbow(int delay, float saturation, double speed) {
        double rainbowState = ApacheMath.ceil(((double)System.currentTimeMillis() * speed - (double)delay) / 20.0);
        return Color.getHSBColor((float)((rainbowState %= 360.0) / 360.0), saturation, 1.0f);
    }

    public static int astolfoRainbow(int delay, int offset, int index) {
        double d;
        double rainbowDelay = Math.ceil(System.currentTimeMillis() + (long)(delay * index)) / (double)offset;
        return Color.getHSBColor((double)((float)(d / 360.0)) < 0.5 ? -((float)(rainbowDelay / 360.0)) : (float)((rainbowDelay %= 360.0) / 360.0), 0.5f, 1.0f).getRGB();
    }

    public static Color astolfo(boolean clickgui, int yOffset) {
        float speed = clickgui ? (float)((Integer)ClickGUI.speed.get() * 100) : 1000.0f;
        float hue = System.currentTimeMillis() % (long)((int)speed) + (long)yOffset;
        if (hue > speed) {
            hue -= speed;
        }
        if ((hue /= speed) > 0.5f) {
            hue = 0.5f - (hue - 0.5f);
        }
        return Color.getHSBColor(hue += 0.5f, 0.4f, 1.0f);
    }

    public static Color getColorSwitch(Color firstColor, Color secondColor, float time, int index, long timePerIndex, double speed) {
        long now = (long)(speed * (double)System.currentTimeMillis() + (double)((long)(-index) * timePerIndex));
        float rd = (float)(firstColor.getRed() - secondColor.getRed()) / time;
        float gd = (float)(firstColor.getGreen() - secondColor.getGreen()) / time;
        float bd = (float)(firstColor.getBlue() - secondColor.getBlue()) / time;
        float rd2 = (float)(secondColor.getRed() - firstColor.getRed()) / time;
        float gd2 = (float)(secondColor.getGreen() - firstColor.getGreen()) / time;
        float bd2 = (float)(secondColor.getBlue() - firstColor.getBlue()) / time;
        int re1 = ApacheMath.round((float)secondColor.getRed() + rd * (float)(now % (long)time));
        int ge1 = ApacheMath.round((float)secondColor.getGreen() + gd * (float)(now % (long)time));
        int be1 = ApacheMath.round((float)secondColor.getBlue() + bd * (float)(now % (long)time));
        int re2 = ApacheMath.round((float)firstColor.getRed() + rd2 * (float)(now % (long)time));
        int ge2 = ApacheMath.round((float)firstColor.getGreen() + gd2 * (float)(now % (long)time));
        int be2 = ApacheMath.round((float)firstColor.getBlue() + bd2 * (float)(now % (long)time));
        if (now % ((long)time * 2L) < (long)time) {
            return new Color(ColorManager.getColor(255, re2, ge2, be2));
        }
        return new Color(ColorManager.getColor(255, re1, ge1, be1));
    }

    public static int getColor(int A, int R, int G, int B) {
        return (A & 0xFF) << 24 | (R & 0xFF) << 16 | (G & 0xFF) << 8 | B & 0xFF;
    }

    public static void glColor(Color color) {
        float red = (float)color.getRed() / 255.0f;
        float green = (float)color.getGreen() / 255.0f;
        float blue = (float)color.getBlue() / 255.0f;
        float alpha = (float)color.getAlpha() / 255.0f;
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
    }

    public static String stripColorCodes(String input) {
        return STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }
}

