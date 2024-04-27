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
    public static final IPacket wrap(Packet packet) {
        boolean bl = false;
        return new PacketImpl(packet);
    }

    public static final Packet unwrap(IPacket iPacket) {
        boolean bl = false;
        return ((PacketImpl)iPacket).getWrapped();
    }
}

