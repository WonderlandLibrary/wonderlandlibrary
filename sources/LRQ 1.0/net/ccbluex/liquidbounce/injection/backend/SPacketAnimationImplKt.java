/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketAnimation
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketAnimation;
import net.ccbluex.liquidbounce.injection.backend.SPacketAnimationImpl;
import net.minecraft.network.play.server.SPacketAnimation;

public final class SPacketAnimationImplKt {
    public static final SPacketAnimation unwrap(ISPacketAnimation $this$unwrap) {
        int $i$f$unwrap = 0;
        return (SPacketAnimation)((SPacketAnimationImpl)$this$unwrap).getWrapped();
    }

    public static final ISPacketAnimation wrap(SPacketAnimation $this$wrap) {
        int $i$f$wrap = 0;
        return new SPacketAnimationImpl<SPacketAnimation>($this$wrap);
    }
}

