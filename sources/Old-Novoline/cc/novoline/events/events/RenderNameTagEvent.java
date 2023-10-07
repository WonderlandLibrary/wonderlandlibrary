package cc.novoline.events.events;

import cc.novoline.events.events.callables.CancellableEvent;
import net.minecraft.entity.Entity;

public class RenderNameTagEvent extends CancellableEvent {

    final Entity entity;

    public RenderNameTagEvent(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return this.entity;
    }

}
