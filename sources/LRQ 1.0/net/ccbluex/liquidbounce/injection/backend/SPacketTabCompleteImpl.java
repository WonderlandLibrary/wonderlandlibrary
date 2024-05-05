/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.server.SPacketTabComplete
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketTabComplete;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketTabComplete;

public final class SPacketTabCompleteImpl<T extends SPacketTabComplete>
extends PacketImpl<T>
implements ISPacketTabComplete {
    @Override
    public String[] getCompletions() {
        return ((SPacketTabComplete)this.getWrapped()).func_149630_c();
    }

    public SPacketTabCompleteImpl(T wrapped) {
        super((Packet)wrapped);
    }
}

