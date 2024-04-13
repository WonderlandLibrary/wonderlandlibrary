package com.canon.majik.api.utils.turok.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;

/**
 * @author SrRina
 * @since 02/10/20 at 1:36pm
 */
public class TurokDisplay {
    private Minecraft mc;

    private int scaleFactor;

    private float partialTicks;

    public TurokDisplay(Minecraft mc) {
        this.mc = mc;

        this.scaleFactor = 1;
    }

    public int getWidth() {
        return this.mc.displayWidth;
    }

    public int getHeight() {
        return this.mc.displayHeight;
    }

    public int getScaledWidth() {
        this.onUpdate();

        return (int) ((double) this.mc.displayWidth / this.scaleFactor);
    }

    public int getScaledHeight() {
        this.onUpdate();

        return (int) ((double) this.mc.displayHeight / this.scaleFactor);
    }

    public int getScaleFactor() {
        return scaleFactor;
    }

    public void setPartialTicks(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public float getRenderPartialTicks() {
        float _partialTicks = mc.isGamePaused() ? (Minecraft.getDebugFPS() / this.mc.getTickLength()) : mc.getRenderPartialTicks();

        return _partialTicks;
    }

    protected void onUpdate() {
        boolean isUnicode = this.mc.isUnicode();

        int minecraftScale = this.mc.gameSettings.guiScale;

        if (minecraftScale == 0) {
            minecraftScale = 1000;
        }

        while (this.scaleFactor < minecraftScale && (getWidth() / (this.scaleFactor + 1) >= 320) && (getHeight() / (this.scaleFactor + 1) >= 240)) {
            ++this.scaleFactor;
        }

        if (isUnicode && this.scaleFactor % 2 != 0 && this.scaleFactor != 1) {
            --this.scaleFactor;
        }
    }
}
