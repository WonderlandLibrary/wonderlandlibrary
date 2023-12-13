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

public class NoSlowDown
extends Module {
    public static Setting percentage;

    public NoSlowDown() {
        super("NoSlowDown", Category.Movement);
        percentage = new Setting("Percentage", this, 100.0, 0.0, 100.0, false);
        Main.instance.setmgr.rSetting(percentage);
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        double a = percentage.getValDouble();
        this.setSuffix(a + "%");
    }
}

