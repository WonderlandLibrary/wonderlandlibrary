// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.util;

import org.lwjgl.opengl.GL11;
import java.util.function.BiConsumer;
import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import java.util.HashMap;
import java.util.Map;

public class RenderUtils
{
    private final Map<Integer, Boolean> glCapMap;
    
    public RenderUtils() {
        this.glCapMap = new HashMap<Integer, Boolean>();
    }
    
    public void glColor(final int red, final int green, final int blue, final int alpha) {
        GlStateManager.color(red / 255.0f, green / 255.0f, blue / 255.0f, alpha / 255.0f);
    }
    
    public void glColor(final Color color) {
        final float red = color.getRed() / 255.0f;
        final float green = color.getGreen() / 255.0f;
        final float blue = color.getBlue() / 255.0f;
        final float alpha = color.getAlpha() / 255.0f;
        GlStateManager.color(red, green, blue, alpha);
    }
    
    private void glColor(final int hex) {
        final float alpha = (hex >> 24 & 0xFF) / 255.0f;
        final float red = (hex >> 16 & 0xFF) / 255.0f;
        final float green = (hex >> 8 & 0xFF) / 255.0f;
        final float blue = (hex & 0xFF) / 255.0f;
        GlStateManager.color(red, green, blue, alpha);
    }
    
    public void resetCaps() {
        this.glCapMap.forEach(this::setGlState);
    }
    
    public void enableGlCap(final int cap) {
        this.setGlCap(cap, true);
    }
    
    public void enableGlCap(final int... caps) {
        for (final int cap : caps) {
            this.setGlCap(cap, true);
        }
    }
    
    public void disableGlCap(final int cap) {
        this.setGlCap(cap, false);
    }
    
    public void disableGlCap(final int... caps) {
        for (final int cap : caps) {
            this.setGlCap(cap, false);
        }
    }
    
    public void setGlCap(final int cap, final boolean state) {
        this.glCapMap.put(cap, GL11.glGetBoolean(cap));
        this.setGlState(cap, state);
    }
    
    public void setGlState(final int cap, final boolean state) {
        if (state) {
            GL11.glEnable(cap);
        }
        else {
            GL11.glDisable(cap);
        }
    }
}
