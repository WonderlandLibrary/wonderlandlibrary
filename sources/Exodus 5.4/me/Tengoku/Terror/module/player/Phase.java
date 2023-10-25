/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.player;

import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventUpdate;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import net.minecraft.client.Minecraft;

public class Phase
extends Module {
    public Phase() {
        super("Phase", 37, Category.PLAYER, "Move through blocks.");
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (Minecraft.thePlayer != null) {
            Minecraft.thePlayer.setPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + -3.0, Minecraft.thePlayer.posZ);
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}

