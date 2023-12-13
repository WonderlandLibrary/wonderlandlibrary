/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.movement;

import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventPreMotionUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.utils.movement.MovementUtil;
import net.minecraft.client.Minecraft;

public class WaterSpeed
extends Module {
    public int ticks = 0;
    public Setting speed = new Setting("Speed", this, 1.0, 0.1f, 5.0, false);

    public WaterSpeed() {
        super("WaterSpeed", Category.Movement);
        Main.instance.setmgr.rSetting(this.speed);
    }

    @EventTarget
    public void onPreMotion(EventPreMotionUpdate event) {
        if (Minecraft.player.isInWater()) {
            ++this.ticks;
            if (this.ticks == 4) {
                MovementUtil.setSpeed(this.speed.getValDouble());
            }
            if (this.ticks >= 5) {
                MovementUtil.setSpeed(this.speed.getValDouble());
                this.ticks = 0;
            }
            MovementUtil.setSpeed(this.speed.getValDouble());
        }
    }
}

