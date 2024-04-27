/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.PacketBuffer
 *  net.minecraft.network.play.client.CPacketCustomPayload
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketCustomPayload;
import net.ccbluex.liquidbounce.api.network.IPacketBuffer;
import net.ccbluex.liquidbounce.injection.backend.PacketBufferImpl;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;

public final class CPacketCustomPayloadImpl
extends PacketImpl
implements ICPacketCustomPayload {
    @Override
    public void setData(IPacketBuffer iPacketBuffer) {
        PacketBuffer packetBuffer;
        IPacketBuffer iPacketBuffer2 = iPacketBuffer;
        CPacketCustomPayload cPacketCustomPayload = (CPacketCustomPayload)this.getWrapped();
        boolean bl = false;
        cPacketCustomPayload.field_149561_c = packetBuffer = ((PacketBufferImpl)iPacketBuffer2).getWrapped();
    }

    @Override
    public IPacketBuffer getData() {
        PacketBuffer packetBuffer = ((CPacketCustomPayload)this.getWrapped()).field_149561_c;
        boolean bl = false;
        return new PacketBufferImpl(packetBuffer);
    }

    @Override
    public String getChannelName() {
        return ((CPacketCustomPayload)this.getWrapped()).func_149559_c();
    }

    public CPacketCustomPayloadImpl(CPacketCustomPayload cPacketCustomPayload) {
        super((Packet)cPacketCustomPayload);
    }
}

