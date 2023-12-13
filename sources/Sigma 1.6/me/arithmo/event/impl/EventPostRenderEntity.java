package me.arithmo.event.impl;

import me.arithmo.event.Event;
import net.minecraft.entity.Entity;

public class EventPostRenderEntity extends Event{
    private Entity ent;
    
    public EventPostRenderEntity(final Entity ent) {
        this.ent = ent;
    }
    
    public Entity getEntity() {
        return this.ent;
    }
}
