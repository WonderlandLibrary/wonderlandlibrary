/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayerDigging;
import net.ccbluex.liquidbounce.injection.backend.CPacketPlayerDiggingImpl;
import net.minecraft.network.play.client.CPacketPlayerDigging;

public final class CPacketPlayerDiggingImplKt {
    public static final CPacketPlayerDigging unwrap(ICPacketPlayerDigging $this$unwrap) {
        int $i$f$unwrap = 0;
        return (CPacketPlayerDigging)((CPacketPlayerDiggingImpl)$this$unwrap).getWrapped();
    }

    public static final ICPacketPlayerDigging wrap(CPacketPlayerDigging $this$wrap) {
        int $i$f$wrap = 0;
        return new CPacketPlayerDiggingImpl<CPacketPlayerDigging>($this$wrap);
    }
}

