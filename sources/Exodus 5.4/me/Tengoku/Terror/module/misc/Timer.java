/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.misc;

import de.Hero.settings.Setting;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventUpdate;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;

public class Timer
extends Module {
    @Override
    public void setup() {
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Speed", this, 1.0, 0.1f, 10.0, true));
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Timer.mc.timer.timerSpeed = 1.0f;
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        double d = Exodus.INSTANCE.settingsManager.getSettingByName("Speed").getValDouble();
        this.setDisplayName("Timer \ufffdf" + (int)d);
        Timer.mc.timer.timerSpeed = (float)Exodus.INSTANCE.settingsManager.getSettingByModule("Speed", this).getValDouble();
    }

    public Timer() {
        super("Timer", 0, Category.MISC, "Changes the mc timer.");
    }
}

