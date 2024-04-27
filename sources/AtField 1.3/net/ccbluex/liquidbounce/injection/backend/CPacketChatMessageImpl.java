/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketChatMessage
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketChatMessage;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;

public final class CPacketChatMessageImpl
extends PacketImpl
implements ICPacketChatMessage {
    @Override
    public void setMessage(String string) {
        ((CPacketChatMessage)this.getWrapped()).field_149440_a = string;
    }

    public CPacketChatMessageImpl(CPacketChatMessage cPacketChatMessage) {
        super((Packet)cPacketChatMessage);
    }

    @Override
    public String getMessage() {
        return ((CPacketChatMessage)this.getWrapped()).field_149440_a;
    }
}

