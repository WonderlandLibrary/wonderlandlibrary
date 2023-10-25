/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.render;

import de.Hero.settings.Setting;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventTick;
import me.Tengoku.Terror.event.events.EventUpdate;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import net.minecraft.client.Minecraft;

public class TimeChanger
extends Module {
    @Override
    public void onEnable() {
        super.onEnable();
    }

    @EventTarget
    public void onTick(EventTick eventTick) {
        Minecraft.theWorld.setWorldTime((long)Exodus.INSTANCE.settingsManager.getSettingByName("Time").getValDouble());
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        this.setDisplayName("Time Changer");
    }

    @Override
    public void setup() {
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Time", this, 20000.0, 20000.0, 28000.0, true));
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public TimeChanger() {
        super("Time Changer", 0, Category.RENDER, "");
    }
}

