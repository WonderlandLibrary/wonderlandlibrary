package me.crimp.claudius.event.events;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.Event;

public class EntitySpawnEvent extends Event {
    private final Entity entity;
    private final Type type;

    public EntitySpawnEvent(Entity entity, Type type) {
        this.entity = entity;
        this.type = type;
    }

    public Entity getEntity() {
        return entity;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        Spawn, Despawn
    }
}