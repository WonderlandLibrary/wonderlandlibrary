package com.initial.events.impl;

import com.initial.events.*;
import net.minecraft.entity.*;

public class EventAttack extends Event
{
    public Entity entity;
    
    public EventAttack(final Entity entity) {
        this.entity = entity;
    }
    
    public Entity getEntity() {
        return this.entity;
    }
    
    public void setEntity(final Entity entity) {
        this.entity = entity;
    }
}
