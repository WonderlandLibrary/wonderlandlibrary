/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketPlayer$PositionRotation
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayerPosLook;
import net.ccbluex.liquidbounce.injection.backend.CPacketPlayerPosLookImpl;
import net.minecraft.network.play.client.CPacketPlayer;

public final class CPacketPlayerPosLookImplKt {
    public static final CPacketPlayer.PositionRotation unwrap(ICPacketPlayerPosLook $this$unwrap) {
        int $i$f$unwrap = 0;
        return (CPacketPlayer.PositionRotation)((CPacketPlayerPosLookImpl)$this$unwrap).getWrapped();
    }

    public static final ICPacketPlayerPosLook wrap(CPacketPlayer.PositionRotation $this$wrap) {
        int $i$f$wrap = 0;
        return new CPacketPlayerPosLookImpl<CPacketPlayer.PositionRotation>($this$wrap);
    }
}

