package tech.atani.client.listener.event.minecraft.render.player;

import net.minecraft.entity.Entity;
import tech.atani.client.listener.event.Event;

public class EntityRendererEvent extends Event {
    Entity entity;

    public EntityRendererEvent(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }
}