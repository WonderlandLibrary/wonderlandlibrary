/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.ModelPlayer
 *  net.minecraft.entity.player.EntityPlayer
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.event;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.event.Event;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.player.EntityPlayer;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u000b"}, d2={"Lnet/dev/important/event/UpdateModelEvent;", "Lnet/dev/important/event/Event;", "player", "Lnet/minecraft/entity/player/EntityPlayer;", "model", "Lnet/minecraft/client/model/ModelPlayer;", "(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/client/model/ModelPlayer;)V", "getModel", "()Lnet/minecraft/client/model/ModelPlayer;", "getPlayer", "()Lnet/minecraft/entity/player/EntityPlayer;", "LiquidBounce"})
public final class UpdateModelEvent
extends Event {
    @NotNull
    private final EntityPlayer player;
    @NotNull
    private final ModelPlayer model;

    public UpdateModelEvent(@NotNull EntityPlayer player, @NotNull ModelPlayer model) {
        Intrinsics.checkNotNullParameter(player, "player");
        Intrinsics.checkNotNullParameter(model, "model");
        this.player = player;
        this.model = model;
    }

    @NotNull
    public final EntityPlayer getPlayer() {
        return this.player;
    }

    @NotNull
    public final ModelPlayer getModel() {
        return this.model;
    }
}

