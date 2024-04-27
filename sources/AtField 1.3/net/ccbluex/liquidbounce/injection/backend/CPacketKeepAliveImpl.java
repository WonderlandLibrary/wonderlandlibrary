/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketKeepAlive
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketKeepAlive;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketKeepAlive;

public final class CPacketKeepAliveImpl
extends PacketImpl
implements ICPacketKeepAlive {
    public CPacketKeepAliveImpl(CPacketKeepAlive cPacketKeepAlive) {
        super((Packet)cPacketKeepAlive);
    }
}

