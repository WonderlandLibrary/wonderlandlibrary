/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.server.SPacketResourcePackSend
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketResourcePackSend;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketResourcePackSend;

public final class SPacketResourcePackSendImpl
extends PacketImpl
implements ISPacketResourcePackSend {
    @Override
    public String getUrl() {
        return ((SPacketResourcePackSend)this.getWrapped()).func_179783_a();
    }

    @Override
    public String getHash() {
        return ((SPacketResourcePackSend)this.getWrapped()).func_179784_b();
    }

    public SPacketResourcePackSendImpl(SPacketResourcePackSend sPacketResourcePackSend) {
        super((Packet)sPacketResourcePackSend);
    }
}

