/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.player;

import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventUpdate;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;

public class FastPlace
extends Module {
    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        FastPlace.mc.rightClickDelayTimer = 0;
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public FastPlace() {
        super("FastPlace", 0, Category.PLAYER, "Places blocks really fast.");
    }
}

