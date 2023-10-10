package ravenweave.client.event.impl;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.weavemc.loader.api.event.Event;

public class EntityJoinWorldEvent extends Event {
    public Entity entity;
    public World world;
    public EntityJoinWorldEvent(Entity entity, World worldObj) {
        this.entity = entity;
        this.world = worldObj;
    }
}
