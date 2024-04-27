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

public final class CPacketChatMessageImpl<T extends CPacketChatMessage>
extends PacketImpl<T>
implements ICPacketChatMessage {
    @Override
    public String getMessage() {
        return ((CPacketChatMessage)this.getWrapped()).field_149440_a;
    }

    @Override
    public void setMessage(String value) {
        ((CPacketChatMessage)this.getWrapped()).field_149440_a = value;
    }

    public CPacketChatMessageImpl(T wrapped) {
        super((Packet)wrapped);
    }
}

