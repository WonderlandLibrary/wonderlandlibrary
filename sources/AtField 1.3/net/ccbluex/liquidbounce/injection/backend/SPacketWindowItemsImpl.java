/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.server.SPacketWindowItems
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketWindowItems;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketWindowItems;

public final class SPacketWindowItemsImpl
extends PacketImpl
implements ISPacketWindowItems {
    public SPacketWindowItemsImpl(SPacketWindowItems sPacketWindowItems) {
        super((Packet)sPacketWindowItems);
    }

    @Override
    public int getWindowId() {
        return ((SPacketWindowItems)this.getWrapped()).func_148911_c();
    }
}

