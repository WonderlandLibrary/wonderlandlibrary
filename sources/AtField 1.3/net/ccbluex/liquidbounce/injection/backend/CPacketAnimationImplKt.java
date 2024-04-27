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
    public static final ICPacketAnimation wrap(CPacketAnimation cPacketAnimation) {
        boolean bl = false;
        return new CPacketAnimationImpl(cPacketAnimation);
    }

    public static final CPacketAnimation unwrap(ICPacketAnimation iCPacketAnimation) {
        boolean bl = false;
        return (CPacketAnimation)((CPacketAnimationImpl)iCPacketAnimation).getWrapped();
    }
}

