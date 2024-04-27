/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  org.jetbrains.annotations.Nullable
 */
package net.dev.important.event;

import kotlin.Metadata;
import net.dev.important.event.Event;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u000f\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\u0004R\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2={"Lnet/dev/important/event/AttackEvent;", "Lnet/dev/important/event/Event;", "targetEntity", "Lnet/minecraft/entity/Entity;", "(Lnet/minecraft/entity/Entity;)V", "getTargetEntity", "()Lnet/minecraft/entity/Entity;", "LiquidBounce"})
public final class AttackEvent
extends Event {
    @Nullable
    private final Entity targetEntity;

    public AttackEvent(@Nullable Entity targetEntity) {
        this.targetEntity = targetEntity;
    }

    @Nullable
    public final Entity getTargetEntity() {
        return this.targetEntity;
    }
}

