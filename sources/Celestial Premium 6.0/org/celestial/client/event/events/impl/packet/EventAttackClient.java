/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.event.events.impl.packet;

import net.minecraft.entity.Entity;
import org.celestial.client.event.events.callables.EventCancellable;

public class EventAttackClient
extends EventCancellable {
    private final Entity entity;
    private final boolean preAttack;

    public EventAttackClient(Entity targetEntity, boolean preAttack) {
        this.entity = targetEntity;
        this.preAttack = preAttack;
    }

    public Entity getTargetEntity() {
        return this.entity;
    }

    public boolean isPreAttack() {
        return this.preAttack;
    }

    public boolean isPostAttack() {
        return !this.preAttack;
    }
}

