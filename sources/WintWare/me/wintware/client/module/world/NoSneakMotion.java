/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.world;

import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventPreMotionUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.utils.movement.MovementUtil;
import net.minecraft.client.Minecraft;

public class NoSneakMotion
extends Module {
    public NoSneakMotion() {
        super("NoSneakMotion", Category.World);
    }

    @EventTarget
    public void onPreMotion(EventPreMotionUpdate event) {
        if (Minecraft.player.isSneaking()) {
            MovementUtil.setSpeed(0.2);
        }
    }
}

