/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.render;

import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventHurtCam;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;

public class NoHurtCam
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
    public void onPre(EventHurtCam eventHurtCam) {
        if (eventHurtCam.isPre()) {
            eventHurtCam.setCancelled(true);
        }
    }

    public NoHurtCam() {
        super("NoHurtCam", 0, Category.RENDER, "Disables the camera shake when taking damage.");
    }
}

