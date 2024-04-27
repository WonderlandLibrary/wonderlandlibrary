/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayerBlockPlacement;
import net.ccbluex.liquidbounce.injection.backend.CPacketPlayerBlockPlacementImpl;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;

public final class CPacketPlayerBlockPlacementImplKt {
    public static final ICPacketPlayerBlockPlacement wrap(CPacketPlayerTryUseItemOnBlock cPacketPlayerTryUseItemOnBlock) {
        boolean bl = false;
        return new CPacketPlayerBlockPlacementImpl(cPacketPlayerTryUseItemOnBlock);
    }

    public static final CPacketPlayerTryUseItemOnBlock unwrap(ICPacketPlayerBlockPlacement iCPacketPlayerBlockPlacement) {
        boolean bl = false;
        return (CPacketPlayerTryUseItemOnBlock)((CPacketPlayerBlockPlacementImpl)iCPacketPlayerBlockPlacement).getWrapped();
    }
}

