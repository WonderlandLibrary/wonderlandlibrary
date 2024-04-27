/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketEntityAction
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketEntityAction;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;

public final class CPacketEntityActionImpl
extends PacketImpl
implements ICPacketEntityAction {
    public CPacketEntityActionImpl(CPacketEntityAction cPacketEntityAction) {
        super((Packet)cPacketEntityAction);
    }
}

