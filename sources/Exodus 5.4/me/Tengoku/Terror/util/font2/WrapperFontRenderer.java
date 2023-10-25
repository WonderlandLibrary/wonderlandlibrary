/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.util.font2;

import java.util.List;
import java.util.Random;
import net.minecraft.client.gui.FontRenderer;

public class WrapperFontRenderer {
    private FontRenderer real;

    public int splitStringWidth(String string, int n) {
        return this.real.splitStringWidth(string, n);
    }

    public int drawStringWithShadow(String string, float f, float f2, int n) {
        return this.real.drawStringWithShadow(string, f, f2, n);
    }

    public int getFONT_HEIGHT() {
        return this.real.FONT_HEIGHT;
    }

    public void drawSplitString(String string, int n, int n2, int n3, int n4) {
        this.real.drawSplitString(string, n, n2, n3, n4);
    }

    public int getStringWidth(String string) {
        return this.real.getStringWidth(string);
    }

    public boolean getUnicodeFlag() {
        return this.real.getUnicodeFlag();
    }

    public int getCharWidth(char c) {
        return this.real.getCharWidth(c);
    }

    public void setFontRandom(Random random) {
        this.real.fontRandom = random;
    }

    public static String getFormatFromString(String string) {
        return FontRenderer.getFormatFromString(string);
    }

    public String trimStringToWidth(String string, int n) {
        return this.real.trimStringToWidth(string, n);
    }

    public WrapperFontRenderer(FontRenderer fontRenderer) {
        this.real = fontRenderer;
    }

    public boolean getBidiFlag() {
        return this.real.getBidiFlag();
    }

    public List listFormattedStringToWidth(String string, int n) {
        return this.real.listFormattedStringToWidth(string, n);
    }

    public void setFONT_HEIGHT(int n) {
        this.real.FONT_HEIGHT = n;
    }

    public void setBidiFlag(boolean bl) {
        this.real.setBidiFlag(bl);
    }

    public String trimStringToWidth(String string, int n, boolean bl) {
        return this.real.trimStringToWidth(string, n, bl);
    }

    public int drawString(String string, float f, float f2, int n, boolean bl) {
        return this.real.drawString(string, f, f2, n, bl);
    }

    public int drawString(String string, int n, int n2, int n3) {
        return this.real.drawString(string, n, n2, n3);
    }

    public Random getFontRandom() {
        return this.real.fontRandom;
    }

    public void setUnicodeFlag(boolean bl) {
        this.real.setUnicodeFlag(bl);
    }

    public int getColorCode(char c) {
        return this.real.getColorCode(c);
    }

    public FontRenderer unwrap() {
        return this.real;
    }
}

