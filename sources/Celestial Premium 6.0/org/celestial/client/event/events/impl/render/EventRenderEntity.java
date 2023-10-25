/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.event.events.impl.render;

import net.minecraft.entity.Entity;
import org.celestial.client.event.events.callables.EventCancellable;

public class EventRenderEntity
extends EventCancellable {
    private final Entity entity;

    public EventRenderEntity(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return this.entity;
    }
}

