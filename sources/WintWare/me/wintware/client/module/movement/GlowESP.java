/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.movement;

import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class GlowESP
extends Module {
    public GlowESP() {
        super("GlowESP", Category.Visuals);
    }

    @Override
    public void onDisable() {
        for (EntityPlayer player : GlowESP.mc.world.playerEntities) {
            if (!player.isGlowing()) continue;
            player.setGlowing(false);
        }
        super.onDisable();
    }

    @EventTarget
    public void onEvent(EventUpdate e) {
        if (Minecraft.player == null) {
            return;
        }
        for (Entity player : GlowESP.mc.world.loadedEntityList) {
            if (!(player instanceof EntityPlayer)) continue;
            player.setGlowing(true);
        }
    }
}

