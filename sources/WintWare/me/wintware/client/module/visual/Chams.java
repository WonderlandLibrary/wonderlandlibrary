/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.visual;

import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.Event3D;
import me.wintware.client.event.impl.EventPreMotionUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;

public class Chams
extends Module {
    public Chams() {
        super("Chams", Category.Visuals);
    }

    @EventTarget
    public void onUpdate(EventPreMotionUpdate event) {
    }

    @EventTarget
    public void onEvent(Event3D eventRender3D) {
    }
}

