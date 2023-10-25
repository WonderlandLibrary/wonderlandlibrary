/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.render;

import de.Hero.settings.Setting;
import java.awt.Color;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.Event2D;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.util.RoundedRect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class Radar
extends Module {
    @EventTarget
    public void onEvent(Event2D event2D) {
        GlStateManager.pushMatrix();
        int n = (int)Exodus.INSTANCE.settingsManager.getSettingByModule("X", this).getValDouble();
        int n2 = (int)Exodus.INSTANCE.settingsManager.getSettingByModule("Y", this).getValDouble();
        int n3 = 70;
        RoundedRect.drawRoundedRect(n, n2, n + n3, n2 + n3, 5.0, -1879048192);
        for (Entity entity : Minecraft.theWorld.loadedEntityList) {
            if (!(entity instanceof EntityLivingBase)) continue;
            double d = Minecraft.thePlayer.posX - entity.posX;
            double d2 = Minecraft.thePlayer.posZ - entity.posZ;
            if (!(d <= (double)(n3 / 2)) || !(d >= (double)(-n3 / 2)) || !(d2 <= (double)(n3 / 2)) || !(d2 >= (double)(-n3 / 2))) continue;
            if (entity == Minecraft.thePlayer) {
                Gui.drawRect(d - 1.0 + (double)n + (double)(n3 / 2), d2 - 1.0 + (double)n2 + (double)(n3 / 2), d + 1.0 + (double)n + (double)(n3 / 2), d2 + 1.0 + (double)n2 + (double)(n3 / 2), new Color(0, 255, 0).darker().getRGB());
                continue;
            }
            if (entity.isInvisible() || !(entity instanceof EntityPlayer)) continue;
            Gui.drawRect(d - 1.0 + (double)n + (double)(n3 / 2), d2 - 1.0 + (double)n2 + (double)(n3 / 2), d + 1.0 + (double)n + (double)(n3 / 2), d2 + 1.0 + (double)n2 + (double)(n3 / 2), -65536);
        }
        GlStateManager.popMatrix();
    }

    public Radar() {
        super("Radar", 0, Category.RENDER, "Shows nearby entities.");
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("X", this, 1.0, 1.0, 1200.0, true));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Y", this, 1.0, 1.0, 1200.0, true));
    }
}

