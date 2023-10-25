/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.render;

import de.Hero.settings.Setting;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.Event2D;
import me.Tengoku.Terror.event.events.EventUpdate;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.util.Timer;
import me.Tengoku.Terror.util.font.FontUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

public class SessionStats
extends Module {
    float minutes;
    Timer timer = new Timer();
    float seconds;
    Timer timer3;
    FontRenderer fr;
    Timer timer2 = new Timer();
    float hours;

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        if (SessionStats.mc.getCurrentServerData().serverIP.contains("hypixel") && this.timer.hasTimeElapsed(1000L, true)) {
            this.seconds += 1.0f;
            if (this.seconds > 59.0f) {
                this.seconds = 0.0f;
                this.minutes += 1.0f;
                if (this.minutes > 59.0f) {
                    this.seconds = 0.0f;
                    this.minutes = 0.0f;
                    this.hours += 1.0f;
                }
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.seconds = 0.0f;
        this.hours = 0.0f;
        this.minutes = 0.0f;
    }

    public SessionStats() {
        super("SessionStats", 0, Category.RENDER, "Wow");
        this.timer3 = new Timer();
        this.fr = Minecraft.fontRendererObj;
    }

    @Override
    public void setup() {
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("X", this, 1.0, 1.0, 1200.0, true));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Y", this, 1.0, 1.0, 1200.0, true));
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @EventTarget
    public void onEvent(Event2D event2D) {
        String string = Exodus.INSTANCE.settingsManager.getSettingByName("HUD Font").getValString();
        if (mc.getCurrentServerData() != null && SessionStats.mc.getCurrentServerData().serverIP.contains("hypixel")) {
            GlStateManager.pushMatrix();
            int n = (int)((double)GuiScreen.width - Exodus.INSTANCE.settingsManager.getSettingByModule("X", this).getValDouble());
            int n2 = (int)((double)GuiScreen.height - Exodus.INSTANCE.settingsManager.getSettingByModule("Y", this).getValDouble());
            int n3 = 40;
            if (string.equalsIgnoreCase("Normal")) {
                this.fr.drawStringWithShadow(String.valueOf((int)this.hours) + "h" + " " + (int)this.minutes + "m" + " " + (int)this.seconds + "s", (float)GuiScreen.width - (float)Exodus.INSTANCE.settingsManager.getSettingByModule("X", this).getValDouble(), (float)GuiScreen.height - (float)Exodus.INSTANCE.settingsManager.getSettingByModule("Y", this).getValDouble(), -1);
            } else {
                FontUtil.normal.drawStringWithShadow(String.valueOf((int)this.hours) + "h" + " " + (int)this.minutes + "m" + " " + (int)this.seconds + "s", (float)((double)GuiScreen.width - Exodus.INSTANCE.settingsManager.getSettingByModule("X", this).getValDouble()), (float)((double)GuiScreen.height - Exodus.INSTANCE.settingsManager.getSettingByModule("Y", this).getValDouble()), -1);
            }
            GlStateManager.popMatrix();
        }
    }
}

