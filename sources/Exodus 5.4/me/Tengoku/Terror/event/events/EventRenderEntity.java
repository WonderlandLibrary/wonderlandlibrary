/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.event.events;

import me.Tengoku.Terror.event.Event;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class EventRenderEntity
extends Event<EventRenderEntity> {
    Entity entity;

    public Entity getEntity() {
        return this.entity;
    }

    public void setEntity(EntityLivingBase entityLivingBase) {
        this.entity = entityLivingBase;
    }

    public EventRenderEntity(Entity entity) {
        this.entity = entity;
        this.setEntity((EntityLivingBase)entity);
    }
}

