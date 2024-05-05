package net.caffeinemc.phosphor.api.event.events;

import net.caffeinemc.phosphor.api.event.Cancellable;
import net.minecraft.entity.Entity;

@SuppressWarnings("all")
public class EntityMoveEvent extends Cancellable {
    private static final EntityMoveEvent INSTANCE = new EntityMoveEvent();

    public Entity entity;

    public static EntityMoveEvent get(Entity entity) {
        INSTANCE.setCancelled(false);
        INSTANCE.entity = entity;
        return INSTANCE;
    }
}
