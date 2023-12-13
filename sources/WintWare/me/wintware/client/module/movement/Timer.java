/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.movement;

import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;

public class Timer
extends Module {
    public Timer() {
        super("Timer", Category.Movement);
        Main.instance.setmgr.rSetting(new Setting("Timer", this, 1.0, 0.1, 10.0, false));
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (this.getState()) {
            Timer.mc.timer.timerSpeed = Main.instance.setmgr.getSettingByName("Timer").getValFloat();
            this.setSuffix("" + Main.instance.setmgr.getSettingByName("Timer").getValFloat());
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Timer.mc.timer.timerSpeed = 1.0f;
    }
}

