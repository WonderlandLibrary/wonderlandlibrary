/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketPlayerPosLook
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketPosLook;
import net.ccbluex.liquidbounce.injection.backend.SPacketPosLookImpl;
import net.minecraft.network.play.server.SPacketPlayerPosLook;

public final class SPacketPosLookImplKt {
    public static final ISPacketPosLook wrap(SPacketPlayerPosLook sPacketPlayerPosLook) {
        boolean bl = false;
        return new SPacketPosLookImpl(sPacketPlayerPosLook);
    }

    public static final SPacketPlayerPosLook unwrap(ISPacketPosLook iSPacketPosLook) {
        boolean bl = false;
        return (SPacketPlayerPosLook)((SPacketPosLookImpl)iSPacketPosLook).getWrapped();
    }
}

