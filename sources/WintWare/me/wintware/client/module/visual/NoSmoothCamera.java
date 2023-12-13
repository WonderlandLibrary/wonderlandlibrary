/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.visual;

import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventPreMotionUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;

public class NoSmoothCamera
extends Module {
    public NoSmoothCamera() {
        super("NoSmoothCamera", Category.Visuals);
    }

    @EventTarget
    public void onUpdate(EventPreMotionUpdate event) {
        NoSmoothCamera.mc.gameSettings.smoothCamera = false;
    }
}

