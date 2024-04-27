/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.event;

import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.event.Event;

public final class RenderEntityEvent
extends Event {
    private final IEntity entity;
    private final double x;
    private final double y;
    private final double z;
    private final float entityYaw;
    private final float partialTicks;

    public final IEntity getEntity() {
        return this.entity;
    }

    public final double getX() {
        return this.x;
    }

    public final double getY() {
        return this.y;
    }

    public final double getZ() {
        return this.z;
    }

    public final float getEntityYaw() {
        return this.entityYaw;
    }

    public final float getPartialTicks() {
        return this.partialTicks;
    }

    public RenderEntityEvent(IEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        this.entity = entity;
        this.x = x;
        this.y = y;
        this.z = z;
        this.entityYaw = entityYaw;
        this.partialTicks = partialTicks;
    }
}

