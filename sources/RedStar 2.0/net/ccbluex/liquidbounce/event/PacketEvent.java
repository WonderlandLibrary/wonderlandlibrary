package net.ccbluex.liquidbounce.event;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.event.CancellableEvent;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\u0000\n\n\b\u000020B\r0¢R0¢\b\n\u0000\b¨"}, d2={"Lnet/ccbluex/liquidbounce/event/PacketEvent;", "Lnet/ccbluex/liquidbounce/event/CancellableEvent;", "packet", "Lnet/ccbluex/liquidbounce/api/minecraft/network/IPacket;", "(Lnet/ccbluex/liquidbounce/api/minecraft/network/IPacket;)V", "getPacket", "()Lnet/ccbluex/liquidbounce/api/minecraft/network/IPacket;", "Pride"})
public final class PacketEvent
extends CancellableEvent {
    @NotNull
    private final IPacket packet;

    @NotNull
    public final IPacket getPacket() {
        return this.packet;
    }

    public PacketEvent(@NotNull IPacket packet) {
        Intrinsics.checkParameterIsNotNull(packet, "packet");
        this.packet = packet;
    }
}
