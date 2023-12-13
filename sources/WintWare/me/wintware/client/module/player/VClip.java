/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.player;

import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventPreMotionUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.client.Minecraft;

public class VClip
extends Module {
    public VClip() {
        super("VClip", Category.Player);
        Main.instance.setmgr.rSetting(new Setting("Y", this, 50.0, 1.0, 200.0, false));
    }

    @EventTarget
    public void onUpdate(EventPreMotionUpdate event) {
        if (Minecraft.player == null && VClip.mc.world == null) {
            return;
        }
        float tp = Main.instance.setmgr.getSettingByName("Y").getValFloat();
        for (int i = 0; i < 1; ++i) {
            Minecraft.player.setEntityBoundingBox(Minecraft.player.getEntityBoundingBox().offset(0.0, -tp, 0.0));
            this.toggle();
        }
    }
}

