package vip.radium.event.impl.entity;

import net.minecraft.entity.Entity;
import vip.radium.event.CancellableEvent;

public final class SpawnParticleEntityEvent extends CancellableEvent {

    private final Entity entity;
    private final int type;
    private int multiplier;

    public SpawnParticleEntityEvent(Entity entity, int type, int multiplier) {
        this.entity = entity;
        this.type = type;
        this.multiplier = multiplier;
    }

    public Entity getEntity() {
        return entity;
    }

    public int getType() {
        return type;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }

}
