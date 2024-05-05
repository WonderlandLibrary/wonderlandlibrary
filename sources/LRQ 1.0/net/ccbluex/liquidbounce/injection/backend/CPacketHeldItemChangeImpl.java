/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketHeldItemChange;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;

public final class CPacketHeldItemChangeImpl<T extends CPacketHeldItemChange>
extends PacketImpl<T>
implements ICPacketHeldItemChange {
    @Override
    public int getSlotId() {
        return ((CPacketHeldItemChange)this.getWrapped()).func_149614_c();
    }

    public CPacketHeldItemChangeImpl(T wrapped) {
        super((Packet)wrapped);
    }
}

