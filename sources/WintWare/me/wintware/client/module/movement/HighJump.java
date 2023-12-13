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
import net.minecraft.client.Minecraft;

public class HighJump
extends Module {
    public HighJump() {
        super("WaterJump", Category.Movement);
        Main.instance.setmgr.rSetting(new Setting("Motion", this, 1.0, 0.0, 25.0, false));
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (Minecraft.player.isInWater()) {
            if (Minecraft.player.motionY < 0.0) {
                Minecraft.player.jump();
                Minecraft.player.motionY = Main.instance.setmgr.getSettingByName("Motion").getValDouble();
                Minecraft.player.jumpMovementFactor = 0.5f;
            }
        }
    }
}

