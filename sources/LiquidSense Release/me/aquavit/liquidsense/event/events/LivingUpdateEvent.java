package me.aquavit.liquidsense.event.events;

import me.aquavit.liquidsense.event.CancellableEvent;
import net.minecraft.entity.Entity;

public class LivingUpdateEvent extends CancellableEvent {
    private Entity entity;
    public LivingUpdateEvent(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return this.entity;
    }
}
