/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.ui;

import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.util.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class SaturnUI {
    public Minecraft mc = Minecraft.getMinecraft();
    public FontRenderer fr = Minecraft.fontRendererObj;

    public void draw() {
        ScaledResolution scaledResolution = new ScaledResolution(this.mc);
        GlStateManager.pushMatrix();
        GlStateManager.translate(4.0f, 4.0f, 0.0f);
        GlStateManager.scale(1.0f, 1.0f, 1.0f);
        GlStateManager.translate(-4.0f, -4.0f, 0.0f);
        this.fr.drawString("E", 4.0, 4.0, ColorUtils.getRainbow(4.0f, 0.4f, 1.0f));
        this.fr.drawString("xodus", 10.0, 4.0, -1);
        GlStateManager.translate(4.0f, 4.0f, 0.0f);
        GlStateManager.scale(0.5, 0.5, 1.0);
        GlStateManager.translate(-4.0f, -4.0f, 0.0f);
        GlStateManager.popMatrix();
        int n = 0;
        for (Module module : Exodus.INSTANCE.moduleManager.getModules()) {
            double d = n * (this.fr.FONT_HEIGHT + 6);
            Gui.drawRect(scaledResolution.getScaledWidth() - this.fr.getStringWidth(module.getDisplayName()) - 4, d, scaledResolution.getScaledWidth(), (double)(4 + this.fr.FONT_HEIGHT) + d, -1879048192);
            this.fr.drawString(module.getDisplayName(), scaledResolution.getScaledWidth() - this.fr.getStringWidth(module.getDisplayName()) - 4, 4.0 + d, -1);
            ++n;
        }
    }
}

