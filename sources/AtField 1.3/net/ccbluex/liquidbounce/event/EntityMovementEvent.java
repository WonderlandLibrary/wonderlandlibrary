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

public final class EntityMovementEvent
extends Event {
    private final IEntity movedEntity;

    public final EntityMovementEvent copy(IEntity iEntity) {
        return new EntityMovementEvent(iEntity);
    }

    public EntityMovementEvent(IEntity iEntity) {
        this.movedEntity = iEntity;
    }

    public final IEntity component1() {
        return this.movedEntity;
    }

    public int hashCode() {
        IEntity iEntity = this.movedEntity;
        return iEntity != null ? iEntity.hashCode() : 0;
    }

    public String toString() {
        return "EntityMovementEvent(movedEntity=" + this.movedEntity + ")";
    }

    public static EntityMovementEvent copy$default(EntityMovementEvent entityMovementEvent, IEntity iEntity, int n, Object object) {
        if ((n & 1) != 0) {
            iEntity = entityMovementEvent.movedEntity;
        }
        return entityMovementEvent.copy(iEntity);
    }

    public final IEntity getMovedEntity() {
        return this.movedEntity;
    }

    public boolean equals(@Nullable Object object) {
        block3: {
            block2: {
                if (this == object) break block2;
                if (!(object instanceof EntityMovementEvent)) break block3;
                EntityMovementEvent entityMovementEvent = (EntityMovementEvent)object;
                if (!this.movedEntity.equals(entityMovementEvent.movedEntity)) break block3;
            }
            return true;
        }
        return false;
    }
}

