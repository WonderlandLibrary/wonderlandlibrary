/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.event;

import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.event.Event;
import org.jetbrains.annotations.Nullable;

public final class AttackEvent
extends Event {
    private final IEntity targetEntity;

    public final IEntity getTargetEntity() {
        return this.targetEntity;
    }

    public AttackEvent(@Nullable IEntity iEntity) {
        this.targetEntity = iEntity;
    }
}

