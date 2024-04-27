/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.event;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.event.Event;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\f\u0018\u00002\u00020\u0001B5\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\u0005\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\t\u00a2\u0006\u0002\u0010\u000bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\n\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000fR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0012R\u0011\u0010\u0007\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0012\u00a8\u0006\u0015"}, d2={"Lnet/dev/important/event/RenderEntityEvent;", "Lnet/dev/important/event/Event;", "entity", "Lnet/minecraft/entity/Entity;", "x", "", "y", "z", "entityYaw", "", "partialTicks", "(Lnet/minecraft/entity/Entity;DDDFF)V", "getEntity", "()Lnet/minecraft/entity/Entity;", "getEntityYaw", "()F", "getPartialTicks", "getX", "()D", "getY", "getZ", "LiquidBounce"})
public final class RenderEntityEvent
extends Event {
    @NotNull
    private final Entity entity;
    private final double x;
    private final double y;
    private final double z;
    private final float entityYaw;
    private final float partialTicks;

    public RenderEntityEvent(@NotNull Entity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        Intrinsics.checkNotNullParameter(entity, "entity");
        this.entity = entity;
        this.x = x;
        this.y = y;
        this.z = z;
        this.entityYaw = entityYaw;
        this.partialTicks = partialTicks;
    }

    @NotNull
    public final Entity getEntity() {
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
}

