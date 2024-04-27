/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.dev.important.event;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.event.Event;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0010H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0011"}, d2={"Lnet/dev/important/event/EntityMovementEvent;", "Lnet/dev/important/event/Event;", "movedEntity", "Lnet/minecraft/entity/Entity;", "(Lnet/minecraft/entity/Entity;)V", "getMovedEntity", "()Lnet/minecraft/entity/Entity;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "LiquidBounce"})
public final class EntityMovementEvent
extends Event {
    @NotNull
    private final Entity movedEntity;

    public EntityMovementEvent(@NotNull Entity movedEntity) {
        Intrinsics.checkNotNullParameter(movedEntity, "movedEntity");
        this.movedEntity = movedEntity;
    }

    @NotNull
    public final Entity getMovedEntity() {
        return this.movedEntity;
    }

    @NotNull
    public final Entity component1() {
        return this.movedEntity;
    }

    @NotNull
    public final EntityMovementEvent copy(@NotNull Entity movedEntity) {
        Intrinsics.checkNotNullParameter(movedEntity, "movedEntity");
        return new EntityMovementEvent(movedEntity);
    }

    public static /* synthetic */ EntityMovementEvent copy$default(EntityMovementEvent entityMovementEvent, Entity entity, int n, Object object) {
        if ((n & 1) != 0) {
            entity = entityMovementEvent.movedEntity;
        }
        return entityMovementEvent.copy(entity);
    }

    @NotNull
    public String toString() {
        return "EntityMovementEvent(movedEntity=" + this.movedEntity + ')';
    }

    public int hashCode() {
        return this.movedEntity.hashCode();
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof EntityMovementEvent)) {
            return false;
        }
        EntityMovementEvent entityMovementEvent = (EntityMovementEvent)other;
        return Intrinsics.areEqual(this.movedEntity, entityMovementEvent.movedEntity);
    }
}

