/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.event;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0010H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0011"}, d2={"Lnet/ccbluex/liquidbounce/event/EntityMovementEvent;", "Lnet/ccbluex/liquidbounce/event/Event;", "movedEntity", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "(Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;)V", "getMovedEntity", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "LiKingSense"})
public final class EntityMovementEvent
extends Event {
    @NotNull
    private final IEntity movedEntity;

    @NotNull
    public final IEntity getMovedEntity() {
        return this.movedEntity;
    }

    public EntityMovementEvent(@NotNull IEntity movedEntity) {
        Intrinsics.checkParameterIsNotNull((Object)movedEntity, (String)"movedEntity");
        this.movedEntity = movedEntity;
    }

    @NotNull
    public final IEntity component1() {
        return this.movedEntity;
    }

    @NotNull
    public final EntityMovementEvent copy(@NotNull IEntity movedEntity) {
        Intrinsics.checkParameterIsNotNull((Object)movedEntity, (String)"movedEntity");
        return new EntityMovementEvent(movedEntity);
    }

    public static /* synthetic */ EntityMovementEvent copy$default(EntityMovementEvent entityMovementEvent, IEntity iEntity, int n, Object object) {
        if ((n & 1) != 0) {
            iEntity = entityMovementEvent.movedEntity;
        }
        return entityMovementEvent.copy(iEntity);
    }

    @NotNull
    public String toString() {
        return "EntityMovementEvent(movedEntity=" + this.movedEntity + ")";
    }

    public int hashCode() {
        IEntity iEntity = this.movedEntity;
        return iEntity != null ? iEntity.hashCode() : 0;
    }

    public boolean equals(@Nullable Object object) {
        block3: {
            block2: {
                if (this == object) break block2;
                if (!(object instanceof EntityMovementEvent)) break block3;
                EntityMovementEvent entityMovementEvent = (EntityMovementEvent)object;
                if (!Intrinsics.areEqual((Object)this.movedEntity, (Object)entityMovementEvent.movedEntity)) break block3;
            }
            return true;
        }
        return false;
    }
}

