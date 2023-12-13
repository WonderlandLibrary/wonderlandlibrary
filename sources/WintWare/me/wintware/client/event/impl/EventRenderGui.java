/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.event.impl;

import me.wintware.client.event.Event;
import net.minecraft.client.gui.ScaledResolution;

public class EventRenderGui
implements Event {
    private final ScaledResolution scaledResolution;
    private final float partialTicks;

    public EventRenderGui(ScaledResolution scaledResolution, float partialTicks) {
        this.scaledResolution = scaledResolution;
        this.partialTicks = partialTicks;
    }

    public ScaledResolution getScaledResolution() {
        return this.scaledResolution;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }
}

