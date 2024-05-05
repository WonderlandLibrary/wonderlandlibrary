/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.handshake.client.C00Handshake
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.handshake.client.ICPacketHandshake;
import net.ccbluex.liquidbounce.injection.backend.CPacketHandshakeImpl;
import net.minecraft.network.handshake.client.C00Handshake;

public final class CPacketHandshakeImplKt {
    public static final C00Handshake unwrap(ICPacketHandshake $this$unwrap) {
        int $i$f$unwrap = 0;
        return (C00Handshake)((CPacketHandshakeImpl)$this$unwrap).getWrapped();
    }

    public static final ICPacketHandshake wrap(C00Handshake $this$wrap) {
        int $i$f$wrap = 0;
        return new CPacketHandshakeImpl<C00Handshake>($this$wrap);
    }
}

