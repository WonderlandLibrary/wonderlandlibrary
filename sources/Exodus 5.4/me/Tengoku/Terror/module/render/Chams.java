/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.render;

import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventRenderEntity;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.GlStateManager;

public class Chams
extends Module {
    public Chams() {
        super("Chams", 0, Category.RENDER, "See players through walls, but better.");
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @EventTarget
    public void onPre(EventRenderEntity eventRenderEntity) {
        if (eventRenderEntity.getEntity() instanceof EntityOtherPlayerMP) {
            GlStateManager.enablePolygonOffset();
            GlStateManager.doPolygonOffset(1.0f, -1100000.0f);
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}

