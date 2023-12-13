// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.font;

import java.awt.Color;
import com.elementars.eclient.module.core.Global;
import com.elementars.eclient.util.Helper;

public class RainbowTextRenderer implements Helper
{
    private static int rgb;
    public static int a;
    public static int r;
    public static int g;
    public static int b;
    static float hue;
    public int FONT_HEIGHT;
    
    public RainbowTextRenderer() {
        this.FONT_HEIGHT = 9;
    }
    
    public void updateRainbow() {
        RainbowTextRenderer.rgb = Color.HSBtoRGB(RainbowTextRenderer.hue, Global.rainbowSaturation.getValue() / 255.0f, Global.rainbowLightness.getValue() / 255.0f);
        RainbowTextRenderer.a = (RainbowTextRenderer.rgb >>> 24 & 0xFF);
        RainbowTextRenderer.r = (RainbowTextRenderer.rgb >>> 16 & 0xFF);
        RainbowTextRenderer.g = (RainbowTextRenderer.rgb >>> 8 & 0xFF);
        RainbowTextRenderer.b = (RainbowTextRenderer.rgb & 0xFF);
        RainbowTextRenderer.hue += 1.0E-5f;
        if (RainbowTextRenderer.hue > 1.0f) {
            --RainbowTextRenderer.hue;
        }
    }
    
    public int updateRainbow(final int IN) {
        float hue2 = Color.RGBtoHSB(new Color(IN).getRed(), new Color(IN).getGreen(), new Color(IN).getBlue(), null)[0];
        hue2 += Global.rainbowAmount.getValue() / 1000.0f;
        if (hue2 > 1.0f) {
            --hue2;
        }
        return Color.HSBtoRGB(hue2, Global.rainbowSaturation.getValue() / 255.0f, Global.rainbowLightness.getValue() / 255.0f);
    }
    
    public int drawStringWithShadow(final String text, final float x, final float y, int color) {
        if (color == -1) {
            color = RainbowTextRenderer.rgb;
            this.updateRainbow();
        }
        else {
            color = this.updateRainbow(color);
        }
        RainbowTextRenderer.fontRenderer.drawStringWithShadow(text, x, y, color);
        return color;
    }
    
    public int drawString(final String text, final int x, final int y, int color) {
        if (color == -1) {
            color = RainbowTextRenderer.rgb;
        }
        this.updateRainbow();
        RainbowTextRenderer.fontRenderer.drawString(text, x, y, color);
        return color;
    }
    
    public int getStringWidth(final String text) {
        return RainbowTextRenderer.fontRenderer.getStringWidth(text);
    }
    
    public int getCharWidth(final char character) {
        return RainbowTextRenderer.fontRenderer.getCharWidth(character);
    }
    
    public int getHeight() {
        return this.FONT_HEIGHT;
    }
    
    static {
        RainbowTextRenderer.hue = 0.01f;
    }
}
