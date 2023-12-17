// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.math;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;

public class ScaleMath
{
    private int scale;
    
    public ScaleMath(final int scale) {
        this.scale = scale;
    }
    
    public void pushScale() {
        final ScaledResolution rs = new ScaledResolution(Minecraft.getMinecraft());
        final double scale = ScaledResolution.getScaleFactor() / Math.pow(ScaledResolution.getScaleFactor(), 2.0);
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale * this.scale, scale * this.scale, scale * this.scale);
    }
    
    public int calc(final int value) {
        final ScaledResolution rs = new ScaledResolution(Minecraft.getMinecraft());
        return value * ScaledResolution.getScaleFactor() / this.scale;
    }
    
    public void popScale() {
        GlStateManager.scale((float)this.scale, (float)this.scale, (float)this.scale);
        GlStateManager.popMatrix();
    }
    
    public Vec2i getMouse(final int mouseX, final int mouseY, final ScaledResolution rs) {
        return new Vec2i(mouseX * ScaledResolution.getScaleFactor() / this.scale, mouseY * ScaledResolution.getScaleFactor() / this.scale);
    }
    
    public int getScale() {
        return this.scale;
    }
    
    public void setScale(final int scale) {
        this.scale = scale;
    }
}
