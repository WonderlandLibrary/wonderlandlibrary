package me.nyan.flush.event.impl;

import me.nyan.flush.event.Event;
import net.minecraft.entity.Entity;

public class EventRenderEntity extends Event {
    private final Entity entity;

    public EventRenderEntity(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }
}
