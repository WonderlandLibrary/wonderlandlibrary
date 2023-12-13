// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui;

import net.minecraft.util.MathHelper;
import net.minecraft.client.Minecraft;

public class ScaledResolution
{
    private final double scaledWidthD;
    private final double scaledHeightD;
    private int scaledWidth;
    private int scaledHeight;
    private int scaleFactor;
    private static final String __OBFID = "CL_00000666";
    
    public ScaledResolution(final Minecraft mcIn, final int p_i46324_2_, final int p_i46324_3_) {
        this.scaledWidth = p_i46324_2_;
        this.scaledHeight = p_i46324_3_;
        this.scaleFactor = 1;
        final boolean var4 = mcIn.isUnicode();
        int var5 = mcIn.gameSettings.guiScale;
        if (var5 == 0) {
            var5 = 1000;
        }
        while (this.scaleFactor < var5 && this.scaledWidth / (this.scaleFactor + 1) >= 320 && this.scaledHeight / (this.scaleFactor + 1) >= 240) {
            ++this.scaleFactor;
        }
        if (var4 && this.scaleFactor % 2 != 0 && this.scaleFactor != 1) {
            --this.scaleFactor;
        }
        this.scaledWidthD = this.scaledWidth / this.scaleFactor;
        this.scaledHeightD = this.scaledHeight / this.scaleFactor;
        this.scaledWidth = MathHelper.ceiling_double_int(this.scaledWidthD);
        this.scaledHeight = MathHelper.ceiling_double_int(this.scaledHeightD);
    }
    
    public int getScaledWidth() {
        return this.scaledWidth;
    }
    
    public int getScaledHeight() {
        return this.scaledHeight;
    }
    
    public double getScaledWidth_double() {
        return this.scaledWidthD;
    }
    
    public double getScaledHeight_double() {
        return this.scaledHeightD;
    }
    
    public int getScaleFactor() {
        return this.scaleFactor;
    }
}
