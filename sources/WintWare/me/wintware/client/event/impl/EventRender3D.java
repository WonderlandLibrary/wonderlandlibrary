/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.event.impl;

import me.wintware.client.event.callables.EventCancellable;

public class EventRender3D
extends EventCancellable {
    public float particlTicks;

    public EventRender3D(float particlTicks) {
        this.particlTicks = particlTicks;
    }

    public float getParticlTicks() {
        return this.particlTicks;
    }

    public void setParticlTicks(float particlTicks) {
        this.particlTicks = particlTicks;
    }
}

