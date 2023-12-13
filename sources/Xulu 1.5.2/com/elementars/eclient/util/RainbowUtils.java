// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.util;

import java.awt.Color;
import com.elementars.eclient.module.core.Global;

public class RainbowUtils
{
    private static int rgb;
    public static int a;
    public static int r;
    public static int g;
    public static int b;
    static float hue;
    
    public static void updateRainbow() {
        RainbowUtils.rgb = Color.HSBtoRGB(RainbowUtils.hue, Global.rainbowSaturation.getValue() / 255.0f, Global.rainbowLightness.getValue() / 255.0f);
        RainbowUtils.a = (RainbowUtils.rgb >>> 24 & 0xFF);
        RainbowUtils.r = (RainbowUtils.rgb >>> 16 & 0xFF);
        RainbowUtils.g = (RainbowUtils.rgb >>> 8 & 0xFF);
        RainbowUtils.b = (RainbowUtils.rgb & 0xFF);
        RainbowUtils.hue += Global.rainbowspeed.getValue() / 1000.0f;
        if (RainbowUtils.hue > 1.0f) {
            --RainbowUtils.hue;
        }
    }
    
    static {
        RainbowUtils.hue = 0.01f;
    }
}
