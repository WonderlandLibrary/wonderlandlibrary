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
    public static final ISPacketAnimation wrap(SPacketAnimation sPacketAnimation) {
        boolean bl = false;
        return new SPacketAnimationImpl(sPacketAnimation);
    }

    public static final SPacketAnimation unwrap(ISPacketAnimation iSPacketAnimation) {
        boolean bl = false;
        return (SPacketAnimation)((SPacketAnimationImpl)iSPacketAnimation).getWrapped();
    }
}

