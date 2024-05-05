/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketPlayer
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayer;
import net.ccbluex.liquidbounce.injection.backend.CPacketPlayerImpl;
import net.minecraft.network.play.client.CPacketPlayer;

public final class CPacketPlayerImplKt {
    public static final CPacketPlayer unwrap(ICPacketPlayer $this$unwrap) {
        int $i$f$unwrap = 0;
        return (CPacketPlayer)((CPacketPlayerImpl)$this$unwrap).getWrapped();
    }

    public static final ICPacketPlayer wrap(CPacketPlayer $this$wrap) {
        int $i$f$wrap = 0;
        return new CPacketPlayerImpl<CPacketPlayer>($this$wrap);
    }
}

