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
    private final double z;
    private final double y;
    private final float entityYaw;
    private final float partialTicks;

    public RenderEntityEvent(IEntity iEntity, double d, double d2, double d3, float f, float f2) {
        this.entity = iEntity;
        this.x = d;
        this.y = d2;
        this.z = d3;
        this.entityYaw = f;
        this.partialTicks = f2;
    }

    public final IEntity getEntity() {
        return this.entity;
    }

    public final double getZ() {
        return this.z;
    }

    public final float getPartialTicks() {
        return this.partialTicks;
    }

    public final double getY() {
        return this.y;
    }

    public final double getX() {
        return this.x;
    }

    public final float getEntityYaw() {
        return this.entityYaw;
    }
}

