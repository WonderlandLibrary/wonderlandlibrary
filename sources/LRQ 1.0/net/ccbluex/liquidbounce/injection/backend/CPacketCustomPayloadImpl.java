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

public final class CPacketCustomPayloadImpl<T extends CPacketCustomPayload>
extends PacketImpl<T>
implements ICPacketCustomPayload {
    @Override
    public IPacketBuffer getData() {
        PacketBuffer $this$wrap$iv = ((CPacketCustomPayload)this.getWrapped()).field_149561_c;
        boolean $i$f$wrap = false;
        return new PacketBufferImpl($this$wrap$iv);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void setData(IPacketBuffer value) {
        void $this$unwrap$iv;
        PacketBuffer packetBuffer;
        IPacketBuffer iPacketBuffer = value;
        CPacketCustomPayload cPacketCustomPayload = (CPacketCustomPayload)this.getWrapped();
        boolean $i$f$unwrap = false;
        cPacketCustomPayload.field_149561_c = packetBuffer = ((PacketBufferImpl)$this$unwrap$iv).getWrapped();
    }

    @Override
    public String getChannelName() {
        return ((CPacketCustomPayload)this.getWrapped()).func_149559_c();
    }

    public CPacketCustomPayloadImpl(T wrapped) {
        super((Packet)wrapped);
    }
}

