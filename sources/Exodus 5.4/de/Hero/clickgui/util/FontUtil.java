/*
 * Decompiled with CFR 0.152.
 */
package de.Hero.clickgui.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.StringUtils;

public class FontUtil {
    private static FontRenderer fontRenderer;

    public static int getStringWidth(String string) {
        return fontRenderer.getStringWidth(StringUtils.stripControlCodes(string));
    }

    public static void drawCenteredStringWithShadow(String string, double d, double d2, int n) {
        FontUtil.drawStringWithShadow(string, d - (double)(fontRenderer.getStringWidth(string) / 2), d2, n);
    }

    public static void setupFontUtils() {
        Minecraft.getMinecraft();
        fontRenderer = Minecraft.fontRendererObj;
    }

    public static void drawTotalCenteredString(String string, double d, double d2, int n) {
        FontUtil.drawString(string, d - (double)(fontRenderer.getStringWidth(string) / 2), d2 - (double)(FontUtil.fontRenderer.FONT_HEIGHT / 2), n);
    }

    public static int getFontHeight() {
        return FontUtil.fontRenderer.FONT_HEIGHT;
    }

    public static void drawCenteredString(String string, double d, double d2, int n) {
        FontUtil.drawString(string, d - (double)(fontRenderer.getStringWidth(string) / 2), d2, n);
    }

    public static void drawStringWithShadow(String string, double d, double d2, int n) {
        fontRenderer.drawStringWithShadow(string, (float)d, (float)d2, n);
    }

    public static void drawString(String string, double d, double d2, int n) {
        fontRenderer.drawString(string, d, d2, n);
    }

    public static void drawTotalCenteredStringWithShadow(String string, double d, double d2, int n) {
        FontUtil.drawStringWithShadow(string, d - (double)(fontRenderer.getStringWidth(string) / 2), d2 - (double)((float)FontUtil.fontRenderer.FONT_HEIGHT / 2.0f), n);
    }
}

