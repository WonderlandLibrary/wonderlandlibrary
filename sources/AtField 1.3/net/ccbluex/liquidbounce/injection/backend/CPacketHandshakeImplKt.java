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
    public static final ICPacketHandshake wrap(C00Handshake c00Handshake) {
        boolean bl = false;
        return new CPacketHandshakeImpl(c00Handshake);
    }

    public static final C00Handshake unwrap(ICPacketHandshake iCPacketHandshake) {
        boolean bl = false;
        return (C00Handshake)((CPacketHandshakeImpl)iCPacketHandshake).getWrapped();
    }
}

