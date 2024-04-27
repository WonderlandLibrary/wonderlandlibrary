/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import net.minecraft.network.Packet;

public final class PacketImplKt {
    public static final Packet<?> unwrap(IPacket $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((PacketImpl)$this$unwrap).getWrapped();
    }

    public static final IPacket wrap(Packet<?> $this$wrap) {
        int $i$f$wrap = 0;
        return new PacketImpl($this$wrap);
    }
}

