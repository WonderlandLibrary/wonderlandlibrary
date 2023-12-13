/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.event.impl;

import me.wintware.client.event.callables.EventCancellable;
import net.minecraft.client.gui.ScaledResolution;

public class EventRender2D
extends EventCancellable {
    private final ScaledResolution resolution;
    private final float partialticks;

    public EventRender2D(ScaledResolution resolution, float partialticks) {
        this.resolution = resolution;
        this.partialticks = partialticks;
    }

    public ScaledResolution getResolution() {
        return this.resolution;
    }

    public float getPartialTicks() {
        return this.partialticks;
    }
}

