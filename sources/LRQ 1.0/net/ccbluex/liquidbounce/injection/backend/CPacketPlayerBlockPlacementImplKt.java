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
    public static final CPacketPlayerTryUseItemOnBlock unwrap(ICPacketPlayerBlockPlacement $this$unwrap) {
        int $i$f$unwrap = 0;
        return (CPacketPlayerTryUseItemOnBlock)((CPacketPlayerBlockPlacementImpl)$this$unwrap).getWrapped();
    }

    public static final ICPacketPlayerBlockPlacement wrap(CPacketPlayerTryUseItemOnBlock $this$wrap) {
        int $i$f$wrap = 0;
        return new CPacketPlayerBlockPlacementImpl<CPacketPlayerTryUseItemOnBlock>($this$wrap);
    }
}

