/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.movement;

import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.client.Minecraft;

public class NoJumpDelay
extends Module {
    public NoJumpDelay() {
        super("NoJumpDelay", Category.Movement);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        Minecraft.player.jumpTicks = 0;
    }
}

