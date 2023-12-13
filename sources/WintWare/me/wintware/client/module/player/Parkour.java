/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.player;

import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.client.Minecraft;

public class Parkour
extends Module {
    public Parkour() {
        super("Parkour", Category.Player);
    }

    @EventTarget
    public void onLocalPlayerUpdate(EventUpdate e) {
        if (Minecraft.player.onGround) {
            if (!Minecraft.player.isSneaking() && !Parkour.mc.gameSettings.keyBindJump.isPressed()) {
                if (Parkour.mc.world.getCollisionBoxes(Minecraft.player, Minecraft.player.getEntityBoundingBox().offset(0.0, -0.5, 0.0).expand(-0.001, 0.0, -0.001)).isEmpty()) {
                    Minecraft.player.jump();
                }
            }
        }
    }
}

