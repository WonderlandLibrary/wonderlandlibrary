/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.movement;

import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventSafeWalk;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;

public class SafeWalk
extends Module {
    @EventTarget
    public void onUpdate(EventSafeWalk eventSafeWalk) {
        eventSafeWalk.setSafeWalk(true);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public SafeWalk() {
        super("SafeWalk", 0, Category.MOVEMENT, "Prevents you from falling off a cliff.");
    }
}

