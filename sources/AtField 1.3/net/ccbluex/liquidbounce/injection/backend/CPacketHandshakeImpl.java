/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.EnumConnectionState
 *  net.minecraft.network.Packet
 *  net.minecraft.network.handshake.client.C00Handshake
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.IEnumConnectionState;
import net.ccbluex.liquidbounce.api.minecraft.network.handshake.client.ICPacketHandshake;
import net.ccbluex.liquidbounce.injection.backend.EnumConnectionStateImpl;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.Packet;
import net.minecraft.network.handshake.client.C00Handshake;

public final class CPacketHandshakeImpl
extends PacketImpl
implements ICPacketHandshake {
    @Override
    public IEnumConnectionState getRequestedState() {
        EnumConnectionState enumConnectionState = ((C00Handshake)this.getWrapped()).func_149594_c();
        boolean bl = false;
        return new EnumConnectionStateImpl(enumConnectionState);
    }

    @Override
    public String getIp() {
        return ((C00Handshake)this.getWrapped()).field_149598_b;
    }

    public CPacketHandshakeImpl(C00Handshake c00Handshake) {
        super((Packet)c00Handshake);
    }

    @Override
    public void setIp(String string) {
        ((C00Handshake)this.getWrapped()).field_149598_b = string;
    }

    @Override
    public int getPort() {
        return ((C00Handshake)this.getWrapped()).field_149599_c;
    }
}

