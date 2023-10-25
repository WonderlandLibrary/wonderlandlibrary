/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;

public class ScaledResolution {
    private final double scaledWidthD;
    private final double scaledHeightD;
    private int scaledWidth;
    private int scaledHeight;
    private static int scaleFactor;

    public ScaledResolution(Minecraft minecraftClient) {
        this.scaledWidth = minecraftClient.displayWidth;
        this.scaledHeight = minecraftClient.displayHeight;
        scaleFactor = 1;
        boolean flag = minecraftClient.isUnicode();
        int i = minecraftClient.gameSettings.guiScale;
        if (i == 0) {
            i = 1000;
        }
        while (true) {
            if (scaleFactor >= i) break;
            if (this.scaledWidth / (scaleFactor + 1) < 320) break;
            if (this.scaledHeight / (scaleFactor + 1) < 240) break;
            ++scaleFactor;
        }
        if (flag) {
            if (scaleFactor % 2 != 0) {
                if (scaleFactor != 1) {
                    --scaleFactor;
                }
            }
        }
        this.scaledWidthD = (double)this.scaledWidth / (double)scaleFactor;
        this.scaledHeightD = (double)this.scaledHeight / (double)scaleFactor;
        this.scaledWidth = MathHelper.ceil(this.scaledWidthD);
        this.scaledHeight = MathHelper.ceil(this.scaledHeightD);
    }

    public int getScaledWidth() {
        return this.scaledWidth;
    }

    public int getScaledHeight() {
        return this.scaledHeight;
    }

    public int getScaleFactor() {
        return scaleFactor;
    }
}

