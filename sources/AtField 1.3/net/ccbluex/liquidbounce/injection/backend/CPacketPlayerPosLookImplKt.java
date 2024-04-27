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
    public static final ICPacketPlayerPosLook wrap(CPacketPlayer.PositionRotation positionRotation) {
        boolean bl = false;
        return new CPacketPlayerPosLookImpl(positionRotation);
    }

    public static final CPacketPlayer.PositionRotation unwrap(ICPacketPlayerPosLook iCPacketPlayerPosLook) {
        boolean bl = false;
        return (CPacketPlayer.PositionRotation)((CPacketPlayerPosLookImpl)iCPacketPlayerPosLook).getWrapped();
    }
}

