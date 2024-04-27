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
    public static final CPacketPlayerDigging unwrap(ICPacketPlayerDigging iCPacketPlayerDigging) {
        boolean bl = false;
        return (CPacketPlayerDigging)((CPacketPlayerDiggingImpl)iCPacketPlayerDigging).getWrapped();
    }

    public static final ICPacketPlayerDigging wrap(CPacketPlayerDigging cPacketPlayerDigging) {
        boolean bl = false;
        return new CPacketPlayerDiggingImpl(cPacketPlayerDigging);
    }
}

