/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketClientStatus
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketClientStatus;
import net.ccbluex.liquidbounce.injection.backend.CPacketClientStatusImpl;
import net.minecraft.network.play.client.CPacketClientStatus;

public final class CPacketClientStatusImplKt {
    public static final CPacketClientStatus unwrap(ICPacketClientStatus $this$unwrap) {
        int $i$f$unwrap = 0;
        return (CPacketClientStatus)((CPacketClientStatusImpl)$this$unwrap).getWrapped();
    }

    public static final ICPacketClientStatus wrap(CPacketClientStatus $this$wrap) {
        int $i$f$wrap = 0;
        return new CPacketClientStatusImpl<CPacketClientStatus>($this$wrap);
    }
}

