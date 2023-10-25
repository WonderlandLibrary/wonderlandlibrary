/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.ui;

import java.io.IOException;
import me.Tengoku.Terror.API.ExodusAPI;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.event.events.EventRenderGUI;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.util.ColorUtils;
import me.Tengoku.Terror.util.font.FontUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class CustomIngameGui2 {
    ScaledResolution sr;
    FontRenderer fr;
    public Minecraft mc = Minecraft.getMinecraft();

    public CustomIngameGui2() {
        this.sr = new ScaledResolution(this.mc);
        this.fr = Minecraft.fontRendererObj;
    }

    public void draw() {
        EventRenderGUI eventRenderGUI = new EventRenderGUI();
        eventRenderGUI.call();
        String string = Exodus.INSTANCE.settingsManager.getSettingByName("HUD Font").getValString();
        String string2 = Exodus.INSTANCE.settingsManager.getSettingByName("HUD Mode").getValString();
        String string3 = Exodus.INSTANCE.settingsManager.getSettingByName("ArrayList Color").getValString();
        boolean bl = Exodus.INSTANCE.settingsManager.getSettingByName("ArrayList Outline").getValBoolean();
        if (string2.equalsIgnoreCase("Exodus")) {
            if (string.equalsIgnoreCase("Normal")) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(4.0f, 4.0f, 0.0f);
                GlStateManager.scale(1.0f, 1.0f, 1.0f);
                GlStateManager.translate(-4.0f, -4.0f, 0.0f);
                this.fr.drawStringWithShadow("E", 4.0f, 4.0f, ColorUtils.getRainbow(4.0f, 0.4f, 1.0f));
                this.fr.drawStringWithShadow("xodus", 10.0f, 4.0f, -1);
                try {
                    this.fr.drawStringWithShadow(String.valueOf(ExodusAPI.getUserName()) + " | " + ExodusAPI.getRole(), 1.0f, GuiScreen.height - 12, -1);
                }
                catch (IOException iOException) {
                    iOException.printStackTrace();
                }
                GlStateManager.translate(4.0f, 4.0f, 0.0f);
                GlStateManager.scale(0.5, 0.5, 1.0);
                GlStateManager.translate(-4.0f, -4.0f, 0.0f);
                GlStateManager.popMatrix();
            } else {
                GlStateManager.pushMatrix();
                GlStateManager.translate(4.0f, 4.0f, 0.0f);
                GlStateManager.scale(1.0f, 1.0f, 1.0f);
                GlStateManager.translate(-4.0f, -4.0f, 0.0f);
                FontUtil.normal.drawStringWithShadow("E", 2.0, 4.0f, ColorUtils.getRainbow(4.0f, 0.4f, 1.0f));
                FontUtil.normal.drawStringWithShadow("xodus", 8.0, 4.0f, -1);
                try {
                    FontUtil.normal.drawStringWithShadow(String.valueOf(ExodusAPI.getUserName()) + " | " + ExodusAPI.getRole(), 1.0, GuiScreen.height - 12, -1);
                }
                catch (IOException iOException) {
                    iOException.printStackTrace();
                }
                GlStateManager.translate(4.0f, 4.0f, 0.0f);
                GlStateManager.scale(0.5, 0.5, 1.0);
                GlStateManager.translate(-4.0f, -4.0f, 0.0f);
                GlStateManager.popMatrix();
            }
        }
        int n = 0;
        double d = n * this.fr.FONT_HEIGHT;
        int n2 = 2;
        for (Module module : Exodus.INSTANCE.moduleManager.getModules()) {
            int n3 = 0;
            double d2 = Exodus.INSTANCE.settingsManager.getSettingByName("ArrayList Speed").getValDouble();
            int n4 = this.sr.getScaledWidth() - Minecraft.fontRendererObj.getStringWidth(module.getDisplayName()) - 1;
            if (string3.equalsIgnoreCase("OldSaturn")) {
                n3 = ColorUtils.getRainbowWave((float)d2, 0.4f, 1.0f, n * 100);
            }
            if (string3.equalsIgnoreCase("Astolfo")) {
                n3 = ColorUtils.astolfo((float)d2, 0.5f, 1.0f, n * 50).getRGB();
            }
            if (string3.equalsIgnoreCase("White")) {
                n3 = -1;
            }
            if (string3.equalsIgnoreCase("RGB")) {
                n3 = ColorUtils.getRainbowWave((float)d2, 1.0f, 1.0f, n * 100);
            }
            if (!module.isToggled()) continue;
            if (string.equalsIgnoreCase("Normal")) {
                this.fr.drawStringWithShadow(module.getDisplayName(), this.sr.getScaledWidth() - this.fr.getStringWidth(module.getDisplayName()) - 4, 4 + n * this.fr.FONT_HEIGHT, n3);
            }
            ++n;
        }
    }
}

