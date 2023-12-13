package de.tired.base.event.events;

import de.tired.base.event.Event;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class EventRenderEntity extends Event {
    Entity entity;


    public EventRenderEntity(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(EntityLivingBase entity) {
        this.entity = entity;
    }


}