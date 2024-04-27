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
    public static final ICPacketPlayer wrap(CPacketPlayer cPacketPlayer) {
        boolean bl = false;
        return new CPacketPlayerImpl(cPacketPlayer);
    }

    public static final CPacketPlayer unwrap(ICPacketPlayer iCPacketPlayer) {
        boolean bl = false;
        return (CPacketPlayer)((CPacketPlayerImpl)iCPacketPlayer).getWrapped();
    }
}

