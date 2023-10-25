/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.player;

import de.Hero.settings.Setting;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventMotion;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import net.minecraft.client.Minecraft;

public class AntiAim
extends Module {
    @Override
    public void setup() {
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Rage", this, true));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Spin Bot Pitch", this, 90.0, 1.0, 90.0, true));
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Spin Bot Yaw", this, 90.0, 1.0, 360.0, true));
    }

    public AntiAim() {
        super("Spin Bot", 0, Category.PLAYER, "You spin me right round, baby right round.");
    }

    @EventTarget
    public void onUpdate(EventMotion eventMotion) {
        double d = Exodus.INSTANCE.settingsManager.getSettingByName("Spin Bot Pitch").getValDouble();
        double d2 = Exodus.INSTANCE.settingsManager.getSettingByName("Spin Bot Yaw").getValDouble();
        boolean bl = Exodus.INSTANCE.settingsManager.getSettingByName("Rage").getValBoolean();
        eventMotion.setPitch((float)d);
        if (bl) {
            eventMotion.setYaw((float)((double)((float)d2) + Math.random() * Math.PI * Math.random() * d * 360.0));
        } else {
            eventMotion.setYaw((float)d2);
        }
        if (Exodus.INSTANCE.moduleManager.getModuleByName("Head Rotations").isToggled()) {
            Minecraft.thePlayer.rotationPitchHead = EventMotion.getPitch();
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        AntiAim.mc.timer.timerSpeed = 1.0f;
    }
}

