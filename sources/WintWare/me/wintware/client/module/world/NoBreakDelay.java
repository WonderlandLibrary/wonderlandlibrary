/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.world;

import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;

public class NoBreakDelay
extends Module {
    public NoBreakDelay() {
        super("NoBreakDelay", Category.World);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        NoBreakDelay.mc.playerController.blockHitDelay = 0;
    }
}

