/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.render;

import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventMotion;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import net.minecraft.client.Minecraft;

public class Bob
extends Module {
    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @EventTarget
    public void onPre(EventMotion eventMotion) {
        if (!eventMotion.isOnGround()) {
            if (Minecraft.thePlayer.isMoving()) {
                Minecraft.thePlayer.cameraYaw = 0.1f;
            }
        }
    }

    public Bob() {
        super("Bob", 0, Category.RENDER, "Parkinson syndrome. fainton");
    }
}

