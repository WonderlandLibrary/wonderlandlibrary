/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.event;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.event.CancellableEvent;
import net.minecraft.network.Packet;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0011\u0012\n\u0010\u0002\u001a\u0006\u0012\u0002\b\u00030\u0003\u00a2\u0006\u0002\u0010\u0004R\u0015\u0010\u0002\u001a\u0006\u0012\u0002\b\u00030\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2={"Lnet/dev/important/event/PacketEvent;", "Lnet/dev/important/event/CancellableEvent;", "packet", "Lnet/minecraft/network/Packet;", "(Lnet/minecraft/network/Packet;)V", "getPacket", "()Lnet/minecraft/network/Packet;", "LiquidBounce"})
public final class PacketEvent
extends CancellableEvent {
    @NotNull
    private final Packet<?> packet;

    public PacketEvent(@NotNull Packet<?> packet) {
        Intrinsics.checkNotNullParameter(packet, "packet");
        this.packet = packet;
    }

    @NotNull
    public final Packet<?> getPacket() {
        return this.packet;
    }
}

