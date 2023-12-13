/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.combat;

import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.utils.other.MathUtils;

public class Reach
extends Module {
    public static Setting expand;

    public Reach() {
        super("Reach", Category.Combat);
        expand = new Setting("Reach Expand", this, 3.2, 3.0, 5.0, false);
        Main.instance.setmgr.rSetting(expand);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        this.setSuffix("" + MathUtils.round(expand.getValFloat(), 1));
    }
}

