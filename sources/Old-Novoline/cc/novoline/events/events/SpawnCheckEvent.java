package cc.novoline.events.events;

import net.minecraft.entity.Entity;

public class SpawnCheckEvent implements Event {

    private final Entity entity;

    public SpawnCheckEvent(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return this.entity;
    }

}
