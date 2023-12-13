// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.font;

import java.awt.Font;
import com.elementars.eclient.util.Wrapper;
import com.elementars.eclient.font.custom.CustomFont;
import java.util.regex.Pattern;

public class CFontManager
{
    private static final Pattern COLOR_CODE_PATTERN;
    private final int[] colorCodes;
    public static CustomFont customFont;
    public static XFontRenderer xFontRenderer;
    public static RainbowTextRenderer rainbowTextRenderer;
    
    public CFontManager() {
        this.colorCodes = new int[] { 0, 170, 43520, 43690, 11141120, 11141290, 16755200, 11184810, 5592405, 5592575, 5635925, 5636095, 16733525, 16733695, 16777045, 16777215 };
    }
    
    public float drawStringWithShadow(final String text, double x, final double y, final int color, final boolean isRainbow) {
        if (com.elementars.eclient.module.core.CustomFont.customFontMode.getValue().equalsIgnoreCase("Normal")) {
            return CFontManager.customFont.drawStringWithShadow(text, x, y - com.elementars.eclient.module.core.CustomFont.fontOffset.getValue(), color);
        }
        if (com.elementars.eclient.module.core.CustomFont.customFontMode.getValue().equalsIgnoreCase("Xdolf")) {
            CFontManager.xFontRenderer.drawStringWithShadow(text, (int)x, (int)y - com.elementars.eclient.module.core.CustomFont.fontOffset.getValue(), color);
            return 0.0f;
        }
        if (com.elementars.eclient.module.core.CustomFont.customFontMode.getValue().equalsIgnoreCase("Rainbow")) {
            int rgb = isRainbow ? color : -1;
            int displayColor = 0;
            final char[] characters = text.toCharArray();
            final String[] parts = CFontManager.COLOR_CODE_PATTERN.split(text);
            int index = 0;
            for (final String s : parts) {
                final String[] split;
                final String[] parts2 = split = s.split("");
                for (final String s2 : split) {
                    if (displayColor == 0) {
                        rgb = CFontManager.rainbowTextRenderer.drawStringWithShadow(s2, (float)x, (float)y, rgb);
                    }
                    else {
                        rgb = CFontManager.rainbowTextRenderer.updateRainbow(rgb);
                        Wrapper.getMinecraft().fontRenderer.drawStringWithShadow(s2, (float)x, (float)y, displayColor);
                    }
                    try {
                        x += CFontManager.rainbowTextRenderer.getCharWidth(s2.charAt(0));
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    ++index;
                }
                if (index < characters.length) {
                    final char colorCode = characters[index];
                    if (colorCode == '§') {
                        final char colorChar = characters[index + 1];
                        final int codeIndex = "0123456789abcdef".indexOf(colorChar);
                        if (codeIndex < 0) {
                            if (colorChar == 'r') {
                                displayColor = 0;
                            }
                        }
                        else {
                            displayColor = this.colorCodes[codeIndex];
                        }
                        index += 2;
                    }
                }
            }
            return 0.0f;
        }
        return 0.0f;
    }
    
    public float drawStringWithShadow(final String text, final double x, final double y, final int color) {
        return this.drawStringWithShadow(text, x, y, color, false);
    }
    
    public float drawString(final String text, final float x, final float y, final int color) {
        if (com.elementars.eclient.module.core.CustomFont.customFontMode.getValue().equalsIgnoreCase("Normal")) {
            return CFontManager.customFont.drawString(text, x, y - com.elementars.eclient.module.core.CustomFont.fontOffset.getValue(), color);
        }
        return (float)CFontManager.xFontRenderer.drawStringWithShadow(text, x, y - com.elementars.eclient.module.core.CustomFont.fontOffset.getValue(), color);
    }
    
    public float drawCenteredStringWithShadow(final String text, final float x, final float y, final int color) {
        if (com.elementars.eclient.module.core.CustomFont.customFontMode.getValue().equalsIgnoreCase("Normal")) {
            return CFontManager.customFont.drawCenteredStringWithShadow(text, x, y, color);
        }
        CFontManager.xFontRenderer.drawCenteredString(text, (int)x, (int)y, color, true);
        return 0.0f;
    }
    
    public float drawCenteredString(final String text, final float x, final float y, final int color) {
        if (com.elementars.eclient.module.core.CustomFont.customFontMode.getValue().equalsIgnoreCase("Normal")) {
            return CFontManager.customFont.drawCenteredString(text, x, y, color);
        }
        CFontManager.xFontRenderer.drawCenteredString(text, (int)x, (int)y, color);
        return 0.0f;
    }
    
    public float drawString(final String text, final double x, final double y, final int color, final boolean shadow) {
        if (com.elementars.eclient.module.core.CustomFont.customFontMode.getValue().equalsIgnoreCase("Normal")) {
            return CFontManager.customFont.drawString(text, x, y, color, shadow);
        }
        if (shadow) {
            return (float)CFontManager.xFontRenderer.drawStringWithShadow(text, (float)x, (float)y, color);
        }
        return (float)CFontManager.xFontRenderer.drawString(text, (float)x, (float)y, color);
    }
    
    public float getHeight() {
        if (com.elementars.eclient.module.core.CustomFont.customFontMode.getValue().equalsIgnoreCase("Normal")) {
            return (float)CFontManager.customFont.getHeight();
        }
        if (com.elementars.eclient.module.core.CustomFont.customFontMode.getValue().equalsIgnoreCase("Rainbow")) {
            return (float)CFontManager.rainbowTextRenderer.getHeight();
        }
        return (float)CFontManager.xFontRenderer.getHeight();
    }
    
    public int getStringWidth(final String text) {
        if (com.elementars.eclient.module.core.CustomFont.customFontMode.getValue().equalsIgnoreCase("Normal")) {
            return CFontManager.customFont.getStringWidth(text);
        }
        if (com.elementars.eclient.module.core.CustomFont.customFontMode.getValue().equalsIgnoreCase("Rainbow")) {
            return CFontManager.rainbowTextRenderer.getStringWidth(text);
        }
        if (com.elementars.eclient.module.core.CustomFont.customFontMode.getValue().equalsIgnoreCase("Xdolf")) {
            return CFontManager.xFontRenderer.getStringWidth(text);
        }
        return 0;
    }
    
    static {
        COLOR_CODE_PATTERN = Pattern.compile("§[0123456789abcdefklmnor]");
        CFontManager.customFont = new CustomFont(new Font("Verdana", 0, 18), true, false);
        CFontManager.xFontRenderer = new XFontRenderer(new Font("Verdana", 0, 36), true, 8);
        CFontManager.rainbowTextRenderer = new RainbowTextRenderer();
    }
}
