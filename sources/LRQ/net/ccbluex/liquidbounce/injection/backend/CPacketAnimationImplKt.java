/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.CPacketAnimation
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketAnimation;
import net.ccbluex.liquidbounce.injection.backend.CPacketAnimationImpl;
import net.minecraft.network.play.client.CPacketAnimation;

public final class CPacketAnimationImplKt {
    public static final CPacketAnimation unwrap(ICPacketAnimation $this$unwrap) {
        int $i$f$unwrap = 0;
        return (CPacketAnimation)((CPacketAnimationImpl)$this$unwrap).getWrapped();
    }

    public static final ICPacketAnimation wrap(CPacketAnimation $this$wrap) {
        int $i$f$wrap = 0;
        return new CPacketAnimationImpl<CPacketAnimation>($this$wrap);
    }
}

