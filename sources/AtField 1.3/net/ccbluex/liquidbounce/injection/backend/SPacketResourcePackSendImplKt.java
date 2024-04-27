/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.SPacketResourcePackSend
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketResourcePackSend;
import net.ccbluex.liquidbounce.injection.backend.SPacketResourcePackSendImpl;
import net.minecraft.network.play.server.SPacketResourcePackSend;

public final class SPacketResourcePackSendImplKt {
    public static final ISPacketResourcePackSend wrap(SPacketResourcePackSend sPacketResourcePackSend) {
        boolean bl = false;
        return new SPacketResourcePackSendImpl(sPacketResourcePackSend);
    }

    public static final SPacketResourcePackSend unwrap(ISPacketResourcePackSend iSPacketResourcePackSend) {
        boolean bl = false;
        return (SPacketResourcePackSend)((SPacketResourcePackSendImpl)iSPacketResourcePackSend).getWrapped();
    }
}

