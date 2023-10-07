package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;

public class ScaledResolution
{
    private final double scaledWidthD;
    private final double scaledHeightD;
    private int scaledWidth;
    private int scaledHeight;
    private int scaleFactor;
    private static int scale;
    private static ScaledResolution lastResolution;

    private ScaledResolution(Minecraft mc)
    {
        this.scaledWidth = mc.displayWidth;
        this.scaledHeight = mc.displayHeight;

        this.scaleFactor = 1;
        boolean flag = mc.isUnicode();
        scale = mc.gameSettings.guiScale;

        if (scale == 0)
        {
            scale = 1000;
        }

        while (this.scaleFactor < scale && this.scaledWidth / (this.scaleFactor + 1) >= 320 && this.scaledHeight / (this.scaleFactor + 1) >= 240)
        {
            ++this.scaleFactor;
        }

        if (flag && this.scaleFactor % 2 != 0 && this.scaleFactor != 1)
        {
            --this.scaleFactor;
        }

        this.scaledWidthD = (double)this.scaledWidth / (double)this.scaleFactor;
        this.scaledHeightD = (double)this.scaledHeight / (double)this.scaleFactor;
        this.scaledWidth = MathHelper.ceiling_double_int(this.scaledWidthD);
        this.scaledHeight = MathHelper.ceiling_double_int(this.scaledHeightD);
    }
    public static ScaledResolution fetchResolution(final Minecraft minecraft) {
        if (lastResolution == null) {
            return lastResolution = new ScaledResolution(minecraft);
        }

        if (scale != minecraft.gameSettings.particleSetting) {
            return lastResolution = new ScaledResolution(minecraft);
        }

        if (lastResolution.scaledWidth != minecraft.displayWidth || lastResolution.scaledHeight != minecraft.displayHeight) {
            return lastResolution = new ScaledResolution(minecraft);
        }

        return lastResolution;
    }
    public int getScaledWidth()
    {
        return this.scaledWidth;
    }

    public int getScaledHeight()
    {
        return this.scaledHeight;
    }

    public double getScaledWidth_double()
    {
        return this.scaledWidthD;
    }

    public double getScaledHeight_double()
    {
        return this.scaledHeightD;
    }

    public int getScaleFactor()
    {
        return this.scaleFactor;
    }
}
