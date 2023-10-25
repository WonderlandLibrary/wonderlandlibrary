/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;

public class ScaledResolution {
    private int scaledWidth;
    private final double scaledHeightD;
    private int scaledHeight;
    private final double scaledWidthD;
    private int scaleFactor;

    public double getScaledWidth_double() {
        return this.scaledWidthD;
    }

    public int getScaledWidth() {
        return this.scaledWidth;
    }

    public ScaledResolution(Minecraft minecraft) {
        this.scaledWidth = minecraft.displayWidth;
        this.scaledHeight = Minecraft.displayHeight;
        this.scaleFactor = 1;
        boolean bl = minecraft.isUnicode();
        int n = Minecraft.gameSettings.guiScale;
        if (n == 0) {
            n = 1000;
        }
        while (this.scaleFactor < n && this.scaledWidth / (this.scaleFactor + 1) >= 320 && this.scaledHeight / (this.scaleFactor + 1) >= 240) {
            ++this.scaleFactor;
        }
        if (bl && this.scaleFactor % 2 != 0 && this.scaleFactor != 1) {
            --this.scaleFactor;
        }
        this.scaledWidthD = (double)this.scaledWidth / (double)this.scaleFactor;
        this.scaledHeightD = (double)this.scaledHeight / (double)this.scaleFactor;
        this.scaledWidth = MathHelper.ceiling_double_int(this.scaledWidthD);
        this.scaledHeight = MathHelper.ceiling_double_int(this.scaledHeightD);
    }

    public double getScaledHeight_double() {
        return this.scaledHeightD;
    }

    public int getScaleFactor() {
        return this.scaleFactor;
    }

    public int getScaledHeight() {
        return this.scaledHeight;
    }
}

