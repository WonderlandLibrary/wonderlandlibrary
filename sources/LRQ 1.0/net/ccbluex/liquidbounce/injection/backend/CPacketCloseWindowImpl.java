/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketCloseWindow
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketCloseWindow;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketCloseWindow;

public final class CPacketCloseWindowImpl<T extends CPacketCloseWindow>
extends PacketImpl<T>
implements ICPacketCloseWindow {
    public CPacketCloseWindowImpl(T wrapped) {
        super((Packet)wrapped);
    }
}

