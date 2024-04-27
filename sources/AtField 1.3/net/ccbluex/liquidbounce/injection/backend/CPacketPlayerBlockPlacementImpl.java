/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayerBlockPlacement;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;

public final class CPacketPlayerBlockPlacementImpl
extends PacketImpl
implements ICPacketPlayerBlockPlacement {
    public CPacketPlayerBlockPlacementImpl(CPacketPlayerTryUseItemOnBlock cPacketPlayerTryUseItemOnBlock) {
        super((Packet)cPacketPlayerTryUseItemOnBlock);
    }
}

