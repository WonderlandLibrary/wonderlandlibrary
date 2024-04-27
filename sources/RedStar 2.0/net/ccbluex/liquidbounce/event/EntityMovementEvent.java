package net.ccbluex.liquidbounce.event;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000*\n\n\n\u0000\n\n\b\n\n\u0000\n\u0000\n\u0000\n\b\n\u0000\n\n\u0000\b\b\u000020B\r0¢J\t0HÆJ\b0\u00002\b\b0HÆJ\t0\n2\b0\fHÖJ\t\r0HÖJ\t0HÖR0¢\b\n\u0000\b¨"}, d2={"Lnet/ccbluex/liquidbounce/event/EntityMovementEvent;", "Lnet/ccbluex/liquidbounce/event/Event;", "movedEntity", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "(Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;)V", "getMovedEntity", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "Pride"})
public final class EntityMovementEvent
extends Event {
    @NotNull
    private final IEntity movedEntity;

    @NotNull
    public final IEntity getMovedEntity() {
        return this.movedEntity;
    }

    public EntityMovementEvent(@NotNull IEntity movedEntity) {
        Intrinsics.checkParameterIsNotNull(movedEntity, "movedEntity");
        this.movedEntity = movedEntity;
    }

    @NotNull
    public final IEntity component1() {
        return this.movedEntity;
    }

    @NotNull
    public final EntityMovementEvent copy(@NotNull IEntity movedEntity) {
        Intrinsics.checkParameterIsNotNull(movedEntity, "movedEntity");
        return new EntityMovementEvent(movedEntity);
    }

    public static EntityMovementEvent copy$default(EntityMovementEvent entityMovementEvent, IEntity iEntity, int n, Object object) {
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
                if (!Intrinsics.areEqual(this.movedEntity, entityMovementEvent.movedEntity)) break block3;
            }
            return true;
        }
        return false;
    }
}
