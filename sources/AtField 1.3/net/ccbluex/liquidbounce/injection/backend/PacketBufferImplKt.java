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
    public static final IPacketBuffer wrap(PacketBuffer packetBuffer) {
        boolean bl = false;
        return new PacketBufferImpl(packetBuffer);
    }

    public static final PacketBuffer unwrap(IPacketBuffer iPacketBuffer) {
        boolean bl = false;
        return ((PacketBufferImpl)iPacketBuffer).getWrapped();
    }
}

