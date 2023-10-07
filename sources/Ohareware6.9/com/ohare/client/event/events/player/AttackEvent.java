package com.ohare.client.event.events.player;

import com.ohare.client.event.Event;
import net.minecraft.entity.Entity;

/**
 * @author Xen for OhareWare
 * @since 8/4/2019
 **/
public class AttackEvent extends Event {
    private Entity entity;

    public AttackEvent(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }
}
