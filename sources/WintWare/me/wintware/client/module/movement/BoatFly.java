/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.movement;

import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityBoat;

public class BoatFly
extends Module {
    public BoatFly() {
        super("BoatFly", Category.Movement);
    }

    @EventTarget
    public void onPreMotion(EventUpdate event) {
        if (Minecraft.player.ridingEntity != null) {
            if (Minecraft.player.ridingEntity instanceof EntityBoat) {
                Minecraft.player.ridingEntity.motionY = BoatFly.mc.gameSettings.keyBindJump.pressed ? 0.5 : 0.0;
            }
        }
    }
}

