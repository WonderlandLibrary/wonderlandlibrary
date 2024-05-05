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

public final class CPacketHandshakeImpl<T extends C00Handshake>
extends PacketImpl<T>
implements ICPacketHandshake {
    @Override
    public int getPort() {
        return ((C00Handshake)this.getWrapped()).field_149599_c;
    }

    @Override
    public String getIp() {
        return ((C00Handshake)this.getWrapped()).field_149598_b;
    }

    @Override
    public void setIp(String value) {
        ((C00Handshake)this.getWrapped()).field_149598_b = value;
    }

    @Override
    public IEnumConnectionState getRequestedState() {
        EnumConnectionState $this$wrap$iv = ((C00Handshake)this.getWrapped()).func_149594_c();
        boolean $i$f$wrap = false;
        return new EnumConnectionStateImpl($this$wrap$iv);
    }

    public CPacketHandshakeImpl(T wrapped) {
        super((Packet)wrapped);
    }
}

