/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.PacketBuffer
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.network.IPacketBuffer;
import net.ccbluex.liquidbounce.injection.backend.PacketBufferImpl;
import net.minecraft.network.PacketBuffer;

public final class PacketBufferImplKt {
    public static final PacketBuffer unwrap(IPacketBuffer $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((PacketBufferImpl)$this$unwrap).getWrapped();
    }

    public static final IPacketBuffer wrap(PacketBuffer $this$wrap) {
        int $i$f$wrap = 0;
        return new PacketBufferImpl($this$wrap);
    }
}

