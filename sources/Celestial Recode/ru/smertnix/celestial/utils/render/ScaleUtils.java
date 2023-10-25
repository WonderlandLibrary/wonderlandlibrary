// 
// Decompiled by Procyon v0.5.36
// 

package ru.smertnix.celestial.utils.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;

public class ScaleUtils
{
    private int scale;
    
    public ScaleUtils(final int scale) {
        this.scale = scale;
    }
    
    public void pushScale() {
        final ScaledResolution rs = new ScaledResolution(Minecraft.getMinecraft());
        final double scale = rs.getScaleFactor() / Math.pow(rs.getScaleFactor(), 2.0);
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale * this.scale, scale * this.scale, scale * this.scale);
    }
    
    public int calc(final int value) {
        final ScaledResolution rs = new ScaledResolution(Minecraft.getMinecraft());
        return value * rs.getScaleFactor() / this.scale;
    }
    
    public void popScale() {
        GlStateManager.scale((float)this.scale, (float)this.scale, (float)this.scale);
        GlStateManager.popMatrix();
    }
    
    public int[] getMouse(final int mouseX, final int mouseY, final ScaledResolution rs) {
        return new int[] { mouseX * rs.getScaleFactor() / this.scale, mouseY * rs.getScaleFactor() / this.scale };
    }
    
    public int getScale() {
        return this.scale;
    }
    
    public void setScale(final int scale) {
        this.scale = scale;
    }
}
