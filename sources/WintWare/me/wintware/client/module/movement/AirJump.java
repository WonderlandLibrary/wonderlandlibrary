/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.movement;

import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.client.Minecraft;

public class AirJump
extends Module {
    public AirJump() {
        super("AirJump", Category.Movement);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        Minecraft.player.onGround = true;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Minecraft.player.onGround = false;
    }
}

