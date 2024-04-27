package net.ccbluex.liquidbounce.event;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.event.Event;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\u0000\n\n\b\u000020B\b0¢R0¢\b\n\u0000\b¨"}, d2={"Lnet/ccbluex/liquidbounce/event/AttackEvent;", "Lnet/ccbluex/liquidbounce/event/Event;", "targetEntity", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "(Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;)V", "getTargetEntity", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "Pride"})
public final class AttackEvent
extends Event {
    @Nullable
    private final IEntity targetEntity;

    @Nullable
    public final IEntity getTargetEntity() {
        return this.targetEntity;
    }

    public AttackEvent(@Nullable IEntity targetEntity) {
        this.targetEntity = targetEntity;
    }
}
