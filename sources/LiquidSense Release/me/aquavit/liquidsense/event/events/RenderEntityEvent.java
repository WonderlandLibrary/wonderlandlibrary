package me.aquavit.liquidsense.event.events;

import me.aquavit.liquidsense.event.Event;
import net.minecraft.entity.Entity;

public class RenderEntityEvent extends Event {
    private Entity entity;
    private double x;
    private double y;
    private double z;
    private float entityYaw;
    private float partialTicks;

    public RenderEntityEvent(Entity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        this.entity = entity;
        this.x = x;
        this.y = y;
        this.z = z;
        this.entityYaw = entityYaw;
        this.partialTicks = partialTicks;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public float getEntityYaw() {
        return this.entityYaw;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }
}

