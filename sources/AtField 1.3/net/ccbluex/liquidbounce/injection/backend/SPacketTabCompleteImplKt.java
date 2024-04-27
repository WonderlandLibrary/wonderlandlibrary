/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketTabComplete
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketTabComplete;
import net.ccbluex.liquidbounce.injection.backend.SPacketTabCompleteImpl;
import net.minecraft.network.play.server.SPacketTabComplete;

public final class SPacketTabCompleteImplKt {
    public static final SPacketTabComplete unwrap(ISPacketTabComplete iSPacketTabComplete) {
        boolean bl = false;
        return (SPacketTabComplete)((SPacketTabCompleteImpl)iSPacketTabComplete).getWrapped();
    }

    public static final ISPacketTabComplete wrap(SPacketTabComplete sPacketTabComplete) {
        boolean bl = false;
        return new SPacketTabCompleteImpl(sPacketTabComplete);
    }
}

