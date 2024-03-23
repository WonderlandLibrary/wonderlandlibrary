package dev.zprestige.prestige.client.event.impl;

import dev.zprestige.prestige.client.event.Event;
import net.minecraft.entity.Entity;

public class RenderHitboxEvent extends Event {
    public Entity entity;

    public RenderHitboxEvent(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return this.entity;
    }
}
