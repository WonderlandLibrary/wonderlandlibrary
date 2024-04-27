package net.ccbluex.liquidbounce.event;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.event.Event;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000 \n\n\n\u0000\n\n\u0000\n\n\b\n\n\b\f\u000020B50000\b0\t\n0\t¢R0¢\b\n\u0000\b\f\rR\b0\t¢\b\n\u0000\bR\n0\t¢\b\n\u0000\bR0¢\b\n\u0000\bR0¢\b\n\u0000\bR0¢\b\n\u0000\b¨"}, d2={"Lnet/ccbluex/liquidbounce/event/RenderEntityEvent;", "Lnet/ccbluex/liquidbounce/event/Event;", "entity", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "x", "", "y", "z", "entityYaw", "", "partialTicks", "(Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;DDDFF)V", "getEntity", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "getEntityYaw", "()F", "getPartialTicks", "getX", "()D", "getY", "getZ", "Pride"})
public final class RenderEntityEvent
extends Event {
    @NotNull
    private final IEntity entity;
    private final double x;
    private final double y;
    private final double z;
    private final float entityYaw;
    private final float partialTicks;

    @NotNull
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

    public RenderEntityEvent(@NotNull IEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        Intrinsics.checkParameterIsNotNull(entity, "entity");
        this.entity = entity;
        this.x = x;
        this.y = y;
        this.z = z;
        this.entityYaw = entityYaw;
        this.partialTicks = partialTicks;
    }
}
