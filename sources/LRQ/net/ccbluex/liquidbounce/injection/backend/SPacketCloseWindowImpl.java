/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.server.SPacketCloseWindow
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ISPacketCloseWindow;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketCloseWindow;

public final class SPacketCloseWindowImpl<T extends SPacketCloseWindow>
extends PacketImpl<T>
implements ISPacketCloseWindow {
    @Override
    public int getWindowId() {
        return ((SPacketCloseWindow)this.getWrapped()).field_148896_a;
    }

    public SPacketCloseWindowImpl(T wrapped) {
        super((Packet)wrapped);
    }
}

