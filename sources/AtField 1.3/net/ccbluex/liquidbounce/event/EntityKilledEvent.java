/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.event;

import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.event.Event;

public final class EntityKilledEvent
extends Event {
    private final IEntityLivingBase entity;

    public final IEntityLivingBase getEntity() {
        return this.entity;
    }

    public EntityKilledEvent(IEntityLivingBase iEntityLivingBase) {
        this.entity = iEntityLivingBase;
    }
}

