// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.util.math.MathHelper;
import net.minecraft.client.Minecraft;

public class ScaledResolution
{
    private final double scaledWidthD;
    private final double scaledHeightD;
    private int scaledWidth;
    private int scaledHeight;
    private static int scaleFactor;
    
    public ScaledResolution(final Minecraft minecraftClient) {
        this.scaledWidth = minecraftClient.displayWidth;
        this.scaledHeight = minecraftClient.displayHeight;
        ScaledResolution.scaleFactor = 1;
        final boolean flag = minecraftClient.isUnicode();
        int i = minecraftClient.gameSettings.guiScale;
        if (i == 0) {
            i = 1000;
        }
        while (ScaledResolution.scaleFactor < i && this.scaledWidth / (ScaledResolution.scaleFactor + 1) >= 320 && this.scaledHeight / (ScaledResolution.scaleFactor + 1) >= 240) {
            ++ScaledResolution.scaleFactor;
        }
        if (flag && ScaledResolution.scaleFactor % 2 != 0 && ScaledResolution.scaleFactor != 1) {
            --ScaledResolution.scaleFactor;
        }
        this.scaledWidthD = this.scaledWidth / (double)ScaledResolution.scaleFactor;
        this.scaledHeightD = this.scaledHeight / (double)ScaledResolution.scaleFactor;
        this.scaledWidth = MathHelper.ceil(this.scaledWidthD);
        this.scaledHeight = MathHelper.ceil(this.scaledHeightD);
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
    
    public static int getScaleFactor() {
        return ScaledResolution.scaleFactor;
    }
}
